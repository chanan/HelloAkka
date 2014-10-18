package controllers;

import actors.gradebook.GradebookActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import models.RequestBase;
import models.courses.Queries;
import models.statuses.StatusHolder;
import models.statuses.StatusResult;
import play.Logger;
import play.libs.Akka;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import models.ResponseBase;
import viewmodels.ViewModelBase;

import static akka.pattern.Patterns.ask;
import static play.libs.F.Promise.wrap;

public class GradebookWithStatus extends Controller {
    private static final ActorRef gradebookActor = Akka.system().actorOf(Props.create(GradebookActor.class), "gradebook_with_status");

    public static F.Promise<Result> course(String courseId) {
        return getResultPromise(gradebookActor, new Queries.CourseRequest(1, null, courseId));
    }

    private static F.Promise<Result> getResultPromise(ActorRef actorRef, RequestBase request) {
        return wrap(ask(actorRef, request, 500))
                .map(obj -> {
                    final StatusResult<RequestBase, ResponseBase, Throwable, Result> status = (StatusResult<RequestBase, ResponseBase, Throwable, Result>) obj;
                    return status.onSuccess(success -> {
                        success.request.ifPresent(r -> Logger.debug("Request: " + r));
                        final ResponseBase response = success.response;
                        final ViewModelBase vm = response.viewmodel;
                        return ok(Json.toJson(vm));
                    }).onFailure(failure -> {
                        failure.request.ifPresent(r -> Logger.error("Request: " + r));
                        Logger.error("Error in request!", failure.cause);
                        final StatusHolder.Failure vm = new StatusHolder.Failure(failure.request, failure.cause);
                        return internalServerError(Json.toJson(vm));
                    }).onFinally(s -> Logger.debug("Execution Time: " + s.getExecutionTime().toString())).toResult();
                });
    }
}
