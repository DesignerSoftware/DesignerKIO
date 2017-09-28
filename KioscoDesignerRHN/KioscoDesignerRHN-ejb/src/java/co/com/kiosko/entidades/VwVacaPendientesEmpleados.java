package co.com.kiosko.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Edwin
 */
@Entity
@Table(name = "VWVACAPENDIENTESEMPLEADOS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VwVacaPendientesEmpleados.findAll", query = "SELECT vw FROM VwVacaPendientesEmpleados vw ")})
public class VwVacaPendientesEmpleados implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "RFVACACION")
    private BigDecimal rfVacacion;
    @JoinColumn(name = "EMPLEADO", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empleados empleado;
    @Column(name = "INICIALCAUSACION")
    @Temporal(TemporalType.DATE)
    @Basic(optional = false)
    @NotNull
    private Date inicialCausacion;
    @Column(name = "FINALCAUSACION")
    @Temporal(TemporalType.DATE)
    @Basic(optional = false)
    @NotNull
    private Date finalCausacion;
    @Column(name = "DIASPENDIENTES")
    @Basic(optional = false)
    @NotNull
    private BigDecimal diasPendientes;
//    @Column(name = "ESTADO")
//    private String estado;
//    @Column(name = "RFNOVEDAD")
//    private BigDecimal rfNovedad;
//    @Column(name = "CONCEPTO")
//    private BigDecimal concepto;
//    @Column(name = "FECHAINICIAL")
//    @Temporal(TemporalType.DATE)
//    private Date fechaInicial;
//    @Column(name = "FECHAFINAL")
//    @Temporal(TemporalType.DATE)
//    private Date fechaFinal;
//    @Column(name = "FECHAREPORTE")
//    @Temporal(TemporalType.TIMESTAMP)
//    private Date fechaReporte;
//    @Column(name = "FORMULA")
//    private BigDecimal formula;
//    @Column(name = "TERMINAL")
//    private String terminal;
//    @Column(name = "TIPO")
//    private String tipo;
//    @Column(name = "USUARIOREPORTA")
//    private BigDecimal usuarioReporta;
//    @Column(name = "VALORTOTAL")
//    private BigDecimal valorTotal;
//    @Column(name = "SECUENCIA")
//    private BigDecimal secuencia;
    @Transient
    private String periodoCausado;

    public BigDecimal getRfVacacion() {
        return rfVacacion;
    }

    public void setRfVacacion(BigDecimal rfVacacion) {
        this.rfVacacion = rfVacacion;
    }

    public Empleados getEmpleado() {
        return empleado;
    }

    public void setEmpleado(Empleados empleado) {
        this.empleado = empleado;
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

    public String getPeriodoCausado() {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        periodoCausado = formatoFecha.format(inicialCausacion)+" a "+formatoFecha.format(finalCausacion);
        return periodoCausado;
    }

    public void setPeriodoCausado(String periodoCausado) {
        System.out.println(this.getClass().getName()+".setPeriodoCausado()");
        System.out.println("No disponible.");
    }
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (rfVacacion != null ? rfVacacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof VwVacaPendientesEmpleados)) {
            return false;
        }
        VwVacaPendientesEmpleados other = (VwVacaPendientesEmpleados) object;
        if ((this.rfVacacion == null && other.rfVacacion != null) || 
            (this.rfVacacion != null && !this.rfVacacion.equals(other.rfVacacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "VwVacaPendientesEmpleados rfVacacion=" + rfVacacion + " ";
    }

}
