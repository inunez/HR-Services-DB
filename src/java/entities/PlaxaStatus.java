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
@Table(name = "plaxa_status")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlaxaStatus.findAll", query = "SELECT p FROM PlaxaStatus p"),
    @NamedQuery(name = "PlaxaStatus.findByStatusCode", query = "SELECT p FROM PlaxaStatus p WHERE p.statusCode = :statusCode"),
    @NamedQuery(name = "PlaxaStatus.findByStatusDescription", query = "SELECT p FROM PlaxaStatus p WHERE p.statusDescription = :statusDescription")})
public class PlaxaStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "status_code")
    private Short statusCode;
    @Size(max = 45)
    @Column(name = "status_description")
    private String statusDescription;
    @OneToMany(mappedBy = "statusCode")
    private Collection<Plaxa> plaxaCollection;

    public PlaxaStatus() {
    }

    public PlaxaStatus(Short statusCode) {
        this.statusCode = statusCode;
    }

    public Short getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Short statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    @XmlTransient
    public Collection<Plaxa> getPlaxaCollection() {
        return plaxaCollection;
    }

    public void setPlaxaCollection(Collection<Plaxa> plaxaCollection) {
        this.plaxaCollection = plaxaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (statusCode != null ? statusCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlaxaStatus)) {
            return false;
        }
        PlaxaStatus other = (PlaxaStatus) object;
        if ((this.statusCode == null && other.statusCode != null) || (this.statusCode != null && !this.statusCode.equals(other.statusCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PlaxaStatus[ statusCode=" + statusCode + " ]";
    }
    
}
