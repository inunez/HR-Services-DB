package controllers;

import entities.Earning;
import entities.EarningSummary;
import entities.Employee;
import entities.Plaxa;
import entities.PoliceCheck;
import entities.Uniform;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
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
import static controllers.util.JsfUtil.addErrorMessage;
import entities.Account;
import entities.ChangeCondition;
import entities.EarningLeave;
import entities.JobTitle;
import entities.ManagerPosition;
import entities.Payroll;
import entities.Position;
import entities.Visa;
import java.util.Date;
import java.util.stream.Collectors;
import org.primefaces.context.RequestContext;

@Named("employeeController")
@SessionScoped
public class EmployeeController implements Serializable {

    @EJB
    private session_beans.EmployeeFacade ejbFacade;
    private List<Employee> items = null;
    private Employee selected;
    private String searchText;
    private String searchType;
    private boolean showEmployeesTable = false;
    private boolean employeeSelected = false;
    private int currentTabIndex = 0;
    private String status = "";
    private ChangeCondition changeEmploymentCondition;

    private Collection<Uniform> uniformSortedCollection;
    private Collection<Earning> earningDistinctCollection;
    private ArrayList<EarningSummary> earningSummaryList;
    private ArrayList<Payroll> payrollList;
    private ArrayList<EarningLeave> leaveList;
    private ArrayList<PoliceCheck> policeCheckList;
    private ArrayList<Visa> visaList;
    private ArrayList<Employee> reportsToList;
    private ArrayList<Employee> filteredEmployees;
    private List<Position> listOfPositions;
    private List<Account> listOfAccounts;
    private Collection<ManagerPosition> employeesByService;
    private List<JobTitle> listOfClassifications;
    
    
    private final int[] DEFAULT_RANGE = {0, 99};
    private final int SEARCH_BY_NAME = 1;
    private final int SEARCH_BY_ID = 2;
    private final int SEARCH_BY_SERVICE = 3;
    private final int SEARCH_BY_POSITION = 4;
    
    public EmployeeController() {
        searchType = Integer.toString(SEARCH_BY_NAME);
        status = "A";
        clearCollections();
    }

    public ChangeCondition getChangeEmploymentCondition() {
        if (changeEmploymentCondition == null){
            changeEmploymentCondition = new ChangeCondition(selected);
        }
        return changeEmploymentCondition;
    }

