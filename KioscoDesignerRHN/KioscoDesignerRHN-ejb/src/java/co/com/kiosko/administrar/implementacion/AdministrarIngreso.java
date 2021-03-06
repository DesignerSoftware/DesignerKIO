package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.entidades.ConexionesKioskos;
import co.com.kiosko.administrar.interfaz.IAdministrarIngreso;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.clasesAyuda.SessionEntityManager;
import co.com.kiosko.conexionFuente.implementacion.SesionEntityManagerFactory;
import co.com.kiosko.conexionFuente.interfaz.ISesionEntityManagerFactory;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionInicial;
import co.com.kiosko.persistencia.interfaz.IPersistenciaConexionesKioskos;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.Stateful;
//import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Trivi�o
 */
//@Stateless
@Stateful
public class AdministrarIngreso implements IAdministrarIngreso, Serializable {

    @EJB
    private IPersistenciaConexionInicial persistenciaConexionInicial;
    @EJB
    private IAdministrarSesiones administrarSessiones;
    @EJB
    private IPersistenciaConexionesKioskos persistenciaConexionesKioskos;

    private String unidadPersistencia;
    private ISesionEntityManagerFactory sessionEMF;

//    private transient EntityManagerFactory emf;
//    public AdministrarIngreso() {
//        sessionEMF = new SesionEntityManagerFactory();
//    }
    @Override
    public boolean conexionIngreso(String unidadPersistencia) {
//        System.out.println(this.getClass().getName() + ".conexionIngreso()");
//        System.out.println("Unidad de persistencia: " + unidadPersistencia);
        boolean resul = false;
        EntityManagerFactory emf;
        try {
//            ISesionEntityManagerFactory sessionEMF = new SesionEntityManagerFactory();
            sessionEMF = new SesionEntityManagerFactory();
            emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
            if (emf != null) {
                resul = true;
//                emf.close();
            } else {
                System.out.println("Error la unidad de persistencia no existe, revisar el archivo XML de persistencia.");
                resul = false;
            }
        } catch (Exception e) {
            System.out.println("Error general: " + e);
            resul = false;
            emf = null;
        }
        if (resul) {
            this.unidadPersistencia = unidadPersistencia;
            System.out.println("Unid. Pesistencia asignada.");
        }
        return resul;
    }

    @Override
    public boolean validarUsuarioyEmpresa(String usuario, String nitEmpresa, String esquema) throws Exception {
//        System.out.println(this.getClass().getName() + ".validarUsuarioyEmpresa()");
        EntityManagerFactory emf;
        boolean resul = false;
        try {
            emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
            if (emf != null) {
                EntityManager em = emf.createEntityManager();
//                persistenciaConexionInicial.setearKiosko(em);
                persistenciaConexionInicial.setearKiosko(em, esquema);
                resul = persistenciaConexionInicial.validarUsuarioyEmpresa(em, usuario, nitEmpresa);
                em.close();
//                emf.close();
            }
        } catch (Exception e) {
            System.out.println("Error validarUsuarioyEmpresa: " + e);
            resul = false;
            throw e;
        }
        return resul;
    }

    @Override
    public boolean validarAutorizador(String usuario, String esquema) throws Exception {
//        System.out.println(this.getClass().getName() + ".validarAutorizador()");
        boolean resul = false;
        EntityManagerFactory emf;
        try {
            emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
            if (emf != null) {
                EntityManager em = emf.createEntityManager();
                persistenciaConexionInicial.setearKiosko(em, esquema);
                resul = persistenciaConexionInicial.validarAutorizador(em, usuario);
                em.close();
            }
        } catch (Exception e) {
            System.out.println("Excepcion validarAutorizador: " + e);
            resul = false;
            throw e;
        }
        return resul;
    }

