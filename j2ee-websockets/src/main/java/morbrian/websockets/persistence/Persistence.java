package morbrian.websockets.persistence;

import morbrian.websockets.model.BaseEntity;
import morbrian.websockets.model.ForumEntity;
import morbrian.websockets.model.MessageEntity;
import org.slf4j.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.security.Principal;

@Stateless public class Persistence {
  @Inject private Logger logger;
  @Inject private Principal principal;
  @Inject private EntityManager em;

  public BaseEntity createEntity(BaseEntity entity) {
    entity.setCreatedByUid(principal.getName());
    entity.setModifiedByUid(principal.getName());
    em.persist(entity);
    return entity;
  }

  public BaseEntity updateEntity(BaseEntity entity) {
    entity.setModifiedByUid(principal.getName());
    return em.merge(entity);
  }

  public void removeEntity(BaseEntity entity) {
    em.remove(em.find(entity.getClass(), entity.getId()));
  }

  public ForumEntity createForum(ForumEntity entity) {
    return (ForumEntity) createEntity(entity);
  }

  public ForumEntity updateForum(ForumEntity entity) {
    return (ForumEntity) updateEntity(entity);
  }

  public MessageEntity createMessage(MessageEntity entity) {
    return (MessageEntity) createEntity(entity);
  }

}
