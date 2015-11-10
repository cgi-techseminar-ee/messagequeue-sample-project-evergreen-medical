package com.cgi.seminar.messaging.consumers;

import com.cgi.seminar.domain.EntityWithExternalId;
import com.cgi.seminar.messaging.messages.MessageWithId;
import com.cgi.seminar.repository.EntityWithExternalIdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

import java.util.function.BiConsumer;

public final class GenericMessageHandler {

    protected static final Logger log = LoggerFactory.getLogger(GenericMessageHandler.class);

    static <T extends EntityWithExternalId, M extends MessageWithId>
    void createOrUpdateEntityFromMessage(Message<M> genericMessage, Class<T> cls,
                                         EntityWithExternalIdRepository<T> repository,
                                         BiConsumer<T, M> fillFields) {
        M message = genericMessage.getPayload();
        log.info("Received {}: {}", message.getClass().getSimpleName(), message);
        String externalId = message.getId();
        T entity = repository.findByExternalId(externalId);
        if (entity == null) {
            try {
                entity = cls.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
            entity.setExternalId(externalId);
        }
        fillFields.accept(entity, message);
        repository.save(entity);
    }
}
