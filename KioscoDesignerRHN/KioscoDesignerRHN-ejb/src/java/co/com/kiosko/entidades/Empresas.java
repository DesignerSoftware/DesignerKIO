package co.com.kiosko.entidades;

import java.io.Serializable;
//import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felipe Triviño
 */
@Entity
@Table(name = "EMPRESAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empresas.findAll", query = "SELECT e FROM Empresas e")})
public class Empresas implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigInteger secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGO")
    private short codigo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NIT")
    private long nit;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRE")
    private String nombre;
    @Lob
    @Column(name = "REGLAMENTO")
    private String reglamento;
    @Lob
    @Column(name = "MANUALADMINISTRATIVO")
    private String manualadministrativo;
    @Size(max = 50)
    @Column(name = "CODIGOALTERNATIVO")
    private String codigoalternativo;
    @Size(max = 50)
    @Column(name = "LOGO")
    private String logo;

    public Empresas() {
    }

    public Empresas(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public Empresas(BigInteger secuencia, short codigo, long nit, String nombre) {
        this.secuencia = secuencia;
        this.codigo = codigo;
        this.nit = nit;
        this.nombre = nombre;
    }

    public BigInteger getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigInteger secuencia) {
        this.secuencia = secuencia;
    }

    public short getCodigo() {
        return codigo;
    }

    public void setCodigo(short codigo) {
        this.codigo = codigo;
    }

    public long getNit() {
        return nit;
    }

    public void setNit(long nit) {
        this.nit = nit;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getReglamento() {
        return reglamento;
    }

    public void setReglamento(String reglamento) {
        this.reglamento = reglamento;
    }

    public String getManualadministrativo() {
        return manualadministrativo;
    }

    public void setManualadministrativo(String manualadministrativo) {
        this.manualadministrativo = manualadministrativo;
    }

    public String getCodigoalternativo() {
        return codigoalternativo;
    }

    public void setCodigoalternativo(String codigoalternativo) {
        this.codigoalternativo = codigoalternativo;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
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
        if (!(object instanceof Empresas)) {
            return false;
        }
        Empresas other = (Empresas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.kiosko.administrar.entidades.Empresas[ secuencia=" + secuencia + " ]";
    }
}
