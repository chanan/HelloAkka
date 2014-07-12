package actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import models.Queries;
import play.Logger;

public class ServiceActor extends AbstractActor {
    private int count = 0;

    public ServiceActor() {
        receive(
                ReceiveBuilder.match(Queries.ToUpperRequest.class, request -> {
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
                    ReceiveBuilder.match(Queries.ToUpperRequest.class, request -> {
                        Logger.debug("About me: " + self().toString() + " Thread: " + Thread.currentThread().getName());
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
