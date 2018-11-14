package co.com.kiosko.clasesAyuda.personalizaModulos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Edwin Hastamorir
 */
public class EmpresaModulo {
    private String nit;
    private List<AuditoriaModulo> auditorias;

    public EmpresaModulo(String nit) {
        this.nit = nit;
        auditorias = new ArrayList<AuditoriaModulo>();
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }
    public void adicionarAuditorias(AuditoriaModulo auditoria){
        auditorias.add(auditoria);
    }
    public List<AuditoriaModulo> getAuditorias(){
        return auditorias;
    }

    @Override
    public String toString() {
        return "EmpresaModulo{" + "nit=" + nit + ", auditoria=" + auditorias + '}';
    }

}
