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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ismael
 */
@Entity
@Table(name = "earning_summary")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EarningSummary.findAll", query = "SELECT e FROM EarningSummary e"),
    @NamedQuery(name = "EarningSummary.findByIdNumber", query = "SELECT e FROM EarningSummary e WHERE e.earningSummaryPK.idNumber = :idNumber"),
    @NamedQuery(name = "EarningSummary.findByStatus", query = "SELECT e FROM EarningSummary e WHERE e.earningSummaryPK.status = :status"),
    @NamedQuery(name = "EarningSummary.findByVersion", query = "SELECT e FROM EarningSummary e WHERE e.earningSummaryPK.version = :version"),
    @NamedQuery(name = "EarningSummary.findByPtdTotalEarnings", query = "SELECT e FROM EarningSummary e WHERE e.ptdTotalEarnings = :ptdTotalEarnings"),
    @NamedQuery(name = "EarningSummary.findByYtdTotalEarnings", query = "SELECT e FROM EarningSummary e WHERE e.ytdTotalEarnings = :ytdTotalEarnings"),
    @NamedQuery(name = "EarningSummary.findByPtdTaxableGross", query = "SELECT e FROM EarningSummary e WHERE e.ptdTaxableGross = :ptdTaxableGross"),
    @NamedQuery(name = "EarningSummary.findByPtdTax", query = "SELECT e FROM EarningSummary e WHERE e.ptdTax = :ptdTax"),
    @NamedQuery(name = "EarningSummary.findByPtdNet", query = "SELECT e FROM EarningSummary e WHERE e.ptdNet = :ptdNet"),
    @NamedQuery(name = "EarningSummary.findByPtdEmployerSuper", query = "SELECT e FROM EarningSummary e WHERE e.ptdEmployerSuper = :ptdEmployerSuper"),
    @NamedQuery(name = "EarningSummary.findByYtdEmployerSuper", query = "SELECT e FROM EarningSummary e WHERE e.ytdEmployerSuper = :ytdEmployerSuper"),
    @NamedQuery(name = "EarningSummary.findByTaxableGross", query = "SELECT e FROM EarningSummary e WHERE e.taxableGross = :taxableGross"),
    @NamedQuery(name = "EarningSummary.findByYtdTax", query = "SELECT e FROM EarningSummary e WHERE e.ytdTax = :ytdTax"),
    @NamedQuery(name = "EarningSummary.findByYtdNetPay", query = "SELECT e FROM EarningSummary e WHERE e.ytdNetPay = :ytdNetPay"),
    @NamedQuery(name = "EarningSummary.findByPaidUpToDate", query = "SELECT e FROM EarningSummary e WHERE e.earningSummaryPK.paidUpToDate = :paidUpToDate"),
    @NamedQuery(name = "EarningSummary.findByHoursWorkedThisPay", query = "SELECT e FROM EarningSummary e WHERE e.hoursWorkedThisPay = :hoursWorkedThisPay"),
    @NamedQuery(name = "EarningSummary.findBySinceHired", query = "SELECT e FROM EarningSummary e WHERE e.sinceHired = :sinceHired"),
    @NamedQuery(name = "EarningSummary.findByServiceHourslastsalarycode", query = "SELECT e FROM EarningSummary e WHERE e.serviceHourslastsalarycode = :serviceHourslastsalarycode")})
