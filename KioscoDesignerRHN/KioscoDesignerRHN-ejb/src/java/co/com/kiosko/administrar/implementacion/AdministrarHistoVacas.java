/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
//import java.math.BigDecimal;
//import java.math.BigInteger;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author Edwin
 */
@Stateful
public class AdministrarHistoVacas implements IAdministrarHistoVacas {

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
        try{
            return persistenciaEmpleados.consultarEmpleadosEmpresa(em, nit);
        } catch (Exception e){
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    @Override
    public List<KioEstadosSolici> consultarEstadoSoliciEmpl(Empleados empl) throws Exception {
        System.out.println(this.getClass().getName()+".consultarEstadoSoliciEmpl()");
        System.out.println("consultarEstadoSoliciEmpl: Empleado: "+empl.getSecuencia());
        EntityManager em = emf.createEntityManager();
        try{
            return persistenciaKioEstadosSolici.consultarEstadosXEmpl(em, empl.getSecuencia());
        } catch (Exception e){
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
    @Override
    public List<KioEstadosSolici> consultarEstadoSoliciEmpre(Empresas empresa) throws Exception {
        System.out.println(this.getClass().getName()+".consultarEstadoSoliciEmpre()");
        System.out.println("consultarEstadoSoliciEmpre: Empresa: "+empresa.getSecuencia());
        EntityManager em = emf.createEntityManager();
        try{
            return persistenciaKioEstadosSolici.consultarEstadosXEmpre(em, empresa.getSecuencia());
        } catch (Exception e){
            throw e;
        } finally {
            if (em != null && em.isOpen()) {
                em.close();
            }
        }
    }
}
