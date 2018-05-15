package co.com.kiosko.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
//import javax.persistence.CascadeType;
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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Edwin
 */
@Entity
@Table(name = "KIOSOLICIVACAS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KioSoliciVacas.findAll", query = "SELECT k FROM KioSoliciVacas k")})
public class KioSoliciVacas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;   
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @JoinColumn(name = "KIONOVEDADSOLICI", referencedColumnName = "SECUENCIA")
//    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    @OneToOne(optional = false)
    private KioNovedadesSolici kioNovedadSolici;
    @Column(name = "FECHAGENERACION")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaGeneracion;
    @Column(name = "FECHAVENCIMIENTO")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimiento;
//    @Size(max = 30)
//    @Column(name = "USUARIO")
//    private String usuario;
    @JoinColumn(name = "EMPLEADOJEFE", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empleados empleadoJefe;
    @JoinColumn(name = "AUTORIZADOR", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Personas autorizador;
    
    @Size(max = 1)
    @Column(name = "ACTIVA")
    private String activa;

    public KioSoliciVacas() {
        inicializa();
    }

    private void inicializa() {
        secuencia = BigDecimal.ZERO;
        fechaGeneracion = new Date();
        kioNovedadSolici = new KioNovedadesSolici();
        activa = "S";
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

    public KioNovedadesSolici getKioNovedadesSolici() {
        return kioNovedadSolici;
    }

    public void setKioNovedadesSolici(KioNovedadesSolici kioNovedadesSolici) {
        this.kioNovedadSolici = kioNovedadesSolici;
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

//    public String getUsuario() {
//        return usuario;
//    }
//
//    public void setUsuario(String usuario) {
//        this.usuario = usuario;
//    }

    public Empleados getEmpleadoJefe() {
        return empleadoJefe;
    }

    public void setEmpleadoJefe(Empleados empleadoJefe) {
        this.empleadoJefe = empleadoJefe;
    }

    public Personas getAutorizador() {
        return autorizador;
    }

    public void setAutorizador(Personas autorizador) {
        this.autorizador = autorizador;
    }
    
    public String getActiva() {
        return activa;
    }

    public void setActiva(String activa) {
        this.activa = activa;
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
        if (!(object instanceof KioSoliciVacas)) {
            return false;
        }
        KioSoliciVacas other = (KioSoliciVacas) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
//        return "KioSoliciVacas{" + "secuencia=" + secuencia + ", empleado=" + empleado + ", kioNovedadSolici=" + kioNovedadSolici + ", fechaGeneracion=" + fechaGeneracion + ", fechaVencimiento=" + fechaVencimiento + ", usuario=" + usuario + ", empleadoJefe=" + empleadoJefe + ", activa=" + activa + '}';
        return "KioSoliciVacas{" + "secuencia=" + secuencia + ", empleado=" + empleado + ", kioNovedadSolici=" + kioNovedadSolici + ", fechaGeneracion=" + fechaGeneracion + ", fechaVencimiento=" + fechaVencimiento + ", empleadoJefe=" + empleadoJefe + ", activa=" + activa + '}';
    }

}
