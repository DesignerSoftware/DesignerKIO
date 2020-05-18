package co.com.kiosko.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Edwin Hastamorir
 */
@Entity
@Table(name = "KIOVIGLOCALIZACIONES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KioVigLocalizaciones.findAll", query = "SELECT k FROM KioVigLocalizaciones k")})
public class KioVigLocalizaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="SECUENCIA")
    private BigDecimal secuencia;
    @Column(name="CODIGO")
    private BigDecimal codigo;
    @Temporal(TemporalType.DATE)
    @Column(name="FECHAVIGENCIA")
    private Date fechaVigencia;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public BigDecimal getCodigo() {
        return codigo;
    }

    public void setCodigo(BigDecimal codigo) {
        this.codigo = codigo;
    }

    public Date getFechaVigencia() {
        return fechaVigencia;
    }

    public void setFechaVigencia(Date fechaVigencia) {
        this.fechaVigencia = fechaVigencia;
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
        if (!(object instanceof KioVigLocalizaciones)) {
            return false;
        }
        KioVigLocalizaciones other = (KioVigLocalizaciones) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.kiosko.entidades.KioVigLocalizaciones[ secuencia=" + secuencia + " ]";
    }
    
}
