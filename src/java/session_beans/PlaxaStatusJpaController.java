/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session_beans;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Plaxa;
import entities.PlaxaStatus;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import controllers.exceptions.RollbackFailureException;

/**
 *
 * @author Ismael
 */
public class PlaxaStatusJpaController implements Serializable {

    public PlaxaStatusJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PlaxaStatus plaxaStatus) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (plaxaStatus.getPlaxaCollection() == null) {
            plaxaStatus.setPlaxaCollection(new ArrayList<Plaxa>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Plaxa> attachedPlaxaCollection = new ArrayList<Plaxa>();
            for (Plaxa plaxaCollectionPlaxaToAttach : plaxaStatus.getPlaxaCollection()) {
                plaxaCollectionPlaxaToAttach = em.getReference(plaxaCollectionPlaxaToAttach.getClass(), plaxaCollectionPlaxaToAttach.getPlaxaPK());
                attachedPlaxaCollection.add(plaxaCollectionPlaxaToAttach);
            }
            plaxaStatus.setPlaxaCollection(attachedPlaxaCollection);
            em.persist(plaxaStatus);
            for (Plaxa plaxaCollectionPlaxa : plaxaStatus.getPlaxaCollection()) {
                PlaxaStatus oldStatusCodeOfPlaxaCollectionPlaxa = plaxaCollectionPlaxa.getStatusCode();
                plaxaCollectionPlaxa.setStatusCode(plaxaStatus);
                plaxaCollectionPlaxa = em.merge(plaxaCollectionPlaxa);
                if (oldStatusCodeOfPlaxaCollectionPlaxa != null) {
                    oldStatusCodeOfPlaxaCollectionPlaxa.getPlaxaCollection().remove(plaxaCollectionPlaxa);
                    oldStatusCodeOfPlaxaCollectionPlaxa = em.merge(oldStatusCodeOfPlaxaCollectionPlaxa);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPlaxaStatus(plaxaStatus.getStatusCode()) != null) {
                throw new PreexistingEntityException("PlaxaStatus " + plaxaStatus + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PlaxaStatus plaxaStatus) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PlaxaStatus persistentPlaxaStatus = em.find(PlaxaStatus.class, plaxaStatus.getStatusCode());
            Collection<Plaxa> plaxaCollectionOld = persistentPlaxaStatus.getPlaxaCollection();
            Collection<Plaxa> plaxaCollectionNew = plaxaStatus.getPlaxaCollection();
            Collection<Plaxa> attachedPlaxaCollectionNew = new ArrayList<Plaxa>();
            for (Plaxa plaxaCollectionNewPlaxaToAttach : plaxaCollectionNew) {
                plaxaCollectionNewPlaxaToAttach = em.getReference(plaxaCollectionNewPlaxaToAttach.getClass(), plaxaCollectionNewPlaxaToAttach.getPlaxaPK());
                attachedPlaxaCollectionNew.add(plaxaCollectionNewPlaxaToAttach);
            }
            plaxaCollectionNew = attachedPlaxaCollectionNew;
            plaxaStatus.setPlaxaCollection(plaxaCollectionNew);
            plaxaStatus = em.merge(plaxaStatus);
            for (Plaxa plaxaCollectionOldPlaxa : plaxaCollectionOld) {
                if (!plaxaCollectionNew.contains(plaxaCollectionOldPlaxa)) {
                    plaxaCollectionOldPlaxa.setStatusCode(null);
                    plaxaCollectionOldPlaxa = em.merge(plaxaCollectionOldPlaxa);
                }
            }
            for (Plaxa plaxaCollectionNewPlaxa : plaxaCollectionNew) {
                if (!plaxaCollectionOld.contains(plaxaCollectionNewPlaxa)) {
                    PlaxaStatus oldStatusCodeOfPlaxaCollectionNewPlaxa = plaxaCollectionNewPlaxa.getStatusCode();
                    plaxaCollectionNewPlaxa.setStatusCode(plaxaStatus);
                    plaxaCollectionNewPlaxa = em.merge(plaxaCollectionNewPlaxa);
                    if (oldStatusCodeOfPlaxaCollectionNewPlaxa != null && !oldStatusCodeOfPlaxaCollectionNewPlaxa.equals(plaxaStatus)) {
                        oldStatusCodeOfPlaxaCollectionNewPlaxa.getPlaxaCollection().remove(plaxaCollectionNewPlaxa);
                        oldStatusCodeOfPlaxaCollectionNewPlaxa = em.merge(oldStatusCodeOfPlaxaCollectionNewPlaxa);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Short id = plaxaStatus.getStatusCode();
                if (findPlaxaStatus(id) == null) {
                    throw new NonexistentEntityException("The plaxaStatus with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Short id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PlaxaStatus plaxaStatus;
            try {
                plaxaStatus = em.getReference(PlaxaStatus.class, id);
                plaxaStatus.getStatusCode();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The plaxaStatus with id " + id + " no longer exists.", enfe);
            }
            Collection<Plaxa> plaxaCollection = plaxaStatus.getPlaxaCollection();
            for (Plaxa plaxaCollectionPlaxa : plaxaCollection) {
                plaxaCollectionPlaxa.setStatusCode(null);
                plaxaCollectionPlaxa = em.merge(plaxaCollectionPlaxa);
            }
            em.remove(plaxaStatus);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PlaxaStatus> findPlaxaStatusEntities() {
        return findPlaxaStatusEntities(true, -1, -1);
    }

    public List<PlaxaStatus> findPlaxaStatusEntities(int maxResults, int firstResult) {
        return findPlaxaStatusEntities(false, maxResults, firstResult);
    }

    private List<PlaxaStatus> findPlaxaStatusEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PlaxaStatus.class));
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

    public PlaxaStatus findPlaxaStatus(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlaxaStatus.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlaxaStatusCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PlaxaStatus> rt = cq.from(PlaxaStatus.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
