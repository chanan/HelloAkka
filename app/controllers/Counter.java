package controllers;

import actors.intro.CountActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import play.libs.Akka;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import static akka.pattern.Patterns.ask;
import static play.libs.F.Promise.wrap;

public class Counter extends Controller {
    private static final ActorRef countActor = Akka.system().actorOf(Props.create(CountActor.class), "counter");

    public static Result add(int count) {
        countActor.tell(new CountActor.Add(count), ActorRef.noSender());
        return ok("Count added: " + count);
    }

    public static F.Promise<Result> count() {
        return wrap(ask(countActor, new CountActor.CountRequest(), 5000)).map(obj -> {
            final CountActor.CountResponse response = (CountActor.CountResponse) obj;
            return ok(Json.toJson(response));
        });
    }
}
