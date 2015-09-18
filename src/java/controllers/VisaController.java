package controllers;

import entities.Visa;
import controllers.util.JsfUtil;
import controllers.util.JsfUtil.PersistAction;
import static controllers.util.JsfUtil.addErrorMessage;
import entities.Employee;
import entities.Visa;
import entities.VisaComment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import session_beans.VisaFacade;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
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
import javax.mail.MessagingException;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@Named("visaController")
@SessionScoped
public class VisaController implements Serializable {

    public enum EditType {

        VIEW, EDIT, COMMENT, BULK_COMMENT
    }

    @EJB
    private session_beans.VisaFacade ejbFacade;
    @EJB
    private session_beans.VisaCommentFacade ejbCommentFacade;
    @EJB
    private session_beans.EmployeeFacade ejbEmployeeFacade;

    private Visa selected;
    private Visa tempSelected;

    private EditType editType;
    private String searchText;
    private String searchType;
    private String status = "";
    private String fileNameReport = "";
    private Set<String> managerEmails;
    private StreamedContent reportFile;
    private String path;

    private List<Visa> items = null;
    private List<Visa> selectedVisas = null;
    private List<Visa> filteredVisas = null;
    private VisaComment selectedComment;
    private List<VisaComment> selectedComments = null;
    private List<VisaComment> deletedComments = null;
    private String newCommentText;
    private ArrayList<Boolean> arrayChanges;
    private boolean changeSelected;

    private final int SEARCH_BY_NAME = 1;
    private final int SEARCH_BY_ID = 2;
    private final int SEARCH_BY_SERVICE = 3;
    private final int SEARCH_BY_POSITION = 4;

    public String getNewCommentText() {
        return newCommentText;
    }

    public void setNewCommentText(String newCommentText) {
        this.newCommentText = newCommentText;
    }

    public boolean isChangeSelected() {
        return changeSelected;
    }

    public ArrayList<Boolean> getArrayChanges() {
        return arrayChanges;
    }

    public void setArrayChanges(ArrayList<Boolean> arrayChanges) {
        this.arrayChanges = arrayChanges;
    }

    public VisaComment getSelectedComment() {
        if (selectedComment == null && tempSelected != null) {
            selectedComment = newComment(tempSelected);
        }
        return selectedComment;
    }

    public Visa getTempSelected() {
        if (tempSelected == null && selectedVisas != null) {
            if (selectedVisas.size() > 0) {
                createTempVisa(selectedVisas.get(0));
            }
        }
        return tempSelected;
    }

    public void setTempSelected(Visa tempSelected) {
        this.tempSelected = tempSelected;
    }

    public List<Visa> getFilteredVisas() {
        return filteredVisas;
    }

    public List<VisaComment> getSelectedComments() {
        return selectedComments;
    }

    public void setSelectedComments(List<VisaComment> selectedComments) {
        this.selectedComments = selectedComments;
    }

    public void setFilteredVisas(List<Visa> filteredVisas) {
        this.filteredVisas = filteredVisas;
    }

    public String getFileNameReport() {
        return fileNameReport;
    }

    public void setFileNameReport(String fileNameReport) {
        this.fileNameReport = fileNameReport;
    }

    public StreamedContent getReportFile() {

        String contentType = FacesContext.getCurrentInstance().getExternalContext().getMimeType(path + fileNameReport);
        InputStream stream = null;
        try {
            stream = new FileInputStream(path + fileNameReport);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VisaController.class.getName()).log(Level.SEVERE, null, ex);
        }
        reportFile = new DefaultStreamedContent(stream, contentType, fileNameReport);

