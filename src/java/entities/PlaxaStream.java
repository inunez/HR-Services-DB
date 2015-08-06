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
@Table(name = "plaxa_stream")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PlaxaStream.findAll", query = "SELECT p FROM PlaxaStream p"),
    @NamedQuery(name = "PlaxaStream.findByStreamId", query = "SELECT p FROM PlaxaStream p WHERE p.streamId = :streamId"),
    @NamedQuery(name = "PlaxaStream.findByStreamDescription", query = "SELECT p FROM PlaxaStream p WHERE p.streamDescription = :streamDescription")})
public class PlaxaStream implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "stream_id")
    private Short streamId;
    @Size(max = 45)
    @Column(name = "stream_description")
    private String streamDescription;
    @OneToMany(mappedBy = "streamId")
    private Collection<PlaxaAccount> plaxaAccountCollection;

    public PlaxaStream() {
    }

    public PlaxaStream(Short streamId) {
        this.streamId = streamId;
    }

    public Short getStreamId() {
        return streamId;
    }

    public void setStreamId(Short streamId) {
        this.streamId = streamId;
    }

    public String getStreamDescription() {
        return streamDescription;
    }

    public void setStreamDescription(String streamDescription) {
        this.streamDescription = streamDescription;
    }

    @XmlTransient
    public Collection<PlaxaAccount> getPlaxaAccountCollection() {
        return plaxaAccountCollection;
    }

    public void setPlaxaAccountCollection(Collection<PlaxaAccount> plaxaAccountCollection) {
        this.plaxaAccountCollection = plaxaAccountCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (streamId != null ? streamId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PlaxaStream)) {
            return false;
        }
        PlaxaStream other = (PlaxaStream) object;
        if ((this.streamId == null && other.streamId != null) || (this.streamId != null && !this.streamId.equals(other.streamId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.PlaxaStream[ streamId=" + streamId + " ]";
    }
    
}
