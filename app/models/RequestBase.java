package models;

import play.mvc.Result;
import scala.concurrent.Promise;

import java.time.Instant;

public class RequestBase {
    public final Promise<Result> promise;
    public final int requestId;
    public final Instant startTime = Instant.now();

    public RequestBase(int requestId, Promise<Result> promise) {
        this.requestId = requestId;
        this.promise = promise;
    }
}