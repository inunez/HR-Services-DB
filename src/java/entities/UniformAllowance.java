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
@Table(name = "uniform_allowance")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UniformAllowance.findAll", query = "SELECT u FROM UniformAllowance u"),
    @NamedQuery(name = "UniformAllowance.findByAllowanceId", query = "SELECT u FROM UniformAllowance u WHERE u.allowanceId = :allowanceId"),
    @NamedQuery(name = "UniformAllowance.findByAllowanceCode", query = "SELECT u FROM UniformAllowance u WHERE u.allowanceCode = :allowanceCode"),
    @NamedQuery(name = "UniformAllowance.findByHourMin", query = "SELECT u FROM UniformAllowance u WHERE u.hourMin = :hourMin"),
    @NamedQuery(name = "UniformAllowance.findByHourMax", query = "SELECT u FROM UniformAllowance u WHERE u.hourMax = :hourMax")})
public class UniformAllowance implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "allowance_id")
    private Short allowanceId;
    @Size(max = 15)
    @Column(name = "allowance_code")
    private String allowanceCode;
    @Column(name = "hour_min")
    private Short hourMin;
    @Column(name = "hour_max")
    private Short hourMax;
    @OneToMany(mappedBy = "allowanceId")
    private Collection<Uniform> uniformCollection;

    public UniformAllowance() {
    }

    public UniformAllowance(Short allowanceId) {
        this.allowanceId = allowanceId;
    }

    public Short getAllowanceId() {
        return allowanceId;
    }

    public void setAllowanceId(Short allowanceId) {
        this.allowanceId = allowanceId;
    }

    public String getAllowanceCode() {
        return allowanceCode;
    }

    public void setAllowanceCode(String allowanceCode) {
        this.allowanceCode = allowanceCode;
    }

    public Short getHourMin() {
        return hourMin;
    }

    public void setHourMin(Short hourMin) {
        this.hourMin = hourMin;
    }

    public Short getHourMax() {
        return hourMax;
    }

    public void setHourMax(Short hourMax) {
        this.hourMax = hourMax;
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
        hash += (allowanceId != null ? allowanceId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UniformAllowance)) {
            return false;
        }
        UniformAllowance other = (UniformAllowance) object;
        if ((this.allowanceId == null && other.allowanceId != null) || (this.allowanceId != null && !this.allowanceId.equals(other.allowanceId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.UniformAllowance[ allowanceId=" + allowanceId + " ]";
    }
    
}
