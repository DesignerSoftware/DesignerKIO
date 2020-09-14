package co.com.kiosko.clasesAyuda;

/**
 *
 * @author Felipe Triviño, Edwin Hastamorir
 */
public class CadenasKioskos implements Comparable{

    private String id;
    private String descripcion;
    private String cadena;
    private String nit;
    private String fondo;
    private String grupo;
    private String emplnomina;
    private String esquema;
    private String captcha;

    public CadenasKioskos(String id, String descripcion, String cadena, String nit, String fondo, String grupo) {
        this.id = id;
        this.descripcion = descripcion;
        this.cadena = cadena;
        this.nit = nit;
        this.fondo = fondo;
        this.grupo = grupo;
        this.emplnomina = null;
        this.esquema = null;
        this.captcha = null;
    }
    
    public CadenasKioskos(String id, String descripcion, String cadena, String nit, String fondo, String grupo, String emplnomina) {
        this.id = id;
        this.descripcion = descripcion;
        this.cadena = cadena;
        this.nit = nit;
        this.fondo = fondo;
        this.grupo = grupo;
        this.emplnomina = emplnomina;
        this.esquema = null;
        this.captcha = null;
    }

    public CadenasKioskos(String id, String descripcion, String cadena, String nit, String fondo, String grupo, String emplnomina, String esquema, String captcha) {
        this.id = id;
        this.descripcion = descripcion;
        this.cadena = cadena;
        this.nit = nit;
        this.fondo = fondo;
        this.grupo = grupo;
        this.emplnomina = emplnomina;
        this.esquema = esquema;
        this.captcha = captcha;
    }    

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getFondo() {
        return fondo;
    }

    public void setFondo(String fondo) {
        this.fondo = fondo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getEmplnomina() {
        return emplnomina;
    }

    public void setEmplnomina(String emplnomina) {
        this.emplnomina = emplnomina;
    }

    public String getEsquema() {
        return esquema;
    }

    public void setEsquema(String esquema) {
        this.esquema = esquema;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
    
    
    @Override
    public int compareTo(Object o) {
        int resultado;
        CadenasKioskos ck = (CadenasKioskos) o;
        if (this.getId().equalsIgnoreCase(ck.getId())){
            resultado = 0;
        } else if (Integer.parseInt(this.getId()) < Integer.parseInt(ck.getId())){
            resultado=-1;
        } else {
            resultado = 1;
        }
        return resultado;
    }
    
}
