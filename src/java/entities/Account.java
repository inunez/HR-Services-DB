/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ismael
 */
@Entity
@Table(name = "account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
    @NamedQuery(name = "Account.findByAccountNumber", query = "SELECT a FROM Account a WHERE a.accountNumber = :accountNumber"),
    @NamedQuery(name = "Account.findByAccountDescription", query = "SELECT a FROM Account a WHERE a.accountDescription = :accountDescription")})
public class Account implements Serializable {
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "account")
    private PlaxaAccount plaxaAccount;
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "account")
    private UniformAccount uniformAccount;

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "account_number")
    private String accountNumber;
    @Size(max = 255)
    @Column(name = "account_description")
    private String accountDescription;
    @OneToMany(mappedBy = "accountNumber")
    private Collection<Earning> earningCollection;
    @OneToMany(mappedBy = "accountNumber")
    private Collection<Payroll> payrollCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private Collection<UniformAccount> uniformAccountCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account")
    private Collection<PlaxaAccount> plaxaAccountCollection;
    
    public Account() {
    }

    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountDescription() {
        return accountDescription;
    }

    public void setAccountDescription(String accountDescription) {
        this.accountDescription = accountDescription;
    }

    @XmlTransient
    public Collection<Earning> getEarningCollection() {
        return earningCollection;
    }

    public void setEarningCollection(Collection<Earning> earningCollection) {
        this.earningCollection = earningCollection;
    }

    @XmlTransient
    public Collection<Payroll> getPayrollCollection() {
        return payrollCollection;
    }

    public void setPayrollCollection(Collection<Payroll> payrollCollection) {
        this.payrollCollection = payrollCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accountNumber != null ? accountNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.accountNumber == null && other.accountNumber != null) || (this.accountNumber != null && !this.accountNumber.equals(other.accountNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Account[ accountNumber=" + accountNumber + " ]";
    }

    @XmlTransient
    public Collection<UniformAccount> getUniformAccountCollection() {
        return uniformAccountCollection;
    }

    public void setUniformAccountCollection(Collection<UniformAccount> uniformAccountCollection) {
        this.uniformAccountCollection = uniformAccountCollection;
    }

    public Collection<PlaxaAccount> getPlaxaAccountCollection() {
        return plaxaAccountCollection;
    }

    public void setPlaxaAccountCollection(Collection<PlaxaAccount> plaxaAccountCollection) {
        this.plaxaAccountCollection = plaxaAccountCollection;
    }

    public PlaxaAccount getPlaxaAccount() {
        return plaxaAccount;
    }

    public void setPlaxaAccount(PlaxaAccount plaxaAccount) {
        this.plaxaAccount = plaxaAccount;
    }

    public UniformAccount getUniformAccount() {
        return uniformAccount;
    }

    public void setUniformAccount(UniformAccount uniformAccount) {
        this.uniformAccount = uniformAccount;
    }
    
}
