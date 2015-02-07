/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ismael
 */
@Entity
@Table(name = "police_check")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PoliceCheck.findAll", query = "SELECT p FROM PoliceCheck p"),
    @NamedQuery(name = "PoliceCheck.findByIdNumber", query = "SELECT p FROM PoliceCheck p WHERE p.policeCheckPK.idNumber = :idNumber"),
    @NamedQuery(name = "PoliceCheck.findByExpiryDate", query = "SELECT p FROM PoliceCheck p WHERE p.expiryDate = :expiryDate"),
    @NamedQuery(name = "PoliceCheck.findByPrecedaComment", query = "SELECT p FROM PoliceCheck p WHERE p.precedaComment = :precedaComment"),
    @NamedQuery(name = "PoliceCheck.findByUpdateDate", query = "SELECT p FROM PoliceCheck p WHERE p.policeCheckPK.updateDate = :updateDate"),
    @NamedQuery(name = "PoliceCheck.findBySnReceived", query = "SELECT p FROM PoliceCheck p WHERE p.snReceived = :snReceived"),
    @NamedQuery(name = "PoliceCheck.findByFirstReportDate", query = "SELECT p FROM PoliceCheck p WHERE p.firstReportDate = :firstReportDate"),
    @NamedQuery(name = "PoliceCheck.findByFynFirstReportSent", query = "SELECT p FROM PoliceCheck p WHERE p.fynFirstReportSent = :fynFirstReportSent"),
    @NamedQuery(name = "PoliceCheck.findByFirstLetterDate", query = "SELECT p FROM PoliceCheck p WHERE p.firstLetterDate = :firstLetterDate"),
    @NamedQuery(name = "PoliceCheck.findByYnFirstLetterSent", query = "SELECT p FROM PoliceCheck p WHERE p.ynFirstLetterSent = :ynFirstLetterSent"),
    @NamedQuery(name = "PoliceCheck.findBySecondLetterDate", query = "SELECT p FROM PoliceCheck p WHERE p.secondLetterDate = :secondLetterDate"),
    @NamedQuery(name = "PoliceCheck.findByYnSecondLetterSent", query = "SELECT p FROM PoliceCheck p WHERE p.ynSecondLetterSent = :ynSecondLetterSent"),
    @NamedQuery(name = "PoliceCheck.findByThirdLetterDate", query = "SELECT p FROM PoliceCheck p WHERE p.thirdLetterDate = :thirdLetterDate"),
    @NamedQuery(name = "PoliceCheck.findByYnThirdlettersent", query = "SELECT p FROM PoliceCheck p WHERE p.ynThirdlettersent = :ynThirdlettersent"),
    @NamedQuery(name = "PoliceCheck.findByManagerPhoneCallDate", query = "SELECT p FROM PoliceCheck p WHERE p.managerPhoneCallDate = :managerPhoneCallDate"),
    @NamedQuery(name = "PoliceCheck.findByYnPhoneCallDone", query = "SELECT p FROM PoliceCheck p WHERE p.ynPhoneCallDone = :ynPhoneCallDone"),
    @NamedQuery(name = "PoliceCheck.findByPrmStatus", query = "SELECT p FROM PoliceCheck p WHERE p.prmStatus = :prmStatus")})
