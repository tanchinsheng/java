package jpa.session;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.entities.ConsultantStatus;

/**
 *
 * @author nbuser
 */
@Stateless
public class ConsultantStatusFacade {
    @PersistenceContext(unitName = "ConsultingAgencyPU")
    private EntityManager em;

    public void create(ConsultantStatus consultantStatus) {
        em.persist(consultantStatus);
    }

    public void edit(ConsultantStatus consultantStatus) {
        em.merge(consultantStatus);
    }

    public void remove(ConsultantStatus consultantStatus) {
        em.remove(em.merge(consultantStatus));
    }

    public ConsultantStatus find(Object id) {
        return em.find(ConsultantStatus.class, id);
    }

    public List<ConsultantStatus> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(ConsultantStatus.class));
        return em.createQuery(cq).getResultList();
    }

    public List<ConsultantStatus> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(ConsultantStatus.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<ConsultantStatus> rt = cq.from(ConsultantStatus.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
