package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import play.Logger;

public class ComplexServiceActor extends AbstractActor {

    public ComplexServiceActor() {
        receive(
                ReceiveBuilder.match(ToUpperRequest.class, request -> {
                    Logger.debug("About ComplexServiceActor ToUpperRequest: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                    final ActorRef coordinator = context().actorOf(Props.create(CoordinatorActor.class));
                    coordinator.forward(request, context());
                }).build()
        );
    }

    public static class CoordinatorActor extends AbstractActor {
        public CoordinatorActor() {
            receive(
                    ReceiveBuilder.match(ToUpperRequest.class, request -> {
                        final ActorRef sender = sender();
                        Logger.debug("About CoordinatorActor ToUpperRequest: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                        final ActorRef worker = context().actorOf(Props.create(Worker.class));
                        context().become(
                                ReceiveBuilder.match(ToUpperResponse.class, response -> {
                                    Logger.debug("About CoordinatorActor ToUpperResponse: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                                    final ToFirstCharLowerRequest firstCharLowerRequest = new ToFirstCharLowerRequest(response.output);
                                    final ActorRef worker2 = context().actorOf(Props.create(Worker.class));
                                    worker2.tell(firstCharLowerRequest, self());
                                    context().become(
                                            ReceiveBuilder.match(ToFirstCharLowerResponse.class, response2 -> {
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
        public Worker() {
            receive(
                    ReceiveBuilder.match(ToUpperRequest.class, request -> {
                        Logger.debug("About ToUpperRequest service: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                        Thread.sleep(3000);
                        sender().tell(new ToUpperResponse(request.input, request.input.toUpperCase()), self());
                        context().stop(self());
                    }).match(ToFirstCharLowerRequest.class, request -> {
                        Logger.debug("About ToFirstCharLowerRequest service: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                        Thread.sleep(2000);
                        final String temp = request.input.substring(0, 1).toLowerCase() + request.input.substring(1);
                        sender().tell(new ToFirstCharLowerResponse(request.input, temp), self());
                        context().stop(self());
                    }).build()
            );
        }
    }

    public static class ToUpperRequest {
        public final String input;

        public ToUpperRequest(String input) {
            this.input = input;
        }
    }

    public static class ToUpperResponse {
        public final String input;
        public final String output;

        public ToUpperResponse(String input, String output) {
            this.input = input;
            this.output = output;
        }
    }

    public static class ToFirstCharLowerRequest {
        public final String input;

        public ToFirstCharLowerRequest(String input) {
            this.input = input;
        }
    }

    public static class ToFirstCharLowerResponse {
        public final String input;
        public final String output;

        public ToFirstCharLowerResponse(String input, String output) {
            this.input = input;
            this.output = output;
        }
    }
}