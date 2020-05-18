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
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 *
 * @author Edwin Hastamorir
 */
@Entity
public class KioLocalizacionesEmpl implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @JoinColumn(name = "KIOLOCALIZA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private KioLocalizaciones kioLocaliza;
    @JoinColumn(name = "KIOSOLICILOCALIZA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private KioSolicisLocaliza kioSolicisLocaliza;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "FECHA")
    private Date fecha;
    @Min(0)
    @Max(100)
    @Digits(integer = 3, fraction = 2)
    @Column(name = "PORCENTAJE")
    private BigDecimal porcentaje;

    public KioLocalizacionesEmpl() {
        this.secuencia = BigDecimal.ONE;
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

    public KioLocalizaciones getKioLocaliza() {
        return kioLocaliza;
    }

    public void setKioLocaliza(KioLocalizaciones kioLocaliza) {
        this.kioLocaliza = kioLocaliza;
    }

    public KioSolicisLocaliza getKioSolicisLocaliza() {
        return kioSolicisLocaliza;
    }

    public void setKioSolicisLocaliza(KioSolicisLocaliza kioSolicisLocaliza) {
        this.kioSolicisLocaliza = kioSolicisLocaliza;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
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
        if (!(object instanceof KioLocalizacionesEmpl)) {
            return false;
        }
        KioLocalizacionesEmpl other = (KioLocalizacionesEmpl) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "KioLocalizacionesEmpl{" + "secuencia=" + secuencia + ", empleado=" + empleado + ", kioLocaliza=" + kioLocaliza + ", kioSolicisLocaliza=" + kioSolicisLocaliza + ", fecha=" + fecha + ", porcentaje=" + porcentaje + '}';
    }

}
