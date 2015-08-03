/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session_beans;

import controllers.exceptions.NonexistentEntityException;
import controllers.exceptions.PreexistingEntityException;
import controllers.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import entities.Account;
import entities.Location;
import entities.ManagerPosition;
import entities.ManagerPositionPK;
import entities.Position;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Ismael Nunez
 */
public class ManagerPositionJpaController implements Serializable {

    public ManagerPositionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ManagerPosition managerPosition) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (managerPosition.getManagerPositionPK() == null) {
            managerPosition.setManagerPositionPK(new ManagerPositionPK());
        }
        managerPosition.getManagerPositionPK().setLocationCode(managerPosition.getLocation().getLocationCode());
        managerPosition.getManagerPositionPK().setPositionId(managerPosition.getPosition().getPositionId());
        managerPosition.getManagerPositionPK().setAccountNumber(managerPosition.getAccount().getAccountNumber());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Account account = managerPosition.getAccount();
            if (account != null) {
                account = em.getReference(account.getClass(), account.getAccountNumber());
                managerPosition.setAccount(account);
            }
            Location location = managerPosition.getLocation();
            if (location != null) {
                location = em.getReference(location.getClass(), location.getLocationCode());
                managerPosition.setLocation(location);
            }
            Position position = managerPosition.getPosition();
            if (position != null) {
                position = em.getReference(position.getClass(), position.getPositionId());
                managerPosition.setPosition(position);
            }
            em.persist(managerPosition);
            if (account != null) {
                account.getManagerPositionCollection().add(managerPosition);
                account = em.merge(account);
            }
            if (location != null) {
                location.getManagerPositionCollection().add(managerPosition);
                location = em.merge(location);
            }
            if (position != null) {
                position.getManagerPositionCollection().add(managerPosition);
                position = em.merge(position);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findManagerPosition(managerPosition.getManagerPositionPK()) != null) {
                throw new PreexistingEntityException("ManagerPosition " + managerPosition + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ManagerPosition managerPosition) throws NonexistentEntityException, RollbackFailureException, Exception {
        managerPosition.getManagerPositionPK().setLocationCode(managerPosition.getLocation().getLocationCode());
        managerPosition.getManagerPositionPK().setPositionId(managerPosition.getPosition().getPositionId());
        managerPosition.getManagerPositionPK().setAccountNumber(managerPosition.getAccount().getAccountNumber());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ManagerPosition persistentManagerPosition = em.find(ManagerPosition.class, managerPosition.getManagerPositionPK());
            Account accountOld = persistentManagerPosition.getAccount();
            Account accountNew = managerPosition.getAccount();
            Location locationOld = persistentManagerPosition.getLocation();
            Location locationNew = managerPosition.getLocation();
            Position positionOld = persistentManagerPosition.getPosition();
            Position positionNew = managerPosition.getPosition();
            if (accountNew != null) {
                accountNew = em.getReference(accountNew.getClass(), accountNew.getAccountNumber());
                managerPosition.setAccount(accountNew);
            }
            if (locationNew != null) {
                locationNew = em.getReference(locationNew.getClass(), locationNew.getLocationCode());
                managerPosition.setLocation(locationNew);
            }
            if (positionNew != null) {
                positionNew = em.getReference(positionNew.getClass(), positionNew.getPositionId());
                managerPosition.setPosition(positionNew);
            }
            managerPosition = em.merge(managerPosition);
            if (accountOld != null && !accountOld.equals(accountNew)) {
                accountOld.getManagerPositionCollection().remove(managerPosition);
                accountOld = em.merge(accountOld);
            }
            if (accountNew != null && !accountNew.equals(accountOld)) {
                accountNew.getManagerPositionCollection().add(managerPosition);
                accountNew = em.merge(accountNew);
            }
            if (locationOld != null && !locationOld.equals(locationNew)) {
                locationOld.getManagerPositionCollection().remove(managerPosition);
                locationOld = em.merge(locationOld);
            }
            if (locationNew != null && !locationNew.equals(locationOld)) {
                locationNew.getManagerPositionCollection().add(managerPosition);
                locationNew = em.merge(locationNew);
            }
            if (positionOld != null && !positionOld.equals(positionNew)) {
                positionOld.getManagerPositionCollection().remove(managerPosition);
                positionOld = em.merge(positionOld);
            }
            if (positionNew != null && !positionNew.equals(positionOld)) {
                positionNew.getManagerPositionCollection().add(managerPosition);
                positionNew = em.merge(positionNew);
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
                ManagerPositionPK id = managerPosition.getManagerPositionPK();
                if (findManagerPosition(id) == null) {
                    throw new NonexistentEntityException("The managerPosition with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(ManagerPositionPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ManagerPosition managerPosition;
            try {
                managerPosition = em.getReference(ManagerPosition.class, id);
                managerPosition.getManagerPositionPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The managerPosition with id " + id + " no longer exists.", enfe);
            }
            Account account = managerPosition.getAccount();
            if (account != null) {
                account.getManagerPositionCollection().remove(managerPosition);
                account = em.merge(account);
            }
            Location location = managerPosition.getLocation();
            if (location != null) {
                location.getManagerPositionCollection().remove(managerPosition);
                location = em.merge(location);
            }
            Position position = managerPosition.getPosition();
            if (position != null) {
                position.getManagerPositionCollection().remove(managerPosition);
                position = em.merge(position);
            }
            em.remove(managerPosition);
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

    public List<ManagerPosition> findManagerPositionEntities() {
        return findManagerPositionEntities(true, -1, -1);
    }

    public List<ManagerPosition> findManagerPositionEntities(int maxResults, int firstResult) {
        return findManagerPositionEntities(false, maxResults, firstResult);
    }

    private List<ManagerPosition> findManagerPositionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ManagerPosition.class));
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

    public ManagerPosition findManagerPosition(ManagerPositionPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ManagerPosition.class, id);
        } finally {
            em.close();
        }
    }

    public int getManagerPositionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ManagerPosition> rt = cq.from(ManagerPosition.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
