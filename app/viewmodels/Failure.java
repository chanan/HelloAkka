package viewmodels;

import models.RequestBase;

import java.util.Optional;

public class Failure extends ViewModelBase {
    public final Throwable cause;

    public Failure(RequestBase request, Throwable cause) {
        super(request);
        this.cause = cause;
    }
}