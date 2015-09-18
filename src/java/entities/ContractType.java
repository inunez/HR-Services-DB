/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ismael Nunez
 */
@Entity
@Table(name = "contract_type")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ContractType.findAll", query = "SELECT c FROM ContractType c"),
    @NamedQuery(name = "ContractType.findByContractType", query = "SELECT c FROM ContractType c WHERE c.contractType = :contractType"),
    @NamedQuery(name = "ContractType.findByContractTypeDescription", query = "SELECT c FROM ContractType c WHERE c.contractTypeDescription = :contractTypeDescription"),
    @NamedQuery(name = "ContractType.findByPriority", query = "SELECT c FROM ContractType c WHERE c.priority = :priority"),
    @NamedQuery(name = "ContractType.findByGroupType", query = "SELECT c FROM ContractType c WHERE c.groupType = :groupType")})
public class ContractType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "contract_type")
    private Integer contractType;
    @Size(max = 45)
    @Column(name = "contract_type_description")
    private String contractTypeDescription;
    @Column(name = "priority")
    private Integer priority;
    @Size(max = 45)
    @Column(name = "group_type")
    private String groupType;
    @Size(max = 45)
    @Column(name = "name_combo")
    private String nameCombo;
    @Size(max = 100)
    @Column(name = "template_name")
    private String templateName;
    
    public ContractType() {
    }

    public ContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public String getContractTypeDescription() {
        return contractTypeDescription;
    }

    public void setContractTypeDescription(String contractTypeDescription) {
        this.contractTypeDescription = contractTypeDescription;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public String getNameCombo() {
        return nameCombo;
    }

    public void setNameCombo(String nameCombo) {
        this.nameCombo = nameCombo;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (contractType != null ? contractType.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ContractType)) {
            return false;
        }
        ContractType other = (ContractType) object;
        if ((this.contractType == null && other.contractType != null) || (this.contractType != null && !this.contractType.equals(other.contractType))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ContractType[ contractType=" + contractType + " ]";
    }
    
}
