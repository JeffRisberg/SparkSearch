package com.incra.services;

import com.incra.models.Facility;
import com.incra.models.Site;
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
 * The <i>FacilityService</i> handles the JPA-based updating of Facility entities.
 *
 * @author Jeffrey Risberg
 * @since February 2014
 */
@Transactional
@Repository
public class FacilityService {

    @PersistenceContext
    private EntityManager em;

    public List<Facility> findEntityList() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Facility> criteria = cb.createQuery(Facility.class);
        criteria.from(Facility.class);

        return em.createQuery(criteria).getResultList();
    }

    public Facility findEntityById(int id) {
        return em.find(Facility.class, id);
    }

    public Facility findEntityByTitle(String title) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Facility> criteria = builder.createQuery(Facility.class);
        Root<Facility> root = criteria.from(Facility.class);

        Path<String> rootTitle = root.get("title");
        criteria.where(builder.equal(rootTitle, title));

        try {
            return em.createQuery(criteria).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public void save(Facility Facility) {
        if (Facility.getId() == null || Facility.getId() == 0) {
            em.persist(Facility);
        } else {
            em.merge(Facility);
        }
    }

    public void delete(Facility Facility) {
        this.delete(Facility.getId());
    }

    public void delete(int FacilityId) {
        Facility existingFacility = this.findEntityById(FacilityId);
        if (null != existingFacility) {
            em.remove(existingFacility);
        }
    }
}
