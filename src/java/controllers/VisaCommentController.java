package controllers;

import entities.VisaComment;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import session_beans.VisaCommentFacade;

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

@Named("visaCommentController")
@SessionScoped
public class VisaCommentController implements Serializable {

    @EJB
    private session_beans.VisaCommentFacade ejbFacade;
    private List<VisaComment> items = null;
    private VisaComment selected;

    public VisaCommentController() {
    }

    public VisaComment getSelected() {
        return selected;
    }

    public void setSelected(VisaComment selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getVisaCommentPK().setIdNumber(selected.getEmployee().getEmployeePK().getIdNumber());
    }

    protected void initializeEmbeddableKey() {
        selected.setVisaCommentPK(new entities.VisaCommentPK());
    }

    private VisaCommentFacade getFacade() {
        return ejbFacade;
    }

    public VisaComment prepareCreate() {
        selected = new VisaComment();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("VisaCommentCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VisaCommentUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VisaCommentDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<VisaComment> getItems() {
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

    public VisaComment getVisaComment(entities.VisaCommentPK id) {
        return getFacade().find(id);
    }

    public List<VisaComment> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<VisaComment> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = VisaComment.class)
    public static class VisaCommentControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VisaCommentController controller = (VisaCommentController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "visaCommentController");
            return controller.getVisaComment(getKey(value));
        }

        entities.VisaCommentPK getKey(String value) {
            entities.VisaCommentPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entities.VisaCommentPK();
            key.setIdNumber(values[0]);
            key.setStatus(values[1]);
            key.setCommentDate(java.sql.Date.valueOf(values[2]));
            return key;
        }

        String getStringKey(entities.VisaCommentPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdNumber());
            sb.append(SEPARATOR);
            sb.append(value.getStatus());
            sb.append(SEPARATOR);
            sb.append(value.getCommentDate());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof VisaComment) {
                VisaComment o = (VisaComment) object;
                return getStringKey(o.getVisaCommentPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), VisaComment.class.getName()});
                return null;
            }
        }

    }

}
