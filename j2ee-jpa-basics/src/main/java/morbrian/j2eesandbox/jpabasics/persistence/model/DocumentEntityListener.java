package morbrian.j2eesandbox.jpabasics.persistence.model;

import java.util.UUID;

import javax.persistence.PrePersist;

/**
 * Created by morbrian on 6/18/15.
 */
public class DocumentEntityListener {

  @PrePersist public void prePersist(DocumentEntity entity) {
    entity.setDocid(UUID.randomUUID().toString());
  }

}
