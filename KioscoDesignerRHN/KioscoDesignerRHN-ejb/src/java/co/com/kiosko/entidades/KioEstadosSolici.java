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
import javax.persistence.OneToOne;
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
@Table(name = "KIOESTADOSSOLICI")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "KioEstadosSolici.findAll", query = "SELECT k FROM KioEstadosSolici k")})
public class KioEstadosSolici implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="SECUENCIA")
    private BigDecimal secuencia;
    @JoinColumn(name = "KIOSOLICIVACA", referencedColumnName = "SECUENCIA")
//    @OneToOne(optional = false, cascade = CascadeType.PERSIST)
    @OneToOne(optional = false)
    private KioSoliciVacas kioSoliciVaca;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHAPROCESAMIENTO")
    private Date fechaProcesamiento;
    @Column(name="ESTADO")
    @Size(max=10)
    private String estado;
    @JoinColumn(name = "EMPLEADOEJECUTA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Empleados empleadoEjecuta;
    @Column(name="NOVEDADSISTEMA")
    private BigDecimal novedadSistema;
    @Column(name="MOTIVOPROCESA")
    private String motivoProcesa;
    @JoinColumn(name = "PERSONAEJECUTA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Personas personaEjecuta;
    
    public KioEstadosSolici() {
        inicializa();
    }

    public KioEstadosSolici(KioSoliciVacas kioSoliciVaca) {
        this.kioSoliciVaca = kioSoliciVaca;
        inicializa();
    }
    
    private void inicializa(){
        secuencia = BigDecimal.ZERO;
        fechaProcesamiento = new Date();
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public KioSoliciVacas getKioSoliciVaca() {
        return kioSoliciVaca;
    }

    public void setKioSoliciVaca(KioSoliciVacas kioSoliciVaca) {
        this.kioSoliciVaca = kioSoliciVaca;
    }

    public Date getFechaProcesamiento() {
        return fechaProcesamiento;
    }

    public void setFechaProcesamiento(Date fechaProcesamiento) {
        this.fechaProcesamiento = fechaProcesamiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Empleados getEmpleadoEjecuta() {
        return empleadoEjecuta;
    }

    public void setEmpleadoEjecuta(Empleados empleadoEjecuta) {
        this.empleadoEjecuta = empleadoEjecuta;
    }

    public BigDecimal getNovedadSistema() {
        return novedadSistema;
    }

    public void setNovedadSistema(BigDecimal novedadSistema) {
        this.novedadSistema = novedadSistema;
    }

    public String getMotivoProcesa() {
        return motivoProcesa;
    }

    public void setMotivoProcesa(String motivoProcesa) {
        this.motivoProcesa = motivoProcesa;
    }

    public Personas getPersonaEjecuta() {
        return personaEjecuta;
    }

    public void setPersonaEjecuta(Personas personaEjecuta) {
        this.personaEjecuta = personaEjecuta;
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
        if (!(object instanceof KioEstadosSolici)) {
            return false;
        }
        KioEstadosSolici other = (KioEstadosSolici) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "KioEstadosSolici{" + "secuencia=" + secuencia + ", kioSoliciVaca=" + kioSoliciVaca + ", fechaProcesamiento=" + fechaProcesamiento + ", estado=" + estado + ", empleadoEjecuta=" + empleadoEjecuta + ", novedadSistema=" + novedadSistema + '}';
    }

    
    
}
