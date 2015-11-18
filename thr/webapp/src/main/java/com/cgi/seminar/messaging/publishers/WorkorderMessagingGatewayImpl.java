package com.cgi.seminar.messaging.publishers;

import com.cgi.seminar.domain.Workorder;
import com.cgi.seminar.messaging.MessagingConfiguration;
import com.cgi.seminar.messaging.messages.WorkorderMessage;
import org.springframework.stereotype.Component;

@Component
public class WorkorderMessagingGatewayImpl extends MessagingGatewayBase implements WorkorderMessagingGateway {

    @Override
    public void publishCreateMessage(Workorder workorder) {
        messagingTemplate.convertAndSend(
            MessagingConfiguration.WORKORDER_EXCHANGE_NAME, null,
            new WorkorderMessage(workorder, WorkorderMessage.CREATE), message -> {
                log.info("Publishing create Workorder message: {}", new String(message.getBody()));
                return message;
            });
    }
}
