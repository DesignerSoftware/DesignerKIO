package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.entidades.OpcionesKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarOpcionesKiosko;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.clasesAyuda.CadenasKioskos;
import co.com.kiosko.clasesAyuda.LeerArchivoXML;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaOpcionesKioskos;
import java.math.BigDecimal;
import java.math.BigInteger;
import javax.ejb.EJB;
//import javax.ejb.Stateless;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */
@Stateful
//@Stateless
public class AdministrarOpcionesKiosko implements IAdministrarOpcionesKiosko {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaOpcionesKioskos persistenciaOpcionesKioskos;
    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;
    private EntityManagerFactory emf;

    @Override
    public void obtenerConexion(String idSesion) {
        emf = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public OpcionesKioskos obtenerOpcionesKiosko(BigInteger secuenciaEmpresa) {
        // try {
        EntityManager em = emf.createEntityManager();
        OpcionesKioskos opciones = null;
        try {
            opciones = persistenciaOpcionesKioskos.consultarOpcionesPorPadre(em, null, secuenciaEmpresa).get(0);
        } catch (Exception e) {
            System.out.println("obtenerOpcionesKiosko:consultaOpcionPadre:excep: " + e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        if (opciones != null) {
            em = emf.createEntityManager();
            try {
                opciones.setOpcionesHijas(persistenciaOpcionesKioskos.consultarOpcionesPorPadre(em, opciones.getSecuencia(), secuenciaEmpresa));
            } catch (Exception ex) {
                System.out.println("obtenerOpcionesKiosko:consultaOpcionPadre:exc: " + ex);
            } finally {
                if (em != null && em.isOpen()) {
                    em.close();
                }
            }
        }
        em = emf.createEntityManager();
        try {
            if (opciones != null && opciones.getOpcionesHijas() != null && !opciones.getOpcionesHijas().isEmpty()) {
                for (int i = 0; i < opciones.getOpcionesHijas().size(); i++) {
                    opciones.getOpcionesHijas().get(i).setOpcionesHijas(persistenciaOpcionesKioskos.consultarOpcionesPorPadre(em, opciones.getOpcionesHijas().get(i).getSecuencia(), secuenciaEmpresa));
                    if (opciones.getOpcionesHijas().get(i).getOpcionesHijas() != null && !opciones.getOpcionesHijas().get(i).getOpcionesHijas().isEmpty()) {
                        for (int j = 0; j < opciones.getOpcionesHijas().get(i).getOpcionesHijas().size(); j++) {
                            opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).setOpcionesHijas(persistenciaOpcionesKioskos.consultarOpcionesPorPadre(em, opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getSecuencia(), secuenciaEmpresa));
                            if (opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getOpcionesHijas() != null && !opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getOpcionesHijas().isEmpty()) {
                                for (int k = 0; k < opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getOpcionesHijas().size(); k++) {
                                    opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getOpcionesHijas().get(k).setOpcionesHijas(persistenciaOpcionesKioskos.consultarOpcionesPorPadre(em, opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getOpcionesHijas().get(k).getSecuencia(), secuenciaEmpresa));
                                    if (opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getOpcionesHijas().get(k).getOpcionesHijas() != null && !opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getOpcionesHijas().get(k).getOpcionesHijas().isEmpty()) {
                                        for (int l = 0; l < opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getOpcionesHijas().get(k).getOpcionesHijas().size(); l++) {
                                            opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getOpcionesHijas().get(k).getOpcionesHijas().get(l).setOpcionesHijas(persistenciaOpcionesKioskos.consultarOpcionesPorPadre(em, opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getOpcionesHijas().get(k).getOpcionesHijas().get(l).getSecuencia(), secuenciaEmpresa));
                                            if (opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getOpcionesHijas().get(k).getOpcionesHijas().get(l).getOpcionesHijas() != null && !opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getOpcionesHijas().get(k).getOpcionesHijas().get(l).getOpcionesHijas().isEmpty()) {
                                                for (int m = 0; m < opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getOpcionesHijas().get(k).getOpcionesHijas().get(l).getOpcionesHijas().size(); m++) {
                                                    opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getOpcionesHijas().get(k).getOpcionesHijas().get(l).getOpcionesHijas().get(m).setOpcionesHijas(persistenciaOpcionesKioskos.consultarOpcionesPorPadre(em, opciones.getOpcionesHijas().get(i).getOpcionesHijas().get(j).getOpcionesHijas().get(k).getOpcionesHijas().get(l).getOpcionesHijas().get(m).getSecuencia(), secuenciaEmpresa));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception exce){
            System.out.println("obtenerOpcionesKiosko:consultaOpciones:excep: " + exce);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        return opciones;
        /*
         * } catch (Exception e) { System.out.println("Error
         * AdministrarOpcionesKiosko.obtenerOpcionesKiosko: " + e); return null;
         * }
         */
    }

    @Override
    public String determinarRol(Empleados empleado, String unidadPersistenciaIngreso) {
        EntityManager em = emf.createEntityManager();
        String rol = "EMPLEADO";
        boolean esJefe = false;
        try {
            esJefe = persistenciaEmpleados.esJefe(em, empleado.getSecuencia(), empleado.getEmpresa().getSecuencia());
        } catch (Exception e) {
            System.out.println("determinarRol: esJefe: excep: " + e);
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
        if (esJefe) {
            rol = rol + ";JEFE";
        }
        String codNomina = "";
        try {
            codNomina = validarUnidadPersistencia(unidadPersistenciaIngreso).getEmplnomina();
        } catch (NumberFormatException nfe) {
            System.out.println("El codigo de empleado de nomina no tiene formato numerico");
        } catch (Exception ex) {
            System.out.println("determinarRol: esNomina: excep: " + ex);
        }
        try {
            if ((codNomina != null) && (!codNomina.isEmpty())) {
                System.out.println("codigo registrado como empleado de nomina: " + codNomina);
                if (empleado.getCodigoempleado().equals(new BigDecimal(codNomina))) {
                    rol = rol + ";NOMINA";
                }
            } else {
                System.out.println("NO hay empleado de nomina registrado: " + codNomina);
            }
        } catch (NullPointerException npe) {
            System.out.println("determinarRol: esNomina: excep: " + npe);
            System.out.println("determinarRol: esNomina: codNomina: " + codNomina);
        } catch (NumberFormatException nfe) {
            System.out.println("determinarRol: esNomina: excep: " + nfe);
        }
        return rol;
    }

    private CadenasKioskos validarUnidadPersistencia(String unidadP) {
        CadenasKioskos resultado = null;
        for (CadenasKioskos elemento : (new LeerArchivoXML()).leerArchivoEmpresasKiosko()) {
            if (elemento.getId().equals(unidadP)) {
                resultado = elemento;
                break;
            }
        }
        return resultado;
    }
}
