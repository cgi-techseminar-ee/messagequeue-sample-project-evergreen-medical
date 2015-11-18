package com.cgi.seminar.messaging.messages;

public class NurseMessage extends MessageWithId {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "NurseMessage{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
