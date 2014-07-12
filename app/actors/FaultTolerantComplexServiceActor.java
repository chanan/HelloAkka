package actors;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import models.Queries;
import play.Logger;
import scala.Option;
import scala.concurrent.duration.Duration;

import java.util.Random;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.stop;
import static akka.actor.SupervisorStrategy.restart;

public class FaultTolerantComplexServiceActor extends AbstractActor {
    public FaultTolerantComplexServiceActor() {
        receive(
                ReceiveBuilder.match(Queries.ToUpperRequest.class, request -> {
                    Logger.debug("About ComplexServiceActor ToUpperRequest: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                    final ActorRef coordinator = context().actorOf(Props.create(SupervisorActor.class));
                    coordinator.forward(request, context());
                }).build()
        );
    }

    public static class SupervisorActor extends AbstractActor {
        private ActorRef sender;
        private Object request;
        private ActorContext context;

        private SupervisorStrategy strategy =
                new OneForOneStrategy(10, Duration.create("1 minute"), DeciderBuilder.
                        match(IllegalArgumentException.class, e -> {
                            sender.tell(new Queries.FailureToUpResponse(request, "Cannot handle the string 'error'!"), self());
                            return stop();
                        }).match(IllegalAccessException.class, e -> {
                            Logger.info("Lets retry our work!");
                            final ActorRef worker = context().actorOf(Props.create(CoordinatorActor.class));
                            worker.tell(request, sender);
                            return stop();
                        }).
                        matchAny(o -> escalate()).build());

        @Override
        public SupervisorStrategy supervisorStrategy() {
            return strategy;
        }

        public SupervisorActor() {
            receive(
                    ReceiveBuilder.matchAny(obj -> {
                        sender = sender();
                        context = context();
                        request = obj;
                        final ActorRef worker = context().actorOf(Props.create(CoordinatorActor.class));
                        worker.forward(obj, context());
                    }).build()
            );
        }
    }

    public static class CoordinatorActor extends AbstractActor {
        public CoordinatorActor() {
            receive(
                    ReceiveBuilder.match(Queries.ToUpperRequest.class, request -> {
                        final ActorRef sender = sender();
                        Logger.debug("About CoordinatorActor ToUpperRequest: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                        final ActorRef worker = context().actorOf(Props.create(Worker.class));
                        worker.tell(request, self());
                        context().become(
                                ReceiveBuilder.match(Queries.ToUpperResponse.class, response -> {
                                    Logger.debug("About CoordinatorActor ToUpperResponse: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                                    final Queries.ToFirstCharLowerRequest firstCharLowerRequest = new Queries.ToFirstCharLowerRequest(response.output);
                                    final ActorRef worker2 = context().actorOf(Props.create(Worker.class));
                                    worker2.tell(firstCharLowerRequest, self());
                                    context().become(
                                            ReceiveBuilder.match(Queries.ToFirstCharLowerResponse.class, response2 -> {
                                                Logger.debug("About CoordinatorActor ToFirstCharLowerResponse: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                                                sender.tell(response2, self());
                                                context().stop(self());
                                            }).build()
                                    );
                                }).build()
                        );
                    }).build()
            );
        }
    }

    public static class Worker extends AbstractActor {
        private final static Random rnd = new Random();
        public Worker() {
            receive(
                    ReceiveBuilder.match(Queries.ToUpperRequest.class, request -> {
                        Logger.debug("About ToUpperRequest service: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                        if(rnd.nextBoolean()) throw new IllegalAccessException("Service went boom!");
                        //if(!rnd.nextBoolean()) throw new IllegalAccessException("Service went boom!");
                        if(request.input.equals("error")) throw new IllegalArgumentException("Cannot handle the word error!");
                        Thread.sleep(1000);
                        sender().tell(new Queries.ToUpperResponse(request.input, request.input.toUpperCase()), self());
                        context().stop(self());
                    }).match(Queries.ToFirstCharLowerRequest.class, request -> {
                        Logger.debug("About ToFirstCharLowerRequest service: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                        Thread.sleep(1000);
                        final String temp = request.input.substring(0, 1).toLowerCase() + request.input.substring(1);
                        sender().tell(new Queries.ToFirstCharLowerResponse(request.input, temp), self());
                        context().stop(self());
                    }).build()
            );
        }
    }
}