    @Override
    public boolean validarUsuarioRegistrado(String usuario, String nitEmpresa) throws Exception {
//        System.out.println(this.getClass().getName() + ".validarUsuarioRegistrado()");
//        EntityManagerFactory emf;
        boolean resul = false;
        EntityManagerFactory emf;
        try {
            emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
            if (emf != null) {
                EntityManager em = emf.createEntityManager();
//                persistenciaConexionInicial.setearKiosko(em);
                resul = persistenciaConexionInicial.validarUsuarioRegistrado(em, usuario, nitEmpresa);
                em.close();
//                emf.close();
            }
        } catch (Exception e) {
            System.out.println("validarUsuarioRegistrado-1: " + e);
            resul = false;
            throw e;
        }
        /*if (!resul) {
            try {
                EntityManager em = emf.createEntityManager();
                resul = persistenciaConexionInicial.validarUsuarioRegistrado(em, usuario);
                em.close();
            } catch (Exception e) {
                System.out.println("validarUsuarioRegistrado-2 " + e);
                resul = false;
                throw e;
            }
        }*/
        return resul;
    }

    @Override
    public boolean validarEstadoUsuario(String usuario, String nitEmpresa) {
//        System.out.println(this.getClass().getName() + ".validarEstadoUsuario()");
        EntityManagerFactory emf;
        boolean resul = false;
        try {
            emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
            if (emf != null) {
                EntityManager em = emf.createEntityManager();
                resul = persistenciaConexionInicial.validarEstadoUsuario(em, usuario, nitEmpresa);
                em.close();
//                emf.close();
            }
        } catch (Exception e) {
            System.out.println("validarEstadoUsuario-1: " + e);
            resul = false;
        }
        if (!resul) {
            try {
                emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
                if (emf != null) {
                    EntityManager em = emf.createEntityManager();
                    resul = persistenciaConexionInicial.validarEstadoUsuario(em, usuario);
                    em.close();
                }
            } catch (Exception e) {
                System.out.println("validarEstadoUsuario-2: " + e);
                resul = false;
            }
        }
        return resul;
    }

    @Override
    public boolean validarIngresoUsuarioRegistrado(String usuario, String clave, String nitEmpresa) {
//        System.out.println(this.getClass().getName() + ".validarIngresoUsuarioRegistrado()");
        EntityManagerFactory emf;
        boolean resul = false;
        
        try {
            emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
            if (emf != null) {
                EntityManager em = emf.createEntityManager();
                resul = persistenciaConexionInicial.validarIngresoUsuarioRegistrado(em, usuario, clave, nitEmpresa);
                em.close();
//                emf.close();
            }
        } catch (Exception e) {
            System.out.println("validarIngresoUsuarioRegistrado-1: " + e);
            resul = false;
        }
        /*if (!resul) {
            try {
                emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
                if (emf != null) {
                    EntityManager em = emf.createEntityManager();
                    resul = persistenciaConexionInicial.validarIngresoUsuarioRegistrado(em, usuario, clave);
                    em.close();
                }
            } catch (Exception e) {
                System.out.println("validarIngresoUsuarioRegistrado-2: " + e);
                resul = false;
            }
        }*/
        return resul;
    }

    @Override
    public boolean adicionarConexionUsuario(String idSesion) throws Exception {
//        System.out.println(this.getClass().getName() + ".adicionarConexionUsuario()");
        boolean resul = false;
        EntityManagerFactory emf;
        try {
            ISesionEntityManagerFactory sessionEMF = new SesionEntityManagerFactory();
            SessionEntityManager sem = new SessionEntityManager(idSesion, unidadPersistencia);
            administrarSessiones.adicionarSesion(sem);
            emf = sessionEMF.crearConexionUsuario(sem.getUnidadPersistencia());
            EntityManager em = emf.createEntityManager();
            persistenciaConexionInicial.setearKiosko(em);
            em.close();
            resul = true;
        } catch (Exception e) {
            System.out.println("Error general: " + e);
            resul = false;
            throw e;
        }
        return resul;
    }

    @Override
    public boolean adicionarConexionUsuario(String idSesion, String esquema) throws Exception {
//        System.out.println(this.getClass().getName() + ".adicionarConexionUsuario()");
        boolean resul = false;
        EntityManagerFactory emf;
        try {
            ISesionEntityManagerFactory sessionEMF = new SesionEntityManagerFactory();
            SessionEntityManager sem = new SessionEntityManager(idSesion, unidadPersistencia);
            administrarSessiones.adicionarSesion(sem);
            emf = sessionEMF.crearConexionUsuario(sem.getUnidadPersistencia());
            EntityManager em = emf.createEntityManager();
            persistenciaConexionInicial.setearKiosko(em, esquema);
            em.close();
            resul = true;
        } catch (Exception e) {
            System.out.println("Error general: " + e);
            resul = false;
            throw e;
        }
        return resul;
    }

