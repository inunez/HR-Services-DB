package session_beans;

import entities.ChangeCondition;
import entities.ContractType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
//@Stateless
public class ContractTypeFacade extends AbstractFacade<ContractType> {
    @PersistenceContext(unitName = "HRServicesDBPU")
    private EntityManager em;
    private List<ContractType> contractTypes;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public ContractTypeFacade(){
        super(ContractType.class);
    }
    
    @Override
    public List<ContractType> findAll(){
        if (contractTypes == null){
            fillContractTypes();
        }
        return contractTypes;
    }
    


    private void fillContractTypes(){
        contractTypes = super.findAll();
    }
    
    public List<ContractType> getContractTypeList(ChangeCondition.VariationType varType) {
        List<ContractType> filtered = new ArrayList<>();
        
        if (contractTypes == null) fillContractTypes();
            
        filtered = contractTypes.stream().filter(c -> c.getGroupType().equals(varType.toString())).collect(Collectors.toCollection(ArrayList::new));
        return filtered;
    }

}
