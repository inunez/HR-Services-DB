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
@Table(name = "classification")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Classification.findAll", query = "SELECT c FROM Classification c"),
    @NamedQuery(name = "Classification.findByClassificationId", query = "SELECT c FROM Classification c WHERE c.classificationId = :classificationId"),
    @NamedQuery(name = "Classification.findByClassificationDescription", query = "SELECT c FROM Classification c WHERE c.classificationDescription = :classificationDescription")})
public class Classification implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(max = 5)
    @Column(name = "classification_id")
    private String classificationId;
    @Size(max = 255)
    @Column(name = "classification_description")
    private String classificationDescription;
    @OneToMany(mappedBy = "classificationId")
    private Collection<Employee> employeeCollection;

    public Classification() {
    }

    public Classification(String classificationId) {
        this.classificationId = classificationId;
    }

    public String getClassificationId() {
        return classificationId;
    }

    public void setClassificationId(String classificationId) {
        this.classificationId = classificationId;
    }

    public String getClassificationDescription() {
        return classificationDescription;
    }

    public void setClassificationDescription(String classificationDescription) {
        this.classificationDescription = classificationDescription;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection() {
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (classificationId != null ? classificationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Classification)) {
            return false;
        }
        Classification other = (Classification) object;
        if ((this.classificationId == null && other.classificationId != null) || (this.classificationId != null && !this.classificationId.equals(other.classificationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Classification[ classificationId=" + classificationId + " ]";
    }
    
}
