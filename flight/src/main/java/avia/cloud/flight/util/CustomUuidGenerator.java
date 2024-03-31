package avia.cloud.flight.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

public class CustomUuidGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) {
        Serializable id = (Serializable) session.getEntityPersister(null, obj).getClassMetadata().getIdentifier(obj, session);
        if (id == null) {
            return UUID.randomUUID().toString();
        }
        return id;
    }
}
