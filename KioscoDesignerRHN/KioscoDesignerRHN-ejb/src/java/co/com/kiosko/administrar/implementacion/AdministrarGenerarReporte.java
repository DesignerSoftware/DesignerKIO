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
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */
@Stateful
public class AdministrarGenerarReporte implements IAdministrarGenerarReporte, Serializable {

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

    private EntityManagerFactory emf;
    private String idSesion;

    @Override
    public void obtenerConexion(String idSesion) {
        System.out.println(this.getClass().getName()+".obtenerConexion()");
        this.idSesion = idSesion;
        emf = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public String generarReporte(String nombreReporte, String tipoReporte, Map parametros) {
        System.out.println(this.getClass().getName()+".generarReporte()");
        try {
            EntityManager em = emf.createEntityManager();
            Generales general = persistenciaGenerales.consultarRutasGenerales(em);
            em.close();

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
//                pathReporteGenerado = reporte.ejecutarReporte(nombreReporte, 
//                        rutaReporte, rutaGenerado, nombreArchivo, tipoReporte, 
//                        parametros, em);
//                EntityManager em2 = administrarSesiones.obtenerConexionSesion(idSesion).createEntityManager();
                EntityManager em2 = emf.createEntityManager();
                pathReporteGenerado = reporte.ejecutarReporte(nombreReporte,
                        rutaReporte, rutaGenerado, nombreArchivo, tipoReporte,
                        parametros, em2);
                em2.close();
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
        System.out.println(this.getClass().getName()+".modificarConexionKisko()");
        boolean resul = false;
        try {
            EntityManager em = emf.createEntityManager();
            resul = persistenciaConexionesKioskos.registrarConexion(em, cnx);
            em.close();
        } catch (Exception e) {
            System.out.println("modificarConexionKisko: " + e);
        }
        return resul;
    }

    @Override
    public boolean enviarCorreo(BigInteger secuenciaEmpresa, String destinatario, String asunto, String mensaje, String pathAdjunto) {
        System.out.println(this.getClass().getName()+".enviarCorreo()");
        boolean resul = false;
        try {
            EntityManager em = emf.createEntityManager();
            ConfiguracionCorreo cc = persistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo(em, secuenciaEmpresa);
//            EnvioCorreo enviarCorreo = new EnvioCorreo();
//            return enviarCorreo.enviarCorreo(cc, destinatario, asunto, mensaje, pathAdjunto);
            em.close();
            resul = EnvioCorreo.enviarCorreo(cc, destinatario, asunto, mensaje, pathAdjunto);
        } catch (Exception e) {
            System.out.println("enviarCorreo: " + e);
        }
        return resul;
    }

    @Override
    public boolean comprobarConfigCorreo(BigInteger secuenciaEmpresa) {
        System.out.println(this.getClass().getName()+".comprobarConfigCorreo()");
        boolean retorno = false;
        try {
            EntityManager em = emf.createEntityManager();
            ConfiguracionCorreo cc = persistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo(em, secuenciaEmpresa);
            em.close();
//            if (cc.getServidorSmtp().length() != 0) {
//                retorno = true;
//            } else {
//                retorno = false;
//            }
            retorno = cc.getServidorSmtp().length() != 0;
        } catch (NullPointerException npe) {
            retorno = false;
            System.out.println("Null ex: "+npe);
        } catch (Exception e) {
            System.out.println("AdministrarGenerarReporte.comprobarConfigCorreo");
            System.out.println("Error validando configuracion");
            System.out.println("ex: " + e);
        }
        return retorno;
    }
}
