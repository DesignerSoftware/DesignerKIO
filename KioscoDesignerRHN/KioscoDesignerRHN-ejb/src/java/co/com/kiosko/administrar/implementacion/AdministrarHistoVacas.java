package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.persistence.EntityManagerFactory;
import co.com.kiosko.administrar.interfaz.IAdministrarHistoVacas;
import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.entidades.Empresas;
import co.com.kiosko.entidades.KioEstadosSolici;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioEstadosSolici;
import java.io.Serializable;
import java.util.ArrayList;
//import java.math.BigDecimal;
//import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Edwin
 */
@Stateful
public class AdministrarHistoVacas implements IAdministrarHistoVacas, Serializable {

    private EntityManagerFactory emf;
    private String idSesion;

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;
    @EJB
    private IPersistenciaKioEstadosSolici persistenciaKioEstadosSolici;

    @Override
    public void obtenerConexion(String idSesion) {
        emf = administrarSesiones.obtenerConexionSesion(idSesion);
    }

    @Override
    public List<Empleados> consultarEmpleadosEmpresa(long nit) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            return persistenciaEmpleados.consultarEmpleadosEmpresa(em, nit);
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<KioEstadosSolici> consultarEstadoSoliciEmpl(Empleados empl) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarEstadoSoliciEmpl()");
        System.out.println("consultarEstadoSoliciEmpl: Empleado: " + empl.getSecuencia());
        EntityManager em = emf.createEntityManager();
        try {
            return persistenciaKioEstadosSolici.consultarEstadosXEmpl(em, empl.getSecuencia());
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<KioEstadosSolici> consultarEstadoSoliciEmpre(Empresas empresa) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarEstadoSoliciEmpre()");
        System.out.println("consultarEstadoSoliciEmpre: Empresa: " + empresa.getSecuencia());
        EntityManager em = emf.createEntityManager();
        try {
            return persistenciaKioEstadosSolici.consultarEstadosXEmpre(em, empresa.getSecuencia());
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<KioEstadosSolici> consultarEstadoSoliciEmpre(Empresas empresa, String estado) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarEstadoSoliciEmpre()-2");
        System.out.println("consultarEstadoSoliciEmpre: Empresa: " + empresa.getSecuencia());
        EntityManager em = emf.createEntityManager();
        try {
            return persistenciaKioEstadosSolici.consultarEstadosXEmpre(em, empresa.getSecuencia(), estado);
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }

    @Override
    public List<KioEstadosSolici> consultarEstadoSoliciEmpre(Empresas empresa, String estado, Empleados emplJefe) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarEstadoSoliciEmpre()-3");
        System.out.println("consultarEstadoSoliciEmpre: Empresa: " + empresa.getSecuencia());
        ArrayList<String> listaEstados = new ArrayList<String>();
        if ("PROCESADO".equalsIgnoreCase(estado)) {
            System.out.println("consultarEstadoSoliciEmpre-PROCESADO");
            listaEstados.clear();
            listaEstados.add("AUTORIZADO");
            listaEstados.add("LIQUIDADO");
            listaEstados.add("RECHAZADO");
        }else if ("SIN PROCESAR".equalsIgnoreCase(estado)) {
            listaEstados.clear();
            listaEstados.add("ENVIADO");
        } else {
            listaEstados.clear();
            listaEstados.add(estado);
        }
        ArrayList listaResul = new ArrayList<KioEstadosSolici>();
        List listaTMP;
        for (int i = 0; i < listaEstados.size(); i++) {
            EntityManager em = emf.createEntityManager();
            if (emplJefe == null) {
                try {
                    listaTMP = persistenciaKioEstadosSolici.consultarEstadosXEmpre(em, empresa.getSecuencia(), listaEstados.get(i));
                    System.out.println("listaTMP-1: "+listaTMP.size());
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (em != null && em.isOpen()) {
                        em.close();
                    }
                }
            } else {
                System.out.println("consultarEstadoSoliciEmpre-else");
                try {
                    listaTMP = persistenciaKioEstadosSolici.consultarEstadosXEmpre(em, empresa.getSecuencia(), listaEstados.get(i), emplJefe.getSecuencia());
                    System.out.println("listaTMP-2: "+listaTMP.size());
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (em != null && em.isOpen()) {
                        em.close();
                    }
                }
            }
            listaResul.addAll(listaTMP);
            System.out.println("listaResul-1: "+listaResul.size());
        }
        return listaResul;
    }
}