public class PoliceCheck implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PoliceCheckPK policeCheckPK;
    @Column(name = "expiry_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;
    @Size(max = 255)
    @Column(name = "preceda_comment")
    private String precedaComment;
    @Basic(optional = false)
    @NotNull
    @Column(name = "sn_received")
    private boolean snReceived;
    @Column(name = "first_report_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstReportDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "fyn_first_report_sent")
    private boolean fynFirstReportSent;
    @Column(name = "first_letter_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstLetterDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "yn_first_letter_sent")
    private boolean ynFirstLetterSent;
    @Column(name = "second_letter_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date secondLetterDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "yn_second_letter_sent")
    private boolean ynSecondLetterSent;
    @Column(name = "third_letter_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date thirdLetterDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "yn_Third_letter_sent")
    private boolean ynThirdlettersent;
    @Column(name = "manager_phone_call_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date managerPhoneCallDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "yn_phone_call_done")
    private boolean ynPhoneCallDone;
    @Size(max = 255)
    @Column(name = "prm_status")
    private String prmStatus;
//    @JoinColumn(name = "id_number", referencedColumnName = "id_number", insertable = false, updatable = false)
    @JoinColumns({
    @JoinColumn(name = "employee_status", referencedColumnName = "status"),
    @JoinColumn(name = "id_number", referencedColumnName = "id_number", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Employee employee;

    public PoliceCheck() {
    }

    public PoliceCheck(PoliceCheckPK policeCheckPK) {
        this.policeCheckPK = policeCheckPK;
    }

    public PoliceCheck(PoliceCheckPK policeCheckPK, boolean snReceived, boolean fynFirstReportSent, boolean ynFirstLetterSent, boolean ynSecondLetterSent, boolean ynThirdlettersent, boolean ynPhoneCallDone) {
        this.policeCheckPK = policeCheckPK;
        this.snReceived = snReceived;
        this.fynFirstReportSent = fynFirstReportSent;
        this.ynFirstLetterSent = ynFirstLetterSent;
        this.ynSecondLetterSent = ynSecondLetterSent;
        this.ynThirdlettersent = ynThirdlettersent;
        this.ynPhoneCallDone = ynPhoneCallDone;
    }

    public PoliceCheck(String idNumber, Date updateDate) {
        this.policeCheckPK = new PoliceCheckPK(idNumber, updateDate);
    }

    public PoliceCheckPK getPoliceCheckPK() {
        return policeCheckPK;
    }

    public void setPoliceCheckPK(PoliceCheckPK policeCheckPK) {
        this.policeCheckPK = policeCheckPK;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getPrecedaComment() {
        return precedaComment;
    }

    public void setPrecedaComment(String precedaComment) {
        this.precedaComment = precedaComment;
    }

    public boolean getSnReceived() {
        return snReceived;
    }

    public void setSnReceived(boolean snReceived) {
        this.snReceived = snReceived;
    }

    public Date getFirstReportDate() {
        return firstReportDate;
    }

    public void setFirstReportDate(Date firstReportDate) {
        this.firstReportDate = firstReportDate;
    }

    public boolean getFynFirstReportSent() {
        return fynFirstReportSent;
    }

    public void setFynFirstReportSent(boolean fynFirstReportSent) {
        this.fynFirstReportSent = fynFirstReportSent;
    }

    public Date getFirstLetterDate() {
        return firstLetterDate;
    }

    public void setFirstLetterDate(Date firstLetterDate) {
        this.firstLetterDate = firstLetterDate;
    }

    public boolean getYnFirstLetterSent() {
        return ynFirstLetterSent;
    }

    public void setYnFirstLetterSent(boolean ynFirstLetterSent) {
        this.ynFirstLetterSent = ynFirstLetterSent;
    }

    public Date getSecondLetterDate() {
        return secondLetterDate;
    }

    public void setSecondLetterDate(Date secondLetterDate) {
        this.secondLetterDate = secondLetterDate;
    }

    public boolean getYnSecondLetterSent() {
        return ynSecondLetterSent;
    }

    public void setYnSecondLetterSent(boolean ynSecondLetterSent) {
        this.ynSecondLetterSent = ynSecondLetterSent;
    }

    public Date getThirdLetterDate() {
        return thirdLetterDate;
    }

    public void setThirdLetterDate(Date thirdLetterDate) {
        this.thirdLetterDate = thirdLetterDate;
    }

    public boolean getYnThirdlettersent() {
        return ynThirdlettersent;
    }

    public void setYnThirdlettersent(boolean ynThirdlettersent) {
        this.ynThirdlettersent = ynThirdlettersent;
    }

    public Date getManagerPhoneCallDate() {
        return managerPhoneCallDate;
    }

    public void setManagerPhoneCallDate(Date managerPhoneCallDate) {
        this.managerPhoneCallDate = managerPhoneCallDate;
    }

    public boolean getYnPhoneCallDone() {
        return ynPhoneCallDone;
    }

    public void setYnPhoneCallDone(boolean ynPhoneCallDone) {
        this.ynPhoneCallDone = ynPhoneCallDone;
    }

    public String getPrmStatus() {
        return prmStatus;
    }

    public void setPrmStatus(String prmStatus) {
        this.prmStatus = prmStatus;
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
        hash += (policeCheckPK != null ? policeCheckPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PoliceCheck)) {
            return false;
        }
        PoliceCheck other = (PoliceCheck) object;
        if ((this.policeCheckPK == null && other.policeCheckPK != null) || (this.policeCheckPK != null && !this.policeCheckPK.equals(other.policeCheckPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PoliceCheck[ policeCheckPK=" + policeCheckPK + " ]";
    }
    
}
