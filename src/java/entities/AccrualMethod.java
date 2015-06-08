/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ismael
 */
@Entity
@Table(name = "accrual_method")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AccrualMethod.findAll", query = "SELECT a FROM AccrualMethod a"),
    @NamedQuery(name = "AccrualMethod.findByClassificationCode", query = "SELECT a FROM AccrualMethod a WHERE a.accrualMethodPK.classificationCode = :classificationCode"),
    @NamedQuery(name = "AccrualMethod.findByAccrualMethodCode", query = "SELECT a FROM AccrualMethod a WHERE a.accrualMethodPK.accrualMethodCode = :accrualMethodCode"),
    @NamedQuery(name = "AccrualMethod.findByAccrualMethodDescription", query = "SELECT a FROM AccrualMethod a WHERE a.accrualMethodDescription = :accrualMethodDescription")})
public class AccrualMethod implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected AccrualMethodPK accrualMethodPK;
    @Size(max = 45)
    @Column(name = "accrual_method_description")
    private String accrualMethodDescription;
    @JoinColumn(name = "classification_code", referencedColumnName = "classification_code", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private ClassificationLeave classificationLeave;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "accrualMethod")
    private Collection<EarningLeave> earningLeaveCollection;

    public AccrualMethod() {
    }

    public AccrualMethod(AccrualMethodPK accrualMethodPK) {
        this.accrualMethodPK = accrualMethodPK;
    }

    public AccrualMethod(String classificationCode, String accrualMethodCode) {
        this.accrualMethodPK = new AccrualMethodPK(classificationCode, accrualMethodCode);
    }

    public AccrualMethodPK getAccrualMethodPK() {
        return accrualMethodPK;
    }

    public void setAccrualMethodPK(AccrualMethodPK accrualMethodPK) {
        this.accrualMethodPK = accrualMethodPK;
    }

    public String getAccrualMethodDescription() {
        return accrualMethodDescription;
    }

    public void setAccrualMethodDescription(String accrualMethodDescription) {
        this.accrualMethodDescription = accrualMethodDescription;
    }

    public ClassificationLeave getClassificationLeave() {
        return classificationLeave;
    }

    public void setClassificationLeave(ClassificationLeave classificationLeave) {
        this.classificationLeave = classificationLeave;
    }

    @XmlTransient
    public Collection<EarningLeave> getEarningLeaveCollection() {
        return earningLeaveCollection;
    }

    public void setEarningLeaveCollection(Collection<EarningLeave> earningLeaveCollection) {
        this.earningLeaveCollection = earningLeaveCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (accrualMethodPK != null ? accrualMethodPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AccrualMethod)) {
            return false;
        }
        AccrualMethod other = (AccrualMethod) object;
        if ((this.accrualMethodPK == null && other.accrualMethodPK != null) || (this.accrualMethodPK != null && !this.accrualMethodPK.equals(other.accrualMethodPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.AccrualMethod[ accrualMethodPK=" + accrualMethodPK + " ]";
    }
    
}
