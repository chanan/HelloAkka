package models.statuses;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;
import java.util.function.Function;

public interface StatusResult<I, O, F, R> {
    StatusResult<I, O, F, R> onSuccess(Function<StatusHolder.Success<I, O>, R> action);

    StatusResult<I, O, F, R> onFailure(Function<StatusHolder.Failure<I, F>, R> action);

    StatusResult<I, O, F, R> onFinally(Consumer<StatusResult<I, O, F, R>> action);

    StatusResult<I, O, F, R> SetExecutionTime(Instant start, Instant end);

    StatusResult<I, O, F, R> SetExecutionTime(long executionTime);

    StatusResult<I, O, F, R> SetExecutionTime(Duration executionTime);

    Duration getExecutionTime();

    R toResult();
}
