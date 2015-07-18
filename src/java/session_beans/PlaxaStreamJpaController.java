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
import entities.PlaxaStream;
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
public class PlaxaStreamJpaController implements Serializable {

    public PlaxaStreamJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PlaxaStream plaxaStream) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (plaxaStream.getPlaxaAccountCollection() == null) {
            plaxaStream.setPlaxaAccountCollection(new ArrayList<PlaxaAccount>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<PlaxaAccount> attachedPlaxaAccountCollection = new ArrayList<PlaxaAccount>();
            for (PlaxaAccount plaxaAccountCollectionPlaxaAccountToAttach : plaxaStream.getPlaxaAccountCollection()) {
                plaxaAccountCollectionPlaxaAccountToAttach = em.getReference(plaxaAccountCollectionPlaxaAccountToAttach.getClass(), plaxaAccountCollectionPlaxaAccountToAttach.getAccountNumber());
                attachedPlaxaAccountCollection.add(plaxaAccountCollectionPlaxaAccountToAttach);
            }
            plaxaStream.setPlaxaAccountCollection(attachedPlaxaAccountCollection);
            em.persist(plaxaStream);
            for (PlaxaAccount plaxaAccountCollectionPlaxaAccount : plaxaStream.getPlaxaAccountCollection()) {
                PlaxaStream oldStreamIdOfPlaxaAccountCollectionPlaxaAccount = plaxaAccountCollectionPlaxaAccount.getStreamId();
                plaxaAccountCollectionPlaxaAccount.setStreamId(plaxaStream);
                plaxaAccountCollectionPlaxaAccount = em.merge(plaxaAccountCollectionPlaxaAccount);
                if (oldStreamIdOfPlaxaAccountCollectionPlaxaAccount != null) {
                    oldStreamIdOfPlaxaAccountCollectionPlaxaAccount.getPlaxaAccountCollection().remove(plaxaAccountCollectionPlaxaAccount);
                    oldStreamIdOfPlaxaAccountCollectionPlaxaAccount = em.merge(oldStreamIdOfPlaxaAccountCollectionPlaxaAccount);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPlaxaStream(plaxaStream.getStreamId()) != null) {
                throw new PreexistingEntityException("PlaxaStream " + plaxaStream + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PlaxaStream plaxaStream) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PlaxaStream persistentPlaxaStream = em.find(PlaxaStream.class, plaxaStream.getStreamId());
            Collection<PlaxaAccount> plaxaAccountCollectionOld = persistentPlaxaStream.getPlaxaAccountCollection();
            Collection<PlaxaAccount> plaxaAccountCollectionNew = plaxaStream.getPlaxaAccountCollection();
            Collection<PlaxaAccount> attachedPlaxaAccountCollectionNew = new ArrayList<PlaxaAccount>();
            for (PlaxaAccount plaxaAccountCollectionNewPlaxaAccountToAttach : plaxaAccountCollectionNew) {
                plaxaAccountCollectionNewPlaxaAccountToAttach = em.getReference(plaxaAccountCollectionNewPlaxaAccountToAttach.getClass(), plaxaAccountCollectionNewPlaxaAccountToAttach.getAccountNumber());
                attachedPlaxaAccountCollectionNew.add(plaxaAccountCollectionNewPlaxaAccountToAttach);
            }
            plaxaAccountCollectionNew = attachedPlaxaAccountCollectionNew;
            plaxaStream.setPlaxaAccountCollection(plaxaAccountCollectionNew);
            plaxaStream = em.merge(plaxaStream);
            for (PlaxaAccount plaxaAccountCollectionOldPlaxaAccount : plaxaAccountCollectionOld) {
                if (!plaxaAccountCollectionNew.contains(plaxaAccountCollectionOldPlaxaAccount)) {
                    plaxaAccountCollectionOldPlaxaAccount.setStreamId(null);
                    plaxaAccountCollectionOldPlaxaAccount = em.merge(plaxaAccountCollectionOldPlaxaAccount);
                }
            }
            for (PlaxaAccount plaxaAccountCollectionNewPlaxaAccount : plaxaAccountCollectionNew) {
                if (!plaxaAccountCollectionOld.contains(plaxaAccountCollectionNewPlaxaAccount)) {
                    PlaxaStream oldStreamIdOfPlaxaAccountCollectionNewPlaxaAccount = plaxaAccountCollectionNewPlaxaAccount.getStreamId();
                    plaxaAccountCollectionNewPlaxaAccount.setStreamId(plaxaStream);
                    plaxaAccountCollectionNewPlaxaAccount = em.merge(plaxaAccountCollectionNewPlaxaAccount);
                    if (oldStreamIdOfPlaxaAccountCollectionNewPlaxaAccount != null && !oldStreamIdOfPlaxaAccountCollectionNewPlaxaAccount.equals(plaxaStream)) {
                        oldStreamIdOfPlaxaAccountCollectionNewPlaxaAccount.getPlaxaAccountCollection().remove(plaxaAccountCollectionNewPlaxaAccount);
                        oldStreamIdOfPlaxaAccountCollectionNewPlaxaAccount = em.merge(oldStreamIdOfPlaxaAccountCollectionNewPlaxaAccount);
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
                Short id = plaxaStream.getStreamId();
                if (findPlaxaStream(id) == null) {
                    throw new NonexistentEntityException("The plaxaStream with id " + id + " no longer exists.");
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
            PlaxaStream plaxaStream;
            try {
                plaxaStream = em.getReference(PlaxaStream.class, id);
                plaxaStream.getStreamId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The plaxaStream with id " + id + " no longer exists.", enfe);
            }
            Collection<PlaxaAccount> plaxaAccountCollection = plaxaStream.getPlaxaAccountCollection();
            for (PlaxaAccount plaxaAccountCollectionPlaxaAccount : plaxaAccountCollection) {
                plaxaAccountCollectionPlaxaAccount.setStreamId(null);
                plaxaAccountCollectionPlaxaAccount = em.merge(plaxaAccountCollectionPlaxaAccount);
            }
            em.remove(plaxaStream);
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

    public List<PlaxaStream> findPlaxaStreamEntities() {
        return findPlaxaStreamEntities(true, -1, -1);
    }

    public List<PlaxaStream> findPlaxaStreamEntities(int maxResults, int firstResult) {
        return findPlaxaStreamEntities(false, maxResults, firstResult);
    }

    private List<PlaxaStream> findPlaxaStreamEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PlaxaStream.class));
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

    public PlaxaStream findPlaxaStream(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlaxaStream.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlaxaStreamCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PlaxaStream> rt = cq.from(PlaxaStream.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
