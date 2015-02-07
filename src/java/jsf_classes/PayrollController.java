package jsf_classes;

import entities.Payroll;
import jsf_classes.util.JsfUtil;
import jsf_classes.util.JsfUtil.PersistAction;
import session_beans.PayrollFacade;

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


@Named("payrollController")
@SessionScoped
public class PayrollController implements Serializable {


    @EJB private session_beans.PayrollFacade ejbFacade;
    private List<Payroll> items = null;
    private Payroll selected;

    public PayrollController() {
    }

    public Payroll getSelected() {
        return selected;
    }

    public void setSelected(Payroll selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
            selected.getPayrollPK().setLevel1Code(selected.getLevel1().getLevel1Code());
    }

    protected void initializeEmbeddableKey() {
        selected.setPayrollPK(new entities.PayrollPK());
    }

    private PayrollFacade getFacade() {
        return ejbFacade;
    }

    public Payroll prepareCreate() {
        selected = new Payroll();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("PayrollCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("PayrollUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("PayrollDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Payroll> getItems() {
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

    public Payroll getPayroll(entities.PayrollPK id) {
        return getFacade().find(id);
    }

    public List<Payroll> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Payroll> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass=Payroll.class)
    public static class PayrollControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PayrollController controller = (PayrollController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "payrollController");
            return controller.getPayroll(getKey(value));
        }

        entities.PayrollPK getKey(String value) {
            entities.PayrollPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entities.PayrollPK();
            key.setIdNumber(values[0]);
            key.setLevel1Code(values[1]);
            key.setEffectiveDate(java.sql.Date.valueOf(values[2]));
            return key;
        }

        String getStringKey(entities.PayrollPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdNumber());
            sb.append(SEPARATOR);
            sb.append(value.getLevel1Code());
            sb.append(SEPARATOR);
            sb.append(value.getEffectiveDate());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Payroll) {
                Payroll o = (Payroll) object;
                return getStringKey(o.getPayrollPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Payroll.class.getName()});
                return null;
            }
        }

    }

}
