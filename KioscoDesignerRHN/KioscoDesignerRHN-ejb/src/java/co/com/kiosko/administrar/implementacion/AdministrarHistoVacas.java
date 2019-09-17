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
import co.com.kiosko.entidades.Personas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioEstadosSolici;
import java.io.Serializable;
//import java.math.BigDecimal;
import java.math.BigInteger;
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

    private transient EntityManagerFactory emf;
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
    public List<Empleados> consultarEmpleadosJefe(long nit, Empleados emplJefe) throws Exception {
        EntityManager em = emf.createEntityManager();
        try {
            return persistenciaEmpleados.consultarEmpleadosXJefe(em, nit, emplJefe.getSecuencia());
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
        } else if ("SIN PROCESAR".equalsIgnoreCase(estado)) {
            listaEstados.clear();
            listaEstados.add("ENVIADO");
        } else {
            listaEstados.clear();
            listaEstados.add(estado);
        }
        ArrayList listaResul = new ArrayList();
        List listaTMP;
        for (int i = 0; i < listaEstados.size(); i++) {
            EntityManager em = emf.createEntityManager();
            if (emplJefe == null) {
                try {
                    listaTMP = persistenciaKioEstadosSolici.consultarEstadosXEmpre(em, empresa.getSecuencia(), listaEstados.get(i));
                    System.out.println("listaTMP-1: " + listaTMP.size());
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
                    System.out.println("listaTMP-2: " + listaTMP.size());
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (em != null && em.isOpen()) {
                        em.close();
                    }
                }
            }
            listaResul.addAll(listaTMP);
            System.out.println("listaResul-1: " + listaResul.size());
        }
        return listaResul;
    }

    @Override
    public List<KioEstadosSolici> consultarEstadoSoliciEmpre(long nit, String estado, Personas autorizador) throws Exception {
        System.out.println(this.getClass().getName() + ".consultarEstadoSoliciEmpre()-4");
        ArrayList<String> listaEstados = new ArrayList<String>();
        BigInteger secEmpresa = BigInteger.ZERO;
        try {
            EntityManager em = emf.createEntityManager();
            secEmpresa = persistenciaEmpleados.consultarEmpresaXNit(em, nit);
            em.close();
        } catch (Exception e){
            System.out.println("consultarEstadoSoliciEmpre:exce: "+e);
        }
        if ("PROCESADO".equalsIgnoreCase(estado)) {
            System.out.println("consultarEstadoSoliciEmpre-PROCESADO");
            listaEstados.clear();
            listaEstados.add("AUTORIZADO");
            listaEstados.add("LIQUIDADO");
            listaEstados.add("RECHAZADO");
        } else if ("SIN PROCESAR".equalsIgnoreCase(estado)) {
            listaEstados.clear();
            listaEstados.add("ENVIADO");
        } else {
            listaEstados.clear();
            listaEstados.add(estado);
        }
        ArrayList listaResul = new ArrayList();
        List listaTMP;
        for (int i = 0; i < listaEstados.size(); i++) {
            EntityManager em = emf.createEntityManager();
            /*if (autorizador == null) {
                try {
                    listaTMP = persistenciaKioEstadosSolici.consultarEstadosXEmpre(em, secEmpresa, listaEstados.get(i));
                    System.out.println("listaTMP-1: " + listaTMP.size());
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (em != null && em.isOpen()) {
                        em.close();
                    }
                }
            } else {*/
            if (autorizador != null) {
                System.out.println("consultarEstadoSoliciEmpre-else");
                try {
                    listaTMP = persistenciaKioEstadosSolici.consultarEstadosXEmpre(em, listaEstados.get(i), autorizador.getSecuencia());
                    System.out.println("listaTMP-2: " + listaTMP.size());
                } catch (Exception e) {
                    throw e;
                } finally {
                    if (em != null && em.isOpen()) {
                        em.close();
                    }
                }
            }else{
                listaTMP = new ArrayList();
            }
            listaResul.addAll(listaTMP);
            System.out.println("listaResul-1: " + listaResul.size());
        }
        return listaResul;
    }
    @Override
    public Empresas consultarInfoEmpresa(long nit){
        Empresas empresa = null;
        EntityManager em = null;
        try{
            em = emf.createEntityManager();
            empresa = persistenciaEmpleados.consultarEmpresa(em, nit);
        }catch (Exception e){
            System.out.println("consultarInfoEmpresa-excepcion: "+e.getMessage());
        }finally{
            if (em != null && em.isOpen()){
                em.close();
            }
        }
        return empresa;
    }
    
    @Override
    public List<Empleados> consultarEmpleadosAutorizador(long nit, Personas per) throws Exception {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            return persistenciaEmpleados.consultarEmpleadosXAutorizador(em, nit, per.getSecuencia());
        } catch (Exception e) {
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}