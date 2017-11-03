package co.com.kiosko.convertidor;

import co.com.kiosko.clasesAyuda.EstadoSolicitud;
import co.com.kiosko.utilidadesUI.OpcionLista;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author Edwin
 */
@FacesConverter("convertidorOpcionLista")
public class ConvertidorOpcionLista implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        System.out.println("getAsObject-ClaseDelvalue: " + value.getClass().getName());
        System.out.println("getAsObject-value: " + value);
        System.out.println("getAsObject-CLComp: " + component.getClass().getName());
        System.out.println("getAsObject-comp: " + component);
        if (EstadoSolicitud.GUARDADO.getEstado().equals(value)){
            return new OpcionLista(EstadoSolicitud.GUARDADO.getEvento(),
                    EstadoSolicitud.GUARDADO.getEstado());
        }
        if (EstadoSolicitud.ENVIADO.getEstado().equals(value)){
            return new OpcionLista(EstadoSolicitud.ENVIADO.getEvento(),
                    EstadoSolicitud.ENVIADO.getEstado());
        }
        if (EstadoSolicitud.AUTORIZADO.getEstado().equals(value)){
            return new OpcionLista(EstadoSolicitud.AUTORIZADO.getEvento(),
                    EstadoSolicitud.AUTORIZADO.getEstado());
        }
        if (EstadoSolicitud.LIQUIDADO.getEstado().equals(value)){
            return new OpcionLista(EstadoSolicitud.LIQUIDADO.getEvento(),
                    EstadoSolicitud.LIQUIDADO.getEstado());
        }
        if (EstadoSolicitud.RECHAZADO.getEstado().equals(value)){
            return new OpcionLista(EstadoSolicitud.RECHAZADO.getEvento(),
                    EstadoSolicitud.RECHAZADO.getEstado());
        }
        if (EstadoSolicitud.CANCELADO.getEstado().equals(value)){
            return new OpcionLista(EstadoSolicitud.CANCELADO.getEvento(),
                    EstadoSolicitud.CANCELADO.getEstado());
        }
        return null;

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        System.out.println("getAsString-ClaseDelValue: " + value.getClass().getName());
        System.out.println("getAsString-value: " + value);
        System.out.println("getAsString-CLComp: " + component.getClass().getName());
        System.out.println("getAsString-comp: " + component);
        if (value == null) {
            return null;
        }
        if (value instanceof OpcionLista) {
            return ((OpcionLista) (value)).getValor();
        } else {
            throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "No es una opcion de lista valida."));
        }
    }

}
