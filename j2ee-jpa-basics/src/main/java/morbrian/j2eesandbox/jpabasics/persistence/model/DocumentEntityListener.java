package morbrian.j2eesandbox.jpabasics.persistence.model;

import javax.persistence.PrePersist;
import java.util.UUID;

/**
 * Created by morbrian on 6/18/15.
 */
public class DocumentEntityListener {

    @PrePersist
    public void prePersist(DocumentEntity entity) {
        entity.setDocid(UUID.randomUUID().toString());
    }

}
