package morbrian.j2eesandbox.jpabasics.persistence.model;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Calendar;

/**
 * Created by morbrian on 6/18/15.
 */
public class BaseEntityListener {

  @PrePersist public void prePersist(BaseEntity entity) {
    Calendar now = Calendar.getInstance();
    entity.setCreatedTime(now);
    entity.setModifiedTime(now);
  }

  @PreUpdate public void preUpdate(BaseEntity entity) {
    Calendar now = Calendar.getInstance();
    entity.setModifiedTime(now);
  }

}
