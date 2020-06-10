package com.bench.android.core.net.httpLoop;


import com.bench.android.core.net.http.base.RequestContainer;

public class LoopMessage {

    private RequestContainer container;
    private Object jsonObj;

    public LoopMessage(RequestContainer container, Object jsonObj) {
        this.container = container;
        this.jsonObj = jsonObj;
    }

    public RequestContainer getContainer() {
        return container;
    }

    public void setContainer(RequestContainer container) {
        this.container = container;
    }

    public Object getJsonObj() {
        return jsonObj;
    }

    public void setJsonObj(Object jsonObj) {
        this.jsonObj = jsonObj;
    }
}
