package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.entidades.OpcionesKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarOpcionesKiosko;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.persistencia.interfaz.IPersistenciaOpcionesKioskos;
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
    private EntityManagerFactory emf;

    @Override
    public void obtenerConexion(String idSesion) {
        emf = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public OpcionesKioskos obtenerOpcionesKiosko(BigInteger secuenciaEmpresa) {
        // try {
        EntityManager em = emf.createEntityManager();
        OpcionesKioskos opciones;
        opciones = persistenciaOpcionesKioskos.consultarOpcionesPorPadre(em, null, secuenciaEmpresa).get(0);

        opciones.setOpcionesHijas(persistenciaOpcionesKioskos.consultarOpcionesPorPadre(em, opciones.getSecuencia(), secuenciaEmpresa));

        if (opciones.getOpcionesHijas() != null && !opciones.getOpcionesHijas().isEmpty()) {
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
        em.close();
        return opciones;
        /*
         * } catch (Exception e) { System.out.println("Error
         * AdministrarOpcionesKiosko.obtenerOpcionesKiosko: " + e); return null;
         * }
         */
    }
}
