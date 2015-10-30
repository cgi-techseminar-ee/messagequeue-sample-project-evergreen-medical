package com.cgi.seminar.messaging.publishers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Component
public class MessagingGatewayConfigurationImpl implements MessagingGatewayConfiguration {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    protected RabbitTemplate messagingTemplate;

    @Inject
    protected AmqpAdmin amqpAdmin;

    @PostConstruct
    public void init() {
        log.info("Configuring messaging");

        messagingTemplate.setMessageConverter(new JsonMessageConverter());

        configureWorkorderPublishSubscribeExchange();
        configureQuestionnaireRequestReplyExchangeAndQueue();
    }

    private void configureWorkorderPublishSubscribeExchange() {
        final FanoutExchange exchange = new FanoutExchange(WORKORDER_EXCHANGE_NAME);
        amqpAdmin.declareExchange(exchange);
        // subscribers are responsible for creating subscription queues
    }

    private void configureQuestionnaireRequestReplyExchangeAndQueue() {
        final DirectExchange exchange = new DirectExchange(QUESTIONNAIRE_EXCHANGE_NAME);
        final Queue queue = new Queue(QUESTIONNAIRE_QUEUE_NAME, true);
        // use queue name as routing key
        final Binding binding = BindingBuilder.bind(queue).to(exchange).with(QUESTIONNAIRE_QUEUE_NAME);

        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(binding);
    }
}
