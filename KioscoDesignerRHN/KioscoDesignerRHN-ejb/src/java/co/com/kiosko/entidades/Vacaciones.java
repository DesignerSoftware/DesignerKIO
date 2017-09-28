package co.com.kiosko.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
//import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityResult;
import javax.persistence.FieldResult;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Temporal;

/**
 *
 * @author Edwin
 */
@Entity
@SqlResultSetMapping(
    name="ConsultasVacaciones",
    entities={
        @EntityResult(
            entityClass=Vacaciones.class,
            fields={
                @FieldResult(name="secuencia", column="SECUENCIA"),
                @FieldResult(name="inicialcausacion", column="INICIALCAUSACION"),
                @FieldResult(name="finalcausacion", column="FINALCAUSACION"),
                @FieldResult(name="diaspendientes", column="DIASPENDIENTES"),
                @FieldResult(name="diaspenientesprecierre", column="DIASPENDIENTESPRECIERRE"),
                @FieldResult(name="empleado", column="EMPLEADO")
            }
        )
    }
)
public class Vacaciones implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private BigDecimal secuencia;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date inicialCausacion;
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date finalCausacion;
    private BigDecimal diasPendientes;
    private BigDecimal diasPendientesPreCierre;
    private BigDecimal empleado;
    

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Date getInicialCausacion() {
        return inicialCausacion;
    }

    public void setInicialCausacion(Date inicialCausacion) {
        this.inicialCausacion = inicialCausacion;
    }

    public Date getFinalCausacion() {
        return finalCausacion;
    }

    public void setFinalCausacion(Date finalCausacion) {
        this.finalCausacion = finalCausacion;
    }

    public BigDecimal getDiasPendientes() {
        return diasPendientes;
    }

    public void setDiasPendientes(BigDecimal diasPendientes) {
        this.diasPendientes = diasPendientes;
    }

    public BigDecimal getDiasPendientesPreCierre() {
        return diasPendientesPreCierre;
    }

    public void setDiasPendientesPreCierre(BigDecimal diasPendientesPreCierre) {
        this.diasPendientesPreCierre = diasPendientesPreCierre;
    }

    public BigDecimal getEmpleado() {
        return empleado;
    }

    public void setEmpleado(BigDecimal empleado) {
        this.empleado = empleado;
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
        if (!(object instanceof Vacaciones)) {
            return false;
        }
        Vacaciones other = (Vacaciones) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.kiosko.entidades.Vacaciones[ secuencia=" + secuencia + " ]";
    }
    
}
