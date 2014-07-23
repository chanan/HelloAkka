package models.statuses;

import java.util.Optional;

public interface StatusCreator<I, O, F> {
    StatusCreator<I, O, F> setSuccess(O response);

    StatusCreator<I, O, F> setSuccess(O response, I request);

    StatusCreator<I, O, F> setSuccess(O response, Optional<I> request);

    StatusCreator<I, O, F> setFailure(F failureCause);

    StatusCreator<I, O, F> setFailure(F failureCause, I request);

    StatusCreator<I, O, F> setFailure(F failureCause, Optional<I> request);
}
