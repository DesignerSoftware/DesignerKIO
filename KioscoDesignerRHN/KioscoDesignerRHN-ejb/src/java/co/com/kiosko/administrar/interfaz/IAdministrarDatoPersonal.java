/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.administrar.interfaz;

import co.com.kiosko.entidades.Empresas;
import javax.ejb.Local;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author usuario
 */
@Local
public interface IAdministrarDatoPersonal {
    public EntityManagerFactory obtenerConexion(String idSesion);
    public Empresas consultarEmpresa(String idSesion, long nit);
}
