package jpa.session;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.entities.Project;

/**
 *
 * @author nbuser
 */
@Stateless
public class ProjectFacade {
    @PersistenceContext(unitName = "ConsultingAgencyPU")
    private EntityManager em;

    public void create(Project project) {
        em.persist(project);
    }

    public void edit(Project project) {
        em.merge(project);
    }

    public void remove(Project project) {
        em.remove(em.merge(project));
    }

    public Project find(Object id) {
        return em.find(Project.class, id);
    }

    public List<Project> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Project.class));
        return em.createQuery(cq).getResultList();
    }

    public List<Project> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Project.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Project> rt = cq.from(Project.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}
