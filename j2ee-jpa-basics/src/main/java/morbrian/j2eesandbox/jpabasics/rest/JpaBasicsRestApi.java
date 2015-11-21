package morbrian.j2eesandbox.jpabasics.rest;

import morbrian.j2eesandbox.jpabasics.digest.DigestController;
import morbrian.j2eesandbox.jpabasics.persistence.data.DocumentRepository;
import morbrian.j2eesandbox.jpabasics.persistence.model.DerivedDocumentEntity;
import morbrian.j2eesandbox.jpabasics.persistence.model.RawDocumentEntity;
import morbrian.j2eesandbox.jpabasics.persistence.service.DocumentPersistence;
import org.slf4j.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;


// TODO: [BCM] we will either decide what methods we need for system troubleshooting, or delete the REST service entirely.
// TODO: at the moment it is only here to facilitate interactive testing when needed.
@Path("/document")
@RequestScoped
public class JpaBasicsRestApi {

    @Inject
    private Logger log;

    @Inject
    private DocumentRepository documentRepo;

    @Inject
    DocumentPersistence documentPersistence;

    @Inject
    DigestController controller;

    @GET
    @Path("/raw")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RawDocumentEntity> findAllRawDocuments() {
        List<RawDocumentEntity> docs = documentRepo.findAllRawDocumentsOrderedByCreatedTime();
        log.info("Found " + docs.size() + " docs.");
        return docs;
    }

    @GET
    @Path("/raw/{docid}")
    @Produces(MediaType.APPLICATION_JSON)
    public RawDocumentEntity findRawDocumentById(@PathParam("docid") String docid) {
        return documentRepo.findRawDocumentById(docid);
    }

    @GET
    @Path("/derived")
    @Produces(MediaType.APPLICATION_JSON)
    public List<DerivedDocumentEntity> findAllDerivedDocuments() {
        List<DerivedDocumentEntity> docs = documentRepo.findAllDerivedDocumentsOrderedByCreatedTime();
        log.info("Found " + docs.size() + " docs.");
        return docs;
    }

    @GET
    @Path("/derived/{docid}")
    @Produces(MediaType.APPLICATION_JSON)
    public DerivedDocumentEntity findDerivedDocumentById(@PathParam("docid") String docid) {
        return documentRepo.findDerivedDocumentById(docid);
    }

    @GET
    @Path("/fetch/data")
    @Produces(MediaType.APPLICATION_JSON)
    public String fetchRawDocuments() {
        controller.requestDataFromClient();
        return "Data Generation Request Forced, Check Logs, You Should See the DocID We Just Created";
    }

}
