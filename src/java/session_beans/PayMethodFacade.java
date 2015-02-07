/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session_beans;

import entities.PayMethod;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ismael
 */
@Stateless
public class PayMethodFacade extends AbstractFacade<PayMethod> {
    @PersistenceContext(unitName = "HRServicesDBPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PayMethodFacade() {
        super(PayMethod.class);
    }
    
}
