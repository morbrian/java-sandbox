package morbrian.j2eesandbox.jpabasics.persistence.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by morbrian on 6/17/15.
 */
@MappedSuperclass @EntityListeners({DocumentEntityListener.class}) public class DocumentEntity
    extends BaseEntity {

  @NotNull @Column(name = "doc_id", length = 255, unique = true, nullable = false) String docid;

  @Enumerated(EnumType.STRING) @Column(name = "doc_source", length = 255, nullable = false)
  DocSource docSource;

  @Enumerated(EnumType.STRING) @Column(name = "doc_format", length = 255, nullable = false)
  DocFormat docFormat;

  @Column(name = "doc_data", length = 2048000) String docData;

  public String getDocid() {
    return docid;
  }

  public void setDocid(String docid) {
    this.docid = docid;
  }

  public DocSource getDocSource() {
    return docSource;
  }

  public void setDocSource(DocSource docSource) {
    this.docSource = docSource;
  }

  public DocFormat getDocFormat() {
    return docFormat;
  }

  public void setDocFormat(DocFormat docFormat) {
    this.docFormat = docFormat;
  }

  public String getDocData() {
    return docData;
  }

  public void setDocData(String docData) {
    this.docData = docData;
  }

}
