package controllers;

import entities.Position;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import session_beans.PositionFacadeApplication;

@FacesConverter("positionConverter")
public class PositionConverter implements Converter {
    @Inject
    private PositionFacadeApplication query;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                List<Position> positions = query.findAll();
                for (Position position : positions) {
                    if(position.getPositionId().toLowerCase().contains(value.toLowerCase()) || 
                            position.getPositionTitle().toLowerCase().contains(value.toLowerCase())){
                        return position;
                    }
                }
                return null;
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid contract type."));
            }
        }
        else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Position) {
            Position p = (Position) value;
            return p.getPositionId();
        }
        return "";
    }
    
}