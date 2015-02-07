package jsf_classes;

import entities.PoliceCheck;
import jsf_classes.util.JsfUtil;
import jsf_classes.util.JsfUtil.PersistAction;
import session_beans.PoliceCheckFacade;

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

@Named("policeCheckController")
@SessionScoped
public class PoliceCheckController implements Serializable {

    @EJB
    private session_beans.PoliceCheckFacade ejbFacade;
    private List<PoliceCheck> items = null;
    private PoliceCheck selected;

    public PoliceCheckController() {
    }

    public PoliceCheck getSelected() {
        return selected;
    }

    public void setSelected(PoliceCheck selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getPoliceCheckPK().setIdNumber(selected.getEmployee().getEmployeePK().getIdNumber());
    }

    protected void initializeEmbeddableKey() {
        selected.setPoliceCheckPK(new entities.PoliceCheckPK());
    }

    private PoliceCheckFacade getFacade() {
        return ejbFacade;
    }

    public PoliceCheck prepareCreate() {
        selected = new PoliceCheck();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PoliceCheckCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PoliceCheckUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PoliceCheckDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<PoliceCheck> getItems() {
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

    public PoliceCheck getPoliceCheck(entities.PoliceCheckPK id) {
        return getFacade().find(id);
    }

    public List<PoliceCheck> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PoliceCheck> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = PoliceCheck.class)
    public static class PoliceCheckControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PoliceCheckController controller = (PoliceCheckController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "policeCheckController");
            return controller.getPoliceCheck(getKey(value));
        }

        entities.PoliceCheckPK getKey(String value) {
            entities.PoliceCheckPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entities.PoliceCheckPK();
            key.setIdNumber(values[0]);
            key.setUpdateDate(java.sql.Date.valueOf(values[1]));
            return key;
        }

        String getStringKey(entities.PoliceCheckPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdNumber());
            sb.append(SEPARATOR);
            sb.append(value.getUpdateDate());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PoliceCheck) {
                PoliceCheck o = (PoliceCheck) object;
                return getStringKey(o.getPoliceCheckPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PoliceCheck.class.getName()});
                return null;
            }
        }

    }

}
