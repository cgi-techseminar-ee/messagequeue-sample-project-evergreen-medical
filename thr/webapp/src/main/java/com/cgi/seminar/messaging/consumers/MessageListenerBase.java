package com.cgi.seminar.messaging.consumers;

import com.cgi.seminar.domain.EntityWithExternalId;
import com.cgi.seminar.messaging.messages.MessageWithId;
import com.cgi.seminar.repository.EntityWithExternalIdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;

public abstract class MessageListenerBase<T extends EntityWithExternalId, M extends MessageWithId> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    void createOrUpdateEntityFromMessage(Message<M> genericMessage, Class<T> cls) {
        M message = genericMessage.getPayload();
        log.info("Received {}: {}", message.getClass().getSimpleName(), message);
        String externalId = message.getId();
        T entity = getRepository().findByExternalId(externalId);
        if (entity == null) {
            try {
                entity = cls.newInstance();
            } catch (IllegalAccessException | InstantiationException e) {
                throw new RuntimeException(e);
            }
            entity.setExternalId(externalId);
        }
        fillEntityFieldsFromMessage(entity, message);
        getRepository().save(entity);
    }

    protected abstract EntityWithExternalIdRepository<T> getRepository();

    protected abstract void fillEntityFieldsFromMessage(T entity, M message);
}
