package com.cgi.seminar.messaging.consumers;

import com.cgi.seminar.domain.Employee;
import com.cgi.seminar.messaging.messages.NurseMessage;
import com.cgi.seminar.repository.EmployeeRepository;
import com.cgi.seminar.repository.EntityWithExternalIdRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Component
public class NurseMessageListener extends MessageListenerBase<Employee, NurseMessage> {

    @Inject
    EmployeeRepository employeeRepository;

    @RabbitListener(queues = {"nurse-queue"}) // must match MessagingConfiguration
    @Transactional
    void onMessageReceived(Message<NurseMessage> message) {
        createOrUpdateEntityFromMessage(message, Employee.class);
    }

    @Override
    protected EntityWithExternalIdRepository<Employee> getRepository() {
        return employeeRepository;
    }

    @Override
    protected void fillEntityFieldsFromMessage(Employee employee, NurseMessage msg) {
        employee.setName(msg.getName());
    }
}
