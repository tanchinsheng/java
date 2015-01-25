package jpa.session;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.entities.Recruiter;

/**
 *
 * @author nbuser
 */
@Stateless
public class RecruiterFacade {
    @PersistenceContext(unitName = "ConsultingAgencyPU")
    private EntityManager em;

    public void create(Recruiter recruiter) {
        em.persist(recruiter);
    }

    public void edit(Recruiter recruiter) {
        em.merge(recruiter);
    }

    public void remove(Recruiter recruiter) {
        em.remove(em.merge(recruiter));
    }

    public Recruiter find(Object id) {
        return em.find(Recruiter.class, id);
    }

    public List<Recruiter> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Recruiter.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Recruiter> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Recruiter.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Recruiter> rt = cq.from(Recruiter.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
