package com.cgi.seminar.messaging.consumers;

import com.cgi.seminar.domain.Employee;
import com.cgi.seminar.messaging.messages.NurseMessage;
import com.cgi.seminar.repository.EmployeeRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static com.cgi.seminar.messaging.consumers.GenericMessageHandler.createOrUpdateEntityFromMessage;

@Component
public class NurseMessageListener {

    @Inject
    EmployeeRepository employeeRepository;

    @RabbitListener(queues = {"nurse-queue"}) // must match MessagingConfiguration
    @Transactional
    void onMessageReceived(Message<NurseMessage> message) {
        createOrUpdateEntityFromMessage(message, Employee.class, employeeRepository, (employee, msg) -> {
            employee.setName(msg.getName());
        });
    }
}
