/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.administrar.implementacion;

import co.com.kiosko.administrar.interfaz.IAdministrarDatoPersonal;
import co.com.kiosko.administrar.interfaz.IAdministrarSesiones;
import co.com.kiosko.entidades.Empresas;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author usuario
 */
@Stateless
public class AdministrarDatoPersonal implements IAdministrarDatoPersonal {

    @EJB
    private IAdministrarSesiones administrarSesiones;
    @EJB
    private IPersistenciaEmpleados persistenciaEmpleados;
    
    @Override
    public EntityManagerFactory obtenerConexion(String idSesion) {
        return administrarSesiones.obtenerConexionSesion(idSesion);
    }
    @Override
    public Empresas consultarEmpresa(String idSesion, long nit){
        Empresas empresa = null;
        EntityManager em = null;
        try{
            em = obtenerConexion(idSesion).createEntityManager();
            empresa = persistenciaEmpleados.consultarEmpresa(em, nit);
        }catch (Exception e){
            System.out.println("consultarEmpresa-excepcion: "+e.getMessage());
        }finally{
            if (em != null && em.isOpen()){
                em.close();
            }
        }
        return empresa;
    }
}
