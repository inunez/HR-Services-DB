package session_beans;

import entities.Account;
import entities.Employee;
import entities.ManagerPosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class ManagerFacadeApplication extends AbstractFacade<ManagerPosition> {
    @PersistenceContext(unitName = "HRServicesDBPU")
    private EntityManager em;
    private List<ManagerPosition> managers;
    
    public ManagerFacadeApplication() {
        super(ManagerPosition.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @Override
    public List<ManagerPosition> findAll(){
        if(managers == null){
            fillList();
        }
        return managers;
    }
     
    private void fillList(){
        managers = super.findAll();
    }
    
    public List<ManagerPosition> filterManager(String filter) {
        if(managers == null){
            fillList();
        }
        List<ManagerPosition> filtered = new ArrayList<>();
        
        managers.stream().forEach((manager) -> {
            Collection<Employee> employees = manager.getPosition().getEmployeeCollection();
            for (Employee employee : employees) {
                String fullName = employee.getFirstName().concat(" ").concat(employee.getSurname());
                Logger.getLogger(this.getClass().getName()).log(Level.INFO, fullName);
                if (fullName.toLowerCase().contains(filter.trim().toLowerCase()) && employee.getEmployeePK().getStatus().equals("A")){
                    List<Employee> activeEmployees = manager.getPosition().getEmployeeCollection().stream().filter(e -> e.getEmployeePK().getStatus().equals("A"))
                            .collect(Collectors.toCollection(ArrayList::new));
                    manager.getPosition().setEmployeeCollection(activeEmployees);
                    filtered.add(manager);
                }
            }
        });        
        return filtered;
    }
    public List<ManagerPosition> filterManager(Account filter) {
        if(managers == null){
            fillList();
        }
        List<ManagerPosition> filtered = managers.stream().filter(m -> m.getAccount().equals(filter)).collect(Collectors.toCollection(ArrayList::new));
        return filtered;
    }
}
