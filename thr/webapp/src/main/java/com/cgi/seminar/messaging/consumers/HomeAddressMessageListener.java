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

@Component
public class HomeAddressMessageListener {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Inject
    LocationRepository locationRepository;

    @RabbitListener(queues = {"homeaddress-queue"}) // must match MessagingConfiguration
    @Transactional
    void onMessageReceived(Message<HomeAddressMessage> message) {
        HomeAddressMessage homeAddressMessage = message.getPayload();
        log.info("Received HomeAddress message: {}", homeAddressMessage);
        String externalId = homeAddressMessage.getId();
        Location location = locationRepository.findByExternalId(externalId);
        if (location == null) {
            location = new Location();
            location.setExternalId(externalId);
        }
        location.setAddress(homeAddressMessage.getAddress());
        location.setName(homeAddressMessage.getName());
        location.setLatitude(homeAddressMessage.getLatitude());
        location.setLongitude(homeAddressMessage.getLongitude());
        locationRepository.save(location);
    }
}
