package morbrian.j2eesandbox.jpabasics.persistence.model;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Created by morbrian on 6/17/15.
 */
@Entity @Table(name = "raw_document") public class RawDocumentEntity extends DocumentEntity {

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "doc_retrieved_date", length = 255, nullable = false) Calendar docRetrievedDate;

  public Calendar getDocRetrievedDate() {
    return docRetrievedDate;
  }

  public void setDocRetrievedDate(Calendar docRetrievedDate) {
    this.docRetrievedDate = docRetrievedDate;
  }

}
