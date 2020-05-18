package co.com.kiosko.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
//import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "KIOLOCALIZACIONES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KioLocalizaciones.findAll", query = "SELECT k FROM KioLocalizaciones k")})
public class KioLocalizaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="SECUENCIA")
    private BigDecimal secuencia;
//    @Column(name="KIOVIGLOCALIZACION")
    @JoinColumn(name = "KIOVIGLOCALIZACION", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private KioVigLocalizaciones kioVigLocalizacion;
    @Column(name="CODIGO")
    private BigDecimal codigo;
    @Column(name="NOMBRE")
//    @Size(max=30)
    private String nombre;

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public KioVigLocalizaciones getKioVigLocalizacion() {
        return kioVigLocalizacion;
    }

    public void setKioVigLocalizacion(KioVigLocalizaciones kioVigLocalizacion) {
        this.kioVigLocalizacion = kioVigLocalizacion;
    }

    public BigDecimal getCodigo() {
        return codigo;
    }

    public void setCodigo(BigDecimal codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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
        if (!(object instanceof KioLocalizaciones)) {
            return false;
        }
        KioLocalizaciones other = (KioLocalizaciones) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "KioLocalizaciones{" + "secuencia=" + secuencia + ", kioviglocalizacion=" + kioVigLocalizacion + ", codigo=" + codigo + ", nombre=" + nombre + '}';
    }

}
