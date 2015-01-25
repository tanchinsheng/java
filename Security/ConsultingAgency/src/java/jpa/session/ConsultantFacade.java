package jpa.session;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.entities.Consultant;

/**
 *
 * @author nbuser
 */
@Stateless
public class ConsultantFacade {
    @PersistenceContext(unitName = "ConsultingAgencyPU")
    private EntityManager em;

    public void create(Consultant consultant) {
        em.persist(consultant);
    }

    public void edit(Consultant consultant) {
        em.merge(consultant);
    }

    public void remove(Consultant consultant) {
        em.remove(em.merge(consultant));
    }

    public Consultant find(Object id) {
        return em.find(Consultant.class, id);
    }

    public List<Consultant> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Consultant.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Consultant> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Consultant.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Consultant> rt = cq.from(Consultant.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
