package jpa.session;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.entities.Client;

/**
 *
 * @author nbuser
 */
@Stateless
public class ClientFacade {
    @PersistenceContext(unitName = "ConsultingAgencyPU")
    private EntityManager em;

    public void create(Client client) {
        em.persist(client);
    }

    public void edit(Client client) {
        em.merge(client);
    }

    public void remove(Client client) {
        em.remove(em.merge(client));
    }

    public Client find(Object id) {
        return em.find(Client.class, id);
    }

    public List<Client> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Client.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Client> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Client.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Client> rt = cq.from(Client.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
