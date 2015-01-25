package jpa.session;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.entities.Address;

/**
 *
 * @author nbuser
 */
@Stateless
public class AddressFacade {
    @PersistenceContext(unitName = "ConsultingAgencyPU")
    private EntityManager em;

    public void create(Address address) {
        em.persist(address);
    }

    public void edit(Address address) {
        em.merge(address);
    }

    public void remove(Address address) {
        em.remove(em.merge(address));
    }

    public Address find(Object id) {
        return em.find(Address.class, id);
    }

    public List<Address> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Address.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Address> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Address.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Address> rt = cq.from(Address.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
