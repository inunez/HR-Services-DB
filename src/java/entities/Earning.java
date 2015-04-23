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
@Table(name = "earning")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Earning.findAll", query = "SELECT e FROM Earning e"),
    @NamedQuery(name = "Earning.findByIdNumber", query = "SELECT e FROM Earning e WHERE e.earningPK.idNumber = :idNumber"),
    @NamedQuery(name = "Earning.findByPayslipNumber", query = "SELECT e FROM Earning e WHERE e.earningPK.payslipNumber = :payslipNumber"),
    @NamedQuery(name = "Earning.findByPaySeparator", query = "SELECT e FROM Earning e WHERE e.paySeparator = :paySeparator"),
    @NamedQuery(name = "Earning.findByDepartmentCode", query = "SELECT e FROM Earning e WHERE e.departmentCode = :departmentCode"),
    @NamedQuery(name = "Earning.findByBaseHours", query = "SELECT e FROM Earning e WHERE e.baseHours = :baseHours"),
    @NamedQuery(name = "Earning.findByBaseRate", query = "SELECT e FROM Earning e WHERE e.baseRate = :baseRate"),
    @NamedQuery(name = "Earning.findByPayFrequency", query = "SELECT e FROM Earning e WHERE e.payFrequency = :payFrequency"),
    @NamedQuery(name = "Earning.findByEmploymentTypeCode", query = "SELECT e FROM Earning e WHERE e.employmentTypeCode = :employmentTypeCode"),
    @NamedQuery(name = "Earning.findByEmployeeTypeCode", query = "SELECT e FROM Earning e WHERE e.employeeTypeCode = :employeeTypeCode"),
    @NamedQuery(name = "Earning.findByYearToDateTaxAmount", query = "SELECT e FROM Earning e WHERE e.yearToDateTaxAmount = :yearToDateTaxAmount"),
    @NamedQuery(name = "Earning.findByAlUnitDescription", query = "SELECT e FROM Earning e WHERE e.alUnitDescription = :alUnitDescription"),
    @NamedQuery(name = "Earning.findByAlBalance", query = "SELECT e FROM Earning e WHERE e.alBalance = :alBalance"),
    @NamedQuery(name = "Earning.findBySlUnitDescription", query = "SELECT e FROM Earning e WHERE e.slUnitDescription = :slUnitDescription"),
    @NamedQuery(name = "Earning.findBySlBalance", query = "SELECT e FROM Earning e WHERE e.slBalance = :slBalance"),
    @NamedQuery(name = "Earning.findByRdoBalance", query = "SELECT e FROM Earning e WHERE e.rdoBalance = :rdoBalance"),
    @NamedQuery(name = "Earning.findByTaxableGross", query = "SELECT e FROM Earning e WHERE e.taxableGross = :taxableGross"),
    @NamedQuery(name = "Earning.findByTaxAmount", query = "SELECT e FROM Earning e WHERE e.taxAmount = :taxAmount"),
    @NamedQuery(name = "Earning.findByNetPay", query = "SELECT e FROM Earning e WHERE e.netPay = :netPay"),
    @NamedQuery(name = "Earning.findByPayDate", query = "SELECT e FROM Earning e WHERE e.payDate = :payDate"),
    @NamedQuery(name = "Earning.findByTaxWeeks", query = "SELECT e FROM Earning e WHERE e.taxWeeks = :taxWeeks"),
    @NamedQuery(name = "Earning.findByPeriodEndingDescription", query = "SELECT e FROM Earning e WHERE e.periodEndingDescription = :periodEndingDescription"),
    @NamedQuery(name = "Earning.findByPeriodEndingDate", query = "SELECT e FROM Earning e WHERE e.periodEndingDate = :periodEndingDate"),
    @NamedQuery(name = "Earning.findByPayMethodCode", query = "SELECT e FROM Earning e WHERE e.payMethodCode = :payMethodCode"),
    @NamedQuery(name = "Earning.findByYearToDateTaxableGross", query = "SELECT e FROM Earning e WHERE e.yearToDateTaxableGross = :yearToDateTaxableGross"),
    @NamedQuery(name = "Earning.findByYearToDateNetPay", query = "SELECT e FROM Earning e WHERE e.yearToDateNetPay = :yearToDateNetPay"),
    @NamedQuery(name = "Earning.findByBaseRateType", query = "SELECT e FROM Earning e WHERE e.baseRateType = :baseRateType"),
    @NamedQuery(name = "Earning.findByRecordType", query = "SELECT e FROM Earning e WHERE e.recordType = :recordType"),
    @NamedQuery(name = "Earning.findByHoursOrAdCode", query = "SELECT e FROM Earning e WHERE e.hoursOrAdCode = :hoursOrAdCode"),
    @NamedQuery(name = "Earning.findByHoursOrAdCodeDesc", query = "SELECT e FROM Earning e WHERE e.hoursOrAdCodeDesc = :hoursOrAdCodeDesc"),
    @NamedQuery(name = "Earning.findByRate", query = "SELECT e FROM Earning e WHERE e.rate = :rate"),
    @NamedQuery(name = "Earning.findByValue", query = "SELECT e FROM Earning e WHERE e.value = :value"),
    @NamedQuery(name = "Earning.findByComments", query = "SELECT e FROM Earning e WHERE e.comments = :comments"),
    @NamedQuery(name = "Earning.findByPeriodStartDate", query = "SELECT e FROM Earning e WHERE e.periodStartDate = :periodStartDate"),
    @NamedQuery(name = "Earning.findByPrintSequence", query = "SELECT e FROM Earning e WHERE e.earningPK.printSequence = :printSequence")})
