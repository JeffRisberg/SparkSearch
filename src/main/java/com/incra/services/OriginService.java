package com.incra.services;

import com.incra.models.Origin;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * The <i>OriginService</i> handles the JPA-based updating of Origin entities.
 *
 * @author Jeffrey Risberg
 * @since February 2014
 */
@Transactional
@Repository
public class OriginService {

    @PersistenceContext
    private EntityManager em;

    public List<Origin> findEntityList() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Origin> criteria = cb.createQuery(Origin.class);
        criteria.from(Origin.class);

        return em.createQuery(criteria).getResultList();
    }

    public Origin findEntityById(int id) {
        return em.find(Origin.class, id);
    }

    public Origin findEntityByName(String name) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Origin> criteria = builder.createQuery(Origin.class);
        Root<Origin> root = criteria.from(Origin.class);

        Path<String> rootName = root.get("name");
        criteria.where(builder.equal(rootName, name));

        try {
            return em.createQuery(criteria).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void save(Origin Origin) {
        if (Origin.getId() == null || Origin.getId() == 0) {
            em.persist(Origin);
        } else {
            em.merge(Origin);
        }
    }

    public void delete(Origin Origin) {
        this.delete(Origin.getId());
    }

    public void delete(int OriginId) {
        Origin existingOrigin = this.findEntityById(OriginId);
        if (null != existingOrigin) {
            em.remove(existingOrigin);
        }
    }
}
