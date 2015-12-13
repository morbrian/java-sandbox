package morbrian.j2eesandbox.jpabasics.digest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.slf4j.Logger;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

import javax.ejb.Singleton;
import javax.inject.Inject;

import morbrian.j2eesandbox.jpabasics.persistence.data.DocumentRepository;
import morbrian.j2eesandbox.jpabasics.persistence.model.DerivedDocumentEntity;
import morbrian.j2eesandbox.jpabasics.persistence.model.DocFormat;
import morbrian.j2eesandbox.jpabasics.persistence.model.DocSource;
import morbrian.j2eesandbox.jpabasics.persistence.model.DocType;
import morbrian.j2eesandbox.jpabasics.persistence.model.RawDocumentEntity;
import morbrian.j2eesandbox.jpabasics.persistence.service.DocumentPersistence;

@Singleton public class DigestController {

  @Inject DocumentPersistence documentPersistence;
  @Inject private Logger log;
  @Inject private DocumentRepository documentRepo;

  public DigestController() {
  }

  public void processData(String docData, DocSource docSource, Calendar docRetrievedDate) {
    // TODO: [BCM] do we want to validate the XML before storing it?
    RawDocumentEntity rawEntity =
        storeRawDocument(docData, docSource, DocFormat.STRING, docRetrievedDate);
    int len = docData.length();
    String trimmedData = docData.substring(0, 4) + docData.substring(len - 1, len);
    processTrimmedData(trimmedData, rawEntity, docSource);
  }

  RawDocumentEntity storeRawDocument(String docData, DocSource docSource, DocFormat docFormat,
      Calendar docRetrievedDate) {
    RawDocumentEntity docEntity = new RawDocumentEntity();
    docEntity.setDocData(docData);
    docEntity.setDocSource(docSource);
    docEntity.setDocFormat(docFormat);
    docEntity.setDocRetrievedDate(docRetrievedDate);
    documentPersistence.createRawDocument(docEntity);
    log.info("Stored Raw Data As DocID: " + docEntity.getDocid());

    return docEntity;
  }

  void processTrimmedData(String trimmedData, RawDocumentEntity rawEntity, DocSource docSource) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      String derivedData = mapper.writeValueAsString(trimmedData);
      String checksum = md5Checksum(derivedData);
      DerivedDocumentEntity docEntity =
          documentRepo.findDerivedDocumentBySourceAndChecksum(docSource, checksum);
      if (docEntity == null) {
        storeAsJsonDocument(derivedData, checksum, rawEntity, docSource, DocType.UNITSTATUS,
            DocFormat.JSON);
      } else {
        log.info(
            "Record already exists for {docSource=" + docSource + ", checksum=" + checksum + " }");
      }
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  void storeAsJsonDocument(String derivedData, String checksum, RawDocumentEntity rawEntity,
      DocSource docSource, DocType docType, DocFormat docFormat) {
    DerivedDocumentEntity docEntity = new DerivedDocumentEntity();
    docEntity.setDocData(derivedData);
    docEntity.setRawSourceDocument(rawEntity);
    docEntity.setDocSource(docSource);
    docEntity.setDocType(docType);
    docEntity.setDocFormat(docFormat);
    docEntity.setChecksum(checksum);
    documentPersistence.createDerivedDocument(docEntity);
    log.info("Stored Derived Data As DocID: " + docEntity.getDocid());
  }

  private String md5Checksum(String input) {
    MessageDigest md5;
    try {
      // hash code from example at: http://stackoverflow.com/questions/415953/how-can-i-generate-an-md5-hash/421696#421696
      md5 = MessageDigest.getInstance(MessageDigestAlgorithms.MD5);
      byte[] digest = md5.digest(input.getBytes());
      BigInteger bigInt = new BigInteger(1, digest);
      String hashtext = bigInt.toString(16);
      // Now we need to zero pad it if you actually want the full 32 chars.
      while (hashtext.length() < 32) {
        hashtext = "0" + hashtext;
      }
      return hashtext;
    } catch (NoSuchAlgorithmException e) {
      log.error("Failed to produce checksum.", e);
      return "FailedChecksum";
    }
  }

  public void requestDataFromClient() {
    DummyClient client = new DummyClient();
    try {
      String docData = new String(client.requestData());
      processData(docData, DocSource.CLIENT, Calendar.getInstance());
    } catch (IOException exc) {
      log.error("Failed to request data from client.", exc);
    }
  }

}
