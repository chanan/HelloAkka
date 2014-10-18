package actors.perRequest;

import actors.gradebook.GradebookActor;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ReceiveTimeout;
import akka.japi.pf.ReceiveBuilder;
import models.RequestBase;
import play.Logger;
import play.libs.Akka;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Results;
import scala.concurrent.Promise;
import scala.concurrent.duration.Duration;
import viewmodels.Failure;
import viewmodels.ViewModelBase;

import java.time.Instant;
import java.util.concurrent.TimeoutException;

public class PerRequestActor extends AbstractActor {
    private static final ActorRef gradebookActor = Akka.system().actorOf(Props.create(GradebookActor.class), "gradebook_perrequest");
    private Promise<Result> promise;
    private RequestBase request;

    public PerRequestActor() {
        context().setReceiveTimeout(Duration.create("2 seconds"));
        receive(
                ReceiveBuilder.match(RequestBase.class, r -> {
                    promise = r.promise;
                    request = r;
                    gradebookActor.tell(r, self());
                }).match(ReceiveTimeout.class, r -> {
                    final Exception e = new TimeoutException("2 second timeout!");
                    final Failure vm = new Failure(request, e);
                    enrichViewModel(vm);
                    promise.success(Results.internalServerError(Json.toJson(vm)));
                    context().stop(self());
                }).match(ViewModelBase.class, vm -> {
                    enrichViewModel(vm);
                    promise.success(Results.ok(Json.toJson(vm)));
                    context().stop(self());
                }).match(Exception.class, e -> {
                    Logger.error("Unhandled error about to be returned to user", e);
                    final Failure vm = new Failure(request, e);
                    enrichViewModel(vm);
                    promise.success(Results.internalServerError(Json.toJson(vm)));
                    context().stop(self());
                }).build());
    }

    private void enrichViewModel(ViewModelBase vm) {
        final java.time.Duration duration = java.time.Duration.between(vm.request.startTime, Instant.now());
        vm.setExecutionTime(duration);
        Logger.debug("Request: " + vm.request + " Duration: " + duration);
    }
}