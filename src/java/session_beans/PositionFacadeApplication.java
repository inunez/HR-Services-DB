package session_beans;

import entities.Position;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class PositionFacadeApplication extends AbstractFacade<Position> {
    @PersistenceContext(unitName = "HRServicesDBPU")
    private EntityManager em;
    private List<Position> positions;
    
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PositionFacadeApplication() {
        super(Position.class);
    }
    
    @Override
    public List<Position> findAll(){
        if(positions == null){
            fillPositions();
        }
        return positions;
    }
     
    private void fillPositions(){
        positions = super.findAll();
    }
    
    public List<Position> filterPosition(String filter) {
        List<Position> filtered = positions.stream().filter(p -> p.getPositionTitle().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
        if (filtered.isEmpty()){
            filtered = positions.stream().filter(p -> p.getPositionId().toLowerCase().contains(filter.toLowerCase()))
                .collect(Collectors.toCollection(ArrayList::new));
        }
        return filtered;
    }
}
