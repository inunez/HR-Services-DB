
package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ChangeCondition {
    
    public enum VariationType {
        PERMANENT, SECONDMENT, HGD
    }
    
   
    private VariationType coecVariationType;
    private Date effectiveDate;
    private Date secondmentDate;
    private Employee employee;
    private Position newPosition;
    private String newPositionName;
    private Account newAccount;
    private Employee newManager;
    private String newClassification;
    private Double newSalary;
    private String newEmploymentType;
    private String newAward;
    private ContractType newContract;
    private Date currentDate;
    private Double newHours;

    public ChangeCondition() {
        this.coecVariationType = VariationType.PERMANENT;
    }

    public ChangeCondition(Employee employee) {
        this.currentDate = new Date();
        this.coecVariationType = VariationType.PERMANENT;
        this.effectiveDate = new Date();
        this.employee = employee;
        this.newPosition = employee.getPositionId();
        this.newPositionName = employee.getPositionId().getPositionTitle();
        List<Payroll> payrollList = new ArrayList<>(employee.getPayrollCollection());
        Comparator<Payroll> dateComparator = (Payroll p1, Payroll p2) -> p2.getPayrollPK().getEffectiveDate().compareTo(p1.getPayrollPK().getEffectiveDate());
        Collections.sort(payrollList, dateComparator);
        Payroll payroll = payrollList.iterator().next();
        this.newAccount = payroll.getAccountNumber();
        for (Employee manager : employee.getReportsToPositionId().getEmployeeCollection()) {
            if(manager.getEmployeePK().getStatus().equals("A")){
                this.newManager = manager;
                break;
            }
        }
        //Collection<Employee> managers = employee.getReportsToPositionId().getEmployeeCollection();//.stream().filter(e -> e.getEmployeePK().getStatus().equals("A"))
//                .collect(Collectors.toCollection(ArrayList::new));
        //if (!managers.isEmpty()){
            //this.newManager = managers.get(0);
        //}
        this.newClassification = payroll.getJobTitle().getJobTitle().concat(" - ").concat(payroll.getJobTitle().getJobTitleDescription());
        this.newSalary = payroll.getBaseRate();
        this.newEmploymentType = payroll.getEmploymentType().getEmploymentType();
        this.newAward = payroll.getAwardCode().getAwardCode();
        this.newHours = Double.parseDouble(payroll.getBaseHoursCode());
    }

    public String getNewPositionName() {
        return newPositionName;
    }

    public void setNewPositionName(String newPositionName) {
        this.newPositionName = newPositionName;
    }

    public Date getCurrentDate() {
        return currentDate;
    }

    public Double getNewHours() {
        return newHours;
    }

    public void setNewHours(Double newHours) {
        this.newHours = newHours;
    }

    public ContractType getNewContract() {
        return newContract;
    }

    public void setNewContract(ContractType newContract) {
        this.newContract = newContract;
    }

    public String getNewAward() {
        if(!"1".equals(newAward)){
            newAward = "0";
        }
        return newAward;
    }

    public void setNewAward(String newAward) {
        this.newAward = newAward;
    }

    public String getNewEmploymentType() {
        return newEmploymentType;
    }

    public void setNewEmploymentType(String newEmploymentType) {
        this.newEmploymentType = newEmploymentType;
    }

    public Double getNewSalary() {
        return newSalary;
    }

    public void setNewSalary(Double newSalary) {
        this.newSalary = newSalary;
    }

    public String getNewClassification() {
        return newClassification;
    }

    public void setNewClassification(String newClassification) {
        this.newClassification = newClassification;
    }

    public Employee getNewManager() {
        return newManager;
    }

    public void setNewManager(Employee newManager) {
        this.newManager = newManager;
    }

    public Position getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(Position newPosition) {
        this.newPosition = newPosition;
    }

    public Account getNewAccount() {
        return newAccount;
    }

    public void setNewAccount(Account newAccount) {
        this.newAccount = newAccount;
    }
    
    public VariationType getCoecVariationType() {
        return coecVariationType;
    }

    public void setCoecVariationType(VariationType coecVariationType) {
        this.coecVariationType = coecVariationType;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public Date getSecondmentDate() {
        return secondmentDate;
    }

    public void setSecondmentDate(Date secondmentDate) {
        this.secondmentDate = secondmentDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
}
