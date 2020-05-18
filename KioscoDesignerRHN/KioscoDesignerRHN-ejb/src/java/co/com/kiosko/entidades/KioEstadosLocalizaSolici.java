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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "KIOESTADOSLOCALIZASOLICI")
public class KioEstadosLocalizaSolici implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="SECUENCIA")
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigDecimal secuencia;
    @JoinColumn(name = "KIOSOLICILOCALIZA", referencedColumnName = "SECUENCIA")
    @OneToOne(optional = false)
    private KioSolicisLocaliza kioSoliciLocaliza;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="FECHAPROCESAMIENTO")
    private Date fechaProcesamiento;
    @Column(name="ESTADO")
    @Size(max=15)
    private String estado;
    @Column(name="MOTIVOPROCESA")
    @Size(max=3000)
    private String motivoProcesa;
    @JoinColumn(name = "PERSONAEJECUTA", referencedColumnName = "SECUENCIA")
    @ManyToOne
    private Personas personaEjecuta;
    @Column(name="USUARIOBD")
    private String usuarioBD;
    
    public KioEstadosLocalizaSolici() {
        inicializa();
    }

    public KioEstadosLocalizaSolici(KioSolicisLocaliza kioSolicisLocaliza) {
        this.kioSoliciLocaliza = kioSolicisLocaliza;
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

    public KioSolicisLocaliza getKioSoliciLocaliza() {
        return kioSoliciLocaliza;
    }

    public void setKioSoliciLocaliza(KioSolicisLocaliza kioSoliciLocaliza) {
        this.kioSoliciLocaliza = kioSoliciLocaliza;
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

    public String getUsuarioBD() {
        return usuarioBD;
    }

    public void setUsuarioBD(String usuarioBD) {
        this.usuarioBD = usuarioBD;
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
        if (!(object instanceof KioEstadosLocalizaSolici)) {
            return false;
        }
        KioEstadosLocalizaSolici other = (KioEstadosLocalizaSolici) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "KioEstadosLocalizaSolici{" + "secuencia=" + secuencia + ", kioSoliciLocaliza=" + kioSoliciLocaliza + ", fechaProcesamiento=" + fechaProcesamiento + ", estado=" + estado + ", motivoProcesa=" + motivoProcesa + ", personaEjecuta=" + personaEjecuta + ", usuarioBD=" + usuarioBD + '}';
    }
    
}
