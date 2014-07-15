package actors;

import akka.actor.AbstractActor;
import akka.actor.AbstractActorWithStash;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import models.Queries;
import play.Logger;

public class ComplexServiceActor extends AbstractActor {

    public ComplexServiceActor() {
        receive(
                ReceiveBuilder.match(Queries.ToUpperRequest.class, request -> {
                    Logger.debug("About ComplexServiceActor ToUpperRequest: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                    final ActorRef coordinator = context().actorOf(Props.create(CoordinatorActor.class));
                    coordinator.forward(request, context());
                }).build()
        );
    }

    public static class CoordinatorActor extends AbstractActorWithStash {
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
                                            }).build()
                                    );
                                }).build()
                        );
                    }).build()
            );
        }
    }

    public static class Worker extends AbstractActor {
        public Worker() {
            receive(
                    ReceiveBuilder.match(Queries.ToUpperRequest.class, request -> {
                        Logger.debug("About ToUpperRequest service: " + self().toString() + " Thread: " + Thread.currentThread().getName());
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