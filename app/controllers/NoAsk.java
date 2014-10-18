package controllers;

import actors.perRequest.PerRequestActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.dispatch.Futures;
import models.RequestBase;
import models.courses.Queries;
import play.libs.Akka;
import play.libs.F;
import play.mvc.Result;
import play.mvc.Controller;
import scala.concurrent.Future;
import scala.concurrent.Promise;

import static play.libs.F.Promise.wrap;

public class NoAsk extends Controller {

    public static F.Promise<Result> noAsk() {
        final Promise<Result> promise = Futures.promise();
        final Future<Result> future = promise.future();
        final ActorRef perRequest = Akka.system().actorOf(Props.create(PerRequestActor.class));
        final RequestBase request = new Queries.CourseRequest(1, promise, "C1");
        perRequest.tell(request, ActorRef.noSender());
        //promise.success(ok("Ok!"));
        return wrap(future);
    }
}