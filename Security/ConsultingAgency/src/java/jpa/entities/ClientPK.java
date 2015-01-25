package jpa.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author nbuser
 */
@Embeddable
public class ClientPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "client_name")
    private String clientName;
    @Basic(optional = false)
    @Column(name = "client_department_number")
    private short clientDepartmentNumber;

    public ClientPK() {
    }

    public ClientPK(String clientName, short clientDepartmentNumber) {
        this.clientName = clientName;
        this.clientDepartmentNumber = clientDepartmentNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public short getClientDepartmentNumber() {
        return clientDepartmentNumber;
    }

    public void setClientDepartmentNumber(short clientDepartmentNumber) {
        this.clientDepartmentNumber = clientDepartmentNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (clientName != null ? clientName.hashCode() : 0);
        hash += (int) clientDepartmentNumber;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientPK)) {
            return false;
        }
        ClientPK other = (ClientPK) object;
        if ((this.clientName == null && other.clientName != null) || (this.clientName != null && !this.clientName.equals(other.clientName))) {
            return false;
        }
        if (this.clientDepartmentNumber != other.clientDepartmentNumber) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "jpa.entities.ClientPK[clientName=" + clientName + ", clientDepartmentNumber=" + clientDepartmentNumber + "]";
    }

}
