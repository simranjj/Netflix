package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Episodes;
import model.Series;

/**
 *
 * @author simra
 */
public class EpisodesJpaController implements Serializable {

    public EpisodesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Episodes episodes) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Series seriesid = episodes.getSeriesid();
            if (seriesid != null) {
                seriesid = em.getReference(seriesid.getClass(), seriesid.getSeriesid());
                episodes.setSeriesid(seriesid);
            }
            em.persist(episodes);
            if (seriesid != null) {
                seriesid.getEpisodesCollection().add(episodes);
                seriesid = em.merge(seriesid);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findEpisodes(episodes.getEpisodeid()) != null) {
                throw new PreexistingEntityException("Episodes " + episodes + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Episodes episodes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Episodes persistentEpisodes = em.find(Episodes.class, episodes.getEpisodeid());
            Series seriesidOld = persistentEpisodes.getSeriesid();
            Series seriesidNew = episodes.getSeriesid();
            if (seriesidNew != null) {
                seriesidNew = em.getReference(seriesidNew.getClass(), seriesidNew.getSeriesid());
                episodes.setSeriesid(seriesidNew);
            }
            episodes = em.merge(episodes);
            if (seriesidOld != null && !seriesidOld.equals(seriesidNew)) {
                seriesidOld.getEpisodesCollection().remove(episodes);
                seriesidOld = em.merge(seriesidOld);
            }
            if (seriesidNew != null && !seriesidNew.equals(seriesidOld)) {
                seriesidNew.getEpisodesCollection().add(episodes);
                seriesidNew = em.merge(seriesidNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = episodes.getEpisodeid();
                if (findEpisodes(id) == null) {
                    throw new NonexistentEntityException("The episodes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(BigDecimal id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Episodes episodes;
            try {
                episodes = em.getReference(Episodes.class, id);
                episodes.getEpisodeid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The episodes with id " + id + " no longer exists.", enfe);
            }
            Series seriesid = episodes.getSeriesid();
            if (seriesid != null) {
                seriesid.getEpisodesCollection().remove(episodes);
                seriesid = em.merge(seriesid);
            }
            em.remove(episodes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Episodes> findEpisodesBySeries(BigDecimal id) {
        EntityManager em = null;
        List<Episodes> ArrayList;
        List<Episodes> episodeList = new ArrayList<>();
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Series seriesid = em.getReference(Series.class, id);
            episodeList =  em.createQuery(
                    "SELECT c FROM Episodes c WHERE c.seriesid = :series")
                    .setParameter("series", seriesid)
                    .getResultList();
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }

        return episodeList;
    }

    public List<Episodes> findEpisodesEntities() {
        return findEpisodesEntities(true, -1, -1);
    }

    public List<Episodes> findEpisodesEntities(int maxResults, int firstResult) {
        return findEpisodesEntities(false, maxResults, firstResult);
    }

    private List<Episodes> findEpisodesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Episodes.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Episodes findEpisodes(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Episodes.class, id);
        } finally {
            em.close();
        }
    }

    public int getEpisodesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Episodes> rt = cq.from(Episodes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
