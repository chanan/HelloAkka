package controllers;

import actors.*;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.PoisonPill;
import akka.actor.Props;
import models.Queries;
import play.libs.Akka;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.index;

import static akka.pattern.Patterns.ask;
import static play.libs.F.Promise.wrap;

public class Application extends Controller {

    private final static ActorRef globalWorker = Akka.system().actorOf(Props.create(WorkActor.class));
    private final static ActorRef workerWithName = Akka.system().actorOf(Props.create(WorkActor.class), "name");
    private final static ActorRef serviceActor = Akka.system().actorOf(Props.create(ServiceActor.class), "service");
    private final static ActorRef complexService = Akka.system().actorOf(Props.create(ComplexServiceActor.class), "complex_service");
    private final static ActorRef faultTolerantService = Akka.system().actorOf(Props.create(FaultTolerantServiceActor.class), "fault");
    private final static ActorRef faultTolerantComplexService = Akka.system().actorOf(Props.create(FaultTolerantComplexServiceActor.class), "fault_complex");

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }

    public static Result doWork() {
        final ActorRef worker = Akka.system().actorOf(Props.create(WorkActor.class));
        worker.tell("Hi", ActorRef.noSender());
        worker.tell(PoisonPill.getInstance(), ActorRef.noSender());
        return ok();
    }

    public static Result doGlobalWork() {
        globalWorker.tell("Hi", ActorRef.noSender());
        return ok();
    }

    public static Result doGlobalWorkWithName() {
        final ActorSelection actor = Akka.system().actorSelection("/user/name");
        actor.tell("hi", ActorRef.noSender());
        return ok();
    }

    public static Result serviceCallTell() {
        serviceActor.tell(new Queries.ToUpperRequest("hello"), ActorRef.noSender());
        return ok();
    }

    public static F.Promise<Result> serviceCallAsk() {
        return wrap(ask(serviceActor, new Queries.ToUpperRequest("hello ask"), 10000)).map(obj -> {
            final Queries.ToUpperResponse response = (Queries.ToUpperResponse) obj;
            return ok(Json.toJson(response));
        });
    }

    public static F.Promise<Result> complexService() {
        return wrap(ask(complexService, new Queries.ToUpperRequest("Hello world"), 10000)).map(obj -> {
           final Queries.ToFirstCharLowerResponse response = (Queries.ToFirstCharLowerResponse) obj;
           return ok(Json.toJson(response));
        });
    }

    public static F.Promise<Result> serviceCallError() {
        return wrap(ask(faultTolerantService, new Queries.ToUpperRequest("error"), 10000)).map(obj -> {
            if(obj instanceof Queries.FailureToUpResponse) {
                return Results.badRequest(Json.toJson(obj));
            }
            final Queries.ToUpperResponse response = (Queries.ToUpperResponse) obj;
            return ok(Json.toJson(response));
        });
    }

    public static F.Promise<Result> FaultTolerantComplexServiceActor() {
        return wrap(ask(faultTolerantComplexService, new Queries.ToUpperRequest("Sometimes we get an error here"), 5000)).map(obj -> {
            if(obj instanceof Queries.FailureToUpResponse) {
                return Results.badRequest(Json.toJson(obj));
            }
            final Queries.ToFirstCharLowerResponse response = (Queries.ToFirstCharLowerResponse) obj;
            return ok(Json.toJson(response));
        });
    }
}