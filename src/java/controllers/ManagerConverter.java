package controllers;

import entities.Employee;
import entities.ManagerPosition;
import java.util.Collection;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;
import session_beans.ManagerFacadeApplication;

@FacesConverter("managerConverter")
public class ManagerConverter implements Converter{
    @Inject
    private ManagerFacadeApplication query;
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if(value != null && value.trim().length() > 0) {
            try {
                List<ManagerPosition> managers = query.findAll();
                for (ManagerPosition manager : managers) {
                    Collection<Employee> employees = manager.getPosition().getEmployeeCollection();
                            //.stream().collect(Collectors.toCollection(ArrayList::new));
                    for (Employee employee : employees) {
                        String fullName = employee.getFirstName().concat(" ").concat(employee.getSurname()).toLowerCase();
                        if(fullName.contains(value.trim().toLowerCase())){
                            return employee;
                        }
                    }
                }
                return null;
            } catch(NumberFormatException e) {
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid manager."));
            }
        }
        else {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        if (value instanceof Employee) {
            Employee manager = (Employee) value;
            return manager.getFullName();
        }
        return "";
    }
    
}
