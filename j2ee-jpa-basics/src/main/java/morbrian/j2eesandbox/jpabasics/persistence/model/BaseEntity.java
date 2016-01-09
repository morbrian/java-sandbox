package morbrian.j2eesandbox.jpabasics.persistence.model;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Created by morbrian on 6/17/15.
 */

// This demonstrates how to create a base class using the MappedSuperClass annotation.
// It informs the JPA implementation to include the annotated attributes from
// this superclass as part of the table definition for java subclasses.
// Without this, JPA won't understand the Java class hierarchy.
@MappedSuperclass @EntityListeners({BaseEntityListener.class}) public class BaseEntity {

  @Temporal(TemporalType.TIMESTAMP) @Column(name = "created_time", updatable = false) Calendar
      createdTime;
  @Temporal(TemporalType.TIMESTAMP) @Column(name = "modified_time") Calendar modifiedTime;
  @Column(name = "created_by_uid", updatable = false, length = 255, nullable = false) String
      createdByUid;
  @Column(name = "modified_by_uid", length = 255, nullable = false) String modifiedByUid;
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Calendar getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Calendar createdTime) {
    this.createdTime = createdTime;
  }

  public Calendar getModifiedTime() {
    return modifiedTime;
  }

  public void setModifiedTime(Calendar modifiedTime) {
    this.modifiedTime = modifiedTime;
  }

  public String getCreatedByUid() {
    return createdByUid;
  }

  public void setCreatedByUid(String createdByUid) {
    this.createdByUid = createdByUid;
  }

  public String getModifiedByUid() {
    return modifiedByUid;
  }

  public void setModifiedByUid(String modifiedByUid) {
    this.modifiedByUid = modifiedByUid;
  }

}
