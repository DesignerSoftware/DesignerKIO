package co.com.kiosko.administrar.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Felipe Triviño
 */
@Entity
@Table(name = "OPCIONESKIOSKOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OpcionesKioskos.findAll", query = "SELECT o FROM OpcionesKioskos o")})
public class OpcionesKioskos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CODIGO")
    private String codigo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "DESCRIPCION")
    private String descripcion;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Size(max = 1000)
    @Column(name = "AYUDA")
    private String ayuda;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "CLASE")
    private String clase;
    @Size(max = 20)
    @Column(name = "TIPOREPORTE")
    private String tiporeporte;
    @Size(max = 50)
    @Column(name = "NOMBREARCHIVO")
    private String nombrearchivo;
    @Size(max = 10)
    @Column(name = "EXTENSION")
    private String extension;
    @JoinColumn(name = "OPCIONKIOSKOPADRE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private OpcionesKioskos opcionkioskopadre;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empresas empresa;
    @Transient
    private List<OpcionesKioskos> opcionesHijas;

    public OpcionesKioskos() {
    }

    public OpcionesKioskos(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public OpcionesKioskos(BigDecimal secuencia, String codigo, String descripcion, String clase) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.clase = clase;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public String getAyuda() {
        return ayuda;
    }

    public void setAyuda(String ayuda) {
        this.ayuda = ayuda;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getTiporeporte() {
        return tiporeporte;
    }

    public void setTiporeporte(String tiporeporte) {
        this.tiporeporte = tiporeporte;
    }

    public String getNombrearchivo() {
        return nombrearchivo;
    }

    public void setNombrearchivo(String nombrearchivo) {
        this.nombrearchivo = nombrearchivo;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public OpcionesKioskos getOpcionkioskopadre() {
        return opcionkioskopadre;
    }

    public void setOpcionkioskopadre(OpcionesKioskos opcionkioskopadre) {
        this.opcionkioskopadre = opcionkioskopadre;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public List<OpcionesKioskos> getOpcionesHijas() {
        return opcionesHijas;
    }

    public void setOpcionesHijas(List<OpcionesKioskos> opcionesHijas) {
        this.opcionesHijas = opcionesHijas;
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
        if (!(object instanceof OpcionesKioskos)) {
            return false;
        }
        OpcionesKioskos other = (OpcionesKioskos) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.kiosko.administrar.entidades.OpcionesKioskos[ secuencia=" + secuencia + " ]";
    }
}
