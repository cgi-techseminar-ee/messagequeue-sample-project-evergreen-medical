package com.cgi.seminar.messaging.consumers;

import com.cgi.seminar.domain.Location;
import com.cgi.seminar.messaging.messages.HomeAddressMessage;
import com.cgi.seminar.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

import static com.cgi.seminar.messaging.consumers.GenericMessageHandler.createOrUpdateEntityFromMessage;

@Component
public class HomeAddressMessageListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    LocationRepository locationRepository;

    @RabbitListener(queues = {"homeaddress-queue"}) // must match MessagingConfiguration
    @Transactional
    void onMessageReceived(Message<HomeAddressMessage> message) {
        createOrUpdateEntityFromMessage(message, Location.class, locationRepository, (location, msg) -> {
            location.setAddress(msg.getAddress());
            location.setName(msg.getName());
            location.setLatitude(msg.getLatitude());
            location.setLongitude(msg.getLongitude());
        });
    }
}
