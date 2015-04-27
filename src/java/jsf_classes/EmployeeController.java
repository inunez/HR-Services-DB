package jsf_classes;

import entities.Earning;
import entities.Employee;
import entities.Plaxa;
import entities.PoliceCheck;
import entities.Uniform;
import jsf_classes.util.JsfUtil;
import jsf_classes.util.JsfUtil.PersistAction;
import session_beans.EmployeeFacade;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
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
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import static jsf_classes.util.JsfUtil.addErrorMessage;
import org.primefaces.context.RequestContext;


@Named("employeeController")
@SessionScoped
public class EmployeeController implements Serializable {

    @EJB private session_beans.EmployeeFacade ejbFacade;
    private List<Employee> items = null;
    private Employee selected;
    private String searchText;
    private String searchType;
    private boolean showEmployeesTable = false;
    private boolean employeeSelected = false;

    private int currentTabIndex=0;
    private String status="";
    
    private final int[] DEFAULT_RANGE = {0,99};
    private final int SEARCH_BY_NAME = 1;
    private final int SEARCH_BY_ID = 2;
    private final int SEARCH_BY_SERVICE = 3;
    private final int SEARCH_BY_POSITION = 4;

    public EmployeeController() {
        searchType = Integer.toString(SEARCH_BY_NAME);
        status = "A";
    }

    public boolean isEmployeeSelected() {
        return employeeSelected;
    }

    public void setEmployeeSelected(boolean employeeSelected) {
        this.employeeSelected = employeeSelected;
    }
    
    public Employee getSelected() {
        return selected;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public void setSelected(Employee selected) {
        this.selected = selected;
        employeeSelected = selected != null; 
    }

    public boolean isShowEmployeesTable() {
        return showEmployeesTable;
    }

    
    public int getCurrentTabIndex() {
        return currentTabIndex;
    }

    public void setCurrentTabIndex(int currentTabIndex) {
        this.currentTabIndex = currentTabIndex;
    }
    
    public void setShowEmployeesTable(boolean showEmployeesTable) {
        this.showEmployeesTable = showEmployeesTable;
    }
    
    protected void setEmbeddableKeys() {
            selected.getEmployeePK().setStatus(selected.getStatus1().getStatus());
    }

    protected void initializeEmbeddableKey() {
        selected.setEmployeePK(new entities.EmployeePK());
    }

    private EmployeeFacade getFacade() {
        return ejbFacade;
    }

    public Employee prepareCreate() {
        selected = new Employee();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("EmployeeCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("EmployeeUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("EmployeeDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Employee> getItems() {
        
        if (items == null) {
            this.searchText = "";
            searchEmployee();
//            this.searchText = "";
//            items = getFacade().findRange(DEFAULT_RANGE);
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

    public Employee getEmployee(entities.EmployeePK id) {
        return getFacade().find(id);
    }

    public List<Employee> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Employee> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass=Employee.class)
    public static class EmployeeControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EmployeeController controller = (EmployeeController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "employeeController");
            return controller.getEmployee(getKey(value));
        }

        entities.EmployeePK getKey(String value) {
            entities.EmployeePK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entities.EmployeePK();
            key.setIdNumber(values[0]);
            key.setStatus(values[1]);
            return key;
        }

        String getStringKey(entities.EmployeePK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdNumber());
            sb.append(SEPARATOR);
            sb.append(value.getStatus());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Employee) {
                Employee o = (Employee) object;
                return getStringKey(o.getEmployeePK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Employee.class.getName()});
                return null;
            }
        }

    } 
    public void searchEmployee(){
        String tempString = "";
        String [] search = new String[4];
        int type = Integer.parseInt(searchType);
        
        showEmployeesTable = false;
        
        tempString = searchText;
        switch (type) {
            case SEARCH_BY_NAME:
                search[0]="findByFullName";
                search[1]="fullName";
                searchText = "%".concat(searchText).concat("%");
                break;
            case SEARCH_BY_ID:
                search[0]="findByIdNumber";
                search[1]="idNumber";
                searchText = "000000".concat(searchText);
                int len = searchText.length();
                searchText = searchText.substring(len-6);
                break;
            case SEARCH_BY_SERVICE:
                search[0]="findByAccount";
                search[1]="accountNumber";
                break;
            case SEARCH_BY_POSITION:
                search[0]="findByPosition";
                search[1]="positionId";
                break;
            default:
                throw new AssertionError();
        }
        search[2] = "status";
        search[3] = status;
        try {
            items = ejbFacade.getEmployeeByType(searchText,search);
            if(items.size()== 1){
                selected = items.get(0);
                currentTabIndex=1;
            }else{
                //test
//                RequestContext.getCurrentInstance().addCallbackParam("tableShown", true);
                if(items.size()>0){
                    int max = DEFAULT_RANGE[1] > items.size()?items.size():DEFAULT_RANGE[1]; 
                    items = items.subList(DEFAULT_RANGE[0], max);
                    showEmployeesTable = true;
                    currentTabIndex = 2;
                }else{
                    RequestContext.getCurrentInstance().addCallbackParam("notValid", true);
                    addErrorMessage("No results found");
                    currentTabIndex = 2;
                }
//                RequestContext request = RequestContext.getCurrentInstance();
//                request.update(":tableEmployeesForm:tablePanel");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            RequestContext.getCurrentInstance().addCallbackParam("notValid", true);
            addErrorMessage("No results found");
//            UIComponent component = FacesContext.getCurrentInstance().getViewRoot().findComponent("searchDialog");
//            if (component != null) {
//                for (Entry thisEntry : component.getAttributes().entrySet()) {
//                    Object key = thisEntry.getKey();
//                    Object value = thisEntry.getValue();
//                    System.out.println(key);
//                }
//            }
        }
        searchText = tempString;
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
    
    public Collection<Earning> getEarningDistinctCollection() {
        return getFacade().getEarningDistinctCollection(selected.getEmployeePK().getIdNumber());
    }
    
    public ArrayList<PoliceCheck> getPoliceCheckList() {
        ArrayList<PoliceCheck> policeCheckList;
        policeCheckList = new ArrayList<>(selected.getPoliceCheckCollection());

        Comparator<PoliceCheck> dateComparator = (PoliceCheck p1, PoliceCheck p2) -> p2.getExpiryDate().compareTo(p1.getExpiryDate());
        Collections.sort(policeCheckList, dateComparator);
        return policeCheckList;
    }
    
    public Collection<Plaxa> getPlaxaSortedCollection() {
        return selected.getPlaxaCollection();
        //return getFacade().getEarningDistinctCollection(selected.getEmployeePK().getIdNumber());
    }
    
    public Collection<Uniform> getUniformSortedCollection() {
        return selected.getUniformCollection();
        //return getFacade().getEarningDistinctCollection(selected.getEmployeePK().getIdNumber());
    }
}


                        