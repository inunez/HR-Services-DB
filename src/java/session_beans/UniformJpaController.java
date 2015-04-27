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
import entities.Employee;
import entities.Uniform;
import entities.UniformAllowance;
import entities.UniformPK;
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
public class UniformJpaController implements Serializable {

    public UniformJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Uniform uniform) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (uniform.getUniformPK() == null) {
            uniform.setUniformPK(new UniformPK());
        }
        uniform.getUniformPK().setIdNumber(uniform.getEmployee().getEmployeePK().getIdNumber());
        uniform.getUniformPK().setStatus(uniform.getEmployee().getEmployeePK().getStatus());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            UniformAccount accountNumber = uniform.getAccountNumber();
            if (accountNumber != null) {
                accountNumber = em.getReference(accountNumber.getClass(), accountNumber.getAccountNumber());
                uniform.setAccountNumber(accountNumber);
            }
            Employee employee = uniform.getEmployee();
            if (employee != null) {
                employee = em.getReference(employee.getClass(), employee.getEmployeePK());
                uniform.setEmployee(employee);
            }
            UniformAllowance allowanceId = uniform.getAllowanceId();
            if (allowanceId != null) {
                allowanceId = em.getReference(allowanceId.getClass(), allowanceId.getAllowanceId());
                uniform.setAllowanceId(allowanceId);
            }
            em.persist(uniform);
            if (accountNumber != null) {
                accountNumber.getUniformCollection().add(uniform);
                accountNumber = em.merge(accountNumber);
            }
            if (employee != null) {
                employee.getUniformCollection().add(uniform);
                employee = em.merge(employee);
            }
            if (allowanceId != null) {
                allowanceId.getUniformCollection().add(uniform);
                allowanceId = em.merge(allowanceId);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findUniform(uniform.getUniformPK()) != null) {
                throw new PreexistingEntityException("Uniform " + uniform + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Uniform uniform) throws NonexistentEntityException, RollbackFailureException, Exception {
        uniform.getUniformPK().setIdNumber(uniform.getEmployee().getEmployeePK().getIdNumber());
        uniform.getUniformPK().setStatus(uniform.getEmployee().getEmployeePK().getStatus());
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Uniform persistentUniform = em.find(Uniform.class, uniform.getUniformPK());
            UniformAccount accountNumberOld = persistentUniform.getAccountNumber();
            UniformAccount accountNumberNew = uniform.getAccountNumber();
            Employee employeeOld = persistentUniform.getEmployee();
            Employee employeeNew = uniform.getEmployee();
            UniformAllowance allowanceIdOld = persistentUniform.getAllowanceId();
            UniformAllowance allowanceIdNew = uniform.getAllowanceId();
            if (accountNumberNew != null) {
                accountNumberNew = em.getReference(accountNumberNew.getClass(), accountNumberNew.getAccountNumber());
                uniform.setAccountNumber(accountNumberNew);
            }
            if (employeeNew != null) {
                employeeNew = em.getReference(employeeNew.getClass(), employeeNew.getEmployeePK());
                uniform.setEmployee(employeeNew);
            }
            if (allowanceIdNew != null) {
                allowanceIdNew = em.getReference(allowanceIdNew.getClass(), allowanceIdNew.getAllowanceId());
                uniform.setAllowanceId(allowanceIdNew);
            }
            uniform = em.merge(uniform);
            if (accountNumberOld != null && !accountNumberOld.equals(accountNumberNew)) {
                accountNumberOld.getUniformCollection().remove(uniform);
                accountNumberOld = em.merge(accountNumberOld);
            }
            if (accountNumberNew != null && !accountNumberNew.equals(accountNumberOld)) {
                accountNumberNew.getUniformCollection().add(uniform);
                accountNumberNew = em.merge(accountNumberNew);
            }
            if (employeeOld != null && !employeeOld.equals(employeeNew)) {
                employeeOld.getUniformCollection().remove(uniform);
                employeeOld = em.merge(employeeOld);
            }
            if (employeeNew != null && !employeeNew.equals(employeeOld)) {
                employeeNew.getUniformCollection().add(uniform);
                employeeNew = em.merge(employeeNew);
            }
            if (allowanceIdOld != null && !allowanceIdOld.equals(allowanceIdNew)) {
                allowanceIdOld.getUniformCollection().remove(uniform);
                allowanceIdOld = em.merge(allowanceIdOld);
            }
            if (allowanceIdNew != null && !allowanceIdNew.equals(allowanceIdOld)) {
                allowanceIdNew.getUniformCollection().add(uniform);
                allowanceIdNew = em.merge(allowanceIdNew);
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
                UniformPK id = uniform.getUniformPK();
                if (findUniform(id) == null) {
                    throw new NonexistentEntityException("The uniform with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(UniformPK id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Uniform uniform;
            try {
                uniform = em.getReference(Uniform.class, id);
                uniform.getUniformPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The uniform with id " + id + " no longer exists.", enfe);
            }
            UniformAccount accountNumber = uniform.getAccountNumber();
            if (accountNumber != null) {
                accountNumber.getUniformCollection().remove(uniform);
                accountNumber = em.merge(accountNumber);
            }
            Employee employee = uniform.getEmployee();
            if (employee != null) {
                employee.getUniformCollection().remove(uniform);
                employee = em.merge(employee);
            }
            UniformAllowance allowanceId = uniform.getAllowanceId();
            if (allowanceId != null) {
                allowanceId.getUniformCollection().remove(uniform);
                allowanceId = em.merge(allowanceId);
            }
            em.remove(uniform);
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

    public List<Uniform> findUniformEntities() {
        return findUniformEntities(true, -1, -1);
    }

    public List<Uniform> findUniformEntities(int maxResults, int firstResult) {
        return findUniformEntities(false, maxResults, firstResult);
    }

    private List<Uniform> findUniformEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Uniform.class));
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

    public Uniform findUniform(UniformPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Uniform.class, id);
        } finally {
            em.close();
        }
    }

    public int getUniformCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Uniform> rt = cq.from(Uniform.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
