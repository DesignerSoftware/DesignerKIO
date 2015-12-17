package co.com.kiosko.clasesAyuda;

/**
 *
 * @author Felipe Triviño
 */
public class CadenasKioskos {

    private String id;
    private String descripcion;
    private String cadena;
    private String nit;

    public CadenasKioskos(String id, String descripcion, String cadena, String nit) {
        this.id = id;
        this.descripcion = descripcion;
        this.cadena = cadena;
        this.nit = nit;
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
}
