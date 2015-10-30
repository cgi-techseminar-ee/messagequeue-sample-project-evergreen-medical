package com.cgi.seminar.messaging.messages;

public class CrudMessage<T> {
    public static final String CREATE = "create";

    private String action;
    private T object;

    public CrudMessage(T object, final String action) {
        this.object = object;
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }
}