    @Override
    public ConexionesKioskos obtenerConexionEmpelado(String codigoEmpleado, String nitEmpresa) {
//        System.out.println(this.getClass().getName() + ".obtenerConexionEmpelado()");
//        System.out.println("codigoEmpleado: " + codigoEmpleado);
//        System.out.println("nitEmpresa: " + nitEmpresa);
        boolean control = false;
        try {
            long nit = Long.parseLong(nitEmpresa);
//            System.out.println("nit revisado: " + nit);
            control = true;
        } catch (NumberFormatException nfe) {
            System.out.println("Excepcion por formato numerico invalido.");
            System.out.println("NIT invalido");
            System.out.println("Revisar el archivo XML de configuracion de empresas");
        }
        try {
            long codEmpl = Long.parseLong(codigoEmpleado);
//            System.out.println("Codigo empleado revisado: " + codEmpl);
            control = control && true;
        } catch (NumberFormatException nfe) {
            System.out.println("Excepcion por formato numerico invalido.");
            System.out.println("Codigo de empleado invalido");
        }
        EntityManagerFactory emf;
        ConexionesKioskos resul = null;
        if (control) {
            try {
                emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
                if (emf != null) {
                    EntityManager em = emf.createEntityManager();
                    resul = persistenciaConexionesKioskos.consultarConexionEmpleado(em, codigoEmpleado, Long.parseLong(nitEmpresa));
                    em.close();
//                    emf.close();
                }
            } catch (Exception e) {
                System.out.println("obtenerConexionEmpelado: Excepcion al consultar el empleado.");
                System.out.println(e);
            }
            if (resul == null) {
                try {
                    emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
                    if (emf != null) {
                        EntityManager em = emf.createEntityManager();
                        resul = persistenciaConexionesKioskos.consultarConexionEmpleado(em, codigoEmpleado);
                        em.close();
//                    emf.close();
                    }
                } catch (Exception e) {
                    System.out.println("obtenerConexionEmpelado: Excepcion al consultar la persona.");
                    System.out.println(e);
                }
            }
        }
        return resul;
    }

    @Override
    public boolean bloquearUsuario(String codigoEmpleado, String nitEmpresa) {
//        System.out.println(this.getClass().getName() + ".bloquearUsuario()");
        EntityManagerFactory emf;
        EntityManager em;
        boolean resul = false;
        ConexionesKioskos cnx = obtenerConexionEmpelado(codigoEmpleado, nitEmpresa);
        cnx.setActivo("N");
        try {
            emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
            if (emf != null) {
                em = emf.createEntityManager();
                resul = persistenciaConexionesKioskos.registrarConexion(em, cnx);
                em.close();
//                emf.close();
            }
        } catch (Exception e) {
            System.out.println("Excepcion al bloquearUsuario");
            System.out.println(e);
        }
        return resul;
    }

    @Override
    public void modificarUltimaConexion(ConexionesKioskos cnx) {
//        System.out.println(this.getClass().getName() + ".modificarUltimaConexion()");
        EntityManagerFactory emf;
        try {
            emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
            if (emf != null) {
                EntityManager em = emf.createEntityManager();
                persistenciaConexionesKioskos.registrarConexion(em, cnx);
                em.close();
//                emf.close();
            }
        } catch (Exception e) {
            System.out.println("Error general " + "modificarUltimaConexion" + ": " + e);
        }

    }

    @Override
    public void cerrarSession(String idSesion) {
//        System.out.println(this.getClass().getName() + ".modificarUltimaConexion()");
        EntityManagerFactory emf;
        try {
            emf = sessionEMF.crearConexionUsuario(unidadPersistencia);
            administrarSessiones.borrarSesion(idSesion);
            emf.close();
        } catch (Exception e) {
            System.out.println("Error general " + "cerrarSession" + ": " + e);
        }
    }
}//Fin de la clase.
