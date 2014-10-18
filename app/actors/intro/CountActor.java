package actors.intro;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import play.Logger;

public class CountActor extends AbstractActor {
    private int count = 0;

    public CountActor() {
        receive(
                ReceiveBuilder.match(Add.class, request -> {
                    Logger.debug("About SimpleActor Add: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                    count = count + request.number;
                }).match(CountRequest.class, request -> {
                    Logger.debug("About SimpleActor CountRequest: " + self().toString() + " Thread: " + Thread.currentThread().getName());
                    CountResponse response = new CountResponse(count);
                    sender().tell(response, self());
                }).build()
        );
    }

    public static class Add {
        public final int number;

        public Add() {
            this.number = 1;
        }

        public Add(int number) {
            this.number = number;
        }
    }

    public static class CountRequest {

    }

    public static class CountResponse {
        public final int count;

        public CountResponse(int count) {
            this.count = count;
        }
    }
}
