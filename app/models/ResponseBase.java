package models;

import viewmodels.ViewModelBase;

public class ResponseBase {
    public final RequestBase request;
    public final ViewModelBase viewmodel;

    public ResponseBase(RequestBase request, ViewModelBase viewmodel) {
        this.request = request;
        this.viewmodel = viewmodel;
    }
}