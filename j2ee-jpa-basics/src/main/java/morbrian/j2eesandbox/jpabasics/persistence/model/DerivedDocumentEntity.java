package morbrian.j2eesandbox.jpabasics.persistence.model;

import javax.persistence.*;

/**
 * Created by morbrian on 6/17/15.
 */
@Entity @Table(name = "derived_document") public class DerivedDocumentEntity
    extends DocumentEntity {

  @Column(name = "checksum", length = 32) String checksum;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "raw_source_id", referencedColumnName = "id") RawDocumentEntity
      rawSourceDocument;

  @Enumerated(EnumType.STRING) @Column(name = "doc_type", length = 255, nullable = false) DocType
      docType;

  public String getChecksum() {
    return checksum;
  }

  public void setChecksum(String checksum) {
    this.checksum = checksum;
  }

  public RawDocumentEntity getRawSourceDocument() {
    return rawSourceDocument;
  }

  public void setRawSourceDocument(RawDocumentEntity rawSourceId) {
    this.rawSourceDocument = rawSourceId;
  }

  public DocType getDocType() {
    return docType;
  }

  public void setDocType(DocType docType) {
    this.docType = docType;
  }
}
