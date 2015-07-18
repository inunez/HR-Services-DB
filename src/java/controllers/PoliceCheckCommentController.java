package controllers;

import entities.PoliceCheckComment;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import session_beans.PoliceCheckCommentFacade;

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

@Named("policeCheckCommentController")
@SessionScoped
public class PoliceCheckCommentController implements Serializable {

    @EJB
    private session_beans.PoliceCheckCommentFacade ejbFacade;
    private List<PoliceCheckComment> items = null;
    private PoliceCheckComment selected;

    public PoliceCheckCommentController() {
    }

    public PoliceCheckComment getSelected() {
        return selected;
    }

    public void setSelected(PoliceCheckComment selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getPoliceCheckCommentPK().setIdNumber(selected.getEmployee().getEmployeePK().getIdNumber());
    }

    protected void initializeEmbeddableKey() {
        selected.setPoliceCheckCommentPK(new entities.PoliceCheckCommentPK());
    }

    private PoliceCheckCommentFacade getFacade() {
        return ejbFacade;
    }

    public PoliceCheckComment prepareCreate() {
        selected = new PoliceCheckComment();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PoliceCheckCommentCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PoliceCheckCommentUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PoliceCheckCommentDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<PoliceCheckComment> getItems() {
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

    public PoliceCheckComment getPoliceCheckComment(entities.PoliceCheckCommentPK id) {
        return getFacade().find(id);
    }

    public List<PoliceCheckComment> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<PoliceCheckComment> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = PoliceCheckComment.class)
    public static class PoliceCheckCommentControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PoliceCheckCommentController controller = (PoliceCheckCommentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "policeCheckCommentController");
            return controller.getPoliceCheckComment(getKey(value));
        }

        entities.PoliceCheckCommentPK getKey(String value) {
            entities.PoliceCheckCommentPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entities.PoliceCheckCommentPK();
            key.setIdNumber(values[0]);
            key.setCommentDate(java.sql.Date.valueOf(values[1]));
            return key;
        }

        String getStringKey(entities.PoliceCheckCommentPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdNumber());
            sb.append(SEPARATOR);
            sb.append(value.getCommentDate());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof PoliceCheckComment) {
                PoliceCheckComment o = (PoliceCheckComment) object;
                return getStringKey(o.getPoliceCheckCommentPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), PoliceCheckComment.class.getName()});
                return null;
            }
        }

    }

}
