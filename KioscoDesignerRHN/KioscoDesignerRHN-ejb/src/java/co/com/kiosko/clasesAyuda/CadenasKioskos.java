package co.com.kiosko.clasesAyuda;

/**
 *
 * @author Felipe Triviño
 */
public class CadenasKioskos {
    
    private String id;
    private String descripcion;
    private String cadena;

    public CadenasKioskos(String id, String descripcion, String cadena) {
        this.id = id;
        this.descripcion = descripcion;
        this.cadena = cadena;
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
}
