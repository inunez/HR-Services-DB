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
@Table(name = "position")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Position.findAll", query = "SELECT p FROM Position p ORDER BY p.positionTitle"),
    @NamedQuery(name = "Position.findByPositionId", query = "SELECT p FROM Position p WHERE p.positionId = :positionId"),
    @NamedQuery(name = "Position.findByPositionTitle", query = "SELECT p FROM Position p WHERE p.positionTitle = :positionTitle")})
public class Position implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "position_id")
    private String positionId;
    @Size(max = 255)
    @Column(name = "position_title")
    private String positionTitle;
    @OneToMany(mappedBy = "positionId")
     Collection<Employee> employeeCollection;
    @OneToMany(mappedBy = "reportsToPositionId")
    private Collection<Employee> employeeCollection1;
    @OneToMany(mappedBy = "positionId")
    private Collection<Earning> earningCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "position")
    private Collection<ManagerPosition> managerPositionCollection;

    public Position() {
    }

    public Position(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionId() {
        return positionId;
    }

    public void setPositionId(String positionId) {
        this.positionId = positionId;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection() {
//        if("70760001".equals(positionId) ){
//            System.out.println("aca");
//        }
        return employeeCollection;
    }

    public void setEmployeeCollection(Collection<Employee> employeeCollection) {
        this.employeeCollection = employeeCollection;
    }

    @XmlTransient
    public Collection<Employee> getEmployeeCollection1() {
        return employeeCollection1;
    }

    public void setEmployeeCollection1(Collection<Employee> employeeCollection1) {
        this.employeeCollection1 = employeeCollection1;
    }

    @XmlTransient
    public Collection<Earning> getEarningCollection() {
        return earningCollection;
    }

    public void setEarningCollection(Collection<Earning> earningCollection) {
        this.earningCollection = earningCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (positionId != null ? positionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Position)) {
            return false;
        }
        Position other = (Position) object;
        if ((this.positionId == null && other.positionId != null) || (this.positionId != null && !this.positionId.equals(other.positionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Position[ positionId=" + positionId + " ]";
    }

    @XmlTransient
    public Collection<ManagerPosition> getManagerPositionCollection() {
        return managerPositionCollection;
    }

    public void setManagerPositionCollection(Collection<ManagerPosition> managerPositionCollection) {
        this.managerPositionCollection = managerPositionCollection;
    }
    
}
