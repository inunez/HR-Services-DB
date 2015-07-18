package controllers;

import entities.PoliceCheck;
import entities.PoliceCheckComment;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import session_beans.PoliceCheckFacade;

import java.io.Serializable;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import entities.Employee;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import javax.naming.InitialContext;
import javax.transaction.UserTransaction;
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
    @EJB
    session_beans.PoliceCheckCommentFacade ejbCommentFacade;
    private List<PoliceCheck> items = null;
    private List<PoliceCheck> selectedPoliceChecks = null;
    private PoliceCheck selected;
    private PoliceCheck tempSelected;
    private PoliceCheckComment selectedComment;
    private List<PoliceCheckComment> selectedComments = null;
    private List<PoliceCheckComment> deletedComments = null;
    private String newCommentText;
    private ArrayList<Boolean> arrayChanges;
    private boolean changeSelected; 

    private EditType editType;

    private String searchText;
    private String searchType;
    private boolean policeCheckSelected = false;
    private String status = "";

    private final int SEARCH_BY_NAME = 1;
    private final int SEARCH_BY_ID = 2;
    private final int SEARCH_BY_SERVICE = 3;
    private final int SEARCH_BY_POSITION = 4;

    public PoliceCheckController() {
        searchType = Integer.toString(SEARCH_BY_NAME);
        status = "A";
        editType = EditType.VIEW;
        arrayChanges = new ArrayList<>();
        resetArrayChanges();
        selectedComments = new ArrayList<>();   
        deletedComments = new ArrayList<>(); 
    }

    private void resetArrayChanges() {
        arrayChanges.clear();
        for (int i = 0; i < 11; i++) {
            arrayChanges.add(Boolean.FALSE);
        }
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

    public PoliceCheck getTempSelected() {
        if (tempSelected == null && selectedPoliceChecks != null) {
            if (selectedPoliceChecks.size() > 0){
                createTempPoliceCheck(selectedPoliceChecks.get(0));
            }
        }
        return tempSelected;
    }

    public void setTempSelected(PoliceCheck tempSelected) {
        this.tempSelected = tempSelected;
    }

    public EditType getEditType() {
        return editType;
    }

    public void setEditType(EditType editType) {
        this.editType = editType;
    }

    private PoliceCheckComment newComment(PoliceCheck basePoliceCheck) {
        PoliceCheckComment newCommentPoliceCheck;
        
//        Date commentDate = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date commentDate = new Date();
        newCommentPoliceCheck = new PoliceCheckComment(basePoliceCheck.getPoliceCheckPK().getIdNumber(), basePoliceCheck.getPoliceCheckPK().getStatus(),
                commentDate);
        newCommentPoliceCheck.setEmployee(basePoliceCheck.getEmployee());

        return newCommentPoliceCheck;
    }

    public PoliceCheckComment getSelectedComment() {
        if (selectedComment == null && tempSelected != null) {
            selectedComment = newComment(tempSelected);
        }
        return selectedComment;
    }

    public void setSelectedComment(PoliceCheckComment selectedComment) {
        this.selectedComment = selectedComment;
    }

    public String getNewCommentText() {
        return newCommentText;
    }

    public void setNewCommentText(String newCommentText) {
        this.newCommentText = newCommentText;
    }

    public ArrayList<Boolean> getArrayChanges() {
        return arrayChanges;
    }

    public void setArrayChanges(ArrayList<Boolean> arrayChanges) {
        this.arrayChanges = arrayChanges;
    }

    public boolean isChangeSelected() {
        return changeSelected;
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
        InitialContext cxt = null;
        UserTransaction userTx = null;

        if (selected != null) {
            setEmbeddableKeys();
            try {
                cxt = new InitialContext();
                userTx = (javax.transaction.UserTransaction) cxt.lookup("java:comp/UserTransaction");
                userTx.begin();
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                    for (PoliceCheckComment comment : deletedComments) {
                        ejbCommentFacade.remove(comment);
                    }
                    for (PoliceCheckComment comment : selected.getEmployee().getPoliceCheckCommentCollection()) {
                        ejbCommentFacade.edit(comment);
                    }
                } else {
                    getFacade().remove(selected);
                }
                userTx.commit();
                RequestContext request = RequestContext.getCurrentInstance();
                request.update("tabViewMain:tablePoliceCheckForm");
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                try {
                    userTx.rollback();
                } catch (Exception e) {
                }
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
                try {
                    userTx.rollback();
                } catch (Exception e) {
                }
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

    public void searchPoliceChecks() throws ParseException {
        String tempString;
        String[] search = new String[4];
        int type = Integer.parseInt(searchType);

        search[2] = "status";
        search[3] = status;

        tempString = searchText;
        switch (type) {
            case SEARCH_BY_NAME:
                search[0] = "findPoliceCheckByFullName";
                search[1] = "fullName";
                searchText = "%".concat(searchText).concat("%");
                break;
            case SEARCH_BY_ID:
                search[0] = "findPoliceCheckByIdNumber";
                search[1] = "idNumber";
                searchText = "000000".concat(searchText);
                int len = searchText.length();
                searchText = searchText.substring(len - 6);
                break;
            case SEARCH_BY_SERVICE:
                search[0] = "findPoliceCheckByAccount";
                search[1] = "accountDesc";
                searchText = "%".concat(searchText.toUpperCase()).concat("%");
                break;
            case SEARCH_BY_POSITION:
                search[0] = "findPoliceCheckByPosition";
                search[1] = "position";
                searchText = "%".concat(searchText.toUpperCase()).concat("%");
                break;
            default:
                throw new AssertionError();
        }

        try {
            List<PoliceCheck> itemsFound = ejbFacade.getPoliceCheckByType(searchText, search);
            if (items == null) {
                items = itemsFound;
                if (items.size() > 0) {
                    Comparator<PoliceCheck> dateComparator;

                    dateComparator = (PoliceCheck p1, PoliceCheck p2) -> p2.getExpiryDate().compareTo(p1.getExpiryDate());
                    Collections.sort(items, dateComparator.reversed());
                }
            }

            if (itemsFound.size() > 0) {
                if (itemsFound.size() < items.size()) {
                    selectedPoliceChecks = itemsFound;
                }
            } else {
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

    public List<PoliceCheckComment> getSelectedComments() {
        return selectedComments;
    }

    public void setSelectedComments(List<PoliceCheckComment> selectedComments) {
        this.selectedComments = selectedComments;
    }

    
    public void onRowSelect(SelectEvent event) {
//        FacesMessage msg = new FacesMessage("PoliceCheck Selected", ((PoliceCheck) event.getObject()).getEmployee().getSurname());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowUnselect(UnselectEvent event) {
//        FacesMessage msg = new FacesMessage("PoliceCheck Unselected", ((PoliceCheck) event.getObject()).getEmployee().getSurname());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowDblClckSelect(final SelectEvent event) {
        editType = EditType.EDIT;
        onRowSelect(event);
    }

    public void checkChangeStatus(){
        changeSelected = false;
        for (boolean item : arrayChanges) {
            if (item){
                changeSelected = true;
                break;
            }
        }
    }
            
    public void onReceivedOptionChange() {
        if (tempSelected == null) {
            createTempPoliceCheck(selectedPoliceChecks.get(0));
        }
        //FacesContext.getCurrentInstance().addMessage("receivedMultiCalendar", new FacesMessage("*Changed"));
        //put comment besides date to let user know that changes will be applied
         if (tempSelected.getYnReceived()) {
            if (tempSelected.getReceivedDate() == null) {
                tempSelected.setReceivedDate(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            }
        } else {
            tempSelected.setReceivedDate(null);
        }

    }

    public void onProcessedOptionChange() {
        if (tempSelected == null) {
            createTempPoliceCheck(selectedPoliceChecks.get(0));
        }

        if (tempSelected.getYnProcessed()) {
            if (tempSelected.getProcessedDate() == null) {
                tempSelected.setProcessedDate(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            }
        } else {
            tempSelected.setProcessedDate(null);
        }

    }

    public void onReportedOptionChange() {
        if (tempSelected == null) {
            createTempPoliceCheck(selectedPoliceChecks.get(0));
        }

        if (tempSelected.getynFirstReportSent()) {
            if (tempSelected.getFirstReportDate() == null) {
                tempSelected.setFirstReportDate(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            }
        } else {
            tempSelected.setFirstReportDate(null);
        }

    }

    public void onFirstLetterOptionChange() {
        if (tempSelected == null) {
            createTempPoliceCheck(selectedPoliceChecks.get(0));
        }

        if (tempSelected.getYnFirstLetterSent()) {
            if (tempSelected.getFirstLetterDate() == null) {
                tempSelected.setFirstLetterDate(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            }
        } else {
//            tempSelected.setFirstLetterDate(null);
        }

    }

    public void onSecondLetterOptionChange() {
        if (tempSelected == null) {
            createTempPoliceCheck(selectedPoliceChecks.get(0));
        }

        if (tempSelected.getYnSecondLetterSent()) {
            if (tempSelected.getSecondLetterDate() == null) {
                tempSelected.setSecondLetterDate(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            }
        } else {
//            tempSelected.setSecondLetterDate(null);
        }

    }

    public void cancel() {
        tempSelected = null;
        selectedPoliceChecks.clear();
        if (arrayChanges.size() > 1) {
            newCommentText = "";
            resetArrayChanges();
        }
        deletedComments.clear();
    }

    public void cancelComment() {
        selectedComment = null;
    }

    private void saveNewComment(PoliceCheck basePoliceCheck) {
        //TO DO assign user
        selectedComment.setUser("Test");
        basePoliceCheck.getEmployee().getPoliceCheckCommentCollection().add(selectedComment);
        selectedComment = null;
    }

    public void saveComment() {
        if (selectedComment != null) {
            saveNewComment(tempSelected);
        } else {
            //error
        }
    }

    public void deleteComments(){
        for (PoliceCheckComment item : selectedComments) {
            deletedComments.add(item);
            tempSelected.getEmployee().getPoliceCheckCommentCollection().remove(item);
        }
    }
    
    public void save() {
        if (selectedPoliceChecks.size() == 1) {
            selected = selectedPoliceChecks.get(0);
            selected.setExpiryDate(tempSelected.getExpiryDate());
            selected.setYnReceived(tempSelected.getYnReceived());
            selected.setReceivedDate(tempSelected.getReceivedDate());
            selected.setYnProcessed(tempSelected.getYnProcessed());
            selected.setProcessedDate(tempSelected.getProcessedDate());
            selected.setYnFirstReportSent(tempSelected.getynFirstReportSent());
            selected.setFirstReportDate(tempSelected.getFirstReportDate());
            selected.setYnFirstLetterSent(tempSelected.getYnFirstLetterSent());
            selected.setFirstLetterDate(tempSelected.getFirstLetterDate());
            selected.setYnSecondLetterSent(tempSelected.getYnSecondLetterSent());
            selected.setSecondLetterDate(tempSelected.getSecondLetterDate());
            selected.setEmployee(tempSelected.getEmployee());
            update();
        } else {
            for (PoliceCheck selectedPoliceCheck : selectedPoliceChecks) {
                if (arrayChanges.get(0)) {
//                    selectedPoliceCheck.setExpiryDate(tempSelected.getExpiryDate());
                }
                if (arrayChanges.get(1)) {
                    selectedPoliceCheck.setYnReceived(tempSelected.getYnReceived());
                    selectedPoliceCheck.setReceivedDate(tempSelected.getReceivedDate());
                }
                if (arrayChanges.get(2)) {
                    selectedPoliceCheck.setYnProcessed(tempSelected.getYnProcessed());
                    selectedPoliceCheck.setProcessedDate(tempSelected.getProcessedDate());
                }
                if (arrayChanges.get(3)) {
                    selectedPoliceCheck.setYnFirstReportSent(tempSelected.getynFirstReportSent());
                    selectedPoliceCheck.setFirstReportDate(tempSelected.getFirstReportDate());
                }
                if (arrayChanges.get(4)) {
                    selectedPoliceCheck.setYnFirstLetterSent(tempSelected.getYnFirstLetterSent());
                    selectedPoliceCheck.setFirstLetterDate(tempSelected.getFirstLetterDate());
                }
                if (arrayChanges.get(5)) {
                    selectedPoliceCheck.setYnSecondLetterSent(tempSelected.getYnSecondLetterSent());
                    selectedPoliceCheck.setSecondLetterDate(tempSelected.getSecondLetterDate());
                }
                if (!"".equals(newCommentText)) {
                    selectedComment = newComment(selectedPoliceCheck);
                    selectedComment.setComment(newCommentText);
                    saveNewComment(selectedPoliceCheck);
                }
                selected = selectedPoliceCheck;
                update();

            }
        }
        newCommentText = "";
        resetArrayChanges();
        tempSelected = null;
        selectedPoliceChecks.clear();
        deletedComments.clear();
    }

    private void createTempPoliceCheck(PoliceCheck policeCheck) {
        if (selectedPoliceChecks.size() == 1) {
            tempSelected = new PoliceCheck(policeCheck.getPoliceCheckPK().getIdNumber(), policeCheck.getPoliceCheckPK().getStatus(),
                    policeCheck.getPoliceCheckPK().getUpdateDate());
            try{
                tempSelected.setEmployee((Employee)policeCheck.getEmployee().clone());
            }catch(CloneNotSupportedException  e){
                
            }
            tempSelected.setExpiryDate(policeCheck.getExpiryDate());
            tempSelected.setFirstLetterDate(policeCheck.getFirstLetterDate());
            tempSelected.setFirstReportDate(policeCheck.getFirstReportDate());
            tempSelected.setManagerPhoneCallDate(policeCheck.getManagerPhoneCallDate());
            tempSelected.setPrecedaComment(policeCheck.getPrecedaComment());
            tempSelected.setPrmStatus(policeCheck.getPrmStatus());
            tempSelected.setProcessedDate(policeCheck.getProcessedDate());
            tempSelected.setReceivedDate(policeCheck.getReceivedDate());
            tempSelected.setSecondLetterDate(policeCheck.getSecondLetterDate());
            tempSelected.setThirdLetterDate(policeCheck.getThirdLetterDate());
            tempSelected.setYnFirstLetterSent(policeCheck.getYnFirstLetterSent());
            tempSelected.setYnFirstReportSent(policeCheck.getynFirstReportSent());
            tempSelected.setYnPhoneCallDone(policeCheck.getYnPhoneCallDone());
            tempSelected.setYnProcessed(policeCheck.getYnProcessed());
            tempSelected.setYnReceived(policeCheck.getYnReceived());
            tempSelected.setYnSecondLetterSent(policeCheck.getYnSecondLetterSent());
            tempSelected.setYnThirdlettersent(policeCheck.getYnThirdlettersent());
        } else {
            tempSelected = new PoliceCheck();
            tempSelected.setYnFirstLetterSent(false);
            tempSelected.setYnFirstReportSent(false);
            tempSelected.setYnProcessed(false);
            tempSelected.setYnReceived(false);
            tempSelected.setYnSecondLetterSent(false);

        }
    }

}
