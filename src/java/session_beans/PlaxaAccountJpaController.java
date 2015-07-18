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
import entities.Account;
import entities.PlaxaLocation;
import entities.PlaxaRegion;
import entities.PlaxaStream;
import entities.Plaxa;
import entities.PlaxaAccount;
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
public class PlaxaAccountJpaController implements Serializable {

    public PlaxaAccountJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PlaxaAccount plaxaAccount) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (plaxaAccount.getPlaxaCollection() == null) {
            plaxaAccount.setPlaxaCollection(new ArrayList<Plaxa>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Account account = plaxaAccount.getAccount();
            if (account != null) {
                account = em.getReference(account.getClass(), account.getAccountNumber());
                plaxaAccount.setAccount(account);
            }
            PlaxaLocation locationId = plaxaAccount.getLocationId();
            if (locationId != null) {
                locationId = em.getReference(locationId.getClass(), locationId.getLocationId());
                plaxaAccount.setLocationId(locationId);
            }
            PlaxaRegion regionId = plaxaAccount.getRegionId();
            if (regionId != null) {
                regionId = em.getReference(regionId.getClass(), regionId.getRegionId());
                plaxaAccount.setRegionId(regionId);
            }
            PlaxaStream streamId = plaxaAccount.getStreamId();
            if (streamId != null) {
                streamId = em.getReference(streamId.getClass(), streamId.getStreamId());
                plaxaAccount.setStreamId(streamId);
            }
            Collection<Plaxa> attachedPlaxaCollection = new ArrayList<Plaxa>();
            for (Plaxa plaxaCollectionPlaxaToAttach : plaxaAccount.getPlaxaCollection()) {
                plaxaCollectionPlaxaToAttach = em.getReference(plaxaCollectionPlaxaToAttach.getClass(), plaxaCollectionPlaxaToAttach.getPlaxaPK());
                attachedPlaxaCollection.add(plaxaCollectionPlaxaToAttach);
            }
            plaxaAccount.setPlaxaCollection(attachedPlaxaCollection);
            em.persist(plaxaAccount);
            if (account != null) {
                account.getPlaxaAccountCollection().add(plaxaAccount);
                account = em.merge(account);
            }
            if (locationId != null) {
                locationId.getPlaxaAccountCollection().add(plaxaAccount);
                locationId = em.merge(locationId);
            }
            if (regionId != null) {
                regionId.getPlaxaAccountCollection().add(plaxaAccount);
                regionId = em.merge(regionId);
            }
            if (streamId != null) {
                streamId.getPlaxaAccountCollection().add(plaxaAccount);
                streamId = em.merge(streamId);
            }
            for (Plaxa plaxaCollectionPlaxa : plaxaAccount.getPlaxaCollection()) {
                PlaxaAccount oldAccountNumberOfPlaxaCollectionPlaxa = plaxaCollectionPlaxa.getAccountNumber();
                plaxaCollectionPlaxa.setAccountNumber(plaxaAccount);
                plaxaCollectionPlaxa = em.merge(plaxaCollectionPlaxa);
                if (oldAccountNumberOfPlaxaCollectionPlaxa != null) {
                    oldAccountNumberOfPlaxaCollectionPlaxa.getPlaxaCollection().remove(plaxaCollectionPlaxa);
                    oldAccountNumberOfPlaxaCollectionPlaxa = em.merge(oldAccountNumberOfPlaxaCollectionPlaxa);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPlaxaAccount(plaxaAccount.getAccountNumber()) != null) {
                throw new PreexistingEntityException("PlaxaAccount " + plaxaAccount + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PlaxaAccount plaxaAccount) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            PlaxaAccount persistentPlaxaAccount = em.find(PlaxaAccount.class, plaxaAccount.getAccountNumber());
            Account accountOld = persistentPlaxaAccount.getAccount();
            Account accountNew = plaxaAccount.getAccount();
            PlaxaLocation locationIdOld = persistentPlaxaAccount.getLocationId();
            PlaxaLocation locationIdNew = plaxaAccount.getLocationId();
            PlaxaRegion regionIdOld = persistentPlaxaAccount.getRegionId();
            PlaxaRegion regionIdNew = plaxaAccount.getRegionId();
            PlaxaStream streamIdOld = persistentPlaxaAccount.getStreamId();
            PlaxaStream streamIdNew = plaxaAccount.getStreamId();
            Collection<Plaxa> plaxaCollectionOld = persistentPlaxaAccount.getPlaxaCollection();
            Collection<Plaxa> plaxaCollectionNew = plaxaAccount.getPlaxaCollection();
            if (accountNew != null) {
                accountNew = em.getReference(accountNew.getClass(), accountNew.getAccountNumber());
                plaxaAccount.setAccount(accountNew);
            }
            if (locationIdNew != null) {
                locationIdNew = em.getReference(locationIdNew.getClass(), locationIdNew.getLocationId());
                plaxaAccount.setLocationId(locationIdNew);
            }
            if (regionIdNew != null) {
                regionIdNew = em.getReference(regionIdNew.getClass(), regionIdNew.getRegionId());
                plaxaAccount.setRegionId(regionIdNew);
            }
            if (streamIdNew != null) {
                streamIdNew = em.getReference(streamIdNew.getClass(), streamIdNew.getStreamId());
                plaxaAccount.setStreamId(streamIdNew);
            }
            Collection<Plaxa> attachedPlaxaCollectionNew = new ArrayList<Plaxa>();
            for (Plaxa plaxaCollectionNewPlaxaToAttach : plaxaCollectionNew) {
                plaxaCollectionNewPlaxaToAttach = em.getReference(plaxaCollectionNewPlaxaToAttach.getClass(), plaxaCollectionNewPlaxaToAttach.getPlaxaPK());
                attachedPlaxaCollectionNew.add(plaxaCollectionNewPlaxaToAttach);
            }
            plaxaCollectionNew = attachedPlaxaCollectionNew;
            plaxaAccount.setPlaxaCollection(plaxaCollectionNew);
            plaxaAccount = em.merge(plaxaAccount);
            if (accountOld != null && !accountOld.equals(accountNew)) {
                accountOld.getPlaxaAccountCollection().remove(plaxaAccount);
                accountOld = em.merge(accountOld);
            }
            if (accountNew != null && !accountNew.equals(accountOld)) {
                accountNew.getPlaxaAccountCollection().add(plaxaAccount);
                accountNew = em.merge(accountNew);
            }
            if (locationIdOld != null && !locationIdOld.equals(locationIdNew)) {
                locationIdOld.getPlaxaAccountCollection().remove(plaxaAccount);
                locationIdOld = em.merge(locationIdOld);
            }
            if (locationIdNew != null && !locationIdNew.equals(locationIdOld)) {
                locationIdNew.getPlaxaAccountCollection().add(plaxaAccount);
                locationIdNew = em.merge(locationIdNew);
            }
            if (regionIdOld != null && !regionIdOld.equals(regionIdNew)) {
                regionIdOld.getPlaxaAccountCollection().remove(plaxaAccount);
                regionIdOld = em.merge(regionIdOld);
            }
            if (regionIdNew != null && !regionIdNew.equals(regionIdOld)) {
                regionIdNew.getPlaxaAccountCollection().add(plaxaAccount);
                regionIdNew = em.merge(regionIdNew);
            }
            if (streamIdOld != null && !streamIdOld.equals(streamIdNew)) {
                streamIdOld.getPlaxaAccountCollection().remove(plaxaAccount);
                streamIdOld = em.merge(streamIdOld);
            }
            if (streamIdNew != null && !streamIdNew.equals(streamIdOld)) {
                streamIdNew.getPlaxaAccountCollection().add(plaxaAccount);
                streamIdNew = em.merge(streamIdNew);
            }
            for (Plaxa plaxaCollectionOldPlaxa : plaxaCollectionOld) {
                if (!plaxaCollectionNew.contains(plaxaCollectionOldPlaxa)) {
                    plaxaCollectionOldPlaxa.setAccountNumber(null);
                    plaxaCollectionOldPlaxa = em.merge(plaxaCollectionOldPlaxa);
                }
            }
            for (Plaxa plaxaCollectionNewPlaxa : plaxaCollectionNew) {
                if (!plaxaCollectionOld.contains(plaxaCollectionNewPlaxa)) {
                    PlaxaAccount oldAccountNumberOfPlaxaCollectionNewPlaxa = plaxaCollectionNewPlaxa.getAccountNumber();
                    plaxaCollectionNewPlaxa.setAccountNumber(plaxaAccount);
                    plaxaCollectionNewPlaxa = em.merge(plaxaCollectionNewPlaxa);
                    if (oldAccountNumberOfPlaxaCollectionNewPlaxa != null && !oldAccountNumberOfPlaxaCollectionNewPlaxa.equals(plaxaAccount)) {
                        oldAccountNumberOfPlaxaCollectionNewPlaxa.getPlaxaCollection().remove(plaxaCollectionNewPlaxa);
                        oldAccountNumberOfPlaxaCollectionNewPlaxa = em.merge(oldAccountNumberOfPlaxaCollectionNewPlaxa);
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
                String id = plaxaAccount.getAccountNumber();
                if (findPlaxaAccount(id) == null) {
                    throw new NonexistentEntityException("The plaxaAccount with id " + id + " no longer exists.");
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
            PlaxaAccount plaxaAccount;
            try {
                plaxaAccount = em.getReference(PlaxaAccount.class, id);
                plaxaAccount.getAccountNumber();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The plaxaAccount with id " + id + " no longer exists.", enfe);
            }
            Account account = plaxaAccount.getAccount();
            if (account != null) {
                account.getPlaxaAccountCollection().remove(plaxaAccount);
                account = em.merge(account);
            }
            PlaxaLocation locationId = plaxaAccount.getLocationId();
            if (locationId != null) {
                locationId.getPlaxaAccountCollection().remove(plaxaAccount);
                locationId = em.merge(locationId);
            }
            PlaxaRegion regionId = plaxaAccount.getRegionId();
            if (regionId != null) {
                regionId.getPlaxaAccountCollection().remove(plaxaAccount);
                regionId = em.merge(regionId);
            }
            PlaxaStream streamId = plaxaAccount.getStreamId();
            if (streamId != null) {
                streamId.getPlaxaAccountCollection().remove(plaxaAccount);
                streamId = em.merge(streamId);
            }
            Collection<Plaxa> plaxaCollection = plaxaAccount.getPlaxaCollection();
            for (Plaxa plaxaCollectionPlaxa : plaxaCollection) {
                plaxaCollectionPlaxa.setAccountNumber(null);
                plaxaCollectionPlaxa = em.merge(plaxaCollectionPlaxa);
            }
            em.remove(plaxaAccount);
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

    public List<PlaxaAccount> findPlaxaAccountEntities() {
        return findPlaxaAccountEntities(true, -1, -1);
    }

    public List<PlaxaAccount> findPlaxaAccountEntities(int maxResults, int firstResult) {
        return findPlaxaAccountEntities(false, maxResults, firstResult);
    }

    private List<PlaxaAccount> findPlaxaAccountEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PlaxaAccount.class));
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

    public PlaxaAccount findPlaxaAccount(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PlaxaAccount.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlaxaAccountCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PlaxaAccount> rt = cq.from(PlaxaAccount.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
