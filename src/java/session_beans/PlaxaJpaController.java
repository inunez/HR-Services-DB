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
import entities.Employee;
import entities.Plaxa;
import entities.PlaxaAccount;
import entities.PlaxaPK;
import entities.PlaxaStatus;
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
public class PlaxaJpaController implements Serializable {

    public PlaxaJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Plaxa plaxa) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (plaxa.getPlaxaPK() == null) {
            plaxa.setPlaxaPK(new PlaxaPK());
        }
        plaxa.getPlaxaPK().setIdNumber(plaxa.getEmployee().getEmployeePK().getIdNumber());
        plaxa.getPlaxaPK().setStatus(plaxa.getEmployee().getEmployeePK().getStatus());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Employee employee = plaxa.getEmployee();
            if (employee != null) {
                employee = em.getReference(employee.getClass(), employee.getEmployeePK());
                plaxa.setEmployee(employee);
            }
            PlaxaAccount accountNumber = plaxa.getAccountNumber();
            if (accountNumber != null) {
                accountNumber = em.getReference(accountNumber.getClass(), accountNumber.getAccountNumber());
                plaxa.setAccountNumber(accountNumber);
            }
            PlaxaStatus statusCode = plaxa.getStatusCode();
            if (statusCode != null) {
                statusCode = em.getReference(statusCode.getClass(), statusCode.getStatusCode());
                plaxa.setStatusCode(statusCode);
            }
            em.persist(plaxa);
            if (employee != null) {
                employee.getPlaxaCollection().add(plaxa);
                employee = em.merge(employee);
            }
            if (accountNumber != null) {
                accountNumber.getPlaxaCollection().add(plaxa);
                accountNumber = em.merge(accountNumber);
            }
            if (statusCode != null) {
                statusCode.getPlaxaCollection().add(plaxa);
                statusCode = em.merge(statusCode);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findPlaxa(plaxa.getPlaxaPK()) != null) {
                throw new PreexistingEntityException("Plaxa " + plaxa + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Plaxa plaxa) throws NonexistentEntityException, RollbackFailureException, Exception {
        plaxa.getPlaxaPK().setIdNumber(plaxa.getEmployee().getEmployeePK().getIdNumber());
        plaxa.getPlaxaPK().setStatus(plaxa.getEmployee().getEmployeePK().getStatus());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Plaxa persistentPlaxa = em.find(Plaxa.class, plaxa.getPlaxaPK());
            Employee employeeOld = persistentPlaxa.getEmployee();
            Employee employeeNew = plaxa.getEmployee();
            PlaxaAccount accountNumberOld = persistentPlaxa.getAccountNumber();
            PlaxaAccount accountNumberNew = plaxa.getAccountNumber();
            PlaxaStatus statusCodeOld = persistentPlaxa.getStatusCode();
            PlaxaStatus statusCodeNew = plaxa.getStatusCode();
            if (employeeNew != null) {
                employeeNew = em.getReference(employeeNew.getClass(), employeeNew.getEmployeePK());
                plaxa.setEmployee(employeeNew);
            }
            if (accountNumberNew != null) {
                accountNumberNew = em.getReference(accountNumberNew.getClass(), accountNumberNew.getAccountNumber());
                plaxa.setAccountNumber(accountNumberNew);
            }
            if (statusCodeNew != null) {
                statusCodeNew = em.getReference(statusCodeNew.getClass(), statusCodeNew.getStatusCode());
                plaxa.setStatusCode(statusCodeNew);
            }
            plaxa = em.merge(plaxa);
            if (employeeOld != null && !employeeOld.equals(employeeNew)) {
                employeeOld.getPlaxaCollection().remove(plaxa);
                employeeOld = em.merge(employeeOld);
            }
            if (employeeNew != null && !employeeNew.equals(employeeOld)) {
                employeeNew.getPlaxaCollection().add(plaxa);
                employeeNew = em.merge(employeeNew);
            }
            if (accountNumberOld != null && !accountNumberOld.equals(accountNumberNew)) {
                accountNumberOld.getPlaxaCollection().remove(plaxa);
                accountNumberOld = em.merge(accountNumberOld);
            }
            if (accountNumberNew != null && !accountNumberNew.equals(accountNumberOld)) {
                accountNumberNew.getPlaxaCollection().add(plaxa);
                accountNumberNew = em.merge(accountNumberNew);
            }
            if (statusCodeOld != null && !statusCodeOld.equals(statusCodeNew)) {
                statusCodeOld.getPlaxaCollection().remove(plaxa);
                statusCodeOld = em.merge(statusCodeOld);
            }
            if (statusCodeNew != null && !statusCodeNew.equals(statusCodeOld)) {
                statusCodeNew.getPlaxaCollection().add(plaxa);
                statusCodeNew = em.merge(statusCodeNew);
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
                PlaxaPK id = plaxa.getPlaxaPK();
                if (findPlaxa(id) == null) {
                    throw new NonexistentEntityException("The plaxa with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(PlaxaPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Plaxa plaxa;
            try {
                plaxa = em.getReference(Plaxa.class, id);
                plaxa.getPlaxaPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The plaxa with id " + id + " no longer exists.", enfe);
            }
            Employee employee = plaxa.getEmployee();
            if (employee != null) {
                employee.getPlaxaCollection().remove(plaxa);
                employee = em.merge(employee);
            }
            PlaxaAccount accountNumber = plaxa.getAccountNumber();
            if (accountNumber != null) {
                accountNumber.getPlaxaCollection().remove(plaxa);
                accountNumber = em.merge(accountNumber);
            }
            PlaxaStatus statusCode = plaxa.getStatusCode();
            if (statusCode != null) {
                statusCode.getPlaxaCollection().remove(plaxa);
                statusCode = em.merge(statusCode);
            }
            em.remove(plaxa);
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

    public List<Plaxa> findPlaxaEntities() {
        return findPlaxaEntities(true, -1, -1);
    }

    public List<Plaxa> findPlaxaEntities(int maxResults, int firstResult) {
        return findPlaxaEntities(false, maxResults, firstResult);
    }

    private List<Plaxa> findPlaxaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Plaxa.class));
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

    public Plaxa findPlaxa(PlaxaPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Plaxa.class, id);
        } finally {
            em.close();
        }
    }

    public int getPlaxaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Plaxa> rt = cq.from(Plaxa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
