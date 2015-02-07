package jsf_classes;

import entities.PersonnelGroup;
import jsf_classes.util.JsfUtil;
import jsf_classes.util.JsfUtil.PersistAction;
import session_beans.PersonnelGroupFacade;

import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;


@Named("personnelGroupController")
@SessionScoped
public class PersonnelGroupController implements Serializable {


    @EJB private session_beans.PersonnelGroupFacade ejbFacade;
    private List<PersonnelGroup> items = null;
    private PersonnelGroup selected;

    public PersonnelGroupController() {
    }

    public PersonnelGroup getSelected() {
        return selected;
    }

    public void setSelected(PersonnelGroup selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
    }

    protected void initializeEmbeddableKey() {
    }

    private PersonnelGroupFacade getFacade() {
        return ejbFacade;
    }

    public PersonnelGroup prepareCreate() {
        selected = new PersonnelGroup();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PersonnelGroupCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PersonnelGroupUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PersonnelGroupDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<PersonnelGroup> getItems() {
        if (items == null) {
            items = getFacade().findAll();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                } else {
                    getFacade().remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            }
        }
    }

    public PersonnelGroup getPersonnelGroup(java.lang.String id) {
        return getFacade().find(id);
    }

    public List<PersonnelGroup> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PersonnelGroup> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass=PersonnelGroup.class)
    public static class PersonnelGroupControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PersonnelGroupController controller = (PersonnelGroupController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "personnelGroupController");
            return controller.getPersonnelGroup(getKey(value));
        }

        java.lang.String getKey(String value) {
            java.lang.String key;
            key = value;
            return key;
        }

        String getStringKey(java.lang.String value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PersonnelGroup) {
                PersonnelGroup o = (PersonnelGroup) object;
                return getStringKey(o.getPersonnelGroupCode());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PersonnelGroup.class.getName()});
                return null;
            }
        }

    }

}
