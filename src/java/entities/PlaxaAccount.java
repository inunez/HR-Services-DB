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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ismael
 */
@Entity
@Table(name = "plaxa_account")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlaxaAccount.findAll", query = "SELECT p FROM PlaxaAccount p"),
    @NamedQuery(name = "PlaxaAccount.findByAccountNumber", query = "SELECT p FROM PlaxaAccount p WHERE p.accountNumber = :accountNumber")})
public class PlaxaAccount implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "account_number")
    private String accountNumber;
    @OneToMany(mappedBy = "accountNumber")
    private Collection<Plaxa> plaxaCollection;
    
//    @Transient
    @JoinColumn(name = "account_number", referencedColumnName = "account_number", insertable = false, updatable = false)
    @ManyToOne (optional=true)
    private Account account;
        
    @JoinColumn(name = "location_id", referencedColumnName = "location_id")
    @ManyToOne
    private PlaxaLocation locationId;
    @JoinColumn(name = "region_id", referencedColumnName = "region_id")
    @ManyToOne
    private PlaxaRegion regionId;
    @JoinColumn(name = "stream_id", referencedColumnName = "stream_id")
    @ManyToOne
    private PlaxaStream streamId;

    public PlaxaAccount() {
    }

    public PlaxaAccount(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @XmlTransient
    public Collection<Plaxa> getPlaxaCollection() {
        return plaxaCollection;
    }

    public void setPlaxaCollection(Collection<Plaxa> plaxaCollection) {
        this.plaxaCollection = plaxaCollection;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public PlaxaLocation getLocationId() {
        return locationId;
    }

    public void setLocationId(PlaxaLocation locationId) {
        this.locationId = locationId;
    }

    public PlaxaRegion getRegionId() {
        return regionId;
    }

    public void setRegionId(PlaxaRegion regionId) {
        this.regionId = regionId;
    }

    public PlaxaStream getStreamId() {
        return streamId;
    }

    public void setStreamId(PlaxaStream streamId) {
        this.streamId = streamId;
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
        if (!(object instanceof PlaxaAccount)) {
            return false;
        }
        PlaxaAccount other = (PlaxaAccount) object;
        if ((this.accountNumber == null && other.accountNumber != null) || (this.accountNumber != null && !this.accountNumber.equals(other.accountNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PlaxaAccount[ accountNumber=" + accountNumber + " ]";
    }
    
}
