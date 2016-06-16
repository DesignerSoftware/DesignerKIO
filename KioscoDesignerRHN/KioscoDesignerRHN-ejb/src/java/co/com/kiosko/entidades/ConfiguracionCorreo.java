package co.com.kiosko.entidades;

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
@Table(name = "CONFICORREOKIOSKO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ConfiguracionCorreo.findAll", query = "SELECT c FROM ConfiguracionCorreo c")})
public class ConfiguracionCorreo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "SECUENCIA")
    private BigDecimal secuencia;
    @JoinColumn(name = "EMPRESA", referencedColumnName = "SECUENCIA")
    @ManyToOne(optional = false)
    private Empresas empresa;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "SERVIDORSMTP")
    private String servidorSmtp;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "PUERTO")
    private String puerto;
    @Size(max = 1)
    @Column(name = "STARTTLS")
    private String starttls;
    @Size(max = 1)
    @Column(name = "USARSSL")
    private String usarssl;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1)
    @Column(name = "AUTENTICADO")
    private String autenticado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "REMITENTE")
    private String remitente;
    @Size(max = 50)
    @Column(name = "CLAVE")
    private String clave;

    public ConfiguracionCorreo() {
    }

    public ConfiguracionCorreo(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public ConfiguracionCorreo(BigDecimal secuencia, String servidorSmtp, String puerto, String autenticado, String remitente) {
        this.secuencia = secuencia;
        this.servidorSmtp = servidorSmtp;
        this.puerto = puerto;
        this.autenticado = autenticado;
        this.remitente = remitente;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Empresas getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresas empresa) {
        this.empresa = empresa;
    }

    public String getServidorSmtp() {
        return servidorSmtp;
    }

    public void setServidorSmtp(String servidorSmtp) {
        this.servidorSmtp = servidorSmtp;
    }

    public String getPuerto() {
        return puerto;
    }

    public void setPuerto(String puerto) {
        this.puerto = puerto;
    }

    public String getStarttls() {
        return starttls;
    }

    public void setStarttls(String starttls) {
        this.starttls = starttls;
    }

    public String getUsarssl() {
        return usarssl;
    }

    public void setUsarssl(String usarssl) {
        this.usarssl = usarssl;
    }

    public String getAutenticado() {
        return autenticado;
    }

    public void setAutenticado(String autenticado) {
        this.autenticado = autenticado;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
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
        if (!(object instanceof ConfiguracionCorreo)) {
            return false;
        }
        ConfiguracionCorreo other = (ConfiguracionCorreo) object;
        if ((this.secuencia == null && other.secuencia != null) || (this.secuencia != null && !this.secuencia.equals(other.secuencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "co.com.kiosko.administrar.entidades.ConfiguracionCorreo[ secuencia=" + secuencia + " ]";
    }
}
