package morbrian.j2eesandbox.jpabasics.persistence.data;

import morbrian.j2eesandbox.jpabasics.persistence.model.DerivedDocumentEntity;
import morbrian.j2eesandbox.jpabasics.persistence.model.DocSource;
import morbrian.j2eesandbox.jpabasics.persistence.model.RawDocumentEntity;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@ApplicationScoped public class DocumentRepository {
  @Inject private Logger log;

  @Inject private EntityManager em;

  public List<RawDocumentEntity> findAllRawDocumentsOrderedByCreatedTime() {
    return findAllRawDocumentsOrderedBy("createdTime");
  }

  public List<RawDocumentEntity> findAllRawDocumentsOrderedBy(String attrName) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<RawDocumentEntity> criteria = cb.createQuery(RawDocumentEntity.class);
    Root<RawDocumentEntity> entity = criteria.from(RawDocumentEntity.class);
    // the order by attribute string refers to the java class attribute not the column name
    criteria.select(entity).orderBy(cb.asc(entity.get(attrName)));
    return em.createQuery(criteria).getResultList();
  }

  public RawDocumentEntity findRawDocumentById(String docid) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<RawDocumentEntity> criteria = cb.createQuery(RawDocumentEntity.class);
    Root<RawDocumentEntity> entity = criteria.from(RawDocumentEntity.class);
    Predicate condition = cb.equal(entity.get("docid"), docid);
    // the order by attribute string refers to the java class attribute not the column name
    criteria.select(entity).where(condition);
    try {
      return em.createQuery(criteria).getSingleResult();
    } catch (NoResultException exc) {
      return null;
    }
  }

  public List<DerivedDocumentEntity> findAllDerivedDocumentsOrderedByCreatedTime() {
    return findAllDerivedDocumentsOrderedBy("createdTime");
  }

  public List<DerivedDocumentEntity> findAllDerivedDocumentsOrderedBy(String attrName) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<DerivedDocumentEntity> criteria = cb.createQuery(DerivedDocumentEntity.class);
    Root<DerivedDocumentEntity> entity = criteria.from(DerivedDocumentEntity.class);
    // the order by attribute string refers to the java class attribute not the column name
    criteria.select(entity).orderBy(cb.asc(entity.get(attrName)));
    return em.createQuery(criteria).getResultList();
  }

  public DerivedDocumentEntity findDerivedDocumentById(String docid) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<DerivedDocumentEntity> criteria = cb.createQuery(DerivedDocumentEntity.class);
    Root<DerivedDocumentEntity> entity = criteria.from(DerivedDocumentEntity.class);
    Predicate condition = cb.equal(entity.get("docid"), docid);
    // the order by attribute string refers to the java class attribute not the column name
    criteria.select(entity).where(condition);
    try {
      return em.createQuery(criteria).getSingleResult();
    } catch (NoResultException exc) {
      return null;
    }
  }

  public DerivedDocumentEntity findDerivedDocumentBySourceAndChecksum(DocSource docSource,
      String checksum) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<DerivedDocumentEntity> criteria = cb.createQuery(DerivedDocumentEntity.class);
    Root<DerivedDocumentEntity> entity = criteria.from(DerivedDocumentEntity.class);
    Predicate conditionChecksum = cb.equal(entity.get("checksum"), checksum);
    Predicate conditionSource = cb.equal(entity.get("docSource"), docSource);
    // the order by attribute string refers to the java class attribute not the column name
    criteria.select(entity).where(conditionChecksum, conditionSource);
    try {
      return em.createQuery(criteria).getSingleResult();
    } catch (NoResultException exc) {
      return null;
    }
  }
}
