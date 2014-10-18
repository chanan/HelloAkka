package controllers;

import actors.gradebook.GradebookActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import models.courses.Queries;
import play.libs.Akka;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;

import static akka.pattern.Patterns.ask;
import static play.libs.F.Promise.wrap;

public class Gradebook extends Controller {
    private static final ActorRef gradebookActor = Akka.system().actorOf(Props.create(GradebookActor.class), "gradebook");

    public static F.Promise<Result> course(String courseId) {
        final Queries.CourseRequest request = new Queries.CourseRequest(1, null, courseId);

        return wrap(ask(gradebookActor, request, 5000)).map(obj -> {
            final Queries.CourseResponse response = (Queries.CourseResponse) obj;
            return ok(Json.toJson(response));
        });
    }
}
