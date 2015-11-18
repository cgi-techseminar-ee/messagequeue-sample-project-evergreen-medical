package com.cgi.seminar.processors;

import com.cgi.seminar.domain.Location;
import com.cgi.seminar.domain.Questionnaire;
import com.cgi.seminar.domain.Workorder;
import com.cgi.seminar.messaging.publishers.QuestionnaireMessagingGateway;
import com.cgi.seminar.messaging.publishers.WorkorderMessagingGateway;
import com.cgi.seminar.repository.QuestionnaireRepository;
import com.cgi.seminar.repository.WorkorderRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class WorkorderProcessorImpl implements WorkorderProcessor {

    @Inject
    private WorkorderRepository workorderRepository;

    @Inject
    private WorkorderMessagingGateway workorderMessagingGateway;

    @Inject
    private QuestionnaireRepository questionnaireRepository;

    @Inject
    private QuestionnaireMessagingGateway questionnaireMessagingGateway;

    public Workorder create(final Workorder workorder) {
        Workorder created = workorderRepository.save(workorder);
        updateQuestionnaire(created);
        workorderMessagingGateway.publishCreateMessage(created);
        return created;
    }

    private void updateQuestionnaire(Workorder workorder) {
        Location location = workorder.getLocation();
        if (location == null) {
            return;
        }
        String locationExternalId = location.getExternalId();
        if (StringUtils.isBlank(locationExternalId)) {
            return;
        }

        // synchronous remote calls should be avoided really, see https://www.rabbitmq.com/tutorials/tutorial-six-java.html
        Questionnaire questionnaire = questionnaireMessagingGateway.getQuestionnaireByLocationId(locationExternalId);
        if (questionnaire == null) {
            return;
        }
        questionnaire = questionnaireRepository.save(questionnaire);
        workorder.setQuestionnaire(questionnaire);
        workorderRepository.save(workorder);
    }
}
