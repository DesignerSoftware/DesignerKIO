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
@Table(name = "GENERALESKIOSKO")
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
    @Size(min = 1, max = 100)
    @Column(name = "PATHREPORTES")
    private String pathreportes;
    @Size(max = 100)
    @Column(name = "UBICAREPORTES")
    private String ubicareportes;
    @Size(max = 200)
    @Column(name = "PATHFOTO")
    private String pathfoto;

    public Generales() {
    }

    public Generales(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public Generales(BigDecimal secuencia, String pathreportes) {
        this.secuencia = secuencia;
        this.pathreportes = pathreportes;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public String getPathreportes() {
        return pathreportes;
    }

    public void setPathreportes(String pathreportes) {
        this.pathreportes = pathreportes;
    }

    public String getUbicareportes() {
        return ubicareportes;
    }

    public void setUbicareportes(String ubicareportes) {
        this.ubicareportes = ubicareportes;
    }

    public String getPathfoto() {
        return pathfoto;
    }

    public void setPathfoto(String pathfoto) {
        this.pathfoto = pathfoto;
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
