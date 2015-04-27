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
import entities.UniformDeaneLocation;
import entities.Account;
import entities.Uniform;
import entities.UniformAccount;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;
import jsf_classes.exceptions.NonexistentEntityException;
import jsf_classes.exceptions.PreexistingEntityException;
import jsf_classes.exceptions.RollbackFailureException;

/**
 *
 * @author Ismael
 */
public class UniformAccountJpaController implements Serializable {

    public UniformAccountJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(UniformAccount uniformAccount) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (uniformAccount.getUniformCollection() == null) {
            uniformAccount.setUniformCollection(new ArrayList<Uniform>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UniformDeaneLocation uniformDeaneLocation = uniformAccount.getUniformDeaneLocation();
            if (uniformDeaneLocation != null) {
                uniformDeaneLocation = em.getReference(uniformDeaneLocation.getClass(), uniformDeaneLocation.getUniformDeaneLocationPK());
                uniformAccount.setUniformDeaneLocation(uniformDeaneLocation);
            }
            Account account = uniformAccount.getAccount();
            if (account != null) {
                account = em.getReference(account.getClass(), account.getAccountNumber());
                uniformAccount.setAccount(account);
            }
            Collection<Uniform> attachedUniformCollection = new ArrayList<Uniform>();
            for (Uniform uniformCollectionUniformToAttach : uniformAccount.getUniformCollection()) {
                uniformCollectionUniformToAttach = em.getReference(uniformCollectionUniformToAttach.getClass(), uniformCollectionUniformToAttach.getUniformPK());
                attachedUniformCollection.add(uniformCollectionUniformToAttach);
            }
            uniformAccount.setUniformCollection(attachedUniformCollection);
            em.persist(uniformAccount);
            if (uniformDeaneLocation != null) {
                uniformDeaneLocation.getUniformAccountCollection().add(uniformAccount);
                uniformDeaneLocation = em.merge(uniformDeaneLocation);
            }
            if (account != null) {
                account.getUniformAccountCollection().add(uniformAccount);
                account = em.merge(account);
            }
            for (Uniform uniformCollectionUniform : uniformAccount.getUniformCollection()) {
                UniformAccount oldAccountNumberOfUniformCollectionUniform = uniformCollectionUniform.getAccountNumber();
                uniformCollectionUniform.setAccountNumber(uniformAccount);
                uniformCollectionUniform = em.merge(uniformCollectionUniform);
                if (oldAccountNumberOfUniformCollectionUniform != null) {
                    oldAccountNumberOfUniformCollectionUniform.getUniformCollection().remove(uniformCollectionUniform);
                    oldAccountNumberOfUniformCollectionUniform = em.merge(oldAccountNumberOfUniformCollectionUniform);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUniformAccount(uniformAccount.getAccountNumber()) != null) {
                throw new PreexistingEntityException("UniformAccount " + uniformAccount + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(UniformAccount uniformAccount) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UniformAccount persistentUniformAccount = em.find(UniformAccount.class, uniformAccount.getAccountNumber());
            UniformDeaneLocation uniformDeaneLocationOld = persistentUniformAccount.getUniformDeaneLocation();
            UniformDeaneLocation uniformDeaneLocationNew = uniformAccount.getUniformDeaneLocation();
            Account accountOld = persistentUniformAccount.getAccount();
            Account accountNew = uniformAccount.getAccount();
            Collection<Uniform> uniformCollectionOld = persistentUniformAccount.getUniformCollection();
            Collection<Uniform> uniformCollectionNew = uniformAccount.getUniformCollection();
            if (uniformDeaneLocationNew != null) {
                uniformDeaneLocationNew = em.getReference(uniformDeaneLocationNew.getClass(), uniformDeaneLocationNew.getUniformDeaneLocationPK());
                uniformAccount.setUniformDeaneLocation(uniformDeaneLocationNew);
            }
            if (accountNew != null) {
                accountNew = em.getReference(accountNew.getClass(), accountNew.getAccountNumber());
                uniformAccount.setAccount(accountNew);
            }
            Collection<Uniform> attachedUniformCollectionNew = new ArrayList<Uniform>();
            for (Uniform uniformCollectionNewUniformToAttach : uniformCollectionNew) {
                uniformCollectionNewUniformToAttach = em.getReference(uniformCollectionNewUniformToAttach.getClass(), uniformCollectionNewUniformToAttach.getUniformPK());
                attachedUniformCollectionNew.add(uniformCollectionNewUniformToAttach);
            }
            uniformCollectionNew = attachedUniformCollectionNew;
            uniformAccount.setUniformCollection(uniformCollectionNew);
            uniformAccount = em.merge(uniformAccount);
            if (uniformDeaneLocationOld != null && !uniformDeaneLocationOld.equals(uniformDeaneLocationNew)) {
                uniformDeaneLocationOld.getUniformAccountCollection().remove(uniformAccount);
                uniformDeaneLocationOld = em.merge(uniformDeaneLocationOld);
            }
            if (uniformDeaneLocationNew != null && !uniformDeaneLocationNew.equals(uniformDeaneLocationOld)) {
                uniformDeaneLocationNew.getUniformAccountCollection().add(uniformAccount);
                uniformDeaneLocationNew = em.merge(uniformDeaneLocationNew);
            }
            if (accountOld != null && !accountOld.equals(accountNew)) {
                accountOld.getUniformAccountCollection().remove(uniformAccount);
                accountOld = em.merge(accountOld);
            }
            if (accountNew != null && !accountNew.equals(accountOld)) {
                accountNew.getUniformAccountCollection().add(uniformAccount);
                accountNew = em.merge(accountNew);
            }
            for (Uniform uniformCollectionOldUniform : uniformCollectionOld) {
                if (!uniformCollectionNew.contains(uniformCollectionOldUniform)) {
                    uniformCollectionOldUniform.setAccountNumber(null);
                    uniformCollectionOldUniform = em.merge(uniformCollectionOldUniform);
                }
            }
            for (Uniform uniformCollectionNewUniform : uniformCollectionNew) {
                if (!uniformCollectionOld.contains(uniformCollectionNewUniform)) {
                    UniformAccount oldAccountNumberOfUniformCollectionNewUniform = uniformCollectionNewUniform.getAccountNumber();
                    uniformCollectionNewUniform.setAccountNumber(uniformAccount);
                    uniformCollectionNewUniform = em.merge(uniformCollectionNewUniform);
                    if (oldAccountNumberOfUniformCollectionNewUniform != null && !oldAccountNumberOfUniformCollectionNewUniform.equals(uniformAccount)) {
                        oldAccountNumberOfUniformCollectionNewUniform.getUniformCollection().remove(uniformCollectionNewUniform);
                        oldAccountNumberOfUniformCollectionNewUniform = em.merge(oldAccountNumberOfUniformCollectionNewUniform);
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
                String id = uniformAccount.getAccountNumber();
                if (findUniformAccount(id) == null) {
                    throw new NonexistentEntityException("The uniformAccount with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UniformAccount uniformAccount;
            try {
                uniformAccount = em.getReference(UniformAccount.class, id);
                uniformAccount.getAccountNumber();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The uniformAccount with id " + id + " no longer exists.", enfe);
            }
            UniformDeaneLocation uniformDeaneLocation = uniformAccount.getUniformDeaneLocation();
            if (uniformDeaneLocation != null) {
                uniformDeaneLocation.getUniformAccountCollection().remove(uniformAccount);
                uniformDeaneLocation = em.merge(uniformDeaneLocation);
            }
            Account account = uniformAccount.getAccount();
            if (account != null) {
                account.getUniformAccountCollection().remove(uniformAccount);
                account = em.merge(account);
            }
            Collection<Uniform> uniformCollection = uniformAccount.getUniformCollection();
            for (Uniform uniformCollectionUniform : uniformCollection) {
                uniformCollectionUniform.setAccountNumber(null);
                uniformCollectionUniform = em.merge(uniformCollectionUniform);
            }
            em.remove(uniformAccount);
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

    public List<UniformAccount> findUniformAccountEntities() {
        return findUniformAccountEntities(true, -1, -1);
    }

    public List<UniformAccount> findUniformAccountEntities(int maxResults, int firstResult) {
        return findUniformAccountEntities(false, maxResults, firstResult);
    }

    private List<UniformAccount> findUniformAccountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(UniformAccount.class));
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

    public UniformAccount findUniformAccount(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(UniformAccount.class, id);
        } finally {
            em.close();
        }
    }

    public int getUniformAccountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<UniformAccount> rt = cq.from(UniformAccount.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
