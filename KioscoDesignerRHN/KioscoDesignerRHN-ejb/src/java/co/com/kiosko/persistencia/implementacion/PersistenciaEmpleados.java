package co.com.kiosko.persistencia.implementacion;

import co.com.kiosko.entidades.Empleados;
import co.com.kiosko.persistencia.interfaz.IPersistenciaEmpleados;
import java.math.BigInteger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;

/**
 *
 * @author Felipe Triviño
 */
@Stateless
public class PersistenciaEmpleados implements IPersistenciaEmpleados {

    @Override
    public Empleados consultarEmpleado(EntityManager eManager, BigInteger codigoEmpleado, long nit) {
        //System.out.println(this.getClass().getName() + ".consultarEmpleado()");
        //System.out.println("codigoEmpleado: " + codigoEmpleado);
        //System.out.println("nit: " + nit);
        try {
//            eManager.getTransaction().begin();
            //System.out.println("Primera consulta");
            String sqlQuery = "SELECT e FROM Empleados e WHERE e.codigoempleado = :codigoEmpleado";
            Query query = eManager.createQuery(sqlQuery);
            query.setParameter("codigoEmpleado", codigoEmpleado);
            Empleados emp = (Empleados) query.getSingleResult();
//            eManager.getTransaction().commit();
            return emp;
        } catch (NonUniqueResultException nure) {
            System.out.println("Hay más de un empleado con el mismo código.");
            System.out.println("Error PersistenciaEmpleados.consultarEmpleado: " + nure);
//            eManager.getTransaction().rollback();
            try {
//                eManager.getTransaction().begin();
                //System.out.println("Segunda consulta");
                String sqlQuery = "SELECT e FROM Empleados e WHERE e.codigoempleado = :codigoEmpleado AND e.empresa.nit = :nit ";
                Query query = eManager.createQuery(sqlQuery);
                query.setParameter("codigoEmpleado", codigoEmpleado);
                query.setParameter("nit", nit);
                Empleados emp = (Empleados) query.getSingleResult();
//                eManager.getTransaction().commit();
                return emp;
            } catch (Exception e) {
                System.out.println("Error PersistenciaEmpleados.consultarEmpleado: " + e);
                try {
//                    eManager.getTransaction().rollback();
                } catch (NullPointerException npe) {
                    System.out.println("Error de nulo en la transacción.");
                }
                return null;
            }
        } catch (NullPointerException npe) {
            System.out.println("No hay empleado con el código dado.");
            System.out.println("Error PersistenciaEmpleados.consultarEmpleado: " + npe);
            try {
//                eManager.getTransaction().rollback();
            } catch (NullPointerException npe2) {
                System.out.println("Error 2 de nulo en la transacción");
            }
            return null;
        }
    }
}
