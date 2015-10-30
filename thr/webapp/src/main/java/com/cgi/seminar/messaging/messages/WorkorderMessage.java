package com.cgi.seminar.messaging.messages;

import com.cgi.seminar.domain.Workorder;

public class WorkorderMessage extends CrudMessage<Workorder> {
    public WorkorderMessage(Workorder workorder, String action) {
        super(workorder, action);
    }
}
