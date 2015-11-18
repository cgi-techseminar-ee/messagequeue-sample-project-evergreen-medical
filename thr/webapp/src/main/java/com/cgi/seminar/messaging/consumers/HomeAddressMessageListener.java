package com.cgi.seminar.messaging.consumers;

import com.cgi.seminar.domain.Location;
import com.cgi.seminar.messaging.messages.HomeAddressMessage;
import com.cgi.seminar.repository.EntityWithExternalIdRepository;
import com.cgi.seminar.repository.LocationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

@Component
public class HomeAddressMessageListener extends MessageListenerBase<Location, HomeAddressMessage> {

    @Inject
    LocationRepository locationRepository;

    @RabbitListener(queues = {"homeaddress-queue"}) // must match MessagingConfiguration
    @Transactional
    void onMessageReceived(Message<HomeAddressMessage> message) {
        createOrUpdateEntityFromMessage(message, Location.class);
    }

    @Override
    protected EntityWithExternalIdRepository<Location> getRepository() {
        return locationRepository;
    }

    @Override
    protected void fillEntityFieldsFromMessage(Location location, HomeAddressMessage msg) {
        location.setAddress(msg.getAddress());
        location.setName(msg.getName());
        location.setLatitude(msg.getLatitude());
        location.setLongitude(msg.getLongitude());
    }
}
