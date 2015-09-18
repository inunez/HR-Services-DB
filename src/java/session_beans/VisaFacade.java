/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session_beans;

import entities.Visa;
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
public class VisaFacade extends AbstractFacade<Visa> {
    @PersistenceContext(unitName = "HRServicesDBPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public VisaFacade() {
        super(Visa.class);
    }
    
    public List<Visa> getVisaByType(String searchText, String[] searchType) throws ParseException {
        Query namedQuery;
         namedQuery = em.createNamedQuery("Visa.".concat(searchType[0]), 
                Visa.class);
        namedQuery.setParameter(searchType[1], searchText);
        if(!"".equals(searchType[2])){
            namedQuery.setParameter(searchType[2], searchType[3]);
        }
        //TO DO put 2 months in a parameter
        LocalDate twoMonths = LocalDate.now().plusMonths(2);
        
        namedQuery.setParameter("visaExpiryDate", new SimpleDateFormat("yyyy-MM-dd").parse(twoMonths.toString()));
        
        return namedQuery.getResultList();
    }  
}
