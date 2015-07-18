package controllers;

import entities.EarningSummary;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import session_beans.EarningSummaryFacade;

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


@Named("earningSummaryController")
@SessionScoped
public class EarningSummaryController implements Serializable {


    @EJB private session_beans.EarningSummaryFacade ejbFacade;
    private List<EarningSummary> items = null;
    private EarningSummary selected;

    public EarningSummaryController() {
    }

    public EarningSummary getSelected() {
        return selected;
    }

    public void setSelected(EarningSummary selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
            selected.getEarningSummaryPK().setIdNumber(selected.getEmployee().getEmployeePK().getIdNumber());
            selected.getEarningSummaryPK().setStatus(selected.getEmployee().getEmployeePK().getStatus());
    }

    protected void initializeEmbeddableKey() {
        selected.setEarningSummaryPK(new entities.EarningSummaryPK());
    }

    private EarningSummaryFacade getFacade() {
        return ejbFacade;
    }

    public EarningSummary prepareCreate() {
        selected = new EarningSummary();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EarningSummaryCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EarningSummaryUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EarningSummaryDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<EarningSummary> getItems() {
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

    public EarningSummary getEarningSummary(entities.EarningSummaryPK id) {
        return getFacade().find(id);
    }

    public List<EarningSummary> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<EarningSummary> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass=EarningSummary.class)
    public static class EarningSummaryControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EarningSummaryController controller = (EarningSummaryController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "earningSummaryController");
            return controller.getEarningSummary(getKey(value));
        }

        entities.EarningSummaryPK getKey(String value) {
            entities.EarningSummaryPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entities.EarningSummaryPK();
            key.setIdNumber(values[0]);
            key.setStatus(values[1]);
            key.setVersion(Short.parseShort(values[2]));
            key.setPaidUpToDate(java.sql.Date.valueOf(values[3]));
            return key;
        }

        String getStringKey(entities.EarningSummaryPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdNumber());
            sb.append(SEPARATOR);
            sb.append(value.getStatus());
            sb.append(SEPARATOR);
            sb.append(value.getVersion());
            sb.append(SEPARATOR);
            sb.append(value.getPaidUpToDate());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof EarningSummary) {
                EarningSummary o = (EarningSummary) object;
                return getStringKey(o.getEarningSummaryPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), EarningSummary.class.getName()});
                return null;
            }
        }

    }

}