public class Earning implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EarningPK earningPK;
    @Size(max = 1)
    @Column(name = "pay_separator")
    private String paySeparator;
    @Size(max = 10)
    @Column(name = "department_code")
    private String departmentCode;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "base_hours")
    private Double baseHours;
    @Column(name = "base_rate")
    private Double baseRate;
    @Size(max = 1)
    @Column(name = "pay_frequency")
    private String payFrequency;
    @Size(max = 1)
    @Column(name = "employment_type_code")
    private String employmentTypeCode;
    @Size(max = 1)
    @Column(name = "employee_type_code")
    private String employeeTypeCode;
    @Column(name = "year_to_date_tax_amount")
    private Double yearToDateTaxAmount;
    @Size(max = 10)
    @Column(name = "al_unit_description")
    private String alUnitDescription;
    @Column(name = "al_balance")
    private Double alBalance;
    @Size(max = 10)
    @Column(name = "sl_unit_description")
    private String slUnitDescription;
    @Column(name = "sl_balance")
    private Double slBalance;
    @Column(name = "rdo_balance")
    private Double rdoBalance;
    @Column(name = "taxable_gross")
    private Double taxableGross;
    @Column(name = "tax_amount")
    private Double taxAmount;
    @Column(name = "net_pay")
    private Double netPay;
    @Column(name = "pay_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date payDate;
    @Column(name = "tax_weeks")
    private Short taxWeeks;
    @Size(max = 20)
    @Column(name = "period_ending_description")
    private String periodEndingDescription;
    @Column(name = "period_ending_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date periodEndingDate;
    @Size(max = 1)
    @Column(name = "pay_method_code")
    private String payMethodCode;
    @Column(name = "year_to_date_taxable_gross")
    private Double yearToDateTaxableGross;
    @Column(name = "year_to_date_net_pay")
    private Double yearToDateNetPay;
    @Size(max = 5)
    @Column(name = "base_rate_type")
    private String baseRateType;
    @Column(name = "record_type")
    private Double recordType;
    @Size(max = 2)
    @Column(name = "hours_or_ad_code")
    private String hoursOrAdCode;
    @Size(max = 100)
    @Column(name = "hours_or_ad_code_desc")
    private String hoursOrAdCodeDesc;
    @Column(name = "rate")
    private Double rate;
    @Column(name = "value")
    private Double value;
    @Size(max = 255)
    @Column(name = "comments")
    private String comments;
    @Column(name = "period_start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date periodStartDate;
    @JoinColumn(name = "account_number", referencedColumnName = "account_number")
    @ManyToOne
    private Account accountNumber;
    @JoinColumn(name = "award_code", referencedColumnName = "award_code")
    @ManyToOne
    private Award awardCode;
    @JoinColumns({
        @JoinColumn(name = "status", referencedColumnName = "status", insertable = false, updatable = false),
        @JoinColumn(name = "id_number", referencedColumnName = "id_number", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Employee employee;
    @JoinColumn(name = "job_title_code", referencedColumnName = "job_title")
    @ManyToOne
    private JobTitle jobTitleCode;
    @JoinColumn(name = "position_id", referencedColumnName = "position_id")
    @ManyToOne
    private Position positionId;

    public Earning() {
    }

    public Earning(EarningPK earningPK) {
        this.earningPK = earningPK;
    }

    public Earning(String idNumber, double payslipNumber, short printSequence) {
        this.earningPK = new EarningPK(idNumber, payslipNumber, printSequence);
    }

    public EarningPK getEarningPK() {
        return earningPK;
    }

    public void setEarningPK(EarningPK earningPK) {
        this.earningPK = earningPK;
    }

    public String getPaySeparator() {
        return paySeparator;
    }

    public void setPaySeparator(String paySeparator) {
        this.paySeparator = paySeparator;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public Double getBaseHours() {
        return baseHours;
    }

    public void setBaseHours(Double baseHours) {
        this.baseHours = baseHours;
    }

    public Double getBaseRate() {
        return baseRate;
    }

    public void setBaseRate(Double baseRate) {
        this.baseRate = baseRate;
    }

    public String getPayFrequency() {
        return payFrequency;
    }

    public void setPayFrequency(String payFrequency) {
        this.payFrequency = payFrequency;
    }

    public String getEmploymentTypeCode() {
        return employmentTypeCode;
    }

    public void setEmploymentTypeCode(String employmentTypeCode) {
        this.employmentTypeCode = employmentTypeCode;
    }

    public String getEmployeeTypeCode() {
        return employeeTypeCode;
    }

    public void setEmployeeTypeCode(String employeeTypeCode) {
        this.employeeTypeCode = employeeTypeCode;
    }

    public Double getYearToDateTaxAmount() {
        return yearToDateTaxAmount;
    }

    public void setYearToDateTaxAmount(Double yearToDateTaxAmount) {
        this.yearToDateTaxAmount = yearToDateTaxAmount;
    }

    public String getAlUnitDescription() {
        return alUnitDescription;
    }

    public void setAlUnitDescription(String alUnitDescription) {
        this.alUnitDescription = alUnitDescription;
    }

    public Double getAlBalance() {
        return alBalance;
    }

    public void setAlBalance(Double alBalance) {
        this.alBalance = alBalance;
    }

    public String getSlUnitDescription() {
        return slUnitDescription;
    }

    public void setSlUnitDescription(String slUnitDescription) {
        this.slUnitDescription = slUnitDescription;
    }

    public Double getSlBalance() {
        return slBalance;
    }

    public void setSlBalance(Double slBalance) {
        this.slBalance = slBalance;
    }

    public Double getRdoBalance() {
        return rdoBalance;
    }

    public void setRdoBalance(Double rdoBalance) {
        this.rdoBalance = rdoBalance;
    }

    public Double getTaxableGross() {
        return taxableGross;
    }

    public void setTaxableGross(Double taxableGross) {
        this.taxableGross = taxableGross;
    }

    public Double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(Double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public Double getNetPay() {
        return netPay;
    }

    public void setNetPay(Double netPay) {
        this.netPay = netPay;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public Short getTaxWeeks() {
        return taxWeeks;
    }

    public void setTaxWeeks(Short taxWeeks) {
        this.taxWeeks = taxWeeks;
    }

    public String getPeriodEndingDescription() {
        return periodEndingDescription;
    }

    public void setPeriodEndingDescription(String periodEndingDescription) {
        this.periodEndingDescription = periodEndingDescription;
    }

    public Date getPeriodEndingDate() {
        return periodEndingDate;
    }

    public void setPeriodEndingDate(Date periodEndingDate) {
        this.periodEndingDate = periodEndingDate;
    }

    public String getPayMethodCode() {
        return payMethodCode;
    }

    public void setPayMethodCode(String payMethodCode) {
        this.payMethodCode = payMethodCode;
    }

    public Double getYearToDateTaxableGross() {
        return yearToDateTaxableGross;
    }

    public void setYearToDateTaxableGross(Double yearToDateTaxableGross) {
        this.yearToDateTaxableGross = yearToDateTaxableGross;
    }

    public Double getYearToDateNetPay() {
        return yearToDateNetPay;
    }

    public void setYearToDateNetPay(Double yearToDateNetPay) {
        this.yearToDateNetPay = yearToDateNetPay;
    }

    public String getBaseRateType() {
        return baseRateType;
    }

    public void setBaseRateType(String baseRateType) {
        this.baseRateType = baseRateType;
    }

    public Double getRecordType() {
        return recordType;
    }

    public void setRecordType(Double recordType) {
        this.recordType = recordType;
    }

    public String getHoursOrAdCode() {
        return hoursOrAdCode;
    }

    public void setHoursOrAdCode(String hoursOrAdCode) {
        this.hoursOrAdCode = hoursOrAdCode;
    }

    public String getHoursOrAdCodeDesc() {
        return hoursOrAdCodeDesc;
    }

    public void setHoursOrAdCodeDesc(String hoursOrAdCodeDesc) {
        this.hoursOrAdCodeDesc = hoursOrAdCodeDesc;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Date getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(Date periodStartDate) {
        this.periodStartDate = periodStartDate;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public JobTitle getJobTitleCode() {
        return jobTitleCode;
    }

    public void setJobTitleCode(JobTitle jobTitleCode) {
        this.jobTitleCode = jobTitleCode;
    }

    public Position getPositionId() {
        return positionId;
    }

    public void setPositionId(Position positionId) {
        this.positionId = positionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (earningPK != null ? earningPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Earning)) {
            return false;
        }
        Earning other = (Earning) object;
        if ((this.earningPK == null && other.earningPK != null) || (this.earningPK != null && !this.earningPK.equals(other.earningPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Earning[ earningPK=" + earningPK + " ]";
    }
    
}
