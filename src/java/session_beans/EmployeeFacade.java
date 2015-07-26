/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session_beans;

import entities.Earning;
import entities.Employee;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Ismael
 */
@Stateless
public class EmployeeFacade extends AbstractFacade<Employee> {
    @PersistenceContext(unitName = "HRServicesDBPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmployeeFacade() {
        super(Employee.class);
    }
    
    public List<Employee> getEmployeeByType(String searchText, String[] searchType) {
      Query namedQuery;
      namedQuery = em.createNamedQuery("Employee.".concat(searchType[0]), 
                Employee.class);
      namedQuery.setParameter(searchType[1], searchText);
      if(!"".equals(searchType[2])){
        namedQuery.setParameter(searchType[2], searchType[3]);
      }
//      return em.createNamedQuery("Employee.".concat(searchType[0]), 
//                Employee.class).setParameter(searchType[1], searchText).getResultList();
      return namedQuery.getResultList();
    }
        
    public Collection<Earning> getEarningDistinctCollection(String idNumber) {
        Query namedQuery;
        namedQuery = em.createNamedQuery("Employee.findEarningDistinct", Earning.class);
        namedQuery.setParameter("idNumber", idNumber);

        return namedQuery.getResultList();
    }
    
    public Employee findManager(Employee employee, boolean checkServiceManager){
        Employee manager;
        
        if (employee.getReportsToPositionId().getEmployeeCollection().iterator().hasNext()){
            //manager assigned
            manager = employee.getReportsToPositionId().getEmployeeCollection().iterator().next();
            //for police check and visa report check team leader or coordinator
            if (checkServiceManager){
                String positionTitle = manager.getPositionId().getPositionTitle().toUpperCase();
                if(positionTitle.contains("COOR") || positionTitle.contains("LEADER") || positionTitle.contains("TL") ){
                    manager = employee.getLocationCode().getEmployee();
                }
            }
        }else{
            //facility manager
            manager = employee.getLocationCode().getEmployee();
        }
        //ROM or COM
        if (manager == null){
            
        }
        return manager;
    }
    
}