public class EarningSummary implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EarningSummaryPK earningSummaryPK;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "ptd_total_earnings")
    private Double ptdTotalEarnings;
    @Column(name = "ytd_total_earnings")
    private Double ytdTotalEarnings;
    @Column(name = "ptd_taxable_gross")
    private Double ptdTaxableGross;
    @Column(name = "ptd_tax")
    private Double ptdTax;
    @Column(name = "ptd_net")
    private Double ptdNet;
    @Column(name = "ptd_employer_super")
    private Double ptdEmployerSuper;
    @Column(name = "ytd_employer_super")
    private Double ytdEmployerSuper;
    @Column(name = "taxable_gross")
    private Double taxableGross;
    @Column(name = "ytd_tax")
    private Double ytdTax;
    @Column(name = "ytd_net_pay")
    private Double ytdNetPay;
    @Column(name = "hours_worked_this_pay")
    private Double hoursWorkedThisPay;
    @Column(name = "since_hired")
    private Double sinceHired;
    @Column(name = "service_Hours_last_salary_code")
    private Double serviceHourslastsalarycode;
    @JoinColumns({
        @JoinColumn(name = "id_number", referencedColumnName = "id_number", insertable = false, updatable = false),
        @JoinColumn(name = "status", referencedColumnName = "status", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Employee employee;

    public EarningSummary() {
    }

    public EarningSummary(EarningSummaryPK earningSummaryPK) {
        this.earningSummaryPK = earningSummaryPK;
    }

    public EarningSummary(String idNumber, String status, short version, Date paidUpToDate) {
        this.earningSummaryPK = new EarningSummaryPK(idNumber, status, version, paidUpToDate);
    }

    public EarningSummaryPK getEarningSummaryPK() {
        return earningSummaryPK;
    }

    public void setEarningSummaryPK(EarningSummaryPK earningSummaryPK) {
        this.earningSummaryPK = earningSummaryPK;
    }

    public Double getPtdTotalEarnings() {
        return ptdTotalEarnings;
    }

    public void setPtdTotalEarnings(Double ptdTotalEarnings) {
        this.ptdTotalEarnings = ptdTotalEarnings;
    }

    public Double getYtdTotalEarnings() {
        return ytdTotalEarnings;
    }

    public void setYtdTotalEarnings(Double ytdTotalEarnings) {
        this.ytdTotalEarnings = ytdTotalEarnings;
    }

    public Double getPtdTaxableGross() {
        return ptdTaxableGross;
    }

    public void setPtdTaxableGross(Double ptdTaxableGross) {
        this.ptdTaxableGross = ptdTaxableGross;
    }

    public Double getPtdTax() {
        return ptdTax;
    }

    public void setPtdTax(Double ptdTax) {
        this.ptdTax = ptdTax;
    }

    public Double getPtdNet() {
        return ptdNet;
    }

    public void setPtdNet(Double ptdNet) {
        this.ptdNet = ptdNet;
    }

    public Double getPtdEmployerSuper() {
        return ptdEmployerSuper;
    }

    public void setPtdEmployerSuper(Double ptdEmployerSuper) {
        this.ptdEmployerSuper = ptdEmployerSuper;
    }

    public Double getYtdEmployerSuper() {
        return ytdEmployerSuper;
    }

    public void setYtdEmployerSuper(Double ytdEmployerSuper) {
        this.ytdEmployerSuper = ytdEmployerSuper;
    }

    public Double getTaxableGross() {
        return taxableGross;
    }

    public void setTaxableGross(Double taxableGross) {
        this.taxableGross = taxableGross;
    }

    public Double getYtdTax() {
        return ytdTax;
    }

    public void setYtdTax(Double ytdTax) {
        this.ytdTax = ytdTax;
    }

    public Double getYtdNetPay() {
        return ytdNetPay;
    }

    public void setYtdNetPay(Double ytdNetPay) {
        this.ytdNetPay = ytdNetPay;
    }

    public Double getHoursWorkedThisPay() {
        return hoursWorkedThisPay;
    }

    public void setHoursWorkedThisPay(Double hoursWorkedThisPay) {
        this.hoursWorkedThisPay = hoursWorkedThisPay;
    }

    public Double getSinceHired() {
        return sinceHired;
    }

    public void setSinceHired(Double sinceHired) {
        this.sinceHired = sinceHired;
    }

    public Double getServiceHourslastsalarycode() {
        return serviceHourslastsalarycode;
    }

    public void setServiceHourslastsalarycode(Double serviceHourslastsalarycode) {
        this.serviceHourslastsalarycode = serviceHourslastsalarycode;
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
        hash += (earningSummaryPK != null ? earningSummaryPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EarningSummary)) {
            return false;
        }
        EarningSummary other = (EarningSummary) object;
        if ((this.earningSummaryPK == null && other.earningSummaryPK != null) || (this.earningSummaryPK != null && !this.earningSummaryPK.equals(other.earningSummaryPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.EarningSummary[ earningSummaryPK=" + earningSummaryPK + " ]";
    }
    
}
