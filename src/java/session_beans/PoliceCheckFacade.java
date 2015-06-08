/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session_beans;

import entities.PoliceCheck;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
public class PoliceCheckFacade extends AbstractFacade<PoliceCheck> {
    @PersistenceContext(unitName = "HRServicesDBPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PoliceCheckFacade() {
        super(PoliceCheck.class);
    }
 
    public List<PoliceCheck> getPoliceCheckByType(String searchText, String[] searchType) throws ParseException {
        Query namedQuery;
         namedQuery = em.createNamedQuery("PoliceCheck.".concat(searchType[0]), 
                PoliceCheck.class);
        namedQuery.setParameter(searchType[1], searchText);
        if(!"".equals(searchType[2])){
            namedQuery.setParameter(searchType[2], searchType[3]);
        }
        LocalDate twoMonths = LocalDate.now().plusMonths(2);
        
        namedQuery.setParameter("expiryDate", new SimpleDateFormat("yyyy-MM-dd").parse(twoMonths.toString()));
        
        return namedQuery.getResultList();
    }   
        
}
