package com.cgi.seminar.messaging.publishers;

import com.cgi.seminar.domain.Workorder;

public interface WorkorderMessagingGateway {
    void publishCreateMessage(Workorder created);
}
