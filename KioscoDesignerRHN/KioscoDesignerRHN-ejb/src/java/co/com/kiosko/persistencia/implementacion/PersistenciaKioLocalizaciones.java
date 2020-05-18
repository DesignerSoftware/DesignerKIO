package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.KioLocalizaciones;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import javax.ejb.Stateless;
import co.com.kiosko.persistencia.interfaz.IPersistenciaKioLocalizaciones;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Edwin Hastamorir
 */
@Stateless
public class PersistenciaKioLocalizaciones implements IPersistenciaKioLocalizaciones {

    @EJB
    IPersistenciaEmpleados persistenciaEmpleados;

    @Override
    public List<KioLocalizaciones> consultarKioLocalizaciones(EntityManager em, BigInteger secuenciaEmpresa) throws Exception {
        System.out.println(this.getClass().getName() + "." + "consultarKioLocalizaciones" + "()");
        List<KioLocalizaciones> listaKioLocalizaciones;
        try {
            if (em != null && em.isOpen()) {
                em.joinTransaction();
                String consulta = "select kl.secuencia, kl.kioviglocalizacion, kvl.fechavigencia,  kl.codigo, kl.nombre "
                        + "from kioviglocalizaciones kvl, empresas em, kiolocalizaciones kl "
                        + "where em.secuencia = kvl.empresa "
                        + "and em.secuencia = ? "
                        + "and kl.kioviglocalizacion = kvl.secuencia "
                        + "and kvl.fechavigencia = (select max(kvli.fechavigencia) "
                        + "from kioviglocalizaciones kvli "
                        + "where kvli.empresa = kvl.empresa "
                        + "and kvli.fechavigencia < sysdate) ";
                Query query = em.createNativeQuery(consulta, KioLocalizaciones.class);
                query.setParameter(1, secuenciaEmpresa);
                listaKioLocalizaciones = query.getResultList();
                System.out.println(this.getClass().getName() + "." + "consultarKioLocalizaciones" + ".tamagnoResultado: " + listaKioLocalizaciones.size());
//                return listaKioLocalizaciones;
            } else {
                listaKioLocalizaciones = null;
                System.out.println(this.getClass().getName() + "." + "consultarKioLocalizaciones" + " EntityManager nulo");
            }
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + "." + "consultarKioLocalizaciones" + ex);
            throw new Exception(ex);
        }
        return listaKioLocalizaciones;
    }

    @Override
    public KioLocalizaciones consultarKioLocalizacion(EntityManager em, BigDecimal secuenciaKioLocalizacion) throws Exception {
        System.out.println(this.getClass().getName() + "." + "consultarKioLocalizacion" + "()");
        KioLocalizaciones kioLocalizacion;
        Object resultado;
        try {
            if (em != null && em.isOpen()) {
                em.joinTransaction();
                String consulta = "select kl.secuencia, kl.kioviglocalizacion, kl.codigo, kl.nombre "
                        + "from kioviglocalizaciones kvl, kiolocalizaciones kl "
                        + "where kl.kioviglocalizacion = kvl.secuencia "
                        + "and kl.secuencia = ? ";
                Query query = em.createNativeQuery(consulta, KioLocalizaciones.class);
                query.setParameter(1, secuenciaKioLocalizacion);
                resultado = query.getSingleResult();
                if (resultado instanceof KioLocalizaciones) {
                    kioLocalizacion = (KioLocalizaciones) resultado;
                } else {
                    kioLocalizacion = null;
                    System.out.println(this.getClass().getName() + "." + "consultarKioLocalizaciones" + " tipo de dato erroneo");
                }
            } else {
                kioLocalizacion = null;
                System.out.println(this.getClass().getName() + "." + "consultarKioLocalizaciones" + " EntityManager nulo");
            }
        } catch (Exception ex) {
            System.out.println(this.getClass().getName() + "." + "consultarKioLocalizaciones" + ex);
            throw new Exception(ex);
        }
        return kioLocalizacion;
    }
}
