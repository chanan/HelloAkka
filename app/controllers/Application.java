package controllers;

import actors.ComplexServiceActor;
import actors.ServiceActor;
import actors.WorkActor;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.PoisonPill;
import akka.actor.Props;
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
        serviceActor.tell(new ServiceActor.ToUpperRequest("hello"), ActorRef.noSender());
        return ok();
    }

    public static F.Promise<Result> serviceCallAsk() {
        return wrap(ask(serviceActor, new ServiceActor.ToUpperRequest("hello ask"), 10000)).map(obj -> {
            final ServiceActor.ToUpperResponse response = (ServiceActor.ToUpperResponse) obj;
            return ok(Json.toJson(response));
        });
    }

    public static F.Promise<Result> complexService() {
        return wrap(ask(complexService, new ComplexServiceActor.ToUpperRequest("Hello world"), 10000)).map(obj -> {
           final ComplexServiceActor.ToFirstCharLowerResponse response = (ComplexServiceActor.ToFirstCharLowerResponse) obj;
           return ok(Json.toJson(response));
        });
    }

    public static F.Promise<Result> serviceCallError() {
        return wrap(ask(serviceActor, new ServiceActor.ToUpperRequest("error"), 10000)).map(obj -> {
            if(obj instanceof ServiceActor.FailureToUpResponse) {
                return Results.badRequest("Cannot upper the word error!");
            }
            final ServiceActor.ToUpperResponse response = (ServiceActor.ToUpperResponse) obj;
            return ok(Json.toJson(response));
        });
    }
}