    public void setChangeEmploymentCondition(ChangeCondition changeEmploymentCondition) {
        this.changeEmploymentCondition = changeEmploymentCondition;
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

    public List<Position> getListOfPositions() {
        /*Load only active positions*/
        if(listOfPositions == null){
            listOfPositions = ejbFacade.getListPosition();
        }
        return listOfPositions;
    }

    public void setListOfPositions(List<Position> listOfPositions) {
        this.listOfPositions = listOfPositions;
    }

    public List<Account> getListOfAccounts() {
        if(listOfAccounts == null){
            listOfAccounts = ejbFacade.getListAccount();
        }
        return listOfAccounts;
    }

    public void setListOfAccounts(List<Account> listOfAccounts) {
        this.listOfAccounts = listOfAccounts;
    }

    public List<JobTitle> getListOfClassifications() {
        if(listOfClassifications == null){
            listOfClassifications = ejbFacade.getListJobTitles();
        }
        return listOfClassifications;
    }

    public void setListOfClassifications(List<JobTitle> listOfClassifications) {
        this.listOfClassifications = listOfClassifications;
    }

    public Collection<ManagerPosition> getEmployeesByService() {
        if(employeesByService == null){
            employeesByService = changeEmploymentCondition.getNewAccount().getManagerPositionCollection();
        }
        return employeesByService;
    }

    public void setEmployeesByService(List<ManagerPosition> employeesByService) {
        this.employeesByService = employeesByService;
    }

    public void setSelected(Employee selected) {
        this.selected = selected;
        employeeSelected = selected != null;
        clearCollections();
    }

    public boolean isShowEmployeesTable() {
        return showEmployeesTable;
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

    public int getCurrentTabIndex() {
        return currentTabIndex;
    }

    public void setCurrentTabIndex(int currentTabIndex) {
        this.currentTabIndex = currentTabIndex;
    }

    public void setShowEmployeesTable(boolean showEmployeesTable) {
        this.showEmployeesTable = showEmployeesTable;
    }

    public ArrayList<Employee> getFilteredEmployees() {
        return filteredEmployees;
    }

    public void setFilteredEmployees(ArrayList<Employee> filteredEmployees) {
        this.filteredEmployees = filteredEmployees;
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

    @FacesConverter(forClass = Employee.class)
    public static class EmployeeControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            EmployeeController controller = (EmployeeController) facesContext.getApplication().getELResolver().
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

    public void searchEmployee() {
        String tempString = "";
        String[] search = new String[4];
        int type = Integer.parseInt(searchType);

        showEmployeesTable = false;

        search[2] = "status";
        search[3] = status;

        tempString = searchText;
        switch (type) {
            case SEARCH_BY_NAME:
                search[0] = "findByFullName";
                search[1] = "fullName";
                searchText = "%".concat(searchText).concat("%");
                break;
            case SEARCH_BY_ID:
                search[0] = "findByIdNumber";
                search[1] = "idNumber";
                searchText = "000000".concat(searchText);
                int len = searchText.length();
                searchText = searchText.substring(len - 6);
                break;
            case SEARCH_BY_SERVICE:
                search[0] = "findByAccount";
                search[1] = "accountDesc";
                searchText = "%".concat(searchText.toUpperCase()).concat("%");
                break;
            case SEARCH_BY_POSITION:
                search[0] = "findByPosition";
                search[1] = "position";
                searchText = "%".concat(searchText.toUpperCase()).concat("%");
                break;
            default:
                throw new AssertionError();
        }

        try {
            items = ejbFacade.getEmployeeByType(searchText, search);
            if (items.size() == 1) {
                clearCollections();
                selected = items.get(0);
                currentTabIndex = 1;
            } else {
                //test
//                RequestContext.getCurrentInstance().addCallbackParam("tableShown", true);
                if (items.size() > 0) {
                    int max = DEFAULT_RANGE[1] > items.size() ? items.size() : DEFAULT_RANGE[1];
                    items = items.subList(DEFAULT_RANGE[0], max);
                    showEmployeesTable = true;
                    currentTabIndex = 2;
                } else {
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

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public Collection<Earning> getEarningDistinctCollection() {

        if (selected != null && earningDistinctCollection == null) {
            earningDistinctCollection = getFacade().getEarningDistinctCollection(selected.getEmployeePK().getIdNumber());
        }
        return earningDistinctCollection;

    }

    public ArrayList<Payroll> getPayrollList() {

        if (selected != null && payrollList == null) {
            payrollList = new ArrayList<>(selected.getPayrollCollection());
            Comparator<Payroll> dateComparator = (Payroll p1, Payroll p2) -> p2.getPayrollPK().getEffectiveDate().compareTo(p1.getPayrollPK().getEffectiveDate());
            Collections.sort(payrollList, dateComparator);
        }
        return payrollList;
    }

    public ArrayList<EarningSummary> getEarningSummaryList() {

        if (selected != null && earningSummaryList == null) {
            earningSummaryList = new ArrayList<>(selected.getEarningSummaryCollection());
            Comparator<EarningSummary> dateComparator = (EarningSummary p1, EarningSummary p2) -> p2.getEarningSummaryPK().getPaidUpToDate().compareTo(p1.getEarningSummaryPK().getPaidUpToDate());
            Collections.sort(earningSummaryList, dateComparator);
        }
        return earningSummaryList;
    }

    public ArrayList<PoliceCheck> getPoliceCheckList() {

        if (selected != null && policeCheckList == null) {
            policeCheckList = new ArrayList<>(selected.getPoliceCheckCollection());

            Comparator<PoliceCheck> dateComparator = (PoliceCheck p1, PoliceCheck p2) -> p2.getExpiryDate().compareTo(p1.getExpiryDate());
            Collections.sort(policeCheckList, dateComparator);
        }
        return policeCheckList;
    }

    public ArrayList<Visa> getVisaList() {

        if (selected != null && visaList == null) {
            visaList = new ArrayList<>(selected.getVisaCollection());

            Comparator<Visa> dateComparator = (Visa p1, Visa p2) -> p2.getVisaIssuedDate().compareTo(p1.getVisaIssuedDate());
            Collections.sort(visaList, dateComparator);
        }
        return visaList;
    }

    public ArrayList<EarningLeave> getLeaveList() {

        if (selected != null && leaveList == null) {
            leaveList = new ArrayList<>(selected.getEarningLeaveCollection());

            Comparator<EarningLeave> dateComparator = (EarningLeave p1, EarningLeave p2) -> p2.getEarningLeavePK().getDateAccruedTo().compareTo(p1.getEarningLeavePK().getDateAccruedTo());
            Collections.sort(leaveList, dateComparator);

            Date day = leaveList.get(0).getEarningLeavePK().getDateAccruedTo();
            leaveList = leaveList.stream().filter(p -> p.getEarningLeavePK().getDateAccruedTo().compareTo(day) == 0).collect(Collectors.toCollection(ArrayList::new));
        }

        return leaveList;
    }

    public ArrayList<Employee> getReportsToList() {

        if (selected != null && reportsToList == null) {
            if (!selected.getReportsToPositionId().getEmployeeCollection().isEmpty()) {
                reportsToList = new ArrayList<>(selected.getReportsToPositionId().getEmployeeCollection());

                reportsToList = reportsToList.stream().filter(p -> p.getEmployeePK().getStatus().equals("A")).collect(Collectors.toCollection(ArrayList::new));
                
            }
        }
        return reportsToList;
    }

    public Collection<Plaxa> getPlaxaSortedCollection() {
        Collection<Plaxa> plaxas = null;
        if (selected != null) {
            plaxas = selected.getPlaxaCollection();
        }
        return plaxas;
        //return getFacade().getEarningDistinctCollection(selected.getEmployeePK().getIdNumber());
    }

    public Collection<Uniform> getUniformSortedCollection() {

        if (selected != null && uniformSortedCollection == null) {
            uniformSortedCollection = selected.getUniformCollection();
        }
        return uniformSortedCollection;
        //return getFacade().getEarningDistinctCollection(selected.getEmployeePK().getIdNumber());
    }

    public void linkToAnotherEmployee() {

//        selected = selected.getReportsToPositionId().getEmployeeCollection().stream().findFirst().get();
        if (selected.getReportsToPositionId().getEmployeeCollection().iterator().hasNext()) {
            selected = selected.getReportsToPositionId().getEmployeeCollection().iterator().next();
            clearCollections();
        }
    }

    private void clearCollections() {
        uniformSortedCollection = null;
        earningDistinctCollection = null;
        payrollList = null;
        earningSummaryList = null;
        leaveList = null;
        policeCheckList = null;
        visaList = null;
        reportsToList = null;
        changeEmploymentCondition = null;
        listOfAccounts = null;
        listOfPositions = null;
        employeesByService = null;
        listOfClassifications = null;
    }
    
    /* COEC */
    
    public void cancelCOEC(){
        changeEmploymentCondition = null;
    }
 
    public void accountChange(){
        changeEmploymentCondition.setNewManager(null);
        employeesByService = null;
    }
    
    public void openCoec(){
        changeEmploymentCondition = null;
    }
}
