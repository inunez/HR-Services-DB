package controllers;

import entities.EarningLeave;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import session_beans.EarningLeaveFacade;

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


@Named("earningLeaveController")
@SessionScoped
public class EarningLeaveController implements Serializable {


    @EJB private session_beans.EarningLeaveFacade ejbFacade;
    private List<EarningLeave> items = null;
    private EarningLeave selected;

    public EarningLeaveController() {
    }

    public EarningLeave getSelected() {
        return selected;
    }

    public void setSelected(EarningLeave selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
            selected.getEarningLeavePK().setStatus(selected.getEmployee().getEmployeePK().getStatus());
            selected.getEarningLeavePK().setIdNumber(selected.getEmployee().getEmployeePK().getIdNumber());
    }

    protected void initializeEmbeddableKey() {
        selected.setEarningLeavePK(new entities.EarningLeavePK());
    }

    private EarningLeaveFacade getFacade() {
        return ejbFacade;
    }

    public EarningLeave prepareCreate() {
        selected = new EarningLeave();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EarningLeaveCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EarningLeaveUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EarningLeaveDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<EarningLeave> getItems() {
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

    public EarningLeave getEarningLeave(entities.EarningLeavePK id) {
        return getFacade().find(id);
    }

    public List<EarningLeave> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<EarningLeave> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass=EarningLeave.class)
    public static class EarningLeaveControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EarningLeaveController controller = (EarningLeaveController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "earningLeaveController");
            return controller.getEarningLeave(getKey(value));
        }

        entities.EarningLeavePK getKey(String value) {
            entities.EarningLeavePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entities.EarningLeavePK();
            key.setIdNumber(values[0]);
            key.setStatus(values[1]);
            key.setVersion(Short.parseShort(values[2]));
            key.setDateAccruedTo(java.sql.Date.valueOf(values[3]));
            key.setClassification(values[4]);
            return key;
        }

        String getStringKey(entities.EarningLeavePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdNumber());
            sb.append(SEPARATOR);
            sb.append(value.getStatus());
            sb.append(SEPARATOR);
            sb.append(value.getVersion());
            sb.append(SEPARATOR);
            sb.append(value.getDateAccruedTo());
            sb.append(SEPARATOR);
            sb.append(value.getClassification());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof EarningLeave) {
                EarningLeave o = (EarningLeave) object;
                return getStringKey(o.getEarningLeavePK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), EarningLeave.class.getName()});
                return null;
            }
        }

    }

}
