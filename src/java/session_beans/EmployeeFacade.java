/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session_beans;

import entities.Account;
import entities.ChangeCondition;
import entities.ContractType;
import entities.Earning;
import entities.Employee;
import entities.JobTitle;
import entities.ManagerPosition;
import entities.Payroll;
import entities.Position;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
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
        if (!"".equals(searchType[2])) {
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

    public Employee findManager(Employee employee, boolean checkServiceManager) {
        Employee manager = null;

        if (employee.getReportsToPositionId().getEmployeeCollection().iterator().hasNext()) {
            //manager assigned
            manager = employee.getReportsToPositionId().getEmployeeCollection().iterator().next();
            //for police check and visa report check team leader or coordinator
            if (checkServiceManager) {
                String positionTitle = manager.getPositionId().getPositionTitle().toUpperCase();
                if (positionTitle.contains("COOR") || positionTitle.contains("LEADER") || positionTitle.contains("TL")) {
                    manager = findManager(manager, checkServiceManager);
                }
            }
        }

        if (manager == null) {
            //facility manager
            Payroll payroll = employee.getPayrollCollection().iterator().next();
            Collection<ManagerPosition> managers = payroll.getAccountNumber().getManagerPositionCollection();
            if (managers.size() == 1) {
                manager = managers.iterator().next().getPosition().getEmployeeCollection().iterator().next();
            }
            if (manager == null) {
                String position = employee.getPositionId().getPositionId();
                if (!position.substring(0, 1).matches("[0-9]")) {
                    String search = position.substring(0, 9).concat("0001");
                    ArrayList<ManagerPosition> managerFacility = managers.stream().filter(p -> p.getPosition().getPositionId().equals(search))
                            .collect(Collectors.toCollection(ArrayList::new));
                    if (!managerFacility.isEmpty()) {
                        manager = managerFacility.iterator().next().getPosition().getEmployeeCollection().iterator().next();
                    }

                    managers = getManagerPositionCollection();

                    if (manager == null) {
                        String search2 = position.substring(0, 6).concat("00-0001");
                        ArrayList<ManagerPosition> managerDivision = managers.stream().filter(p -> p.getPosition().getPositionId().equals(search2))
                                .collect(Collectors.toCollection(ArrayList::new));
                        if (!managerDivision.isEmpty()) {
                            manager = managerDivision.iterator().next().getPosition().getEmployeeCollection().iterator().next();
                        }
                    }
                    if (manager == null) {
                        String search2 = position.substring(0, 4).concat("1000-0001");
                        ArrayList<ManagerPosition> director = managers.stream().filter(p -> p.getPosition().getPositionId().equals(search2))
                                .collect(Collectors.toCollection(ArrayList::new));
                        if (!director.isEmpty()) {
                            manager = director.iterator().next().getPosition().getEmployeeCollection().iterator().next();
                        }
                    }
                } else {
                    manager = managers.iterator().next().getPosition().getEmployeeCollection().iterator().next();
                }
            }
        }

        //No manager found. if manager = employee -> error
        if (manager == null) {
            manager = employee;
        }
        return manager;
    }

    public Collection<ManagerPosition> getManagerPositionCollection() {
        Query namedQuery;
        namedQuery = em.createNamedQuery("ManagerPosition.findAll", ManagerPosition.class);
        return namedQuery.getResultList();
    }

    public List<JobTitle> getListJobTitles() {
        Query namedQuery;
        namedQuery = em.createNamedQuery("JobTitle.findAll", JobTitle.class);

        return namedQuery.getResultList();
    }

}
