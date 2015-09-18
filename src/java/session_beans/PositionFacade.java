package session_beans;

import entities.Position;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class PositionFacade extends AbstractFacade<Position> {
    @PersistenceContext(unitName = "HRServicesDBPU")
    private EntityManager em;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PositionFacade() {
        super(Position.class);
    }
    

}
