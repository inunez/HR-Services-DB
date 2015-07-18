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
import entities.Uniform;
import entities.UniformAllowance;
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
public class UniformAllowanceJpaController implements Serializable {

    public UniformAllowanceJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UniformAllowance uniformAllowance) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (uniformAllowance.getUniformCollection() == null) {
            uniformAllowance.setUniformCollection(new ArrayList<Uniform>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Uniform> attachedUniformCollection = new ArrayList<Uniform>();
            for (Uniform uniformCollectionUniformToAttach : uniformAllowance.getUniformCollection()) {
                uniformCollectionUniformToAttach = em.getReference(uniformCollectionUniformToAttach.getClass(), uniformCollectionUniformToAttach.getUniformPK());
                attachedUniformCollection.add(uniformCollectionUniformToAttach);
            }
            uniformAllowance.setUniformCollection(attachedUniformCollection);
            em.persist(uniformAllowance);
            for (Uniform uniformCollectionUniform : uniformAllowance.getUniformCollection()) {
                UniformAllowance oldAllowanceIdOfUniformCollectionUniform = uniformCollectionUniform.getAllowanceId();
                uniformCollectionUniform.setAllowanceId(uniformAllowance);
                uniformCollectionUniform = em.merge(uniformCollectionUniform);
                if (oldAllowanceIdOfUniformCollectionUniform != null) {
                    oldAllowanceIdOfUniformCollectionUniform.getUniformCollection().remove(uniformCollectionUniform);
                    oldAllowanceIdOfUniformCollectionUniform = em.merge(oldAllowanceIdOfUniformCollectionUniform);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUniformAllowance(uniformAllowance.getAllowanceId()) != null) {
                throw new PreexistingEntityException("UniformAllowance " + uniformAllowance + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UniformAllowance uniformAllowance) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UniformAllowance persistentUniformAllowance = em.find(UniformAllowance.class, uniformAllowance.getAllowanceId());
            Collection<Uniform> uniformCollectionOld = persistentUniformAllowance.getUniformCollection();
            Collection<Uniform> uniformCollectionNew = uniformAllowance.getUniformCollection();
            Collection<Uniform> attachedUniformCollectionNew = new ArrayList<Uniform>();
            for (Uniform uniformCollectionNewUniformToAttach : uniformCollectionNew) {
                uniformCollectionNewUniformToAttach = em.getReference(uniformCollectionNewUniformToAttach.getClass(), uniformCollectionNewUniformToAttach.getUniformPK());
                attachedUniformCollectionNew.add(uniformCollectionNewUniformToAttach);
            }
            uniformCollectionNew = attachedUniformCollectionNew;
            uniformAllowance.setUniformCollection(uniformCollectionNew);
            uniformAllowance = em.merge(uniformAllowance);
            for (Uniform uniformCollectionOldUniform : uniformCollectionOld) {
                if (!uniformCollectionNew.contains(uniformCollectionOldUniform)) {
                    uniformCollectionOldUniform.setAllowanceId(null);
                    uniformCollectionOldUniform = em.merge(uniformCollectionOldUniform);
                }
            }
            for (Uniform uniformCollectionNewUniform : uniformCollectionNew) {
                if (!uniformCollectionOld.contains(uniformCollectionNewUniform)) {
                    UniformAllowance oldAllowanceIdOfUniformCollectionNewUniform = uniformCollectionNewUniform.getAllowanceId();
                    uniformCollectionNewUniform.setAllowanceId(uniformAllowance);
                    uniformCollectionNewUniform = em.merge(uniformCollectionNewUniform);
                    if (oldAllowanceIdOfUniformCollectionNewUniform != null && !oldAllowanceIdOfUniformCollectionNewUniform.equals(uniformAllowance)) {
                        oldAllowanceIdOfUniformCollectionNewUniform.getUniformCollection().remove(uniformCollectionNewUniform);
                        oldAllowanceIdOfUniformCollectionNewUniform = em.merge(oldAllowanceIdOfUniformCollectionNewUniform);
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
                Short id = uniformAllowance.getAllowanceId();
                if (findUniformAllowance(id) == null) {
                    throw new NonexistentEntityException("The uniformAllowance with id " + id + " no longer exists.");
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
            UniformAllowance uniformAllowance;
            try {
                uniformAllowance = em.getReference(UniformAllowance.class, id);
                uniformAllowance.getAllowanceId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The uniformAllowance with id " + id + " no longer exists.", enfe);
            }
            Collection<Uniform> uniformCollection = uniformAllowance.getUniformCollection();
            for (Uniform uniformCollectionUniform : uniformCollection) {
                uniformCollectionUniform.setAllowanceId(null);
                uniformCollectionUniform = em.merge(uniformCollectionUniform);
            }
            em.remove(uniformAllowance);
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

    public List<UniformAllowance> findUniformAllowanceEntities() {
        return findUniformAllowanceEntities(true, -1, -1);
    }

    public List<UniformAllowance> findUniformAllowanceEntities(int maxResults, int firstResult) {
        return findUniformAllowanceEntities(false, maxResults, firstResult);
    }

    private List<UniformAllowance> findUniformAllowanceEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UniformAllowance.class));
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

    public UniformAllowance findUniformAllowance(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UniformAllowance.class, id);
        } finally {
            em.close();
        }
    }

    public int getUniformAllowanceCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UniformAllowance> rt = cq.from(UniformAllowance.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
