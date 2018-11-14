package co.com.kiosko.clasesAyuda.personalizaModulos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class AuditoriaModulo {

    private String codigo;
    private List<EmailMod> emailMods;

    public AuditoriaModulo() {
        emailMods = new ArrayList<EmailMod>();
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public List<EmailMod> getEmailMods() {
        return emailMods;
    }

    public void setEmailMods(List<EmailMod> emails) {
        this.emailMods = emails;
    }

    public void adicionarCorreoModulo(EmailMod emailMod) {
        emailMods.add(emailMod);
    }

    @Override
    public String toString() {
        return "AuditoriaModulo{" + "codigo=" + codigo + ", emails=" + emailMods + '}';
    }

}
