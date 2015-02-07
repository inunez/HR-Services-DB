/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
//import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ismael
 */
@Entity
@Table(name = "payroll")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Payroll.findAll", query = "SELECT p FROM Payroll p"),
    @NamedQuery(name = "Payroll.findByIdNumber", query = "SELECT p FROM Payroll p WHERE p.payrollPK.idNumber = :idNumber"),
    @NamedQuery(name = "Payroll.findByLevel1Code", query = "SELECT p FROM Payroll p WHERE p.payrollPK.level1Code = :level1Code"),
    @NamedQuery(name = "Payroll.findByEffectiveDate", query = "SELECT p FROM Payroll p WHERE p.payrollPK.effectiveDate = :effectiveDate"),
    @NamedQuery(name = "Payroll.findByBaseRateCode", query = "SELECT p FROM Payroll p WHERE p.baseRateCode = :baseRateCode"),
    @NamedQuery(name = "Payroll.findByBaseRate", query = "SELECT p FROM Payroll p WHERE p.baseRate = :baseRate"),
    @NamedQuery(name = "Payroll.findByAwardRate", query = "SELECT p FROM Payroll p WHERE p.awardRate = :awardRate"),
    @NamedQuery(name = "Payroll.findByBaseHoursCode", query = "SELECT p FROM Payroll p WHERE p.baseHoursCode = :baseHoursCode"),
    @NamedQuery(name = "Payroll.findByBaseHoursAmount", query = "SELECT p FROM Payroll p WHERE p.baseHoursAmount = :baseHoursAmount"),
    @NamedQuery(name = "Payroll.findByAutoPay", query = "SELECT p FROM Payroll p WHERE p.autoPay = :autoPay"),
    @NamedQuery(name = "Payroll.findByPositionFteHoursPerWeek", query = "SELECT p FROM Payroll p WHERE p.positionFteHoursPerWeek = :positionFteHoursPerWeek"),
    @NamedQuery(name = "Payroll.findByStandardDay", query = "SELECT p FROM Payroll p WHERE p.standardDay = :standardDay"),
    @NamedQuery(name = "Payroll.findBySecurityGroup", query = "SELECT p FROM Payroll p WHERE p.securityGroup = :securityGroup"),
    @NamedQuery(name = "Payroll.findByAwardClassDescription", query = "SELECT p FROM Payroll p WHERE p.awardClassDescription = :awardClassDescription"),
    @NamedQuery(name = "Payroll.findByHouseProvided", query = "SELECT p FROM Payroll p WHERE p.houseProvided = :houseProvided"),
    @NamedQuery(name = "Payroll.findByAdjServDate", query = "SELECT p FROM Payroll p WHERE p.adjServDate = :adjServDate"),
    @NamedQuery(name = "Payroll.findByPaySummaryStartDate", query = "SELECT p FROM Payroll p WHERE p.paySummaryStartDate = :paySummaryStartDate"),
    @NamedQuery(name = "Payroll.findByTaxFileNumber", query = "SELECT p FROM Payroll p WHERE p.taxFileNumber = :taxFileNumber")})
