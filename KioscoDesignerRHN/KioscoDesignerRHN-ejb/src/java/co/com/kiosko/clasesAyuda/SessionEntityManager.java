package co.com.kiosko.clasesAyuda;

import javax.annotation.PreDestroy;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Felipe Triviño
 */
public class SessionEntityManager {

    private String idSession;
    private EntityManagerFactory emf;
    private EntityManager em;

    public SessionEntityManager(String idSession, EntityManagerFactory emf) {
        this.idSession = idSession;
        this.emf = emf;
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

    public EntityManager getEm() {
        if (emf.isOpen()) {
            em = emf.createEntityManager();
        }
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }
	@PreDestroy
	public void destruct(){
		emf.close();
	}
}
