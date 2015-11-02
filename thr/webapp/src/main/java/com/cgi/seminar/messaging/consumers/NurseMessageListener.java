package com.cgi.seminar.messaging.consumers;

import com.cgi.seminar.domain.Employee;
import com.cgi.seminar.messaging.messages.NurseMessage;
import com.cgi.seminar.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Component
public class NurseMessageListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    EmployeeRepository employeeRepository;

    @RabbitListener(queues = {"nurse-queue"}) // must match MessagingConfiguration
    @Transactional
    void onMessageReceived(Message<NurseMessage> message) {
        NurseMessage nurseMessage = message.getPayload();
        log.info("Received Nurse message: {}", nurseMessage);
        String externalId = nurseMessage.getId();
        Employee employee = employeeRepository.findByExternalId(externalId);
        if (employee == null) {
            employee = new Employee();
            employee.setExternalId(externalId);
        }
        employee.setName(nurseMessage.getName());
        employeeRepository.save(employee);
    }
}
