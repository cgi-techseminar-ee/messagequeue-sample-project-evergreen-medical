package com.cgi.seminar.messaging.publishers;

import com.cgi.seminar.domain.Questionnaire;

public interface QuestionnaireMessagingGateway {
    Questionnaire getQuestionnaireByLocationId(String locationExternalId);
}
