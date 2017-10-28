package co.com.kiosko.clasesAyuda;

/**
 *
 * @author Edwin
 */
public enum EstadosSolicitud {

    GUARDADO("GUARDAR", "GUARDADO"),
    ENVIADO("ENVIAR", "ENVIADO"),
    AUTORIZADO("AUTORIZAR", "AUTORIZADO"),
    LIQUIDADO("LIQUIDAR", "LIQUIDADO"),
    RECHAZADO("RECHAZAR", "RECHAZADO"),
    CANCELADO("CANCELAR", "CANCELADO");

    private String evento;
    private String estado;

    private EstadosSolicitud(String evento, String estado) {
        this.evento = evento;
        this.estado = estado;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    
}
