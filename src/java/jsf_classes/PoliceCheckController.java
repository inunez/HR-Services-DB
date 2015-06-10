package jsf_classes;

import entities.PoliceCheck;
import jsf_classes.util.JsfUtil;
import jsf_classes.util.JsfUtil.PersistAction;
import session_beans.PoliceCheckFacade;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import static jsf_classes.util.JsfUtil.addErrorMessage;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

@Named("policeCheckController")
@SessionScoped
public class PoliceCheckController implements Serializable {

    public enum EditType {
        VIEW, EDIT, COMMENT, BULK_COMMENT
    }
     
    @EJB
    private session_beans.PoliceCheckFacade ejbFacade;
    private List<PoliceCheck> items = null;
    private List<PoliceCheck> selectedPoliceChecks = null;
    private PoliceCheck selected;
    private EditType editType;

    private String searchText;
    private String searchType;
    private boolean policeCheckSelected = false;
    private String status="";
    
    private final int SEARCH_BY_NAME = 1;
    private final int SEARCH_BY_ID = 2;
    private final int SEARCH_BY_SERVICE = 3;
    private final int SEARCH_BY_POSITION = 4;

    public PoliceCheckController() {
        searchType = Integer.toString(SEARCH_BY_NAME);
        status = "A";
        editType = EditType.VIEW;
    }

    public String getSearchText() {
        return searchText;
    }

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public boolean isPoliceCheckSelected() {
        return policeCheckSelected;
    }

    public void setPoliceCheckSelected(boolean policeCheckSelected) {
        this.policeCheckSelected = policeCheckSelected;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public PoliceCheck getSelected() {
        return selected;
    }

    public void setSelected(PoliceCheck selected) {
        this.selected = selected;
    }

    public EditType getEditType() {
        return editType;
    }

    public void setEditType(EditType editType) {
        this.editType = editType;
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

    public List<PoliceCheck> getItems() throws ParseException {
        if (items == null) {
            this.searchText = "";
            searchPoliceChecks();
//            items = getFacade().findAll();
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

    public void searchPoliceChecks() throws ParseException{
        String tempString;
        String [] search = new String[4];
        int type = Integer.parseInt(searchType);
        
        search[2] = "status";
        search[3] = status;
        
        tempString = searchText;
        switch (type) {
            case SEARCH_BY_NAME:
                search[0]="findPoliceCheckByFullName";
                search[1]="fullName";
                searchText = "%".concat(searchText).concat("%");
                break;
            case SEARCH_BY_ID:
                search[0]="findPoliceCheckByIdNumber";
                search[1]="idNumber";
                searchText = "000000".concat(searchText);
                int len = searchText.length();
                searchText = searchText.substring(len-6);
                break;
            case SEARCH_BY_SERVICE:
                search[0]="findPoliceCheckByAccount";
                search[1]="accountDesc";
                searchText = "%".concat(searchText.toUpperCase()).concat("%");
                break;
            case SEARCH_BY_POSITION:
                search[0]="findPoliceCheckByPosition";
                search[1]="position";
                searchText = "%".concat(searchText.toUpperCase()).concat("%");
                break;
            default:
                throw new AssertionError();
        }

        try {
            List<PoliceCheck> itemsFound = ejbFacade.getPoliceCheckByType(searchText,search);
            if(items == null){
                items = itemsFound;
                if (items.size() > 0) {
                    Comparator<PoliceCheck> dateComparator;

                    dateComparator = (PoliceCheck p1, PoliceCheck p2) -> p2.getExpiryDate().compareTo(p1.getExpiryDate());
                    Collections.sort(items, dateComparator.reversed());
                }
            }
            
            if(itemsFound.size()>0){
                if(itemsFound.size() < items.size()){
                    selectedPoliceChecks = itemsFound;
                }
            }else{
                RequestContext.getCurrentInstance().addCallbackParam("notValid", true);
                addErrorMessage("No results found");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            RequestContext.getCurrentInstance().addCallbackParam("notValid", true);
            addErrorMessage("No results found");
        }
        searchText = tempString;
    }
    
    
    public List<PoliceCheck> getSelectedPoliceChecks() {
        return selectedPoliceChecks;
    }
 
    public void setSelectedPoliceChecks(List<PoliceCheck> selectedPoliceChecks) {
        this.selectedPoliceChecks = selectedPoliceChecks;
    }
    
    public void onRowSelect(SelectEvent event) {
        FacesMessage msg = new FacesMessage("PoliceCheck Selected", ((PoliceCheck) event.getObject()).getEmployee().getSurname());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
 
    public void onRowUnselect(UnselectEvent event) {
        FacesMessage msg = new FacesMessage("PoliceCheck Unselected", ((PoliceCheck) event.getObject()).getEmployee().getSurname());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    
    public void save(){
        
    }
    
}

