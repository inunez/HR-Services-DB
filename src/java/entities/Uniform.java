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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ismael
 */
@Entity
@Table(name = "uniform")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Uniform.findAll", query = "SELECT u FROM Uniform u"),
    @NamedQuery(name = "Uniform.findByIdNumber", query = "SELECT u FROM Uniform u WHERE u.uniformPK.idNumber = :idNumber"),
    @NamedQuery(name = "Uniform.findByStatus", query = "SELECT u FROM Uniform u WHERE u.uniformPK.status = :status"),
    @NamedQuery(name = "Uniform.findByDateSent", query = "SELECT u FROM Uniform u WHERE u.uniformPK.dateSent = :dateSent"),
    @NamedQuery(name = "Uniform.findByIndex", query = "SELECT u FROM Uniform u WHERE u.uniformPK.index = :index"),
    @NamedQuery(name = "Uniform.findByChangeDate", query = "SELECT u FROM Uniform u WHERE u.changeDate = :changeDate")})
public class Uniform implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UniformPK uniformPK;
    @Column(name = "change_date")
    @Temporal(TemporalType.DATE)
    private Date changeDate;
    @JoinColumn(name = "account_number", referencedColumnName = "account_number")
    @ManyToOne
    private UniformAccount accountNumber;
    @JoinColumns({
        @JoinColumn(name = "status", referencedColumnName = "status", insertable = false, updatable = false),
        @JoinColumn(name = "id_number", referencedColumnName = "id_number", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Employee employee;
    
    @JoinColumn(name = "allowance_id", referencedColumnName = "allowance_id")
    @ManyToOne
    private UniformAllowance allowanceId;   
    
    public Uniform() {
    }

    public Uniform(UniformPK uniformPK) {
        this.uniformPK = uniformPK;
    }

    public Uniform(String idNumber, String status, Date dateSent, Integer index) {
        this.uniformPK = new UniformPK(idNumber, status, dateSent, index);
    }

    public UniformPK getUniformPK() {
        return uniformPK;
    }

    public void setUniformPK(UniformPK uniformPK) {
        this.uniformPK = uniformPK;
    }

    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    public UniformAccount getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(UniformAccount accountNumber) {
        this.accountNumber = accountNumber;
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
        hash += (uniformPK != null ? uniformPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Uniform)) {
            return false;
        }
        Uniform other = (Uniform) object;
        if ((this.uniformPK == null && other.uniformPK != null) || (this.uniformPK != null && !this.uniformPK.equals(other.uniformPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Uniform[ uniformPK=" + uniformPK + " ]";
    }

    public UniformAllowance getAllowanceId() {
        return allowanceId;
    }

    public void setAllowanceId(UniformAllowance allowanceId) {
        this.allowanceId = allowanceId;
    }
    
}