        return reportFile;
    }

    public EditType getEditType() {
        return editType;
    }

    public void setEditType(EditType editType) {
        this.editType = editType;
    }

    public List<Visa> getSelectedVisas() {
        return selectedVisas;
    }

    public void setSelectedVisas(List<Visa> selectedVisas) {
        this.selectedVisas = selectedVisas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public VisaController() {
        searchType = Integer.toString(SEARCH_BY_NAME);
        status = "A";
        editType = EditType.VIEW;
        arrayChanges = new ArrayList<>();
        resetArrayChanges();
        selectedComments = new ArrayList<>();
        deletedComments = new ArrayList<>();
        path = ResourceBundle.getBundle("/Parameters").getString("Path");

        managerEmails = new HashSet();
    }

    public Visa getSelected() {
        return selected;
    }

    public void setSelected(Visa selected) {
        this.selected = selected;
    }

    protected void setEmbeddableKeys() {
        selected.getVisaPK().setIdNumber(selected.getEmployee().getEmployeePK().getIdNumber());
    }

    protected void initializeEmbeddableKey() {
        selected.setVisaPK(new entities.VisaPK());
    }

    private VisaFacade getFacade() {
        return ejbFacade;
    }

    public Visa prepareCreate() {
        selected = new Visa();
        initializeEmbeddableKey();
        return selected;
    }

    public void create() {
        persist(PersistAction.CREATE, ResourceBundle.getBundle("/Bundle").getString("VisaCreated"));
        if (!JsfUtil.isValidationFailed()) {
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public void update() {
        persist(PersistAction.UPDATE, ResourceBundle.getBundle("/Bundle").getString("VisaUpdated"));
    }

    public void destroy() {
        persist(PersistAction.DELETE, ResourceBundle.getBundle("/Bundle").getString("VisaDeleted"));
        if (!JsfUtil.isValidationFailed()) {
            selected = null; // Remove selection
            items = null;    // Invalidate list of items to trigger re-query.
        }
    }

    public List<Visa> getItems() throws ParseException {
        if (items == null) {
            this.searchText = "";
            searchVisas();
        }
        return items;
    }

    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            setEmbeddableKeys();
            try {
                if (persistAction != PersistAction.DELETE) {
                    getFacade().edit(selected);
                    for (VisaComment comment : deletedComments) {
                        ejbCommentFacade.remove(comment);
                    }
                    for (VisaComment comment : selected.getEmployee().getVisaCommentCollection()) {
                        ejbCommentFacade.edit(comment);
                    }
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

    public Visa getVisa(entities.VisaPK id) {
        return getFacade().find(id);
    }

    public List<Visa> getItemsAvailableSelectMany() {
        return getFacade().findAll();
    }

    public List<Visa> getItemsAvailableSelectOne() {
        return getFacade().findAll();
    }

    @FacesConverter(forClass = Visa.class)
    public static class VisaControllerConverter implements Converter {

        private static final String SEPARATOR = "#";
        private static final String SEPARATOR_ESCAPED = "\\#";

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            VisaController controller = (VisaController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "visaController");
            return controller.getVisa(getKey(value));
        }

        entities.VisaPK getKey(String value) {
            entities.VisaPK key;
            String values[] = value.split(SEPARATOR_ESCAPED);
            key = new entities.VisaPK();
            key.setIdNumber(values[0]);
            key.setPassportNumber(values[1]);
            key.setUpdateDate(java.sql.Date.valueOf(values[2]));
            return key;
        }

        String getStringKey(entities.VisaPK value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value.getIdNumber());
            sb.append(SEPARATOR);
            sb.append(value.getPassportNumber());
            sb.append(SEPARATOR);
            sb.append(value.getUpdateDate());
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Visa) {
                Visa o = (Visa) object;
                return getStringKey(o.getVisaPK());
            } else {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "object {0} is of type {1}; expected type: {2}", new Object[]{object, object.getClass().getName(), Visa.class.getName()});
                return null;
            }
        }

    }

    public void searchVisas() throws ParseException {
        String tempString;
        String[] search = new String[4];
        int type = Integer.parseInt(searchType);

        search[2] = "status";
        search[3] = status;

        tempString = searchText;
        switch (type) {
            case SEARCH_BY_NAME:
                search[0] = "findVisaByFullName";
                search[1] = "fullName";
                searchText = "%".concat(searchText).concat("%");
                break;
            case SEARCH_BY_ID:
                search[0] = "findVisaByIdNumber";
                search[1] = "idNumber";
                searchText = "000000".concat(searchText);
                int len = searchText.length();
                searchText = searchText.substring(len - 6);
                break;
            case SEARCH_BY_SERVICE:
                search[0] = "findVisaByAccount";
                search[1] = "accountDesc";
                searchText = "%".concat(searchText.toUpperCase()).concat("%");
                break;
            case SEARCH_BY_POSITION:
                search[0] = "findVisaByPosition";
                search[1] = "position";
                searchText = "%".concat(searchText.toUpperCase()).concat("%");
                break;
            default:
                throw new AssertionError();
        }

        try {
            List<Visa> itemsFound = ejbFacade.getVisaByType(searchText, search);
            if (items == null) {
                items = itemsFound;
                if (items.size() > 0) {
                    Comparator<Visa> dateComparator;

                    dateComparator = (Visa p1, Visa p2) -> p2.getVisaExpiryDate().compareTo(p1.getVisaExpiryDate());
                    Collections.sort(items, dateComparator.reversed());
                }
            }

            if (itemsFound.size() > 0) {
                if (itemsFound.size() < items.size()) {
                    selectedVisas = itemsFound;
                }
            } else {
                RequestContext.getCurrentInstance().addCallbackParam("notValid", true);
                addErrorMessage("No results found");
                selectedVisas.clear();
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            RequestContext.getCurrentInstance().addCallbackParam("notValid", true);
            addErrorMessage("No results found");
        }
        searchText = "";
    }

    public void processVisaReport() {

        //TO DO get names from properties
        String[] tabNames = {"CCHNE", "HO", "NTHC", "STH", "SYDC", "SYDN", "WMH MSIC", "WST", "Tables"};

        managerEmails.clear();
        Map tabCounter = new HashMap();
        Map listDivision;
        Employee manager;
        Date now = new Date();
        int row;

        for (String tabName : tabNames) {
            tabCounter.put(tabName, 1);
        }

        //Reset name as flag
        fileNameReport = "";

        try {
            FileInputStream file = new FileInputStream(new File(path + ResourceBundle.getBundle("/Parameters").getString("fileNameTemplateVisa")));

            HSSFWorkbook workbook = new HSSFWorkbook(file);
            HSSFSheet sheet = workbook.getSheetAt(8); //tables
            Cell cell;

            listDivision = loadListDivision(sheet);
            //TO DO sort collection by Division - Manager Full name and Expiry Date
            for (Visa visa : items) {
                String division = visa.getEmployee().getLevel3Code().getLevel3CodeDescription();
                String tab = (String) listDivision.get(division);
                sheet = workbook.getSheet(tab);
                row = (Integer) tabCounter.get(tab);

                cell = sheet.getRow(row).getCell(0);
                manager = ejbEmployeeFacade.findManager(visa.getEmployee(), true);
                if (manager == visa.getEmployee()) {
                    cell.setCellValue("Manager Not Found");
                } else {
                    cell.setCellValue(manager.getFirstName() + " " + manager.getSurname());
                    managerEmails.add(manager.getEmail().getEmail());
                }

                cell = sheet.getRow(row).getCell(1);
                cell.setCellValue(division);

                String status = getExpiryDateStatus(visa.getVisaExpiryDate());
                cell = sheet.getRow(row).getCell(2);
                cell.setCellValue(status);

                cell = sheet.getRow(row).getCell(3);
                cell.setCellValue(Double.valueOf(visa.getVisaPK().getIdNumber()));

                cell = sheet.getRow(row).getCell(4);
                cell.setCellValue(visa.getEmployee().getSurname());

                cell = sheet.getRow(row).getCell(5);
                cell.setCellValue(visa.getEmployee().getFirstName());

                cell = sheet.getRow(row).getCell(6);
                cell.setCellValue(visa.getEmployee().getPayrollCollection().iterator().next().getAccountNumber().getAccountDescription());

                cell = sheet.getRow(row).getCell(7);
                cell.setCellValue(visa.getVisaIssuedDate());

                cell = sheet.getRow(row).getCell(8);
                cell.setCellValue(visa.getVisaExpiryDate());

                cell = sheet.getRow(row).getCell(9);
                cell.setCellValue(visa.getPrecedaComment1());
                
                String comment;
                cell = sheet.getRow(row).getCell(10);
                if (visa.getEmployee().getVisaCommentCollection().isEmpty()) {
                    comment = "";
                } else {
                    comment = visa.getEmployee().getVisaCommentCollection().iterator().next().getComment();
                }
                cell.setCellValue(comment);

                tabCounter.replace(tab, ++row);
            }

            //Delete empty rows
            Row currentRow;
            for (int f = 0; f < workbook.getNumberOfSheets(); f++) {
                sheet = workbook.getSheetAt(f);
                int lastRow = (Integer) tabCounter.get(workbook.getSheetName(f));
                for (int i = lastRow; i < sheet.getLastRowNum(); i++) {
                    currentRow = sheet.getRow(i);
                    if (currentRow != null) {
                        sheet.removeRow(currentRow);
                    }
                }
            }
            file.close();

            workbook.removeSheetAt(8);
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            fileNameReport = String.format("Visa Report %d %s %d.xls", cal.get(Calendar.DAY_OF_MONTH),
                    new SimpleDateFormat("MMMM").format(cal.getTime()), cal.get(Calendar.YEAR));

            FileOutputStream outFile = new FileOutputStream(new File(path + fileNameReport));
            workbook.write(outFile);
            outFile.close();

            String successMessage = ResourceBundle.getBundle("/Bundle").getString("reportCreate");
            JsfUtil.addSuccessMessage(successMessage);
        } catch (FileNotFoundException e) {
            String successMessage = ResourceBundle.getBundle("/Bundle").getString("fileNotFound");
            JsfUtil.addSuccessMessage(successMessage);
        } catch (IOException e) {
            String successMessage = ResourceBundle.getBundle("/Bundle").getString("errorCreatingReport");
            JsfUtil.addSuccessMessage(successMessage);
        }
    }

    public void createVisaLetters() {
        System.out.println("letter");
    }

    public void onRowSelect(SelectEvent event) {
//        FaCcesMessage msg = new FacesMessage("Visa Selected", ((Visa) event.getObject()).getEmployee().getSurname());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowUnselect(UnselectEvent event) {
//        FacesMessage msg = new FacesMessage("Visa Unselected", ((Visa) event.getObject()).getEmployee().getSurname());
//        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowDblClckSelect(final SelectEvent event) {
        editType = EditType.EDIT;
        onRowSelect(event);
    }

    public String getExpiryDateClass(Date expiryDate) {
        String expiryDateClass = "";

        if (expiryDate != null) {
            expiryDateClass = getExpiryDateStatus(expiryDate);
            if (expiryDateClass.equals(ResourceBundle.getBundle("/Bundle").getString("VisaDue"))) {
                expiryDateClass = "Due";
            }
        }

        return expiryDateClass;
    }

    private String getExpiryDateStatus(Date expiryDate) {
        Date now = new Date();
        String visaStatus = "";

        if (expiryDate.compareTo(now) < 0) {
            visaStatus = ResourceBundle.getBundle("/Bundle").getString("VisaExpired");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(now);
            cal.add(Calendar.DATE, 7); //minus number would decrement the days
            Date nextWeek = cal.getTime();
            if (expiryDate.before(nextWeek)) {
                visaStatus = ResourceBundle.getBundle("/Bundle").getString("VisaDue");
            }
        }
        return visaStatus;
    }

    public void sendReportByEmail() throws IOException {

        if (!"".equals(this.fileNameReport)) {
            List<String> cc = new ArrayList<>();
            List<String> attachments = new ArrayList<>();

            //TO DO remove these line in production
            managerEmails.clear();
            managerEmails.add("ismael.nunez@me.com");
            cc.add("inunez@unitingcarenswact.org.au");
            cc.add("annzaragoza@unitingcarenswact.org.au");
            cc.add("andresavellaneda@unitingcarenswact.org.au");

            attachments.add(path + fileNameReport);
            String formCHC = ResourceBundle.getBundle("/Parameters").getString("formCHC");
            String attachmentCHC = ResourceBundle.getBundle("/Parameters").getString("attachmentCHC");
            if (!"".equals(formCHC)) {
                attachments.add(path + formCHC);
            }
            if (!"".equals(attachmentCHC)) {
                attachments.add(path + attachmentCHC);
            }

            //TO DO activate this in UCA
            String user = System.getProperty("user.name");

            XWPFDocument docx = new XWPFDocument(
                    new FileInputStream(path + "body.docx"));
            //using XWPFWordExtractor Class
            XWPFWordExtractor we = new XWPFWordExtractor(docx);
            String body = we.getText();

            try {
                //Get host and port from properties
                //Subject and message from a word file
                //user and password from User object
                JsfUtil.sendEmail("ismael.nunez@me.com", "Columbia01", managerEmails, cc,
                        ResourceBundle.getBundle("/Parameters").getString("emailSubjectCHC"), body, attachments);

                String successMessage = ResourceBundle.getBundle("/Bundle").getString("emailSent");
                JsfUtil.addSuccessMessage(successMessage);
            } catch (MessagingException ex) {
                String successMessage = ResourceBundle.getBundle("/Bundle").getString("emailNotSent");
                JsfUtil.addSuccessMessage(successMessage);
//            Logger.getLogger(VisaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            String successMessage = ResourceBundle.getBundle("/Bundle").getString("reportNotGenerated");
            JsfUtil.addSuccessMessage(successMessage);
        }
    }

    private Map loadListDivision(HSSFSheet sheet) {
        Map list = new HashMap();
        String division;
        String tab;

        Iterator<Row> iterator = sheet.rowIterator();

        while (iterator.hasNext()) {
            Row currentRow = iterator.next();
            division = currentRow.getCell(0).getStringCellValue();
            tab = currentRow.getCell(1).getStringCellValue();
            list.put(division, tab);
        }

        return list;
    }

    public void cancel() {
        tempSelected = null;
        selectedVisas.clear();
        if (arrayChanges.size() > 1) {
            newCommentText = "";
            resetArrayChanges();
        }
        deletedComments.clear();
    }

    private void resetArrayChanges() {
        arrayChanges.clear();
        for (int i = 0; i < 11; i++) {
            arrayChanges.add(Boolean.FALSE);
        }
    }

    public void onReportedOptionChange() {
        if (tempSelected == null) {
            createTempVisa(selectedVisas.get(0));
        }

        if (tempSelected.getYnFirstReportSent()) {
            if (tempSelected.getFirstReportDate() == null) {
                tempSelected.setFirstReportDate(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            }
        } else {
            tempSelected.setFirstReportDate(null);
        }

    }

    public void onFirstLetterOptionChange() {
        if (tempSelected == null) {
            createTempVisa(selectedVisas.get(0));
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
            createTempVisa(selectedVisas.get(0));
        }

        if (tempSelected.getYnSecondLetterSent()) {
            if (tempSelected.getSecondLetterDate() == null) {
                tempSelected.setSecondLetterDate(Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()));
            }
        } else {
//            tempSelected.setSecondLetterDate(null);
        }
    }

    private void createTempVisa(Visa visa) {
        if (selectedVisas.size() == 1) {
            tempSelected = new Visa(visa.getVisaPK().getIdNumber(), visa.getVisaPK().getStatus(), visa.getVisaPK().getPassportNumber(),
                    visa.getVisaPK().getUpdateDate());
            try {
                tempSelected.setEmployee((Employee) visa.getEmployee().clone());
            } catch (CloneNotSupportedException e) {

            }
            tempSelected.setVisaCountry(visa.getVisaCountry());
            tempSelected.setVisaIssuedDate(visa.getVisaIssuedDate());
            tempSelected.setVisaExpiryDate(visa.getVisaExpiryDate());
            tempSelected.setFirstLetterDate(visa.getFirstLetterDate());
            tempSelected.setFirstReportDate(visa.getFirstReportDate());
            tempSelected.setManagerPhoneCallDate(visa.getManagerPhoneCallDate());
            tempSelected.setSecondLetterDate(visa.getSecondLetterDate());
            tempSelected.setYnFirstLetterSent(visa.getYnFirstLetterSent());
            tempSelected.setYnFirstReportSent(visa.getYnFirstReportSent());
            tempSelected.setYnPhoneCallDone(visa.getYnPhoneCallDone());
            tempSelected.setYnSecondLetterSent(visa.getYnSecondLetterSent());
            tempSelected.setPrecedaComment1(visa.getPrecedaComment1());
            tempSelected.setPrecedaComment2(visa.getPrecedaComment2());
            tempSelected.setPrecedaComment3(visa.getPrecedaComment3());
        } else {
            tempSelected = new Visa();
            tempSelected.setYnFirstLetterSent(false);
            tempSelected.setYnFirstReportSent(false);
            tempSelected.setYnSecondLetterSent(false);
        }
    }

    private VisaComment newComment(Visa baseVisa) {
        VisaComment newCommentVisa = null;

//        Date commentDate = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
        Date commentDate = new Date();
        if (baseVisa.getVisaPK() != null) {
            newCommentVisa = new VisaComment(baseVisa.getVisaPK().getIdNumber(), baseVisa.getVisaPK().getStatus(),
                    commentDate);
            newCommentVisa.setEmployee(baseVisa.getEmployee());
        }
        return newCommentVisa;
    }

    public void deleteComments() {
        for (VisaComment item : selectedComments) {
            deletedComments.add(item);
            tempSelected.getEmployee().getVisaCommentCollection().remove(item);
        }
    }

    public void save() {
        if (selectedVisas.size() == 1) {
            selected = selectedVisas.get(0);
            selected.setVisaIssuedDate(tempSelected.getVisaIssuedDate());
            selected.setVisaExpiryDate(tempSelected.getVisaExpiryDate());
            selected.setYnFirstReportSent(tempSelected.getYnFirstReportSent());
            selected.setFirstReportDate(tempSelected.getFirstReportDate());
            selected.setYnFirstLetterSent(tempSelected.getYnFirstLetterSent());
            selected.setFirstLetterDate(tempSelected.getFirstLetterDate());
            selected.setYnSecondLetterSent(tempSelected.getYnSecondLetterSent());
            selected.setSecondLetterDate(tempSelected.getSecondLetterDate());
            selected.setEmployee(tempSelected.getEmployee());
            update();
        } else {
            for (Visa selectedVisa : selectedVisas) {
                if (arrayChanges.get(0)) {
//                    selectedVisa.setExpiryDate(tempSelected.getExpiryDate());
                }

                if (arrayChanges.get(4)) {
                    selectedVisa.setYnFirstLetterSent(tempSelected.getYnFirstLetterSent());
                    selectedVisa.setFirstLetterDate(tempSelected.getFirstLetterDate());
                }
                if (arrayChanges.get(5)) {
                    selectedVisa.setYnSecondLetterSent(tempSelected.getYnSecondLetterSent());
                    selectedVisa.setSecondLetterDate(tempSelected.getSecondLetterDate());
                }
                if (!"".equals(newCommentText)) {
                    selectedComment = newComment(selectedVisa);
                    selectedComment.setComment(newCommentText);
                    saveNewComment(selectedVisa);
                }
                selected = selectedVisa;
                update();

            }
        }
        newCommentText = "";
        resetArrayChanges();
        tempSelected = null;
        selectedVisas.clear();
        deletedComments.clear();
    }

    private void saveNewComment(Visa baseVisa) {
        //TO DO assign user
        selectedComment.setUser("Test");
        baseVisa.getEmployee().getVisaCommentCollection().add(selectedComment);
        selectedComment = null;
    }

    public void checkChangeStatus() {
        changeSelected = false;
        for (boolean item : arrayChanges) {
            if (item) {
                changeSelected = true;
                break;
            }
        }
    }

    public void cancelComment() {
        selectedComment = null;
    }

    public void saveComment() {
        if (selectedComment != null) {
            saveNewComment(tempSelected);
        } else {
            //error
        }
    }

    public void removeVisas() {
        for (Visa item : selectedVisas) {
            for (VisaComment comment : item.getEmployee().getVisaCommentCollection()) {
                ejbCommentFacade.remove(comment);
            }
            getFacade().remove(item);
//            selectedVisas.remove(item);
            items.remove(item);
        }
        selectedVisas.clear();
    }
}
