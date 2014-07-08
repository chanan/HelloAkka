package actors;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import play.Logger;
import scala.concurrent.duration.Duration;

import static akka.actor.SupervisorStrategy.resume;
import static akka.actor.SupervisorStrategy.restart;
import static akka.actor.SupervisorStrategy.stop;
import static akka.actor.SupervisorStrategy.escalate;

public class ServiceActor extends AbstractActor {
    private int count = 0;

    public ServiceActor() {
        receive(
                ReceiveBuilder.match(ServiceActor.ToUpperRequest.class, request -> {
                    Logger.debug("About service: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                    count++;
                    final ActorRef worker = context().actorOf(Props.create(Worker.class, count));
                    worker.forward(request, context());
                }).matchAny(obj -> {
                    Logger.error("I cannot handle objects of type: " + obj.getClass().getName());
                }).build()
        );
    }

    public static class Worker extends AbstractActor {
        public Worker(int count) {
            receive(
                    ReceiveBuilder.match(ServiceActor.ToUpperRequest.class, request -> {
                        Logger.debug("About me: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                        //if(request.input.equals("error")) throw new IllegalArgumentException("Cannot handle the word error!");
                        Thread.sleep(5000);
                        Logger.debug("Did the work: " + request.input.toUpperCase() + "-" + count);
                        final ToUpperResponse response = new ToUpperResponse(request.input, request.input.toUpperCase() + "-" + count);
                        sender().tell(response, self());
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

    public static class FailureToUpResponse {

    }
}
