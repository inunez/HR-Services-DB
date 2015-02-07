package jsf_classes;

import entities.Earning;
import jsf_classes.util.JsfUtil;
import jsf_classes.util.JsfUtil.PersistAction;
import session_beans.EarningFacade;

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


@Named("earningController")
@SessionScoped
public class EarningController implements Serializable {


    @EJB private session_beans.EarningFacade ejbFacade;
    private List<Earning> items = null;
    private Earning selected;

    public EarningController() {
    }

    public Earning getSelected() {
        return selected;
    }

    public void setSelected(Earning selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
            selected.getEarningPK().setIdNumber(selected.getEmployee().getEmployeePK().getIdNumber());
    }

    protected void initializeEmbeddableKey() {
        selected.setEarningPK(new entities.EarningPK());
    }

    private EarningFacade getFacade() {
        return ejbFacade;
    }

    public Earning prepareCreate() {
        selected = new Earning();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EarningCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EarningUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EarningDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Earning> getItems() {
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

    public Earning getEarning(entities.EarningPK id) {
        return getFacade().find(id);
    }

    public List<Earning> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Earning> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass=Earning.class)
    public static class EarningControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EarningController controller = (EarningController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "earningController");
            return controller.getEarning(getKey(value));
        }

        entities.EarningPK getKey(String value) {
            entities.EarningPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entities.EarningPK();
            key.setIdNumber(values[0]);
            key.setPayslipNumber(Double.parseDouble(values[1]));
            key.setPrintSequence(Short.parseShort(values[2]));
            return key;
        }

        String getStringKey(entities.EarningPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdNumber());
            sb.append(SEPARATOR);
            sb.append(value.getPayslipNumber());
            sb.append(SEPARATOR);
            sb.append(value.getPrintSequence());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Earning) {
                Earning o = (Earning) object;
                return getStringKey(o.getEarningPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Earning.class.getName()});
                return null;
            }
        }

    }

}
