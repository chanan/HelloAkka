package actors;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import models.Queries;
import play.Logger;
import scala.concurrent.duration.Duration;

import static akka.actor.SupervisorStrategy.escalate;
import static akka.actor.SupervisorStrategy.stop;

public class FaultTolerantServiceActor extends AbstractActor {
    private int count = 0;

    public FaultTolerantServiceActor() {
        receive(
                ReceiveBuilder.match(Queries.ToUpperRequest.class, request -> {
                    Logger.debug("About service: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                    count++;
                    final ActorRef supervisor = context().actorOf(Props.create(SupervisorActor.class, count));
                    supervisor.forward(request, context());
                }).matchAny(obj -> {
                    Logger.error("I cannot handle objects of type: " + obj.getClass().getName());
                }).build()
        );
    }

    public static class SupervisorActor extends AbstractActor {
        private ActorRef sender;
        private Object request;

        private SupervisorStrategy strategy =
                new OneForOneStrategy(10, Duration.create("1 minute"), DeciderBuilder.
                        match(IllegalArgumentException.class, e -> {
                            sender.tell(new Queries.FailureToUpResponse(request, "Cannot handle the string 'error'!"), self());
                            return stop();
                        }).
                        matchAny(o -> escalate()).build());

        @Override
        public SupervisorStrategy supervisorStrategy() {
            return strategy;
        }

        public SupervisorActor(int count) {
            receive(
                    ReceiveBuilder.matchAny(obj -> {
                        sender = sender();
                        request = obj;
                        final ActorRef worker = context().actorOf(Props.create(Worker.class, count));
                        worker.forward(obj, context());
                    }).build()
            );
        }
    }

    public static class Worker extends AbstractActor {
        public Worker(int count) {
            receive(
                    ReceiveBuilder.match(Queries.ToUpperRequest.class, request -> {
                        Logger.debug("About me: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                        if(request.input.equals("error")) throw new IllegalArgumentException("Cannot handle the word error!");
                        Thread.sleep(5000);
                        Logger.debug("Did the work: " + request.input.toUpperCase() + "-" + count);
                        final Queries.ToUpperResponse response = new Queries.ToUpperResponse(request.input, request.input.toUpperCase() + "-" + count);
                        sender().tell(response, self());
                        context().stop(self());
                    }).build()
            );
        }
    }
}
