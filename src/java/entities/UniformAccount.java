/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
@Table(name = "uniform_account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UniformAccount.findAll", query = "SELECT u FROM UniformAccount u"),
    @NamedQuery(name = "UniformAccount.findByAccountNumber", query = "SELECT u FROM UniformAccount u WHERE u.accountNumber = :accountNumber")})
public class UniformAccount implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "account_number")
    private String accountNumber;
    
    @OneToMany(mappedBy = "accountNumber")
    private Collection<Uniform> uniformCollection;
    
    @JoinColumn(name = "account_number", referencedColumnName = "account_number", insertable = false, updatable = false)
    @ManyToOne(optional = true)
    private Account account;
    
    @JoinColumns({
        @JoinColumn(name = "region", referencedColumnName = "region"),
        @JoinColumn(name = "ship_to", referencedColumnName = "ship_to")})
    @ManyToOne
    private UniformDeaneLocation uniformDeaneLocation;

    public UniformAccount() {
    }

    public UniformAccount(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @XmlTransient
    public Collection<Uniform> getUniformCollection() {
        return uniformCollection;
    }

    public void setUniformCollection(Collection<Uniform> uniformCollection) {
        this.uniformCollection = uniformCollection;
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
        if (!(object instanceof UniformAccount)) {
            return false;
        }
        UniformAccount other = (UniformAccount) object;
        if ((this.accountNumber == null && other.accountNumber != null) || (this.accountNumber != null && !this.accountNumber.equals(other.accountNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.UniformAccount[ accountNumber=" + accountNumber + " ]";
    }

    public UniformDeaneLocation getUniformDeaneLocation() {
        return uniformDeaneLocation;
    }

    public void setUniformDeaneLocation(UniformDeaneLocation uniformDeaneLocation) {
        this.uniformDeaneLocation = uniformDeaneLocation;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    
}
