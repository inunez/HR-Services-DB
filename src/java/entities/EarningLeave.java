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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ismael
 */
@Entity
@Table(name = "earning_leave")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EarningLeave.findAll", query = "SELECT e FROM EarningLeave e"),
    @NamedQuery(name = "EarningLeave.findByIdNumber", query = "SELECT e FROM EarningLeave e WHERE e.earningLeavePK.idNumber = :idNumber"),
    @NamedQuery(name = "EarningLeave.findByStatus", query = "SELECT e FROM EarningLeave e WHERE e.earningLeavePK.status = :status"),
    @NamedQuery(name = "EarningLeave.findByVersion", query = "SELECT e FROM EarningLeave e WHERE e.earningLeavePK.version = :version"),
    @NamedQuery(name = "EarningLeave.findByDateAccruedTo", query = "SELECT e FROM EarningLeave e WHERE e.earningLeavePK.dateAccruedTo = :dateAccruedTo"),
    @NamedQuery(name = "EarningLeave.findByClassification", query = "SELECT e FROM EarningLeave e WHERE e.earningLeavePK.classification = :classification"),
    @NamedQuery(name = "EarningLeave.findByAccrualMethod", query = "SELECT e FROM EarningLeave e WHERE e.accrualMethod = :accrualMethod"),
    @NamedQuery(name = "EarningLeave.findByLastYearsProrata", query = "SELECT e FROM EarningLeave e WHERE e.lastYearsProrata = :lastYearsProrata"),
    @NamedQuery(name = "EarningLeave.findByCurrentProrata", query = "SELECT e FROM EarningLeave e WHERE e.currentProrata = :currentProrata"),
    @NamedQuery(name = "EarningLeave.findByEntitlement", query = "SELECT e FROM EarningLeave e WHERE e.entitlement = :entitlement"),
    @NamedQuery(name = "EarningLeave.findByTaken", query = "SELECT e FROM EarningLeave e WHERE e.taken = :taken"),
    @NamedQuery(name = "EarningLeave.findByTakenWithCert", query = "SELECT e FROM EarningLeave e WHERE e.takenWithCert = :takenWithCert"),
    @NamedQuery(name = "EarningLeave.findByNetEntitlement", query = "SELECT e FROM EarningLeave e WHERE e.netEntitlement = :netEntitlement"),
    @NamedQuery(name = "EarningLeave.findByTotalEntitlement", query = "SELECT e FROM EarningLeave e WHERE e.totalEntitlement = :totalEntitlement"),
    @NamedQuery(name = "EarningLeave.findByTotalLiability", query = "SELECT e FROM EarningLeave e WHERE e.totalLiability = :totalLiability"),
    @NamedQuery(name = "EarningLeave.findByPaidDinceAnnivTotal", query = "SELECT e FROM EarningLeave e WHERE e.paidDinceAnnivTotal = :paidDinceAnnivTotal"),
    @NamedQuery(name = "EarningLeave.findByPaidSinceAnnivProrataPortion", query = "SELECT e FROM EarningLeave e WHERE e.paidSinceAnnivProrataPortion = :paidSinceAnnivProrataPortion"),
    @NamedQuery(name = "EarningLeave.findByLeaveTakenValue", query = "SELECT e FROM EarningLeave e WHERE e.leaveTakenValue = :leaveTakenValue")})
