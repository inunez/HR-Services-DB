
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
    private Account newAccount;
    private ManagerPosition newManager;
    private JobTitle newClassification;
    private Double newSalary;
    private EmploymentType newEmploymentType;
    private String newAward;

    public ChangeCondition() {
        this.coecVariationType = VariationType.PERMANENT;
    }

    public ChangeCondition(Employee employee) {
        this.coecVariationType = VariationType.PERMANENT;
        this.employee = employee;
        this.newPosition = employee.getPositionId();
        List<Payroll> payrollList = new ArrayList<>(employee.getPayrollCollection());
        Comparator<Payroll> dateComparator = (Payroll p1, Payroll p2) -> p2.getPayrollPK().getEffectiveDate().compareTo(p1.getPayrollPK().getEffectiveDate());
        Collections.sort(payrollList, dateComparator);
        Payroll payroll = payrollList.iterator().next();
        this.newAccount = payroll.getAccountNumber();
        this.newManager = this.newAccount.getManagerPositionCollection().iterator().next();
        this.newClassification = payroll.getJobTitle();
        this.newSalary = payroll.getBaseRate();
        this.newEmploymentType = payroll.getEmploymentType();
        this.newAward = payroll.getAwardCode().getAwardCode();
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

    public EmploymentType getNewEmploymentType() {
        return newEmploymentType;
    }

    public void setNewEmploymentType(EmploymentType newEmploymentType) {
        this.newEmploymentType = newEmploymentType;
    }

    public Double getNewSalary() {
        return newSalary;
    }

    public void setNewSalary(Double newSalary) {
        this.newSalary = newSalary;
    }

    public JobTitle getNewClassification() {
        return newClassification;
    }

    public void setNewClassification(JobTitle newClassification) {
        this.newClassification = newClassification;
    }

    public ManagerPosition getNewManager() {
        return newManager;
    }

    public void setNewManager(ManagerPosition newManager) {
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
