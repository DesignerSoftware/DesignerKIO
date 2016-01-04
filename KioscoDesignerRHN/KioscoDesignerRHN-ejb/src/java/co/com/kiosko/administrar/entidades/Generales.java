/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.administrar.entidades;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Felipe Triviño
 */
@Entity
@Table(name = "GENERALES")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Generales.findAll", query = "SELECT g FROM Generales g")})
public class Generales implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PATHERROR")
    private String patherror;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PATHPROCESO")
    private String pathproceso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "ARCHIVOERROR")
    private String archivoerror;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "ARCHIVOPROCESO")
    private String archivoproceso;
    @Size(max = 1)
    @Column(name = "INSTANCIAERROR")
    private String instanciaerror;
    @Size(max = 1)
    @Column(name = "INSTANCIAPROCESO")
    private String instanciaproceso;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NOMBRECOMPANIA")
    private String nombrecompania;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "TITULOVENTANA")
    private String tituloventana;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PATHRAIZ")
    private String pathraiz;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PATHAUDITORIA")
    private String pathauditoria;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PATHBASICO")
    private String pathbasico;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PATHGERENCIAL")
    private String pathgerencial;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PATHGRAFICOS")
    private String pathgraficos;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PATHHELPDESK")
    private String pathhelpdesk;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PATHINSTALADOR")
    private String pathinstalador;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PATHHERRAMIENTAS")
    private String pathherramientas;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PATHNOMINA")
    private String pathnomina;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PATHPRUEBAS")
    private String pathpruebas;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PATHREPORTES")
    private String pathreportes;
    @Column(name = "ALTOBOTONWEB")
    private Short altobotonweb;
    @Column(name = "ALTOBOTONWINDOWS")
    private Short altobotonwindows;
    @Size(max = 1)
    @Column(name = "LOGADISCO")
    private String logadisco;
    @Size(max = 1)
    @Column(name = "ERRORADISCO")
    private String erroradisco;
    @Size(max = 50)
    @Column(name = "ARCHIVOBACKUP")
    private String archivobackup;
    @Size(max = 50)
    @Column(name = "ARCHIVORESTORE")
    private String archivorestore;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ARCHIVOLIQUIDACION")
    private String archivoliquidacion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "ARCHIVOCIERRE")
    private String archivocierre;
    @Size(max = 50)
    @Column(name = "ARCHIVOTRIGGERPK")
    private String archivotriggerpk;
    @Size(max = 50)
    @Column(name = "ARCHIVOBACKUPTABLA")
    private String archivobackuptabla;
    @Size(max = 50)
    @Column(name = "ARCHIVOCARGATABLA")
    private String archivocargatabla;
    @Size(max = 50)
    @Column(name = "ARCHIVOTRIGGERTRACE")
    private String archivotriggertrace;
    @Size(max = 50)
    @Column(name = "ARCHIVOTRIGGERSEQ")
    private String archivotriggerseq;
    @Size(max = 50)
    @Column(name = "ARCHIVOCONEXION")
    private String archivoconexion;
    @Size(max = 50)
    @Column(name = "ARCHIVOTRACETABLAS")
    private String archivotracetablas;
    @Size(max = 50)
    @Column(name = "ARCHIVOOPCIONESSEL")
    private String archivoopcionessel;
    @Size(max = 120)
    @Column(name = "PATHSERVIDORBD")
    private String pathservidorbd;
    @Size(max = 120)
    @Column(name = "PATHSERVIDORAPL")
    private String pathservidorapl;
    @Size(max = 120)
    @Column(name = "PATHSERVIDORAPLHOST")
    private String pathservidoraplhost;
    @Size(max = 120)
    @Column(name = "PATHSERVIDORBDHOST")
    private String pathservidorbdhost;
    @Size(max = 120)
    @Column(name = "PATHTEMPORALES")
    private String pathtemporales;
    @Size(max = 120)
    @Column(name = "PATHSERVIDORWEB")
    private String pathservidorweb;
    @Size(max = 50)
    @Column(name = "RUTASYSTEM32")
    private String rutasystem32;
    @Size(max = 100)
    @Column(name = "UBICAREPORTES")
    private String ubicareportes;
    @Size(max = 50)
    @Column(name = "IDSREPORTES")
    private String idsreportes;
    @Size(max = 200)
    @Column(name = "PATHFOTO")
    private String pathfoto;
    @Size(max = 100)
    @Column(name = "UBICAPROYECTOINDICADORES")
    private String ubicaproyectoindicadores;
    @Size(max = 100)
    @Column(name = "WEBSERVICESCDI")
    private String webservicescdi;
    @Size(max = 1)
    @Column(name = "PREVALIDACONTABILIDAD")
    private String prevalidacontabilidad;
    @Size(max = 1)
    @Column(name = "PREVALIDABLOQUEAINGRESO")
    private String prevalidabloqueaingreso;
    @Size(max = 100)
    @Column(name = "WEBSERVICESPRESUPUESTO")
    private String webservicespresupuesto;
    @Size(max = 10)
    @Column(name = "HTTPVERSION")
    private String httpversion;
    @Size(max = 50)
    @Column(name = "CLAVECDI")
    private String clavecdi;
    @Size(max = 50)
    @Column(name = "WEBSERVICESTESORERIA")
    private String webservicestesoreria;
    @Size(max = 100)
    @Column(name = "RUTAFTPBD")
    private String rutaftpbd;
    @Size(max = 5)
    @Column(name = "VERSIONFORMS")
    private String versionforms;
    @Size(max = 1)
    @Column(name = "REPORTEADORUSAFTP")
    private String reporteadorusaftp;
    @Size(max = 100)
    @Column(name = "SERVERSMTP_SERVER")
    private String serversmtpServer;
    @Size(max = 100)
    @Column(name = "SERVERDOMINIO")
    private String serverdominio;
    @Size(max = 10)
    @Column(name = "CAMPODELIMITADOREXCEL")
    private String campodelimitadorexcel;
    @Size(max = 1)
    @Column(name = "REPORTEADORUSALIMITE64K")
    private String reporteadorusalimite64k;
    @Size(max = 100)
    @Column(name = "WALLETDIRECTORIO")
    private String walletdirectorio;
    @Size(max = 100)
    @Column(name = "WALLETCLAVE")
    private String walletclave;

    public Generales() {
    }

    public Generales(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Generales(BigDecimal secuencia, String patherror, String pathproceso, String archivoerror, String archivoproceso, String nombrecompania, String tituloventana, String pathraiz, String pathauditoria, String pathbasico, String pathgerencial, String pathgraficos, String pathhelpdesk, String pathinstalador, String pathherramientas, String pathnomina, String pathpruebas, String pathreportes, String archivoliquidacion, String archivocierre) {
        this.secuencia = secuencia;
        this.patherror = patherror;
        this.pathproceso = pathproceso;
        this.archivoerror = archivoerror;
        this.archivoproceso = archivoproceso;
        this.nombrecompania = nombrecompania;
        this.tituloventana = tituloventana;
        this.pathraiz = pathraiz;
        this.pathauditoria = pathauditoria;
        this.pathbasico = pathbasico;
        this.pathgerencial = pathgerencial;
        this.pathgraficos = pathgraficos;
        this.pathhelpdesk = pathhelpdesk;
        this.pathinstalador = pathinstalador;
        this.pathherramientas = pathherramientas;
        this.pathnomina = pathnomina;
        this.pathpruebas = pathpruebas;
        this.pathreportes = pathreportes;
        this.archivoliquidacion = archivoliquidacion;
        this.archivocierre = archivocierre;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public String getPatherror() {
        return patherror;
    }

    public void setPatherror(String patherror) {
        this.patherror = patherror;
    }

    public String getPathproceso() {
        return pathproceso;
    }

    public void setPathproceso(String pathproceso) {
        this.pathproceso = pathproceso;
    }

    public String getArchivoerror() {
        return archivoerror;
    }

    public void setArchivoerror(String archivoerror) {
        this.archivoerror = archivoerror;
    }

    public String getArchivoproceso() {
        return archivoproceso;
    }

    public void setArchivoproceso(String archivoproceso) {
        this.archivoproceso = archivoproceso;
    }

    public String getInstanciaerror() {
        return instanciaerror;
    }

    public void setInstanciaerror(String instanciaerror) {
        this.instanciaerror = instanciaerror;
    }

    public String getInstanciaproceso() {
        return instanciaproceso;
    }

    public void setInstanciaproceso(String instanciaproceso) {
        this.instanciaproceso = instanciaproceso;
    }

    public String getNombrecompania() {
        return nombrecompania;
    }

    public void setNombrecompania(String nombrecompania) {
        this.nombrecompania = nombrecompania;
    }

    public String getTituloventana() {
        return tituloventana;
    }

    public void setTituloventana(String tituloventana) {
        this.tituloventana = tituloventana;
    }

    public String getPathraiz() {
        return pathraiz;
    }

    public void setPathraiz(String pathraiz) {
        this.pathraiz = pathraiz;
    }

    public String getPathauditoria() {
        return pathauditoria;
    }

    public void setPathauditoria(String pathauditoria) {
        this.pathauditoria = pathauditoria;
    }

    public String getPathbasico() {
        return pathbasico;
    }

    public void setPathbasico(String pathbasico) {
        this.pathbasico = pathbasico;
    }

    public String getPathgerencial() {
        return pathgerencial;
    }

    public void setPathgerencial(String pathgerencial) {
        this.pathgerencial = pathgerencial;
    }

    public String getPathgraficos() {
        return pathgraficos;
    }

    public void setPathgraficos(String pathgraficos) {
        this.pathgraficos = pathgraficos;
    }

    public String getPathhelpdesk() {
        return pathhelpdesk;
    }

    public void setPathhelpdesk(String pathhelpdesk) {
        this.pathhelpdesk = pathhelpdesk;
    }

    public String getPathinstalador() {
        return pathinstalador;
    }

    public void setPathinstalador(String pathinstalador) {
        this.pathinstalador = pathinstalador;
    }

    public String getPathherramientas() {
        return pathherramientas;
    }

    public void setPathherramientas(String pathherramientas) {
        this.pathherramientas = pathherramientas;
    }

    public String getPathnomina() {
        return pathnomina;
    }

    public void setPathnomina(String pathnomina) {
        this.pathnomina = pathnomina;
    }

    public String getPathpruebas() {
        return pathpruebas;
    }

    public void setPathpruebas(String pathpruebas) {
        this.pathpruebas = pathpruebas;
    }

    public String getPathreportes() {
        return pathreportes;
    }

    public void setPathreportes(String pathreportes) {
        this.pathreportes = pathreportes;
    }

    public Short getAltobotonweb() {
        return altobotonweb;
    }

    public void setAltobotonweb(Short altobotonweb) {
        this.altobotonweb = altobotonweb;
    }

    public Short getAltobotonwindows() {
        return altobotonwindows;
    }

    public void setAltobotonwindows(Short altobotonwindows) {
        this.altobotonwindows = altobotonwindows;
    }

    public String getLogadisco() {
        return logadisco;
    }

    public void setLogadisco(String logadisco) {
        this.logadisco = logadisco;
    }

    public String getErroradisco() {
        return erroradisco;
    }

    public void setErroradisco(String erroradisco) {
        this.erroradisco = erroradisco;
    }

    public String getArchivobackup() {
        return archivobackup;
    }

    public void setArchivobackup(String archivobackup) {
        this.archivobackup = archivobackup;
    }

    public String getArchivorestore() {
        return archivorestore;
    }

    public void setArchivorestore(String archivorestore) {
        this.archivorestore = archivorestore;
    }

    public String getArchivoliquidacion() {
        return archivoliquidacion;
    }

    public void setArchivoliquidacion(String archivoliquidacion) {
        this.archivoliquidacion = archivoliquidacion;
    }

    public String getArchivocierre() {
        return archivocierre;
    }

    public void setArchivocierre(String archivocierre) {
        this.archivocierre = archivocierre;
    }

    public String getArchivotriggerpk() {
        return archivotriggerpk;
    }

    public void setArchivotriggerpk(String archivotriggerpk) {
        this.archivotriggerpk = archivotriggerpk;
    }

    public String getArchivobackuptabla() {
        return archivobackuptabla;
    }

    public void setArchivobackuptabla(String archivobackuptabla) {
        this.archivobackuptabla = archivobackuptabla;
    }

    public String getArchivocargatabla() {
        return archivocargatabla;
    }

    public void setArchivocargatabla(String archivocargatabla) {
        this.archivocargatabla = archivocargatabla;
    }

    public String getArchivotriggertrace() {
        return archivotriggertrace;
    }

    public void setArchivotriggertrace(String archivotriggertrace) {
        this.archivotriggertrace = archivotriggertrace;
    }

    public String getArchivotriggerseq() {
        return archivotriggerseq;
    }

    public void setArchivotriggerseq(String archivotriggerseq) {
        this.archivotriggerseq = archivotriggerseq;
    }

    public String getArchivoconexion() {
        return archivoconexion;
    }

    public void setArchivoconexion(String archivoconexion) {
        this.archivoconexion = archivoconexion;
    }

    public String getArchivotracetablas() {
        return archivotracetablas;
    }

    public void setArchivotracetablas(String archivotracetablas) {
        this.archivotracetablas = archivotracetablas;
    }

    public String getArchivoopcionessel() {
        return archivoopcionessel;
    }

    public void setArchivoopcionessel(String archivoopcionessel) {
        this.archivoopcionessel = archivoopcionessel;
    }

    public String getPathservidorbd() {
        return pathservidorbd;
    }

    public void setPathservidorbd(String pathservidorbd) {
        this.pathservidorbd = pathservidorbd;
    }

    public String getPathservidorapl() {
        return pathservidorapl;
    }

    public void setPathservidorapl(String pathservidorapl) {
        this.pathservidorapl = pathservidorapl;
    }

    public String getPathservidoraplhost() {
        return pathservidoraplhost;
    }

    public void setPathservidoraplhost(String pathservidoraplhost) {
        this.pathservidoraplhost = pathservidoraplhost;
    }

    public String getPathservidorbdhost() {
        return pathservidorbdhost;
    }

    public void setPathservidorbdhost(String pathservidorbdhost) {
        this.pathservidorbdhost = pathservidorbdhost;
    }

    public String getPathtemporales() {
        return pathtemporales;
    }

    public void setPathtemporales(String pathtemporales) {
        this.pathtemporales = pathtemporales;
    }

    public String getPathservidorweb() {
        return pathservidorweb;
    }

    public void setPathservidorweb(String pathservidorweb) {
        this.pathservidorweb = pathservidorweb;
    }

    public String getRutasystem32() {
        return rutasystem32;
    }

    public void setRutasystem32(String rutasystem32) {
        this.rutasystem32 = rutasystem32;
    }

    public String getUbicareportes() {
        return ubicareportes;
    }

    public void setUbicareportes(String ubicareportes) {
        this.ubicareportes = ubicareportes;
    }

    public String getIdsreportes() {
        return idsreportes;
    }

    public void setIdsreportes(String idsreportes) {
        this.idsreportes = idsreportes;
    }

    public String getPathfoto() {
        return pathfoto;
    }

    public void setPathfoto(String pathfoto) {
        this.pathfoto = pathfoto;
    }

    public String getUbicaproyectoindicadores() {
        return ubicaproyectoindicadores;
    }

    public void setUbicaproyectoindicadores(String ubicaproyectoindicadores) {
        this.ubicaproyectoindicadores = ubicaproyectoindicadores;
    }

    public String getWebservicescdi() {
        return webservicescdi;
    }

    public void setWebservicescdi(String webservicescdi) {
        this.webservicescdi = webservicescdi;
    }

    public String getPrevalidacontabilidad() {
        return prevalidacontabilidad;
    }

    public void setPrevalidacontabilidad(String prevalidacontabilidad) {
        this.prevalidacontabilidad = prevalidacontabilidad;
    }

    public String getPrevalidabloqueaingreso() {
        return prevalidabloqueaingreso;
    }

    public void setPrevalidabloqueaingreso(String prevalidabloqueaingreso) {
        this.prevalidabloqueaingreso = prevalidabloqueaingreso;
    }

    public String getWebservicespresupuesto() {
        return webservicespresupuesto;
    }

    public void setWebservicespresupuesto(String webservicespresupuesto) {
        this.webservicespresupuesto = webservicespresupuesto;
    }

    public String getHttpversion() {
        return httpversion;
    }

    public void setHttpversion(String httpversion) {
        this.httpversion = httpversion;
    }

    public String getClavecdi() {
        return clavecdi;
    }

    public void setClavecdi(String clavecdi) {
        this.clavecdi = clavecdi;
    }

    public String getWebservicestesoreria() {
        return webservicestesoreria;
    }

    public void setWebservicestesoreria(String webservicestesoreria) {
        this.webservicestesoreria = webservicestesoreria;
    }

    public String getRutaftpbd() {
        return rutaftpbd;
    }

    public void setRutaftpbd(String rutaftpbd) {
        this.rutaftpbd = rutaftpbd;
    }

    public String getVersionforms() {
        return versionforms;
    }

    public void setVersionforms(String versionforms) {
        this.versionforms = versionforms;
    }

    public String getReporteadorusaftp() {
        return reporteadorusaftp;
    }

    public void setReporteadorusaftp(String reporteadorusaftp) {
        this.reporteadorusaftp = reporteadorusaftp;
    }

    public String getServersmtpServer() {
        return serversmtpServer;
    }

    public void setServersmtpServer(String serversmtpServer) {
        this.serversmtpServer = serversmtpServer;
    }

    public String getServerdominio() {
        return serverdominio;
    }

    public void setServerdominio(String serverdominio) {
        this.serverdominio = serverdominio;
    }

    public String getCampodelimitadorexcel() {
        return campodelimitadorexcel;
    }

    public void setCampodelimitadorexcel(String campodelimitadorexcel) {
        this.campodelimitadorexcel = campodelimitadorexcel;
    }

    public String getReporteadorusalimite64k() {
        return reporteadorusalimite64k;
    }

    public void setReporteadorusalimite64k(String reporteadorusalimite64k) {
        this.reporteadorusalimite64k = reporteadorusalimite64k;
    }

    public String getWalletdirectorio() {
        return walletdirectorio;
    }

    public void setWalletdirectorio(String walletdirectorio) {
        this.walletdirectorio = walletdirectorio;
    }

    public String getWalletclave() {
        return walletclave;
    }

    public void setWalletclave(String walletclave) {
        this.walletclave = walletclave;
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
        if (!(object instanceof Generales)) {
            return false;
        }
        Generales other = (Generales) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.kiosko.administrar.entidades.Generales[ secuencia=" + secuencia + " ]";
    }
    
}
