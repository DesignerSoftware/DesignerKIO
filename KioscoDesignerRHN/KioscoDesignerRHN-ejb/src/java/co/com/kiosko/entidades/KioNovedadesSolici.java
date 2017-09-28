package co.com.kiosko.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import javax.persistence.OneToOne;
//import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
//import javax.persistence.Transient;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Edwin
 */
@Entity
@Table(name = "KIONOVEDADESSOLICI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KioNovedadesSolici.findAll", query = "SELECT k FROM KioNovedadesSolici k")})
public class KioNovedadesSolici implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @OneToOne(optional = false)
    private Empleados empleado;
    @Column(name = "FECHAINICIALDISFRUTE")
    @Temporal(TemporalType.DATE)
    private Date fechaInicialDisfrute;
    @Column(name = "DIAS")
    private BigInteger dias;
    @Size(max = 30)
    @Column(name = "TIPO")
    private String tipo;
    @Size(max = 30)
    @Column(name = "SUBTIPO")
    private String subtipo;
    @JoinColumn(name = "VACACION", referencedColumnName = "RFVACACION")
    @ManyToOne(optional = false)
    private VwVacaPendientesEmpleados vacacion;
    @Column(name = "FECHASIGUIENTEFINVACA")
    @Temporal(TemporalType.DATE)
    private Date fechaSiguienteFinVaca;
    @Column(name = "ADELANTAPAGOHASTA")
    @Temporal(TemporalType.DATE)
    private Date adelantaPagoHasta;
    @Column(name = "FECHASISTEMA")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaSistema;

    public KioNovedadesSolici() {
        inicializa();
    }
    
    private void inicializa(){
        this.secuencia = BigDecimal.ZERO;
        this.tipo="VACACION";
        this.fechaSistema = new Date();
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
    }

    public Date getFechaInicialDisfrute() {
        return fechaInicialDisfrute;
    }

    public void setFechaInicialDisfrute(Date fechaInicialDisfrute) {
        this.fechaInicialDisfrute = fechaInicialDisfrute;
    }

    public BigInteger getDias() {
        return dias;
    }

    public void setDias(BigInteger dias) {
        this.dias = dias;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getSubtipo() {
        return subtipo;
    }

    public void setSubtipo(String subtipo) {
        this.subtipo = subtipo;
    }

    public VwVacaPendientesEmpleados getVacacion() {
        return vacacion;
    }

    public void setVacacion(VwVacaPendientesEmpleados vacacion) {
        this.vacacion = vacacion;
    }

    public Date getFechaSistema() {
        return fechaSistema;
    }

    public void setFechaSistema(Date fechaSistema) {
        this.fechaSistema = fechaSistema;
    }

//    public String getUsuario() {
//        return usuario;
//    }

//    public void setUsuario(String usuario) {
//        this.usuario = usuario;
//    }

    public Date getFechaSiguienteFinVaca() {
        return fechaSiguienteFinVaca;
    }

    public void setFechaSiguienteFinVaca(Date fechaSiguienteFinVaca) {
        this.fechaSiguienteFinVaca = fechaSiguienteFinVaca;
    }

//    public String getEstado() {
//        return estado;
//    }

//    public void setEstado(String estado) {
//        this.estado = estado;
//    }

//    public String getAdelantaPago() {
//        return adelantaPago;
//    }

//    public void setAdelantaPago(String adelantaPago) {
//        this.adelantaPago = adelantaPago;
//    }

//    public BigDecimal getProporcionAdelantaPago() {
//        return proporcionAdelantaPago;
//    }

//    public void setProporcionAdelantaPago(BigDecimal proporcionAdelantaPago) {
//        this.proporcionAdelantaPago = proporcionAdelantaPago;
//    }

    public Date getAdelantaPagoHasta() {
        return adelantaPagoHasta;
    }

    public void setAdelantaPagoHasta(Date adelantaPagoHasta) {
        this.adelantaPagoHasta = adelantaPagoHasta;
    }

//    public String getPagarPorFuera() {
//        return pagarPorFuera;
//    }

//    public void setPagarPorFuera(String pagarPorFuera) {
//        this.pagarPorFuera = pagarPorFuera;
//    }

//    public Date getFechaInicioNomina() {
//        return fechaInicioNomina;
//    }

//    public void setFechaInicioNomina(Date fechaInicioNomina) {
//        this.fechaInicioNomina = fechaInicioNomina;
//    }

//    public Date getFechaPago() {
//        return fechaPago;
//    }
//
//    public void setFechaPago(Date fechaPago) {
//        this.fechaPago = fechaPago;
//    }

//    public String getPagado() {
//        return pagado;
//    }

//    public void setPagado(String pagado) {
//        this.pagado = pagado;
//    }

//    public BigDecimal getVacaDiasAplazados() {
//        return vacaDiasAplazados;
//    }

//    public void setVacaDiasAplazados(BigDecimal vacaDiasAplazados) {
//        this.vacaDiasAplazados = vacaDiasAplazados;
//    }

//    public BigDecimal getVacaDiasAplazadosDisfrutados() {
//        return vacaDiasAplazadosDisfrutados;
//    }

//    public void setVacaDiasAplazadosDisfrutados(BigDecimal vacaDiasAplazadosDisfrutados) {
//        this.vacaDiasAplazadosDisfrutados = vacaDiasAplazadosDisfrutados;
//    }

//    public String getPeriodo() {
//        return periodo;
//    }
//
//    public Double getDiasDisponibles() {
//        return diasDisponibles;
//    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (secuencia != null ? secuencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof KioNovedadesSolici)) {
            return false;
        }
        KioNovedadesSolici other = (KioNovedadesSolici) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "KioNovedadesSolici{" + "secuencia=" + secuencia + ", empleado=" + empleado + ", fechaInicialDisfrute=" + fechaInicialDisfrute + ", dias=" + dias + ", tipo=" + tipo + ", subtipo=" + subtipo + ", vacacion=" + vacacion + ", fechaSiguienteFinVaca=" + fechaSiguienteFinVaca + ", adelantaPagoHasta=" + adelantaPagoHasta + ", fechaSistema=" + fechaSistema + '}';
    }
    
}
