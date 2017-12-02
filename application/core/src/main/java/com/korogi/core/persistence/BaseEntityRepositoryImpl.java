package com.korogi.core.persistence;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.korogi.core.domain.BaseEntity;

/**
 * Abstract base class for classes that do CRUD operations to the database.
 *
 * @param <E> the entity to do CRUD operations with
 *
 * @author Daan Peelman
 *
 * @see BaseEntity
 * @see EntityRepository
 */
public abstract class BaseEntityRepositoryImpl<E extends BaseEntity> implements EntityRepository<E> {
    @PersistenceContext
    protected EntityManager em;

    private Class<E> entityClass;

    public BaseEntityRepositoryImpl(Class<E> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public E findById(Long id) {
        return em.find(entityClass, id);
    }

    @Override
    public E saveOrUpdate(E entity) {
        if(entity.getId() == null) {
            return save(entity);
        } else {
            return update(entity);
        }
    }

    private E save(E entity) {
        em.persist(entity);
        return entity;
    }

    private E update(E entity) {
        return em.merge(entity);
    }

    @Override
    public void delete(E entity) {
        em.remove(entity);
    }
}