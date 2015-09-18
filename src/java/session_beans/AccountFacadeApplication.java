package session_beans;

import entities.Account;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class AccountFacadeApplication extends AbstractFacade<Account> {
    @PersistenceContext(unitName = "HRServicesDBPU")
    private EntityManager em;
    private List<Account> accounts;
    
    public AccountFacadeApplication() {
        super(Account.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @Override
    public List<Account> findAll(){
        if(accounts == null){
            fillList();
        }
        return accounts;
    }
     
    private void fillList(){
        accounts = super.findAll();
    }
    
    public List<Account> filterAccount(String filter) {
        List<Account> filtered = accounts.stream().filter(p -> p.getAccountDescription().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
        
        return filtered;
    }
}
