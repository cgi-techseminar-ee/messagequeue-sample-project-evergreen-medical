package com.cgi.seminar.messaging.messages;

public abstract class MessageWithId {

    protected String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
