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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Edwin Hastamorir
 */
@Entity
public class KioSolicisLocaliza implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "FECHAGENERACION")
    private Date fechaGeneracion;
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHAVENCIMIENTO")
    private Date fechaVencimiento;
    @Column(name = "USUARIO")
    private String usuario;
    @Column(name = "ACTIVA")
    private String activa;
    @JoinColumn(name = "EMPLEADOJEFE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empleados empleadoJefe;
    @JoinColumn(name = "KIOAUTORIZADOR", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Personas kioAutorizador;

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

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getActiva() {
        return activa;
    }

    public void setActiva(String activa) {
        this.activa = activa;
    }

    public Empleados getEmpleadoJefe() {
        return empleadoJefe;
    }

    public void setEmpleadoJefe(Empleados empleadoJefe) {
        this.empleadoJefe = empleadoJefe;
    }

    public Personas getKioAutorizador() {
        return kioAutorizador;
    }

    public void setKioAutorizador(Personas kioAutorizador) {
        this.kioAutorizador = kioAutorizador;
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
        if (!(object instanceof KioSolicisLocaliza)) {
            return false;
        }
        KioSolicisLocaliza other = (KioSolicisLocaliza) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "KioSolicisLocaliza{" + "secuencia=" + secuencia + ", empleado=" + empleado + ", fechaGeneracion=" + fechaGeneracion + ", fechaVencimiento=" + fechaVencimiento + ", usuario=" + usuario + ", activa=" + activa + ", empleadoJefe=" + empleadoJefe + ", kioAutorizador=" + kioAutorizador + '}';
    }

}
