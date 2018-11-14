package co.com.kiosko.clasesAyuda.personalizaModulos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Edwin Hastamorir
 */
public class ConfigModulos {
    private List<ModuloConfig> modulos;

    public ConfigModulos() {
        modulos = new ArrayList<ModuloConfig>();
    }
    public void adicionarModulo(ModuloConfig modulo){
        modulos.add(modulo);
    }

    public List<ModuloConfig> getModulos() {
        return modulos;
    }

    @Override
    public String toString() {
        return "ConfigModulos{" + "modulos=" + modulos + '}';
    }
    
}
