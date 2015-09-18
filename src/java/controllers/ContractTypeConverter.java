package controllers;

import session_beans.ContractTypeFacade;
import entities.ContractType;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

@FacesConverter("contractTypeConverter")
public class ContractTypeConverter implements Converter {
    @Inject
    private ContractTypeFacade query;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                List<ContractType> contractTypes = query.findAll();
                for (ContractType contractType : contractTypes) {
                    if(contractType.getContractType() == Integer.parseInt(value)){
                        return contractType;
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
        if (value instanceof ContractType) {
            ContractType ct = (ContractType) value;
            return ct.getContractType().toString();
        }
        return "";
    }
    
}
