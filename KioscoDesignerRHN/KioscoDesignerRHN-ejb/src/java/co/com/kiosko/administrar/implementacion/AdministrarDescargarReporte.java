package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarDescargarReporte;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.clasesAyuda.ReporteGenerado;
import co.com.kiosko.correo.EnvioCorreo;
import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.entidades.ConfiguracionCorreo;
import co.com.kiosko.entidades.Generales;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionesKioskos;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConfiguracionCorreo;
import co.com.kiosko.persistencia.interfaz.IPersistenciaGenerales;
import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

/**
 *
 * @author Edwin Hastamorir
 */
@Stateful
public class AdministrarDescargarReporte implements IAdministrarDescargarReporte, Serializable {

    private transient EntityManagerFactory emf;
    private String idSesion;

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaConfiguracionCorreo persistenciaConfiguracionCorreo;
    @EJB
    private IPersistenciaConexionesKioskos persistenciaConexionesKioskos;
    @EJB
    private IPersistenciaGenerales persistenciaGenerales;

    @Override
    public void obtenerConexion(String idSesion) {
        try {
            this.idSesion = idSesion;
            emf = administrarSesiones.obtenerConexionSesion(idSesion);
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error obtenerConexion: " + e);
        }
    }

    private EntityManager obtenerConexion() {
        try {
            emf = administrarSesiones.obtenerConexionSesion(idSesion);
            if (emf != null && emf.isOpen()) {
                return emf.createEntityManager();
            }
        } catch (Exception e) {
            System.out.println(this.getClass().getName() + ": " + "Error obtenerConexion - 2: " + e);
        }
        return null;
    }

    @Override
    public List<ReporteGenerado> consultarReporte(String nombreDirectorio, String codigoEmpleado, Date fechaDesde, Date fechaHasta) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            Generales general = persistenciaGenerales.consultarRutasGenerales(em);
            String rutaReporte = general.getPathreportes();
            List<ReporteGenerado> reportes = new ArrayList<ReporteGenerado>();

            File folder = new File(rutaReporte + nombreDirectorio);
            File[] listOfFiles = folder.listFiles();
            SimpleDateFormat formato = new SimpleDateFormat("yyyyMMdd");
            for (File listOfFile : listOfFiles) {
                if (listOfFile.isFile()) {
                    String archivo = listOfFile.getName();
                    String archivoSinExt = archivo.substring(0, archivo.lastIndexOf("."));
                    String fecha = archivoSinExt.substring(archivoSinExt.length() - 8);
                    if (archivo.startsWith(codigoEmpleado)
                            && (fechaDesde.compareTo(formato.parse(fecha)) <= 0 && fechaHasta.compareTo(formato.parse(fecha)) >= 0)) {
                        ReporteGenerado infoReporte = new ReporteGenerado();
                        infoReporte.setNombre(archivo);
                        infoReporte.setRuta(rutaReporte + nombreDirectorio + File.separator + listOfFile.getName());
                        reportes.add(infoReporte);
                    }
                }
            }
            return reportes;
        } catch (Exception ex) {
            System.out.println("Error AdministrarGenerarReporte.consultarReporte: " + ex);
            return null;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean modificarConexionKisko(ConexionesKioskos cnx) {
        EntityManager em = null;
        boolean res = false;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            res = persistenciaConexionesKioskos.registrarConexion(em, cnx);
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ".modificarConexionKisko() error " + ex);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return res;
    }

    @Override
    public boolean enviarCorreo(BigInteger secuenciaEmpresa, String destinatario, String asunto, String mensaje, List<ReporteGenerado> pathArchivos) {
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            ConfiguracionCorreo cc = persistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo(em, secuenciaEmpresa);
            EnvioCorreo enviarCorreo = new EnvioCorreo();
            return enviarCorreo.enviarCorreo(cc, destinatario, asunto, mensaje, pathArchivos);
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + ".enviarCorreo() " + ex);
            return false;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public boolean comprobarConfigCorreo(BigInteger secuenciaEmpresa) {
        boolean retorno = false;
        EntityManager em = null;
        try {
            if (em != null && em.isOpen()) {
            } else {
                em = obtenerConexion();
            }
            ConfiguracionCorreo cc = persistenciaConfiguracionCorreo.consultarConfiguracionServidorCorreo(em, secuenciaEmpresa);
            retorno = cc != null && cc.getServidorSmtp().length() != 0;
        } catch (NullPointerException npe) {
            retorno = false;
        } catch (Exception e) {
            System.out.println("AdministrarDescargarReporte.comprobarConfigCorreo");
            System.out.println("Error validando configuracion");
            System.out.println("ex: " + e);
        }
        return retorno;
    }
}
