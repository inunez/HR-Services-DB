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
import entities.UniformAccount;
import entities.UniformDeaneLocation;
import entities.UniformDeaneLocationPK;
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
public class UniformDeaneLocationJpaController implements Serializable {

    public UniformDeaneLocationJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UniformDeaneLocation uniformDeaneLocation) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (uniformDeaneLocation.getUniformDeaneLocationPK() == null) {
            uniformDeaneLocation.setUniformDeaneLocationPK(new UniformDeaneLocationPK());
        }
        if (uniformDeaneLocation.getUniformAccountCollection() == null) {
            uniformDeaneLocation.setUniformAccountCollection(new ArrayList<UniformAccount>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<UniformAccount> attachedUniformAccountCollection = new ArrayList<UniformAccount>();
            for (UniformAccount uniformAccountCollectionUniformAccountToAttach : uniformDeaneLocation.getUniformAccountCollection()) {
                uniformAccountCollectionUniformAccountToAttach = em.getReference(uniformAccountCollectionUniformAccountToAttach.getClass(), uniformAccountCollectionUniformAccountToAttach.getAccountNumber());
                attachedUniformAccountCollection.add(uniformAccountCollectionUniformAccountToAttach);
            }
            uniformDeaneLocation.setUniformAccountCollection(attachedUniformAccountCollection);
            em.persist(uniformDeaneLocation);
            for (UniformAccount uniformAccountCollectionUniformAccount : uniformDeaneLocation.getUniformAccountCollection()) {
                UniformDeaneLocation oldUniformDeaneLocationOfUniformAccountCollectionUniformAccount = uniformAccountCollectionUniformAccount.getUniformDeaneLocation();
                uniformAccountCollectionUniformAccount.setUniformDeaneLocation(uniformDeaneLocation);
                uniformAccountCollectionUniformAccount = em.merge(uniformAccountCollectionUniformAccount);
                if (oldUniformDeaneLocationOfUniformAccountCollectionUniformAccount != null) {
                    oldUniformDeaneLocationOfUniformAccountCollectionUniformAccount.getUniformAccountCollection().remove(uniformAccountCollectionUniformAccount);
                    oldUniformDeaneLocationOfUniformAccountCollectionUniformAccount = em.merge(oldUniformDeaneLocationOfUniformAccountCollectionUniformAccount);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUniformDeaneLocation(uniformDeaneLocation.getUniformDeaneLocationPK()) != null) {
                throw new PreexistingEntityException("UniformDeaneLocation " + uniformDeaneLocation + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UniformDeaneLocation uniformDeaneLocation) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UniformDeaneLocation persistentUniformDeaneLocation = em.find(UniformDeaneLocation.class, uniformDeaneLocation.getUniformDeaneLocationPK());
            Collection<UniformAccount> uniformAccountCollectionOld = persistentUniformDeaneLocation.getUniformAccountCollection();
            Collection<UniformAccount> uniformAccountCollectionNew = uniformDeaneLocation.getUniformAccountCollection();
            Collection<UniformAccount> attachedUniformAccountCollectionNew = new ArrayList<UniformAccount>();
            for (UniformAccount uniformAccountCollectionNewUniformAccountToAttach : uniformAccountCollectionNew) {
                uniformAccountCollectionNewUniformAccountToAttach = em.getReference(uniformAccountCollectionNewUniformAccountToAttach.getClass(), uniformAccountCollectionNewUniformAccountToAttach.getAccountNumber());
                attachedUniformAccountCollectionNew.add(uniformAccountCollectionNewUniformAccountToAttach);
            }
            uniformAccountCollectionNew = attachedUniformAccountCollectionNew;
            uniformDeaneLocation.setUniformAccountCollection(uniformAccountCollectionNew);
            uniformDeaneLocation = em.merge(uniformDeaneLocation);
            for (UniformAccount uniformAccountCollectionOldUniformAccount : uniformAccountCollectionOld) {
                if (!uniformAccountCollectionNew.contains(uniformAccountCollectionOldUniformAccount)) {
                    uniformAccountCollectionOldUniformAccount.setUniformDeaneLocation(null);
                    uniformAccountCollectionOldUniformAccount = em.merge(uniformAccountCollectionOldUniformAccount);
                }
            }
            for (UniformAccount uniformAccountCollectionNewUniformAccount : uniformAccountCollectionNew) {
                if (!uniformAccountCollectionOld.contains(uniformAccountCollectionNewUniformAccount)) {
                    UniformDeaneLocation oldUniformDeaneLocationOfUniformAccountCollectionNewUniformAccount = uniformAccountCollectionNewUniformAccount.getUniformDeaneLocation();
                    uniformAccountCollectionNewUniformAccount.setUniformDeaneLocation(uniformDeaneLocation);
                    uniformAccountCollectionNewUniformAccount = em.merge(uniformAccountCollectionNewUniformAccount);
                    if (oldUniformDeaneLocationOfUniformAccountCollectionNewUniformAccount != null && !oldUniformDeaneLocationOfUniformAccountCollectionNewUniformAccount.equals(uniformDeaneLocation)) {
                        oldUniformDeaneLocationOfUniformAccountCollectionNewUniformAccount.getUniformAccountCollection().remove(uniformAccountCollectionNewUniformAccount);
                        oldUniformDeaneLocationOfUniformAccountCollectionNewUniformAccount = em.merge(oldUniformDeaneLocationOfUniformAccountCollectionNewUniformAccount);
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
                UniformDeaneLocationPK id = uniformDeaneLocation.getUniformDeaneLocationPK();
                if (findUniformDeaneLocation(id) == null) {
                    throw new NonexistentEntityException("The uniformDeaneLocation with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(UniformDeaneLocationPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UniformDeaneLocation uniformDeaneLocation;
            try {
                uniformDeaneLocation = em.getReference(UniformDeaneLocation.class, id);
                uniformDeaneLocation.getUniformDeaneLocationPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The uniformDeaneLocation with id " + id + " no longer exists.", enfe);
            }
            Collection<UniformAccount> uniformAccountCollection = uniformDeaneLocation.getUniformAccountCollection();
            for (UniformAccount uniformAccountCollectionUniformAccount : uniformAccountCollection) {
                uniformAccountCollectionUniformAccount.setUniformDeaneLocation(null);
                uniformAccountCollectionUniformAccount = em.merge(uniformAccountCollectionUniformAccount);
            }
            em.remove(uniformDeaneLocation);
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

    public List<UniformDeaneLocation> findUniformDeaneLocationEntities() {
        return findUniformDeaneLocationEntities(true, -1, -1);
    }

    public List<UniformDeaneLocation> findUniformDeaneLocationEntities(int maxResults, int firstResult) {
        return findUniformDeaneLocationEntities(false, maxResults, firstResult);
    }

    private List<UniformDeaneLocation> findUniformDeaneLocationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UniformDeaneLocation.class));
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

    public UniformDeaneLocation findUniformDeaneLocation(UniformDeaneLocationPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UniformDeaneLocation.class, id);
        } finally {
            em.close();
        }
    }

    public int getUniformDeaneLocationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UniformDeaneLocation> rt = cq.from(UniformDeaneLocation.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
