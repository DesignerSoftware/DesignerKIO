package co.com.kiosko.clasesAyuda.personalizaModulos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Edwin Hastamorir
 */
public class ModuloConfig {
    private String nombre;
    private List<EmpresaModulo> empresas;

    public ModuloConfig(String nombre) {
        this.nombre = nombre;
        empresas = new ArrayList<EmpresaModulo>();
    }

    public void adicionarEmpresa(EmpresaModulo empresa){
        empresas.add(empresa);
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<EmpresaModulo> getEmpresas() {
        return empresas;
    }

    @Override
    public String toString() {
        return "ModuloConfig{" + "nombre=" + nombre + ", empresas=" + empresas + '}';
    }
    
}
