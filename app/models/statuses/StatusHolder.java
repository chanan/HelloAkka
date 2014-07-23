package models.statuses;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * func(O(Created by I) || F) => R
 *
 * A function that transforms an output, optionally created by input, or a failure; to a result
 *
 * @param <I> Input class
 * @param <O> Output class
 * @param <F> Failure class
 * @param <R> Result class
 */
public class StatusHolder<I, O, F, R> implements StatusResult<I, O, F, R>, StatusCreator<I, O, F> {
    protected boolean success;
    protected R result;
    protected Optional<Success<I, O>> successHolder;
    protected Optional<Failure<I, F>> failureHolder;
    protected Optional<Function<Success<I, O>, R>> successAction = Optional.empty();
    protected Optional<Function<Failure<I, F>, R>> failureAction = Optional.empty();
    protected Optional<Consumer<StatusResult<I, O, F, R>>> finallyAction = Optional.empty();
    protected Optional<Duration> executionTime = Optional.empty();

    @Override
    public StatusResult<I, O, F, R> onSuccess(Function<Success<I, O>, R> action) {
        this.successAction = Optional.of(action);
        return this;
    }

    @Override
    public StatusResult<I, O, F, R> onFailure(Function<Failure<I, F>, R> action) {
        this.failureAction = Optional.of(action);
        return this;
    }

    @Override
    public StatusResult<I, O, F, R> onFinally(Consumer<StatusResult<I, O, F, R>> action) {
        this.finallyAction = Optional.of(action);
        return this;
    }

    @Override
    public StatusResult<I, O, F, R> SetExecutionTime(Instant start, Instant end) {
        this.executionTime = Optional.of(Duration.between(start, end));
        this.successHolder.ifPresent(s -> s.executionTime = Optional.of(Duration.between(start, end)));
        this.failureHolder.ifPresent(f -> f.executionTime = Optional.of(Duration.between(start, end)));
        return this;
    }

    @Override
    public StatusResult<I, O, F, R> SetExecutionTime(long executionTime) {
        this.executionTime = Optional.of(Duration.ofMillis(executionTime));
        this.successHolder.ifPresent(s -> s.executionTime = Optional.of(Duration.ofMillis(executionTime)));
        this.failureHolder.ifPresent(f -> f.executionTime = Optional.of(Duration.ofMillis(executionTime)));
        return this;
    }

    @Override
    public StatusResult<I, O, F, R> SetExecutionTime(Duration executionTime) {
        this.executionTime = Optional.of(executionTime);
        this.successHolder.ifPresent(s -> s.executionTime = Optional.of(executionTime));
        this.failureHolder.ifPresent(f -> f.executionTime = Optional.of(executionTime));
        return this;
    }

    @Override
    public Duration getExecutionTime() {
        return this.executionTime.orElse(Duration.ZERO);
    }

    @Override
    public R toResult() {
        if(successAction.isPresent() && success && successHolder.isPresent()) result = successAction.get().apply(successHolder.get());
        if(failureAction.isPresent() && !success && failureHolder.isPresent()) result = failureAction.get().apply(failureHolder.get());
        if(finallyAction.isPresent()) finallyAction.get().accept(this);
        return result;
    }

    @Override
    public StatusCreator<I, O, F> setSuccess(O response) {
        this.success = true;
        this.successHolder = Optional.of(new Success<>(response));
        this.failureHolder = Optional.empty();
        return this;
    }

    @Override
    public StatusCreator<I, O, F> setSuccess(O response, I request) {
        this.success = true;
        this.successHolder = Optional.of(new Success<>(response, request));
        this.failureHolder = Optional.empty();
        return this;
    }

    @Override
    public StatusCreator<I, O, F> setSuccess(O response, Optional<I> request) {
        this.success = true;
        this.successHolder = Optional.of(new Success<>(response, request));
        this.failureHolder = Optional.empty();
        return this;
    }

    @Override
    public StatusCreator<I, O, F> setFailure(F failureCause) {
        this.success = false;
        this.failureHolder = Optional.of(new Failure<>(failureCause));
        this.successHolder = Optional.empty();
        return this;
    }

    @Override
    public StatusCreator<I, O, F> setFailure(F failureCause, I request) {
        this.success = false;
        this.failureHolder = Optional.of(new Failure<>(failureCause, request));
        this.successHolder = Optional.empty();
        return this;
    }

    @Override
    public StatusCreator<I, O, F> setFailure(F failureCause, Optional<I> request) {
        this.success = false;
        this.failureHolder = Optional.of(new Failure<>(failureCause, request));
        this.successHolder = Optional.empty();
        return this;
    }

    public static class Success<I, O> {
        public final O response;
        public final Optional<I> request;
        Optional<Duration> executionTime = Optional.empty();

        public Success(O response, Optional<I> request) {
            this.response = response;
            this.request = request;
        }

        public Success (O response, I request) {
            this.response = response;
            this.request = Optional.of(request);
        }

        public Success(O response) {
            this.response = response;
            this.request = Optional.empty();
        }

        public Duration getExecutionTime() {
            return this.executionTime.orElse(Duration.ZERO);
        }
    }

    public static class Failure<I, F> {
        public final F cause;
        public final Optional<I> request;
        Optional<Duration> executionTime = Optional.empty();

        public Failure(F cause, Optional<I> request) {
            this.cause = cause;
            this.request = request;
        }

        public Failure (F cause) {
            this.cause = cause;
            this.request = Optional.empty();
        }

        public Failure(F cause, I request) {
            this.cause = cause;
            this.request = Optional.of(request);
        }

        public Duration getExecutionTime() {
            return this.executionTime.orElse(Duration.ZERO);
        }
    }
}