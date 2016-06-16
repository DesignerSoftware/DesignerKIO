package co.com.kiosko.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felipe Triviño
 */
@Entity
@Table(name = "PARAMETRIZACLAVE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ParametrizaClave.findAll", query = "SELECT p FROM ParametrizaClave p")})
public class ParametrizaClave implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(name = "FORMATO")
    private String formato;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "MENSAJEVALIDACION")
    private String mensajevalidacion;

    public ParametrizaClave() {
    }

    public ParametrizaClave(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public ParametrizaClave(BigDecimal secuencia, String formato, String mensajevalidacion) {
        this.secuencia = secuencia;
        this.formato = formato;
        this.mensajevalidacion = mensajevalidacion;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public String getMensajevalidacion() {
        return mensajevalidacion;
    }

    public void setMensajevalidacion(String mensajevalidacion) {
        this.mensajevalidacion = mensajevalidacion;
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
        if (!(object instanceof ParametrizaClave)) {
            return false;
        }
        ParametrizaClave other = (ParametrizaClave) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.kiosko.administrar.entidades.ParametrizaClave[ secuencia=" + secuencia + " ]";
    }
    
}
