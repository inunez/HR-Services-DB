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
import entities.PlaxaRegion;
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
public class PlaxaRegionJpaController implements Serializable {

    public PlaxaRegionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PlaxaRegion plaxaRegion) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (plaxaRegion.getPlaxaAccountCollection() == null) {
            plaxaRegion.setPlaxaAccountCollection(new ArrayList<PlaxaAccount>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<PlaxaAccount> attachedPlaxaAccountCollection = new ArrayList<PlaxaAccount>();
            for (PlaxaAccount plaxaAccountCollectionPlaxaAccountToAttach : plaxaRegion.getPlaxaAccountCollection()) {
                plaxaAccountCollectionPlaxaAccountToAttach = em.getReference(plaxaAccountCollectionPlaxaAccountToAttach.getClass(), plaxaAccountCollectionPlaxaAccountToAttach.getAccountNumber());
                attachedPlaxaAccountCollection.add(plaxaAccountCollectionPlaxaAccountToAttach);
            }
            plaxaRegion.setPlaxaAccountCollection(attachedPlaxaAccountCollection);
            em.persist(plaxaRegion);
            for (PlaxaAccount plaxaAccountCollectionPlaxaAccount : plaxaRegion.getPlaxaAccountCollection()) {
                PlaxaRegion oldRegionIdOfPlaxaAccountCollectionPlaxaAccount = plaxaAccountCollectionPlaxaAccount.getRegionId();
                plaxaAccountCollectionPlaxaAccount.setRegionId(plaxaRegion);
                plaxaAccountCollectionPlaxaAccount = em.merge(plaxaAccountCollectionPlaxaAccount);
                if (oldRegionIdOfPlaxaAccountCollectionPlaxaAccount != null) {
                    oldRegionIdOfPlaxaAccountCollectionPlaxaAccount.getPlaxaAccountCollection().remove(plaxaAccountCollectionPlaxaAccount);
                    oldRegionIdOfPlaxaAccountCollectionPlaxaAccount = em.merge(oldRegionIdOfPlaxaAccountCollectionPlaxaAccount);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPlaxaRegion(plaxaRegion.getRegionId()) != null) {
                throw new PreexistingEntityException("PlaxaRegion " + plaxaRegion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PlaxaRegion plaxaRegion) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PlaxaRegion persistentPlaxaRegion = em.find(PlaxaRegion.class, plaxaRegion.getRegionId());
            Collection<PlaxaAccount> plaxaAccountCollectionOld = persistentPlaxaRegion.getPlaxaAccountCollection();
            Collection<PlaxaAccount> plaxaAccountCollectionNew = plaxaRegion.getPlaxaAccountCollection();
            Collection<PlaxaAccount> attachedPlaxaAccountCollectionNew = new ArrayList<PlaxaAccount>();
            for (PlaxaAccount plaxaAccountCollectionNewPlaxaAccountToAttach : plaxaAccountCollectionNew) {
                plaxaAccountCollectionNewPlaxaAccountToAttach = em.getReference(plaxaAccountCollectionNewPlaxaAccountToAttach.getClass(), plaxaAccountCollectionNewPlaxaAccountToAttach.getAccountNumber());
                attachedPlaxaAccountCollectionNew.add(plaxaAccountCollectionNewPlaxaAccountToAttach);
            }
            plaxaAccountCollectionNew = attachedPlaxaAccountCollectionNew;
            plaxaRegion.setPlaxaAccountCollection(plaxaAccountCollectionNew);
            plaxaRegion = em.merge(plaxaRegion);
            for (PlaxaAccount plaxaAccountCollectionOldPlaxaAccount : plaxaAccountCollectionOld) {
                if (!plaxaAccountCollectionNew.contains(plaxaAccountCollectionOldPlaxaAccount)) {
                    plaxaAccountCollectionOldPlaxaAccount.setRegionId(null);
                    plaxaAccountCollectionOldPlaxaAccount = em.merge(plaxaAccountCollectionOldPlaxaAccount);
                }
            }
            for (PlaxaAccount plaxaAccountCollectionNewPlaxaAccount : plaxaAccountCollectionNew) {
                if (!plaxaAccountCollectionOld.contains(plaxaAccountCollectionNewPlaxaAccount)) {
                    PlaxaRegion oldRegionIdOfPlaxaAccountCollectionNewPlaxaAccount = plaxaAccountCollectionNewPlaxaAccount.getRegionId();
                    plaxaAccountCollectionNewPlaxaAccount.setRegionId(plaxaRegion);
                    plaxaAccountCollectionNewPlaxaAccount = em.merge(plaxaAccountCollectionNewPlaxaAccount);
                    if (oldRegionIdOfPlaxaAccountCollectionNewPlaxaAccount != null && !oldRegionIdOfPlaxaAccountCollectionNewPlaxaAccount.equals(plaxaRegion)) {
                        oldRegionIdOfPlaxaAccountCollectionNewPlaxaAccount.getPlaxaAccountCollection().remove(plaxaAccountCollectionNewPlaxaAccount);
                        oldRegionIdOfPlaxaAccountCollectionNewPlaxaAccount = em.merge(oldRegionIdOfPlaxaAccountCollectionNewPlaxaAccount);
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
                Short id = plaxaRegion.getRegionId();
                if (findPlaxaRegion(id) == null) {
                    throw new NonexistentEntityException("The plaxaRegion with id " + id + " no longer exists.");
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
            PlaxaRegion plaxaRegion;
            try {
                plaxaRegion = em.getReference(PlaxaRegion.class, id);
                plaxaRegion.getRegionId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The plaxaRegion with id " + id + " no longer exists.", enfe);
            }
            Collection<PlaxaAccount> plaxaAccountCollection = plaxaRegion.getPlaxaAccountCollection();
            for (PlaxaAccount plaxaAccountCollectionPlaxaAccount : plaxaAccountCollection) {
                plaxaAccountCollectionPlaxaAccount.setRegionId(null);
                plaxaAccountCollectionPlaxaAccount = em.merge(plaxaAccountCollectionPlaxaAccount);
            }
            em.remove(plaxaRegion);
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

    public List<PlaxaRegion> findPlaxaRegionEntities() {
        return findPlaxaRegionEntities(true, -1, -1);
    }

    public List<PlaxaRegion> findPlaxaRegionEntities(int maxResults, int firstResult) {
        return findPlaxaRegionEntities(false, maxResults, firstResult);
    }

    private List<PlaxaRegion> findPlaxaRegionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PlaxaRegion.class));
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

    public PlaxaRegion findPlaxaRegion(Short id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlaxaRegion.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlaxaRegionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PlaxaRegion> rt = cq.from(PlaxaRegion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
