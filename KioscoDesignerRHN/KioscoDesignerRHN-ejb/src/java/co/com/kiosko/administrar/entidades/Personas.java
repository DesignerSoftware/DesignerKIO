package co.com.kiosko.administrar.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felipe Triviño
 */
@Entity
@Table(name = "PERSONAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Personas.findAll", query = "SELECT p FROM Personas p")})
public class Personas implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Size(max = 1)
    @Column(name = "FACTORRH")
    private String factorrh;
    @Column(name = "FECHANACIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechanacimiento;
    @Column(name = "FECHAVENCIMIENTOCERTIFICADO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechavencimientocertificado;
    @Column(name = "FECHAFALLECIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechafallecimiento;
    @Size(max = 2)
    @Column(name = "GRUPOSANGUINEO")
    private String gruposanguineo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "NOMBRE")
    private String nombre;
    @Basic(optional = false)
    @NotNull
    @Column(name = "NUMERODOCUMENTO")
    private BigInteger numerodocumento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "PRIMERAPELLIDO")
    private String primerapellido;
    @Size(max = 20)
    @Column(name = "SEGUNDOAPELLIDO")
    private String segundoapellido;
    @Size(max = 1)
    @Column(name = "SEXO")
    private String sexo;
    @Size(max = 1)
    @Column(name = "VIVIENDAPROPIA")
    private String viviendapropia;
    @Column(name = "CLASELIBRETAMILITAR")
    private Short claselibretamilitar;
    @Column(name = "NUMEROLIBRETAMILITAR")
    private Long numerolibretamilitar;
    @Column(name = "DISTRITOMILITAR")
    private Short distritomilitar;
    @Size(max = 15)
    @Column(name = "CERTIFICADOJUDICIAL")
    private String certificadojudicial;
    @Size(max = 100)
    @Column(name = "PATHFOTO")
    private String pathfoto;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 10)
    @Column(name = "PLACAVEHICULO")
    private String placavehiculo;
    @Size(max = 20)
    @Column(name = "NUMEROMATRICULAPROF")
    private String numeromatriculaprof;
    @Column(name = "FECHAEXPMATRICULA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaexpmatricula;
    @Column(name = "DIGITOVERIFICACIONDOCUMENTO")
    private Short digitoverificaciondocumento;
    @Size(max = 50)
    @Column(name = "OBSERVACION")
    private String observacion;
    @Size(max = 1)
    @Column(name = "VEHICULOEMPRESA")
    private String vehiculoempresa;
    @Size(max = 30)
    @Column(name = "SEGUNDONOMBRE")
    private String segundonombre;
    @Transient
    private String nombreCompleto;

    public Personas() {
    }

    public Personas(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Personas(BigDecimal secuencia, String nombre, BigInteger numerodocumento, String primerapellido) {
        this.secuencia = secuencia;
        this.nombre = nombre;
        this.numerodocumento = numerodocumento;
        this.primerapellido = primerapellido;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public String getFactorrh() {
        return factorrh;
    }

    public void setFactorrh(String factorrh) {
        this.factorrh = factorrh;
    }

    public Date getFechanacimiento() {
        return fechanacimiento;
    }

    public void setFechanacimiento(Date fechanacimiento) {
        this.fechanacimiento = fechanacimiento;
    }

    public Date getFechavencimientocertificado() {
        return fechavencimientocertificado;
    }

    public void setFechavencimientocertificado(Date fechavencimientocertificado) {
        this.fechavencimientocertificado = fechavencimientocertificado;
    }

    public Date getFechafallecimiento() {
        return fechafallecimiento;
    }

    public void setFechafallecimiento(Date fechafallecimiento) {
        this.fechafallecimiento = fechafallecimiento;
    }

    public String getGruposanguineo() {
        return gruposanguineo;
    }

    public void setGruposanguineo(String gruposanguineo) {
        this.gruposanguineo = gruposanguineo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigInteger getNumerodocumento() {
        return numerodocumento;
    }

    public void setNumerodocumento(BigInteger numerodocumento) {
        this.numerodocumento = numerodocumento;
    }

    public String getPrimerapellido() {
        return primerapellido;
    }

    public void setPrimerapellido(String primerapellido) {
        this.primerapellido = primerapellido;
    }

    public String getSegundoapellido() {
        return segundoapellido;
    }

    public void setSegundoapellido(String segundoapellido) {
        this.segundoapellido = segundoapellido;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getViviendapropia() {
        return viviendapropia;
    }

    public void setViviendapropia(String viviendapropia) {
        this.viviendapropia = viviendapropia;
    }

    public Short getClaselibretamilitar() {
        return claselibretamilitar;
    }

    public void setClaselibretamilitar(Short claselibretamilitar) {
        this.claselibretamilitar = claselibretamilitar;
    }

    public Long getNumerolibretamilitar() {
        return numerolibretamilitar;
    }

    public void setNumerolibretamilitar(Long numerolibretamilitar) {
        this.numerolibretamilitar = numerolibretamilitar;
    }

    public Short getDistritomilitar() {
        return distritomilitar;
    }

    public void setDistritomilitar(Short distritomilitar) {
        this.distritomilitar = distritomilitar;
    }

    public String getCertificadojudicial() {
        return certificadojudicial;
    }

    public void setCertificadojudicial(String certificadojudicial) {
        this.certificadojudicial = certificadojudicial;
    }

    public String getPathfoto() {
        return pathfoto;
    }

    public void setPathfoto(String pathfoto) {
        this.pathfoto = pathfoto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPlacavehiculo() {
        return placavehiculo;
    }

    public void setPlacavehiculo(String placavehiculo) {
        this.placavehiculo = placavehiculo;
    }

    public String getNumeromatriculaprof() {
        return numeromatriculaprof;
    }

    public void setNumeromatriculaprof(String numeromatriculaprof) {
        this.numeromatriculaprof = numeromatriculaprof;
    }

    public Date getFechaexpmatricula() {
        return fechaexpmatricula;
    }

    public void setFechaexpmatricula(Date fechaexpmatricula) {
        this.fechaexpmatricula = fechaexpmatricula;
    }

    public Short getDigitoverificaciondocumento() {
        return digitoverificaciondocumento;
    }

    public void setDigitoverificaciondocumento(Short digitoverificaciondocumento) {
        this.digitoverificaciondocumento = digitoverificaciondocumento;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getVehiculoempresa() {
        return vehiculoempresa;
    }

    public void setVehiculoempresa(String vehiculoempresa) {
        this.vehiculoempresa = vehiculoempresa;
    }

    public String getSegundonombre() {
        return segundonombre;
    }

    public void setSegundonombre(String segundonombre) {
        this.segundonombre = segundonombre;
    }

    public String getNombreCompleto() {
        if (nombreCompleto == null) {
            nombreCompleto = getPrimerapellido() + " " + getSegundoapellido() + " " + getNombre();
            if (nombreCompleto.equals("  ")) {
                nombreCompleto = null;
            }
            return nombreCompleto;
        } else {
            return nombreCompleto;
        }
    }

    public void setNombreCompleto(String nombreCompleto) {
        if (nombreCompleto != null) {
            this.nombreCompleto = nombreCompleto.toUpperCase();
        } else {
            this.nombreCompleto = nombreCompleto;
        }
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
        if (!(object instanceof Personas)) {
            return false;
        }
        Personas other = (Personas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.kiosko.administrar.entidades.Personas[ secuencia=" + secuencia + " ]";
    }
}
