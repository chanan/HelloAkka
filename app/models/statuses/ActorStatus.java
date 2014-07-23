package models.statuses;

import models.RequestBase;
import play.mvc.Result;
import models.ResponseBase;

public class ActorStatus extends StatusHolder<RequestBase, ResponseBase, Throwable, Result> {
    private ActorStatus() { }

    public static StatusCreator Create() {
        return new ActorStatus();
    }
}