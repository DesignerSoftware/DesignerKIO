/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.persistencia.interfaz;

import co.com.kiosko.entidades.KioLocalizaciones;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityManager;

/**
 *
 * @author usuario
 */
@Local
public interface IPersistenciaKioLocalizaciones {
    public List<KioLocalizaciones> consultarKioLocalizaciones(EntityManager em, BigInteger secuenciaEmpresa) throws Exception ;

    public KioLocalizaciones consultarKioLocalizacion(EntityManager em, BigDecimal secuenciaKioLocalizacion) throws Exception;
}
