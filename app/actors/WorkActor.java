package actors;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import play.Logger;

public class WorkActor extends AbstractActor {
    public WorkActor() {
        receive(
                ReceiveBuilder.matchAny(obj -> {
                    Logger.debug("About me: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                    Thread.sleep(5000);
                    Logger.debug("Did the work: " + obj.toString().toUpperCase());
                }).build()
        );
    }
}
