package co.com.kiosko.clasesAyuda;

//import javax.annotation.PreDestroy;
//import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 * @author Edwin Hastamorir
 * @version 2.0
 */
public class SessionEntityManager {

    private String idSession;
    private String unidadPersistencia;
    private EntityManagerFactory emf;
//    private EntityManager em;

//    public SessionEntityManager(String idSession, EntityManagerFactory emf) {
//        this.idSession = idSession;
//        this.emf = emf;
//    }

    public SessionEntityManager(String idSession, String unidadPersistencia) {
        this.idSession = idSession;
        this.unidadPersistencia = unidadPersistencia;
    }
    public SessionEntityManager(String idSession, String unidadPersistencia, EntityManagerFactory emf){
        this.idSession = idSession;
        this.unidadPersistencia = unidadPersistencia;
        this.emf = emf;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void cerrarEMF() {
        emf.close();
    }

    public String getIdSession() {
        return idSession;
    }

    public void setIdSession(String idSession) {
        this.idSession = idSession;
    }

//    public EntityManager getEm() {
//        if (emf.isOpen()) {
//            em = emf.createEntityManager();
//        }
//        return em;
//    }

//    public void setEm(EntityManager em) {
//        this.em = em;
//    }

//    @PreDestroy
//    public void destruct() {
//        emf.close();
//    }

    public String getUnidadPersistencia() {
        return unidadPersistencia;
    }

    public void setUnidadPersistencia(String unidadPersistencia) {
        System.out.println(this.getClass().getName()+".setUnidadPersistencia()");
        System.out.println("unidad de persistencia: "+this.unidadPersistencia);
        this.unidadPersistencia = unidadPersistencia;
    }
    
}
