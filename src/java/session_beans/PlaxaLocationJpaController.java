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
import entities.PlaxaAccount;
import entities.PlaxaLocation;
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
public class PlaxaLocationJpaController implements Serializable {

    public PlaxaLocationJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PlaxaLocation plaxaLocation) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (plaxaLocation.getPlaxaAccountCollection() == null) {
            plaxaLocation.setPlaxaAccountCollection(new ArrayList<PlaxaAccount>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<PlaxaAccount> attachedPlaxaAccountCollection = new ArrayList<PlaxaAccount>();
            for (PlaxaAccount plaxaAccountCollectionPlaxaAccountToAttach : plaxaLocation.getPlaxaAccountCollection()) {
                plaxaAccountCollectionPlaxaAccountToAttach = em.getReference(plaxaAccountCollectionPlaxaAccountToAttach.getClass(), plaxaAccountCollectionPlaxaAccountToAttach.getAccountNumber());
                attachedPlaxaAccountCollection.add(plaxaAccountCollectionPlaxaAccountToAttach);
            }
            plaxaLocation.setPlaxaAccountCollection(attachedPlaxaAccountCollection);
            em.persist(plaxaLocation);
            for (PlaxaAccount plaxaAccountCollectionPlaxaAccount : plaxaLocation.getPlaxaAccountCollection()) {
                PlaxaLocation oldLocationIdOfPlaxaAccountCollectionPlaxaAccount = plaxaAccountCollectionPlaxaAccount.getLocationId();
                plaxaAccountCollectionPlaxaAccount.setLocationId(plaxaLocation);
                plaxaAccountCollectionPlaxaAccount = em.merge(plaxaAccountCollectionPlaxaAccount);
                if (oldLocationIdOfPlaxaAccountCollectionPlaxaAccount != null) {
                    oldLocationIdOfPlaxaAccountCollectionPlaxaAccount.getPlaxaAccountCollection().remove(plaxaAccountCollectionPlaxaAccount);
                    oldLocationIdOfPlaxaAccountCollectionPlaxaAccount = em.merge(oldLocationIdOfPlaxaAccountCollectionPlaxaAccount);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPlaxaLocation(plaxaLocation.getLocationId()) != null) {
                throw new PreexistingEntityException("PlaxaLocation " + plaxaLocation + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PlaxaLocation plaxaLocation) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PlaxaLocation persistentPlaxaLocation = em.find(PlaxaLocation.class, plaxaLocation.getLocationId());
            Collection<PlaxaAccount> plaxaAccountCollectionOld = persistentPlaxaLocation.getPlaxaAccountCollection();
            Collection<PlaxaAccount> plaxaAccountCollectionNew = plaxaLocation.getPlaxaAccountCollection();
            Collection<PlaxaAccount> attachedPlaxaAccountCollectionNew = new ArrayList<PlaxaAccount>();
            for (PlaxaAccount plaxaAccountCollectionNewPlaxaAccountToAttach : plaxaAccountCollectionNew) {
                plaxaAccountCollectionNewPlaxaAccountToAttach = em.getReference(plaxaAccountCollectionNewPlaxaAccountToAttach.getClass(), plaxaAccountCollectionNewPlaxaAccountToAttach.getAccountNumber());
                attachedPlaxaAccountCollectionNew.add(plaxaAccountCollectionNewPlaxaAccountToAttach);
            }
            plaxaAccountCollectionNew = attachedPlaxaAccountCollectionNew;
            plaxaLocation.setPlaxaAccountCollection(plaxaAccountCollectionNew);
            plaxaLocation = em.merge(plaxaLocation);
            for (PlaxaAccount plaxaAccountCollectionOldPlaxaAccount : plaxaAccountCollectionOld) {
                if (!plaxaAccountCollectionNew.contains(plaxaAccountCollectionOldPlaxaAccount)) {
                    plaxaAccountCollectionOldPlaxaAccount.setLocationId(null);
                    plaxaAccountCollectionOldPlaxaAccount = em.merge(plaxaAccountCollectionOldPlaxaAccount);
                }
            }
            for (PlaxaAccount plaxaAccountCollectionNewPlaxaAccount : plaxaAccountCollectionNew) {
                if (!plaxaAccountCollectionOld.contains(plaxaAccountCollectionNewPlaxaAccount)) {
                    PlaxaLocation oldLocationIdOfPlaxaAccountCollectionNewPlaxaAccount = plaxaAccountCollectionNewPlaxaAccount.getLocationId();
                    plaxaAccountCollectionNewPlaxaAccount.setLocationId(plaxaLocation);
                    plaxaAccountCollectionNewPlaxaAccount = em.merge(plaxaAccountCollectionNewPlaxaAccount);
                    if (oldLocationIdOfPlaxaAccountCollectionNewPlaxaAccount != null && !oldLocationIdOfPlaxaAccountCollectionNewPlaxaAccount.equals(plaxaLocation)) {
                        oldLocationIdOfPlaxaAccountCollectionNewPlaxaAccount.getPlaxaAccountCollection().remove(plaxaAccountCollectionNewPlaxaAccount);
                        oldLocationIdOfPlaxaAccountCollectionNewPlaxaAccount = em.merge(oldLocationIdOfPlaxaAccountCollectionNewPlaxaAccount);
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
                Short id = plaxaLocation.getLocationId();
                if (findPlaxaLocation(id) == null) {
                    throw new NonexistentEntityException("The plaxaLocation with id " + id + " no longer exists.");
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
            PlaxaLocation plaxaLocation;
            try {
                plaxaLocation = em.getReference(PlaxaLocation.class, id);
                plaxaLocation.getLocationId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The plaxaLocation with id " + id + " no longer exists.", enfe);
            }
            Collection<PlaxaAccount> plaxaAccountCollection = plaxaLocation.getPlaxaAccountCollection();
            for (PlaxaAccount plaxaAccountCollectionPlaxaAccount : plaxaAccountCollection) {
                plaxaAccountCollectionPlaxaAccount.setLocationId(null);
                plaxaAccountCollectionPlaxaAccount = em.merge(plaxaAccountCollectionPlaxaAccount);
            }
            em.remove(plaxaLocation);
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

    public List<PlaxaLocation> findPlaxaLocationEntities() {
        return findPlaxaLocationEntities(true, -1, -1);
    }

    public List<PlaxaLocation> findPlaxaLocationEntities(int maxResults, int firstResult) {
        return findPlaxaLocationEntities(false, maxResults, firstResult);
    }

    private List<PlaxaLocation> findPlaxaLocationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PlaxaLocation.class));
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

    public PlaxaLocation findPlaxaLocation(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlaxaLocation.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlaxaLocationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PlaxaLocation> rt = cq.from(PlaxaLocation.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
