package com.cgi.seminar.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Component
public class MessagingConfigurationImpl implements MessagingConfiguration {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    private RabbitTemplate messagingTemplate;

    @Inject
    private AmqpAdmin amqpAdmin;

    @Inject
    private SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;

    @PostConstruct
    public void init() {
        log.info("Configuring messaging");

        messagingTemplate.setMessageConverter(new JsonMessageConverter());
        rabbitListenerContainerFactory.setMessageConverter(new JsonMessageConverter());

        configureWorkorderPublishSubscribeExchange();
        configureQuestionnaireRequestReplyExchangeAndQueue();
        configureNurseDirectExchangeAndQueue();
        configureHomeAddressDirectExchangeAndQueue();
    }

    private void configureWorkorderPublishSubscribeExchange() {
        final FanoutExchange exchange = new FanoutExchange(WORKORDER_EXCHANGE_NAME);
        amqpAdmin.declareExchange(exchange);
        // subscribers are responsible for creating subscription queues
    }

    private void configureQuestionnaireRequestReplyExchangeAndQueue() {
        configureDirectExchangeAndQueue(QUESTIONNAIRE_EXCHANGE_NAME, QUESTIONNAIRE_QUEUE_NAME);
    }

    private void configureNurseDirectExchangeAndQueue() {
        configureDirectExchangeAndQueue(NURSE_EXCHANGE_NAME, NURSE_QUEUE_NAME);
    }

    private void configureHomeAddressDirectExchangeAndQueue() {
        configureDirectExchangeAndQueue(HOMEADDRESS_EXCHANGE_NAME, HOMEADDRESS_QUEUE_NAME);
    }

    private void configureDirectExchangeAndQueue(final String exchangeName, final String queueName) {
        final DirectExchange exchange = new DirectExchange(exchangeName);
        final Queue queue = new Queue(queueName, true);
        // use queue name as routing key
        final Binding binding = BindingBuilder.bind(queue).to(exchange).with(queueName);

        amqpAdmin.declareExchange(exchange);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(binding);
    }
}
