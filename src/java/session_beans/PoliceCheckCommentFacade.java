/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session_beans;

import entities.PoliceCheckComment;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Ismael
 */
@Stateless
public class PoliceCheckCommentFacade extends AbstractFacade<PoliceCheckComment> {
    @PersistenceContext(unitName = "HRServicesDBPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PoliceCheckCommentFacade() {
        super(PoliceCheckComment.class);
    }
    
}
