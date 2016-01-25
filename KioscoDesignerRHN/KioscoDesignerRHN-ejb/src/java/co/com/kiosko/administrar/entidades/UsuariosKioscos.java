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
@Table(name = "USUARIOSKIOSCOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsuariosKioscos.findAll", query = "SELECT u FROM UsuariosKioscos u")})
public class UsuariosKioscos implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @JoinColumn(name = "KIOSKO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Kioscos kiosko;

    public UsuariosKioscos() {
    }

    public UsuariosKioscos(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Kioscos getKiosko() {
        return kiosko;
    }

    public void setKiosko(Kioscos kiosko) {
        this.kiosko = kiosko;
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
        if (!(object instanceof UsuariosKioscos)) {
            return false;
        }
        UsuariosKioscos other = (UsuariosKioscos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.kiosko.administrar.entidades.UsuariosKioscos[ secuencia=" + secuencia + " ]";
    }
    
}
