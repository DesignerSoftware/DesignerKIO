/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.administrar.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felipe Triviño
 */
@Entity
@Table(name = "EMPRESASOPCIONESKIOSKOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EmpresasOpcionesKioskos.findAll", query = "SELECT e FROM EmpresasOpcionesKioskos e")})
public class EmpresasOpcionesKioskos implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @JoinColumn(name = "OPCIONKIOSKO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private OpcionesKioskos opcionkiosko;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empresas empresa;

    public EmpresasOpcionesKioskos() {
    }

    public EmpresasOpcionesKioskos(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public OpcionesKioskos getOpcionkiosko() {
        return opcionkiosko;
    }

    public void setOpcionkiosko(OpcionesKioskos opcionkiosko) {
        this.opcionkiosko = opcionkiosko;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmpresasOpcionesKioskos)) {
            return false;
        }
        EmpresasOpcionesKioskos other = (EmpresasOpcionesKioskos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.kiosko.administrar.entidades.EmpresasOpcionesKioskos[ secuencia=" + secuencia + " ]";
    }
    
}