public class Payroll implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PayrollPK payrollPK;
    @Size(max = 5)
    @Column(name = "base_rate_code")
    private String baseRateCode;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "base_rate")
    private Double baseRate;
    @Column(name = "award_rate")
    private Double awardRate;
    @Size(max = 5)
    @Column(name = "base_hours_code")
    private String baseHoursCode;
    @Column(name = "base_hours_amount")
    private Double baseHoursAmount;
    @Size(max = 1)
    @Column(name = "auto_pay")
    private String autoPay;
    @Column(name = "position_fte_hours_per_week")
    private Short positionFteHoursPerWeek;
    @Column(name = "standard_day")
    private Double standardDay;
    @Column(name = "security_group")
    private Double securityGroup;
    @Size(max = 50)
    @Column(name = "award_class_description")
    private String awardClassDescription;
    @Size(max = 1)
    @Column(name = "house_provided")
    private String houseProvided;
    @Column(name = "adj_serv_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date adjServDate;
    @Column(name = "pay_summary_start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date paySummaryStartDate;
    @Size(max = 9)
    @Column(name = "tax_file_number")
    private String taxFileNumber;
    @JoinColumn(name = "account_number", referencedColumnName = "account_number")
    @ManyToOne
    private Account accountNumber;
    @JoinColumn(name = "award_code", referencedColumnName = "award_code")
    @ManyToOne
    private Award awardCode;
    @JoinColumn(name = "employment_type", referencedColumnName = "employment_type")
    @ManyToOne
    private EmploymentType employmentType;
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    @ManyToOne
    private Department departmentId;
    @JoinColumn(name = "job_title", referencedColumnName = "job_title")
    @ManyToOne
    private JobTitle jobTitle;
    @JoinColumn(name = "level1_code", referencedColumnName = "level1_code", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Level1 level1;
    @JoinColumn(name = "level3_code", referencedColumnName = "level3_code")
    @ManyToOne
    private Level3 level3Code;
    @JoinColumn(name = "personnel_group_code", referencedColumnName = "personnel_group_code")
    @ManyToOne
    private PersonnelGroup personnelGroupCode;
    @JoinColumn(name = "level4_code", referencedColumnName = "level4_code")
    @ManyToOne
    private Level4 level4Code;
    @JoinColumn(name = "payrun_group_code", referencedColumnName = "payrun_group_code")
    @ManyToOne
    private PayrunGroup payrunGroupCode;
    @JoinColumn(name = "personnel_type", referencedColumnName = "personnel_type")
    @ManyToOne
    private PersonnelType personnelType;
    @JoinColumn(name = "level5_code", referencedColumnName = "level5_code")
    @ManyToOne
    private Level5 level5Code;
    @JoinColumn(name = "pay_frequency_code", referencedColumnName = "pay_frequency_code")
    @ManyToOne
    private PayFrequency payFrequencyCode;
    @JoinColumn(name = "pay_method_code", referencedColumnName = "pay_method_code")
    @ManyToOne
    private PayMethod payMethodCode;
    @JoinColumn(name = "level2_code", referencedColumnName = "state_code")
    @ManyToOne
    private State level2Code;
    @JoinColumns({
        @JoinColumn(name = "status", referencedColumnName = "status", insertable = false, updatable = false),
        @JoinColumn(name = "id_number", referencedColumnName = "id_number", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
//    @JoinColumn(name = "id_number", referencedColumnName = "id_number", insertable = false, updatable = false)
//    @ManyToOne
    private Employee employee;
 
    public Payroll() {
    }

    public Payroll(PayrollPK payrollPK) {
        this.payrollPK = payrollPK;
    }

    public Payroll(String idNumber, String status, String level1Code, Date effectiveDate) {
        this.payrollPK = new PayrollPK(idNumber, status, level1Code, effectiveDate);
    }

    public PayrollPK getPayrollPK() {
        return payrollPK;
    }

    public void setPayrollPK(PayrollPK payrollPK) {
        this.payrollPK = payrollPK;
    }

    public String getBaseRateCode() {
        return baseRateCode;
    }

    public void setBaseRateCode(String baseRateCode) {
        this.baseRateCode = baseRateCode;
    }

    public Double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(Double baseRate) {
        this.baseRate = baseRate;
    }

    public Double getAwardRate() {
        return awardRate;
    }

    public void setAwardRate(Double awardRate) {
        this.awardRate = awardRate;
    }

    public String getBaseHoursCode() {
        return baseHoursCode;
    }

    public void setBaseHoursCode(String baseHoursCode) {
        this.baseHoursCode = baseHoursCode;
    }

    public Double getBaseHoursAmount() {
        return baseHoursAmount;
    }

    public void setBaseHoursAmount(Double baseHoursAmount) {
        this.baseHoursAmount = baseHoursAmount;
    }

    public String getAutoPay() {
        return autoPay;
    }

    public void setAutoPay(String autoPay) {
        this.autoPay = autoPay;
    }

    public Short getPositionFteHoursPerWeek() {
        return positionFteHoursPerWeek;
    }

    public void setPositionFteHoursPerWeek(Short positionFteHoursPerWeek) {
        this.positionFteHoursPerWeek = positionFteHoursPerWeek;
    }

    public Double getStandardDay() {
        return standardDay;
    }

    public void setStandardDay(Double standardDay) {
        this.standardDay = standardDay;
    }

    public Double getSecurityGroup() {
        return securityGroup;
    }

    public void setSecurityGroup(Double securityGroup) {
        this.securityGroup = securityGroup;
    }

    public String getAwardClassDescription() {
        return awardClassDescription;
    }

    public void setAwardClassDescription(String awardClassDescription) {
        this.awardClassDescription = awardClassDescription;
    }

    public String getHouseProvided() {
        return houseProvided;
    }

    public void setHouseProvided(String houseProvided) {
        this.houseProvided = houseProvided;
    }

    public Date getAdjServDate() {
        return adjServDate;
    }

    public void setAdjServDate(Date adjServDate) {
        this.adjServDate = adjServDate;
    }

    public Date getPaySummaryStartDate() {
        return paySummaryStartDate;
    }

    public void setPaySummaryStartDate(Date paySummaryStartDate) {
        this.paySummaryStartDate = paySummaryStartDate;
    }

    public String getTaxFileNumber() {
        return taxFileNumber;
    }

    public void setTaxFileNumber(String taxFileNumber) {
        this.taxFileNumber = taxFileNumber;
    }

    public Account getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Account accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Award getAwardCode() {
        return awardCode;
    }

    public void setAwardCode(Award awardCode) {
        this.awardCode = awardCode;
    }

    public EmploymentType getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(EmploymentType employmentType) {
        this.employmentType = employmentType;
    }

    public Department getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Department departmentId) {
        this.departmentId = departmentId;
    }

    public JobTitle getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }

    public Level1 getLevel1() {
        return level1;
    }

    public void setLevel1(Level1 level1) {
        this.level1 = level1;
    }

    public Level3 getLevel3Code() {
        return level3Code;
    }

    public void setLevel3Code(Level3 level3Code) {
        this.level3Code = level3Code;
    }

    public PersonnelGroup getPersonnelGroupCode() {
        return personnelGroupCode;
    }

    public void setPersonnelGroupCode(PersonnelGroup personnelGroupCode) {
        this.personnelGroupCode = personnelGroupCode;
    }

    public Level4 getLevel4Code() {
        return level4Code;
    }

    public void setLevel4Code(Level4 level4Code) {
        this.level4Code = level4Code;
    }

    public PayrunGroup getPayrunGroupCode() {
        return payrunGroupCode;
    }

    public void setPayrunGroupCode(PayrunGroup payrunGroupCode) {
        this.payrunGroupCode = payrunGroupCode;
    }

    public PersonnelType getPersonnelType() {
        return personnelType;
    }

    public void setPersonnelType(PersonnelType personnelType) {
        this.personnelType = personnelType;
    }

    public Level5 getLevel5Code() {
        return level5Code;
    }

    public void setLevel5Code(Level5 level5Code) {
        this.level5Code = level5Code;
    }

    public PayFrequency getPayFrequencyCode() {
        return payFrequencyCode;
    }

    public void setPayFrequencyCode(PayFrequency payFrequencyCode) {
        this.payFrequencyCode = payFrequencyCode;
    }

    public PayMethod getPayMethodCode() {
        return payMethodCode;
    }

    public void setPayMethodCode(PayMethod payMethodCode) {
        this.payMethodCode = payMethodCode;
    }

    public State getLevel2Code() {
        return level2Code;
    }

    public void setLevel2Code(State level2Code) {
        this.level2Code = level2Code;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (payrollPK != null ? payrollPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Payroll)) {
            return false;
        }
        Payroll other = (Payroll) object;
        if ((this.payrollPK == null && other.payrollPK != null) || (this.payrollPK != null && !this.payrollPK.equals(other.payrollPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Payroll[ payrollPK=" + payrollPK + " ]";
    }
    
}
