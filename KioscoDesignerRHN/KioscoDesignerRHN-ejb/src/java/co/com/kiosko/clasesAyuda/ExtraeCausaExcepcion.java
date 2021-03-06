/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.clasesAyuda;

import java.sql.SQLException;
import java.util.Calendar;

/**
 *
 * @author Edwin
 */
public class ExtraeCausaExcepcion {

    /**
     * Metodo encargado de retornar el ultimo error que se capturo en los try -
     * catch.
     *
     * @param e Exception
     * @return Retorna el ultimo error capturado.
     */
    public static Throwable getLastThrowable(Exception e) {
        Throwable t=null;
        if (e != null && e.getCause() != null) {
            for (t = e.getCause(); t.getCause() != null; t = t.getCause());
            return t;
        }else{
            return e;
        }
    }

    public static int obtenerCodigoSQLException(Exception e) throws Exception {
        int codigo = 0;
        Throwable t = getLastThrowable(e);
        if (t instanceof SQLException) {
            SQLException sqle = (SQLException) t;
            codigo = sqle.getErrorCode();
        } else {
            throw e;
        }
        return codigo;
    }

    public static String obtenerMensajeSQLException(Exception e) {
        String mensaje;
        Throwable t = getLastThrowable(e);
        mensaje = t.getMessage();
        Calendar fecha = Calendar.getInstance();
        System.out.println("mensaje error: "+
                fecha.get(Calendar.YEAR)+"/"+
                (fecha.get(Calendar.MONTH)+1)+"/"+
                fecha.get(Calendar.DAY_OF_MONTH)+" "+
                fecha.get(Calendar.HOUR_OF_DAY)+":"+
                fecha.get(Calendar.MINUTE)+":"+
                fecha.get(Calendar.SECOND)+" "+
                mensaje
        );
        mensaje = "Hubo un error, por favor solicitar la revision al momento: "+
                fecha.get(Calendar.YEAR)+"/"+
                (fecha.get(Calendar.MONTH)+1)+"/"+
                fecha.get(Calendar.DAY_OF_MONTH)+" "+
                fecha.get(Calendar.HOUR_OF_DAY)+":"+
                fecha.get(Calendar.MINUTE)+":"+
                fecha.get(Calendar.SECOND)+" "
                ;
        return mensaje;
    }
}
