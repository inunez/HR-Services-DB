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
@Table(name = "visa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Visa.findAll", query = "SELECT v FROM Visa v"),
    @NamedQuery(name = "Visa.findByIdNumber", query = "SELECT v FROM Visa v WHERE v.visaPK.idNumber = :idNumber"),
    @NamedQuery(name = "Visa.findByPassportNumber", query = "SELECT v FROM Visa v WHERE v.visaPK.passportNumber = :passportNumber"),
    @NamedQuery(name = "Visa.findByVisaIssuedDate", query = "SELECT v FROM Visa v WHERE v.visaIssuedDate = :visaIssuedDate"),
    @NamedQuery(name = "Visa.findByVisaExpiryDate", query = "SELECT v FROM Visa v WHERE v.visaExpiryDate = :visaExpiryDate"),
    @NamedQuery(name = "Visa.findByPrecedaComment1", query = "SELECT v FROM Visa v WHERE v.precedaComment1 = :precedaComment1"),
    @NamedQuery(name = "Visa.findByPrecedaComment2", query = "SELECT v FROM Visa v WHERE v.precedaComment2 = :precedaComment2"),
    @NamedQuery(name = "Visa.findByPrecedaComment3", query = "SELECT v FROM Visa v WHERE v.precedaComment3 = :precedaComment3"),
    @NamedQuery(name = "Visa.findByUpdateDate", query = "SELECT v FROM Visa v WHERE v.visaPK.updateDate = :updateDate"),
    @NamedQuery(name = "Visa.findByFirstReportDate", query = "SELECT v FROM Visa v WHERE v.firstReportDate = :firstReportDate"),
    @NamedQuery(name = "Visa.findByYnFirstReportSent", query = "SELECT v FROM Visa v WHERE v.ynFirstReportSent = :ynFirstReportSent"),
    @NamedQuery(name = "Visa.findByFirstLetterDate", query = "SELECT v FROM Visa v WHERE v.firstLetterDate = :firstLetterDate"),
    @NamedQuery(name = "Visa.findByYnFirstLetterSent", query = "SELECT v FROM Visa v WHERE v.ynFirstLetterSent = :ynFirstLetterSent"),
    @NamedQuery(name = "Visa.findBySecondLetterDate", query = "SELECT v FROM Visa v WHERE v.secondLetterDate = :secondLetterDate"),
    @NamedQuery(name = "Visa.findByYnSecondLetterSent", query = "SELECT v FROM Visa v WHERE v.ynSecondLetterSent = :ynSecondLetterSent"),
    @NamedQuery(name = "Visa.findByManagerPhoneCallDate", query = "SELECT v FROM Visa v WHERE v.managerPhoneCallDate = :managerPhoneCallDate"),
    @NamedQuery(name = "Visa.findByYnPhoneCallDone", query = "SELECT v FROM Visa v WHERE v.ynPhoneCallDone = :ynPhoneCallDone")})
public class Visa implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected VisaPK visaPK;
    @Column(name = "visa_issued_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date visaIssuedDate;
    @Column(name = "visa_expiry_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date visaExpiryDate;
    @Size(max = 255)
    @Column(name = "preceda_comment_1")
    private String precedaComment1;
    @Size(max = 255)
    @Column(name = "preceda_comment_2")
    private String precedaComment2;
    @Size(max = 255)
    @Column(name = "preceda_comment_3")
    private String precedaComment3;
    @Column(name = "first_report_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date firstReportDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "yn_first_report_sent")
    private boolean ynFirstReportSent;
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
    @Column(name = "manager_phone_call_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date managerPhoneCallDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "yn_phone_call_done")
    private boolean ynPhoneCallDone;
    @JoinColumn(name = "visa_country", referencedColumnName = "country")
    @ManyToOne
    private Country visaCountry;
//    @JoinColumn(name = "id_number", referencedColumnName = "id_number", insertable = false, updatable = false)
    @JoinColumns({
    @JoinColumn(name = "employee_status", referencedColumnName = "status"),
    @JoinColumn(name = "id_number", referencedColumnName = "id_number", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Employee employee;

    public Visa() {
    }

    public Visa(VisaPK visaPK) {
        this.visaPK = visaPK;
    }

    public Visa(VisaPK visaPK, boolean ynFirstReportSent, boolean ynFirstLetterSent, boolean ynSecondLetterSent, boolean ynPhoneCallDone) {
        this.visaPK = visaPK;
        this.ynFirstReportSent = ynFirstReportSent;
        this.ynFirstLetterSent = ynFirstLetterSent;
        this.ynSecondLetterSent = ynSecondLetterSent;
        this.ynPhoneCallDone = ynPhoneCallDone;
    }

    public Visa(String idNumber, String passportNumber, Date updateDate) {
        this.visaPK = new VisaPK(idNumber, passportNumber, updateDate);
    }

    public VisaPK getVisaPK() {
        return visaPK;
    }

    public void setVisaPK(VisaPK visaPK) {
        this.visaPK = visaPK;
    }

    public Date getVisaIssuedDate() {
        return visaIssuedDate;
    }

    public void setVisaIssuedDate(Date visaIssuedDate) {
        this.visaIssuedDate = visaIssuedDate;
    }

    public Date getVisaExpiryDate() {
        return visaExpiryDate;
    }

    public void setVisaExpiryDate(Date visaExpiryDate) {
        this.visaExpiryDate = visaExpiryDate;
    }

    public String getPrecedaComment1() {
        return precedaComment1;
    }

    public void setPrecedaComment1(String precedaComment1) {
        this.precedaComment1 = precedaComment1;
    }

    public String getPrecedaComment2() {
        return precedaComment2;
    }

    public void setPrecedaComment2(String precedaComment2) {
        this.precedaComment2 = precedaComment2;
    }

    public String getPrecedaComment3() {
        return precedaComment3;
    }

    public void setPrecedaComment3(String precedaComment3) {
        this.precedaComment3 = precedaComment3;
    }

    public Date getFirstReportDate() {
        return firstReportDate;
    }

    public void setFirstReportDate(Date firstReportDate) {
        this.firstReportDate = firstReportDate;
    }

    public boolean getYnFirstReportSent() {
        return ynFirstReportSent;
    }

    public void setYnFirstReportSent(boolean ynFirstReportSent) {
        this.ynFirstReportSent = ynFirstReportSent;
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

    public Country getVisaCountry() {
        return visaCountry;
    }

    public void setVisaCountry(Country visaCountry) {
        this.visaCountry = visaCountry;
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
        hash += (visaPK != null ? visaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Visa)) {
            return false;
        }
        Visa other = (Visa) object;
        if ((this.visaPK == null && other.visaPK != null) || (this.visaPK != null && !this.visaPK.equals(other.visaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Visa[ visaPK=" + visaPK + " ]";
    }
    
}
