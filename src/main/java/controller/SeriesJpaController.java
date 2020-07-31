package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Episodes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Series;

/**
 *
 * @author simra
 */

public class SeriesJpaController implements Serializable {

    public SeriesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Series series) throws PreexistingEntityException, Exception {
        if (series.getEpisodesCollection() == null) {
            series.setEpisodesCollection(new ArrayList<Episodes>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Episodes> attachedEpisodesCollection = new ArrayList<Episodes>();
            for (Episodes episodesCollectionEpisodesToAttach : series.getEpisodesCollection()) {
                episodesCollectionEpisodesToAttach = em.getReference(episodesCollectionEpisodesToAttach.getClass(), episodesCollectionEpisodesToAttach.getEpisodeid());
                attachedEpisodesCollection.add(episodesCollectionEpisodesToAttach);
            }
            series.setEpisodesCollection(attachedEpisodesCollection);
            em.persist(series);
            for (Episodes episodesCollectionEpisodes : series.getEpisodesCollection()) {
                Series oldSeriesidOfEpisodesCollectionEpisodes = episodesCollectionEpisodes.getSeriesid();
                episodesCollectionEpisodes.setSeriesid(series);
                episodesCollectionEpisodes = em.merge(episodesCollectionEpisodes);
                if (oldSeriesidOfEpisodesCollectionEpisodes != null) {
                    oldSeriesidOfEpisodesCollectionEpisodes.getEpisodesCollection().remove(episodesCollectionEpisodes);
                    oldSeriesidOfEpisodesCollectionEpisodes = em.merge(oldSeriesidOfEpisodesCollectionEpisodes);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findSeries(series.getSeriesid()) != null) {
                throw new PreexistingEntityException("Series " + series + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Series series) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Series persistentSeries = em.find(Series.class, series.getSeriesid());
            Collection<Episodes> episodesCollectionOld = persistentSeries.getEpisodesCollection();
            Collection<Episodes> episodesCollectionNew = series.getEpisodesCollection();
            Collection<Episodes> attachedEpisodesCollectionNew = new ArrayList<Episodes>();
            if(episodesCollectionNew != null){
            for (Episodes episodesCollectionNewEpisodesToAttach : episodesCollectionNew) {
                episodesCollectionNewEpisodesToAttach = em.getReference(episodesCollectionNewEpisodesToAttach.getClass(), episodesCollectionNewEpisodesToAttach.getEpisodeid());
                attachedEpisodesCollectionNew.add(episodesCollectionNewEpisodesToAttach);
            }
            episodesCollectionNew = attachedEpisodesCollectionNew;
            series.setEpisodesCollection(episodesCollectionNew);
            }
            series = em.merge(series);
              if(episodesCollectionOld != null){
            for (Episodes episodesCollectionOldEpisodes : episodesCollectionOld) {
                if (!episodesCollectionNew.contains(episodesCollectionOldEpisodes)) {
                    episodesCollectionOldEpisodes.setSeriesid(null);
                    episodesCollectionOldEpisodes = em.merge(episodesCollectionOldEpisodes);
                }
            }
              }
                 if(episodesCollectionNew != null){
            for (Episodes episodesCollectionNewEpisodes : episodesCollectionNew) {
                if (!episodesCollectionOld.contains(episodesCollectionNewEpisodes)) {
                    Series oldSeriesidOfEpisodesCollectionNewEpisodes = episodesCollectionNewEpisodes.getSeriesid();
                    episodesCollectionNewEpisodes.setSeriesid(series);
                    episodesCollectionNewEpisodes = em.merge(episodesCollectionNewEpisodes);
                    if (oldSeriesidOfEpisodesCollectionNewEpisodes != null && !oldSeriesidOfEpisodesCollectionNewEpisodes.equals(series)) {
                        oldSeriesidOfEpisodesCollectionNewEpisodes.getEpisodesCollection().remove(episodesCollectionNewEpisodes);
                        oldSeriesidOfEpisodesCollectionNewEpisodes = em.merge(oldSeriesidOfEpisodesCollectionNewEpisodes);
                    }
                }
            }}
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                BigDecimal id = series.getSeriesid();
                if (findSeries(id) == null) {
                    throw new NonexistentEntityException("The series with id " + id + " no longer exists.");
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
            Series series;
            try {
                series = em.getReference(Series.class, id);
                series.getSeriesid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The series with id " + id + " no longer exists.", enfe);
            }
            Collection<Episodes> episodesCollection = series.getEpisodesCollection();
            for (Episodes episodesCollectionEpisodes : episodesCollection) {
                episodesCollectionEpisodes.setSeriesid(null);
                episodesCollectionEpisodes = em.merge(episodesCollectionEpisodes);
            }
            em.remove(series);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Series> findSeriesEntities() {
        return findSeriesEntities(true, -1, -1);
    }

    public List<Series> findSeriesEntities(int maxResults, int firstResult) {
        return findSeriesEntities(false, maxResults, firstResult);
    }

    private List<Series> findSeriesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Series.class));
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

    public Series findSeries(BigDecimal id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Series.class, id);
        } finally {
            em.close();
        }
    }

    public int getSeriesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Series> rt = cq.from(Series.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
