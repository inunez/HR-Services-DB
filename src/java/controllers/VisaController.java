package controllers;

import entities.Visa;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import session_beans.VisaFacade;

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


@Named("visaController")
@SessionScoped
public class VisaController implements Serializable {


    @EJB private session_beans.VisaFacade ejbFacade;
    private List<Visa> items = null;
    private Visa selected;

    public VisaController() {
    }

    public Visa getSelected() {
        return selected;
    }

    public void setSelected(Visa selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
            selected.getVisaPK().setIdNumber(selected.getEmployee().getEmployeePK().getIdNumber());
    }

    protected void initializeEmbeddableKey() {
        selected.setVisaPK(new entities.VisaPK());
    }

    private VisaFacade getFacade() {
        return ejbFacade;
    }

    public Visa prepareCreate() {
        selected = new Visa();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("VisaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VisaUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VisaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Visa> getItems() {
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

    public Visa getVisa(entities.VisaPK id) {
        return getFacade().find(id);
    }

    public List<Visa> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Visa> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass=Visa.class)
    public static class VisaControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VisaController controller = (VisaController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "visaController");
            return controller.getVisa(getKey(value));
        }

        entities.VisaPK getKey(String value) {
            entities.VisaPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entities.VisaPK();
            key.setIdNumber(values[0]);
            key.setPassportNumber(values[1]);
            key.setUpdateDate(java.sql.Date.valueOf(values[2]));
            return key;
        }

        String getStringKey(entities.VisaPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdNumber());
            sb.append(SEPARATOR);
            sb.append(value.getPassportNumber());
            sb.append(SEPARATOR);
            sb.append(value.getUpdateDate());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Visa) {
                Visa o = (Visa) object;
                return getStringKey(o.getVisaPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Visa.class.getName()});
                return null;
            }
        }

    }

}