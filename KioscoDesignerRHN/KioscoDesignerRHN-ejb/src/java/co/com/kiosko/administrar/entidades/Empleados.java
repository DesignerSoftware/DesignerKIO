/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
@Table(name = "EMPLEADOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Empleados.findAll", query = "SELECT e FROM Empleados e")})
public class Empleados implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CODIGOEMPLEADO")
    private BigInteger codigoempleado;
    @Column(name = "RUTATRANSPORTE")
    private Integer rutatransporte;
    @Column(name = "TELEFONO")
    private Long telefono;
    @Column(name = "EXTENSION")
    private Integer extension;
    @Size(max = 6)
    @Column(name = "PARQUEADERO")
    private String parqueadero;
    @Size(max = 1)
    @Column(name = "SERVICIORESTAURANTE")
    private String serviciorestaurante;
    @Column(name = "NIVELENDEUDAMIENTO")
    private BigDecimal nivelendeudamiento;
    @Column(name = "TOTALULTIMOPAGO")
    private BigInteger totalultimopago;
    @Column(name = "TOTALULTIMODESCUENTO")
    private BigInteger totalultimodescuento;
    @Column(name = "TOTALULTIMOSOBREGIRO")
    private BigInteger totalultimosobregiro;
    @Size(max = 1)
    @Column(name = "EXCLUIRLIQUIDACION")
    private String excluirliquidacion;
    @Column(name = "CODIGOALTERNATIVODEUDOR")
    private Integer codigoalternativodeudor;
    @Column(name = "CODIGOALTERNATIVOACREEDOR")
    private Long codigoalternativoacreedor;
    @Column(name = "FECHACREACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechacreacion;
    @Size(max = 20)
    @Column(name = "CODIGOALTERNATIVO")
    private String codigoalternativo;
    @Column(name = "TEMPTOTALINGRESOS")
    private BigInteger temptotalingresos;
    @Size(max = 1)
    @Column(name = "EXTRANJERO")
    private String extranjero;
    @Size(max = 1)
    @Column(name = "PAGASUBSIDIOTRANSPORTELEGAL")
    private String pagasubsidiotransportelegal;
    @Column(name = "TEMPBASERECALCULO")
    private BigInteger tempbaserecalculo;
    @Size(max = 30)
    @Column(name = "USUARIOBD")
    private String usuariobd;
    @JoinColumn(name = "PERSONA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Personas persona;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empresas empresa;

    public Empleados() {
    }

    public Empleados(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Empleados(BigDecimal secuencia, BigInteger codigoempleado) {
        this.secuencia = secuencia;
        this.codigoempleado = codigoempleado;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public BigInteger getCodigoempleado() {
        return codigoempleado;
    }

    public void setCodigoempleado(BigInteger codigoempleado) {
        this.codigoempleado = codigoempleado;
    }

    public Integer getRutatransporte() {
        return rutatransporte;
    }

    public void setRutatransporte(Integer rutatransporte) {
        this.rutatransporte = rutatransporte;
    }

    public Long getTelefono() {
        return telefono;
    }

    public void setTelefono(Long telefono) {
        this.telefono = telefono;
    }

    public Integer getExtension() {
        return extension;
    }

    public void setExtension(Integer extension) {
        this.extension = extension;
    }

    public String getParqueadero() {
        return parqueadero;
    }

    public void setParqueadero(String parqueadero) {
        this.parqueadero = parqueadero;
    }

    public String getServiciorestaurante() {
        return serviciorestaurante;
    }

    public void setServiciorestaurante(String serviciorestaurante) {
        this.serviciorestaurante = serviciorestaurante;
    }

    public BigDecimal getNivelendeudamiento() {
        return nivelendeudamiento;
    }

    public void setNivelendeudamiento(BigDecimal nivelendeudamiento) {
        this.nivelendeudamiento = nivelendeudamiento;
    }

    public BigInteger getTotalultimopago() {
        return totalultimopago;
    }

    public void setTotalultimopago(BigInteger totalultimopago) {
        this.totalultimopago = totalultimopago;
    }

    public BigInteger getTotalultimodescuento() {
        return totalultimodescuento;
    }

    public void setTotalultimodescuento(BigInteger totalultimodescuento) {
        this.totalultimodescuento = totalultimodescuento;
    }

    public BigInteger getTotalultimosobregiro() {
        return totalultimosobregiro;
    }

    public void setTotalultimosobregiro(BigInteger totalultimosobregiro) {
        this.totalultimosobregiro = totalultimosobregiro;
    }

    public String getExcluirliquidacion() {
        return excluirliquidacion;
    }

    public void setExcluirliquidacion(String excluirliquidacion) {
        this.excluirliquidacion = excluirliquidacion;
    }

    public Integer getCodigoalternativodeudor() {
        return codigoalternativodeudor;
    }

    public void setCodigoalternativodeudor(Integer codigoalternativodeudor) {
        this.codigoalternativodeudor = codigoalternativodeudor;
    }

    public Long getCodigoalternativoacreedor() {
        return codigoalternativoacreedor;
    }

    public void setCodigoalternativoacreedor(Long codigoalternativoacreedor) {
        this.codigoalternativoacreedor = codigoalternativoacreedor;
    }

    public Date getFechacreacion() {
        return fechacreacion;
    }

    public void setFechacreacion(Date fechacreacion) {
        this.fechacreacion = fechacreacion;
    }

    public String getCodigoalternativo() {
        return codigoalternativo;
    }

    public void setCodigoalternativo(String codigoalternativo) {
        this.codigoalternativo = codigoalternativo;
    }

    public BigInteger getTemptotalingresos() {
        return temptotalingresos;
    }

    public void setTemptotalingresos(BigInteger temptotalingresos) {
        this.temptotalingresos = temptotalingresos;
    }

    public String getExtranjero() {
        return extranjero;
    }

    public void setExtranjero(String extranjero) {
        this.extranjero = extranjero;
    }

    public String getPagasubsidiotransportelegal() {
        return pagasubsidiotransportelegal;
    }

    public void setPagasubsidiotransportelegal(String pagasubsidiotransportelegal) {
        this.pagasubsidiotransportelegal = pagasubsidiotransportelegal;
    }

    public BigInteger getTempbaserecalculo() {
        return tempbaserecalculo;
    }

    public void setTempbaserecalculo(BigInteger tempbaserecalculo) {
        this.tempbaserecalculo = tempbaserecalculo;
    }

    public String getUsuariobd() {
        return usuariobd;
    }

    public void setUsuariobd(String usuariobd) {
        this.usuariobd = usuariobd;
    }

    public Personas getPersona() {
        return persona;
    }

    public void setPersona(Personas persona) {
        this.persona = persona;
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
        if (!(object instanceof Empleados)) {
            return false;
        }
        Empleados other = (Empleados) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.kiosko.administrar.entidades.Empleados[ secuencia=" + secuencia + " ]";
    }
    
}
