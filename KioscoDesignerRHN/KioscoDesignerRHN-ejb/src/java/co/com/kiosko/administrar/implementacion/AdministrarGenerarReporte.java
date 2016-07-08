package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.entidades.ConfiguracionCorreo;
import co.com.kiosko.entidades.Generales;
import co.com.kiosko.administrar.interfaz.IAdministrarGenerarReporte;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionesKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConfiguracionCorreo;
import co.com.kiosko.persistencia.interfaz.IPersistenciaGenerales;
import co.com.kiosko.correo.EnvioCorreo;
import co.com.kiosko.reportes.IniciarReporteInterface;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;

/**
 *
 * @author Felipe Triviño
 */
@Stateful
public class AdministrarGenerarReporte implements IAdministrarGenerarReporte {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaGenerales persistenciaGenerales;
    @EJB
    private IPersistenciaConexionesKioskos persistenciaConexionesKioskos;
    @EJB
    private IPersistenciaConfiguracionCorreo persistenciaConfiguracionCorreo;
    @EJB
    private IniciarReporteInterface reporte;
    private EntityManager em;

    @Override
    public void obtenerConexion(String idSesion) {
        em = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public String generarReporte(String nombreReporte, String tipoReporte, Map parametros) {
        try {
            Generales general = persistenciaGenerales.consultarRutasGenerales(em);
            String pathReporteGenerado = null;
            BigDecimal secuenciaempleado = null;
            String nombreArchivo;
            if (general != null) {
                if (parametros.containsKey("secuenciaempleado")) {
                    secuenciaempleado = (BigDecimal) parametros.get("secuenciaempleado");
                }
                SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddhhmmss");
                String fechaActual = formato.format(new Date());
                if (secuenciaempleado != null) {
                    nombreArchivo = "KSK_" + nombreReporte + "_" + secuenciaempleado.toString() + "_" + fechaActual;
                } else {
                    nombreArchivo = "KSK_" + nombreReporte + "_" + fechaActual;
                }
                String rutaReporte = general.getPathreportes();
                String rutaGenerado = general.getUbicareportes();
                if (tipoReporte.equals("PDF")) {
                    nombreArchivo = nombreArchivo + ".pdf";
                } else if (tipoReporte.equals("XLSX")) {
                    nombreArchivo = nombreArchivo + ".xlsx";
                } else if (tipoReporte.equals("XLS")) {
                    nombreArchivo = nombreArchivo + ".xls";
                } else if (tipoReporte.equals("CSV")) {
                    nombreArchivo = nombreArchivo + ".csv";
                } else if (tipoReporte.equals("HTML")) {
                    nombreArchivo = nombreArchivo + ".html";
                } else if (tipoReporte.equals("DOCX")) {
                    nombreArchivo = nombreArchivo + ".rtf";
                }

                parametros.put("pathImagenes", rutaReporte);
                pathReporteGenerado = reporte.ejecutarReporte(nombreReporte, rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, parametros, em);
                return pathReporteGenerado;
            }
            return pathReporteGenerado;
        } catch (Exception ex) {
            System.out.println("Error AdministrarGenerarReporte.generarReporte: " + ex);
            return null;
        }
    }

    @Override
    public boolean modificarConexionKisko(ConexionesKioskos cnx) {
        return persistenciaConexionesKioskos.registrarConexion(em, cnx);
    }

    @Override
    public boolean enviarCorreo(BigInteger secuenciaEmpresa, String destinatario, String asunto, String mensaje, String pathAdjunto) {
        ConfiguracionCorreo cc = persistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo(em, secuenciaEmpresa);
        EnvioCorreo enviarCorreo = new EnvioCorreo();
        return enviarCorreo.enviarCorreo(cc, destinatario, asunto, mensaje, pathAdjunto);
    }

    @Override
    public boolean comprobarConfigCorreo(BigInteger secuenciaEmpresa) {
        boolean retorno = false;
        try {
            ConfiguracionCorreo cc = persistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo(em, secuenciaEmpresa);
            if (cc.getServidorSmtp().length() != 0) {
                retorno = true;
            } else {
                retorno = false;
            }
        } catch (NullPointerException npe) {
            retorno = false;
        } catch (Exception e) {
            System.out.println("AdministrarGenerarReporte.comprobarConfigCorreo");
            System.out.println("Error validando configuracion");
            System.out.println("ex: " + e);
        }
        return retorno;
    }
}
