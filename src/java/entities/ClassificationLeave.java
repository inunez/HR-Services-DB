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
@Table(name = "classification_leave")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ClassificationLeave.findAll", query = "SELECT c FROM ClassificationLeave c"),
    @NamedQuery(name = "ClassificationLeave.findByClassificationCode", query = "SELECT c FROM ClassificationLeave c WHERE c.classificationCode = :classificationCode"),
    @NamedQuery(name = "ClassificationLeave.findByClassificationDescription", query = "SELECT c FROM ClassificationLeave c WHERE c.classificationDescription = :classificationDescription")})
public class ClassificationLeave implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "classification_code")
    private String classificationCode;
    @Size(max = 45)
    @Column(name = "classification_description")
    private String classificationDescription;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "classificationLeave")
    private Collection<AccrualMethod> accrualMethodCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "classificationLeave")
    private Collection<EarningLeave> earningLeaveCollection;

    public ClassificationLeave() {
    }

    public ClassificationLeave(String classificationCode) {
        this.classificationCode = classificationCode;
    }

    public String getClassificationCode() {
        return classificationCode;
    }

    public void setClassificationCode(String classificationCode) {
        this.classificationCode = classificationCode;
    }

    public String getClassificationDescription() {
        return classificationDescription;
    }

    public void setClassificationDescription(String classificationDescription) {
        this.classificationDescription = classificationDescription;
    }

    @XmlTransient
    public Collection<AccrualMethod> getAccrualMethodCollection() {
        return accrualMethodCollection;
    }

    public void setAccrualMethodCollection(Collection<AccrualMethod> accrualMethodCollection) {
        this.accrualMethodCollection = accrualMethodCollection;
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
        hash += (classificationCode != null ? classificationCode.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClassificationLeave)) {
            return false;
        }
        ClassificationLeave other = (ClassificationLeave) object;
        if ((this.classificationCode == null && other.classificationCode != null) || (this.classificationCode != null && !this.classificationCode.equals(other.classificationCode))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ClassificationLeave[ classificationCode=" + classificationCode + " ]";
    }
    
}
