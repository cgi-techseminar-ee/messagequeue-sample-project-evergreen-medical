package com.cgi.seminar.messaging.publishers;

import com.cgi.seminar.domain.Questionnaire;
import com.cgi.seminar.messaging.messages.QuestionnaireMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;

import static com.cgi.seminar.messaging.publishers.MessagingGatewayConfiguration.QUESTIONNAIRE_EXCHANGE_NAME;
import static com.cgi.seminar.messaging.publishers.MessagingGatewayConfiguration.QUESTIONNAIRE_QUEUE_NAME;

@Component
public class QuestionnaireMessagingGatewayImpl extends MessagingGatewayBase implements QuestionnaireMessagingGateway {

    @Override
    public Questionnaire getQuestionnaireByLocationId(final String locationExternalId) {
        try {
            final Object reply = messagingTemplate.convertSendAndReceive(QUESTIONNAIRE_EXCHANGE_NAME, QUESTIONNAIRE_QUEUE_NAME,
                new HashMap<String, String>() {{ put("locationId", locationExternalId); }},
                message -> {
                    log.info("Sending Questionnaire request message: {}", new String(message.getBody()));
                    return message;
                });
            if (reply == null) {
                log.info("No Questionnaire available for location ID '{}'", locationExternalId);
                return null;
            }
            log.info("Received Questionnaire reply: '{}' (for location ID '{}')", reply, locationExternalId);
            return translateToQuestionnaire((String) reply);
        } catch (IOException e) {
            log.error("Reply deserialization error: {}", e);
        }

        return null;
    }

    private Questionnaire translateToQuestionnaire(final String reply) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        QuestionnaireMessage incomingMessage = mapper.readValue(reply, QuestionnaireMessage.class);
        final Questionnaire questionnaire = new Questionnaire() {{
            setName(incomingMessage.getName());
            setQuestions(incomingMessage.getQuestions());
        }};
        return questionnaire;
    }
}
