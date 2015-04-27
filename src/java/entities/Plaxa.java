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
@Table(name = "plaxa")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Plaxa.findAll", query = "SELECT p FROM Plaxa p"),
    @NamedQuery(name = "Plaxa.findByIdNumber", query = "SELECT p FROM Plaxa p WHERE p.plaxaPK.idNumber = :idNumber"),
    @NamedQuery(name = "Plaxa.findByStatus", query = "SELECT p FROM Plaxa p WHERE p.plaxaPK.status = :status"),
    @NamedQuery(name = "Plaxa.findByPlaxaId", query = "SELECT p FROM Plaxa p WHERE p.plaxaId = :plaxaId"),
    @NamedQuery(name = "Plaxa.findByReportsToFirstName", query = "SELECT p FROM Plaxa p WHERE p.reportsToFirstName = :reportsToFirstName"),
    @NamedQuery(name = "Plaxa.findByReportsToSurname", query = "SELECT p FROM Plaxa p WHERE p.reportsToSurname = :reportsToSurname"),
    @NamedQuery(name = "Plaxa.findByDateLastActivity", query = "SELECT p FROM Plaxa p WHERE p.dateLastActivity = :dateLastActivity")})

public class Plaxa implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PlaxaPK plaxaPK;
    @Size(max = 45)
    @Column(name = "plaxa_id")
    private String plaxaId;
    @Size(max = 45)
    @Column(name = "reports_to_first_name")
    private String reportsToFirstName;
    @Size(max = 45)
    @Column(name = "reports_to_surname")
    private String reportsToSurname;
    @Column(name = "date_last_activity")
    @Temporal(TemporalType.DATE)
    private Date dateLastActivity;
    @JoinColumns({
        @JoinColumn(name = "id_number", referencedColumnName = "id_number", insertable = false, updatable = false),
        @JoinColumn(name = "status", referencedColumnName = "status", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Employee employee;
    @JoinColumn(name = "account_number", referencedColumnName = "account_number")
    @ManyToOne
    private PlaxaAccount accountNumber;
    @JoinColumn(name = "status_code", referencedColumnName = "status_code")
    @ManyToOne
    private PlaxaStatus statusCode;

    public Plaxa() {
    }

    public Plaxa(PlaxaPK plaxaPK) {
        this.plaxaPK = plaxaPK;
    }

    public Plaxa(String idNumber, String status) {
        this.plaxaPK = new PlaxaPK(idNumber, status);
    }

    public PlaxaPK getPlaxaPK() {
        return plaxaPK;
    }

    public void setPlaxaPK(PlaxaPK plaxaPK) {
        this.plaxaPK = plaxaPK;
    }

    public String getPlaxaId() {
        return plaxaId;
    }

    public void setPlaxaId(String plaxaId) {
        this.plaxaId = plaxaId;
    }

    public String getReportsToFirstName() {
        return reportsToFirstName;
    }

    public void setReportsToFirstName(String reportsToFirstName) {
        this.reportsToFirstName = reportsToFirstName;
    }

    public String getReportsToSurname() {
        return reportsToSurname;
    }

    public void setReportsToSurname(String reportsToSurname) {
        this.reportsToSurname = reportsToSurname;
    }

    public String getReportsToFullName() {
        return reportsToFirstName.concat(" ".concat(reportsToSurname));
    }
    
    public Date getDateLastActivity() {
        return dateLastActivity;
    }

    public void setDateLastActivity(Date dateLastActivity) {
        this.dateLastActivity = dateLastActivity;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public PlaxaAccount getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(PlaxaAccount accountNumber) {
        this.accountNumber = accountNumber;
    }

    public PlaxaStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(PlaxaStatus statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (plaxaPK != null ? plaxaPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Plaxa)) {
            return false;
        }
        Plaxa other = (Plaxa) object;
        if ((this.plaxaPK == null && other.plaxaPK != null) || (this.plaxaPK != null && !this.plaxaPK.equals(other.plaxaPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Plaxa[ plaxaPK=" + plaxaPK + " ]";
    }
    
}
