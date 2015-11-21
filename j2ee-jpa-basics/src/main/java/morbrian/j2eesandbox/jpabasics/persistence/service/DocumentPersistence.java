package morbrian.j2eesandbox.jpabasics.persistence.service;

import morbrian.j2eesandbox.jpabasics.persistence.model.DerivedDocumentEntity;
import morbrian.j2eesandbox.jpabasics.persistence.model.RawDocumentEntity;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 * Created by morbrian on 6/17/15.
 */
@Stateless
public class DocumentPersistence {
    @Inject
    private Logger log;

    @Inject
    private EntityManager em;

    public RawDocumentEntity createRawDocument(RawDocumentEntity entity) {
        entity.setCreatedByUid("SYSTEM");
        entity.setModifiedByUid("SYSTEM");
        em.persist(entity);
        return entity;
    }

    public DerivedDocumentEntity createDerivedDocument(DerivedDocumentEntity entity) {
        entity.setCreatedByUid("SYSTEM");
        entity.setModifiedByUid("SYSTEM");
        em.persist(entity);
        return entity;
    }

}