public class EarningLeave implements Serializable {
    @JoinColumns({
        @JoinColumn(name = "accrual_method_code", referencedColumnName = "accrual_method_code"),
        @JoinColumn(name = "classification_code", referencedColumnName = "classification_code", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private AccrualMethod accrualMethod;
    @JoinColumn(name = "classification_code", referencedColumnName = "classification_code", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ClassificationLeave classificationLeave;
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected EarningLeavePK earningLeavePK;
    @Size(max = 10)
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "last_years_prorata")
    private Double lastYearsProrata;
    @Column(name = "current_prorata")
    private Double currentProrata;
    @Column(name = "entitlement")
    private Double entitlement;
    @Column(name = "taken")
    private Double taken;
    @Column(name = "taken_with_cert")
    private Double takenWithCert;
    @Column(name = "net_entitlement")
    private Double netEntitlement;
    @Column(name = "total_entitlement")
    private Double totalEntitlement;
    @Column(name = "total_$_liability")
    private Double totalLiability;
    @Column(name = "paid_dince_anniv_total")
    private Double paidDinceAnnivTotal;
    @Column(name = "paid_since_anniv_prorata_portion")
    private Double paidSinceAnnivProrataPortion;
    @Column(name = "leave_taken_$_value")
    private Double leaveTakenValue;
    @JoinColumns({
        @JoinColumn(name = "id_number", referencedColumnName = "id_number", insertable = false, updatable = false),
        @JoinColumn(name = "status", referencedColumnName = "status", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Employee employee;

    public EarningLeave() {
    }

    public EarningLeave(EarningLeavePK earningLeavePK) {
        this.earningLeavePK = earningLeavePK;
    }

    public EarningLeave(String idNumber, String status, short version, Date dateAccruedTo, String classification) {
        this.earningLeavePK = new EarningLeavePK(idNumber, status, version, dateAccruedTo, classification);
    }

    public EarningLeavePK getEarningLeavePK() {
        return earningLeavePK;
    }

    public void setEarningLeavePK(EarningLeavePK earningLeavePK) {
        this.earningLeavePK = earningLeavePK;
    }

    public Double getLastYearsProrata() {
        return lastYearsProrata;
    }

    public void setLastYearsProrata(Double lastYearsProrata) {
        this.lastYearsProrata = lastYearsProrata;
    }

    public Double getCurrentProrata() {
        return currentProrata;
    }

    public void setCurrentProrata(Double currentProrata) {
        this.currentProrata = currentProrata;
    }

    public Double getEntitlement() {
        return entitlement;
    }

    public void setEntitlement(Double entitlement) {
        this.entitlement = entitlement;
    }

    public Double getTaken() {
        return taken;
    }

    public void setTaken(Double taken) {
        this.taken = taken;
    }

    public Double getTakenWithCert() {
        return takenWithCert;
    }

    public void setTakenWithCert(Double takenWithCert) {
        this.takenWithCert = takenWithCert;
    }

    public Double getNetEntitlement() {
        return netEntitlement;
    }

    public void setNetEntitlement(Double netEntitlement) {
        this.netEntitlement = netEntitlement;
    }

    public Double getTotalEntitlement() {
        return totalEntitlement;
    }

    public void setTotalEntitlement(Double totalEntitlement) {
        this.totalEntitlement = totalEntitlement;
    }

    public Double getTotalLiability() {
        return totalLiability;
    }

    public void setTotalLiability(Double totalLiability) {
        this.totalLiability = totalLiability;
    }

    public Double getPaidDinceAnnivTotal() {
        return paidDinceAnnivTotal;
    }

    public void setPaidDinceAnnivTotal(Double paidDinceAnnivTotal) {
        this.paidDinceAnnivTotal = paidDinceAnnivTotal;
    }

    public Double getPaidSinceAnnivProrataPortion() {
        return paidSinceAnnivProrataPortion;
    }

    public void setPaidSinceAnnivProrataPortion(Double paidSinceAnnivProrataPortion) {
        this.paidSinceAnnivProrataPortion = paidSinceAnnivProrataPortion;
    }

    public Double getLeaveTakenValue() {
        return leaveTakenValue;
    }

    public void setLeaveTakenValue(Double leaveTakenValue) {
        this.leaveTakenValue = leaveTakenValue;
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
        hash += (earningLeavePK != null ? earningLeavePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EarningLeave)) {
            return false;
        }
        EarningLeave other = (EarningLeave) object;
        if ((this.earningLeavePK == null && other.earningLeavePK != null) || (this.earningLeavePK != null && !this.earningLeavePK.equals(other.earningLeavePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.EarningLeave[ earningLeavePK=" + earningLeavePK + " ]";
    }

    public AccrualMethod getAccrualMethod() {
        return accrualMethod;
    }

    public void setAccrualMethod(AccrualMethod accrualMethod) {
        this.accrualMethod = accrualMethod;
    }

    public ClassificationLeave getClassificationLeave() {
        return classificationLeave;
    }

    public void setClassificationLeave(ClassificationLeave classificationLeave) {
        this.classificationLeave = classificationLeave;
    }
    
}
