package viewmodels;

import models.RequestBase;

import java.time.Duration;

public class ViewModelBase {
    public final int requestId;
    public final RequestBase request;
    private Duration executionTime = Duration.ZERO;


    public ViewModelBase(RequestBase request) {
        this.requestId = 1;
        this.request = request;
    }

    public void setExecutionTime(Duration executionTime) {
        this.executionTime = executionTime;
    }

    public Duration getExecutionTime() {
        return executionTime;
    }
}