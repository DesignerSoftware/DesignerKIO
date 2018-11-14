package co.com.kiosko.clasesAyuda.personalizaModulos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author user
 */
public class EmailMod {

    List<String> cuentas;

    public EmailMod() {
        cuentas = new ArrayList<String>();
    }

    public void adicionarCuenta(String cuenta) {
        cuentas.add(cuenta);
    }

    public void removerCuenta(String cuenta) {
        cuentas.remove(encontrarCuenta(cuenta));
    }

    public void removerCuenta(int pos) {
        cuentas.remove(pos);
    }

    public int encontrarCuenta(String cuentaIn) {
        for (int i = 0; i < cuentas.size(); i++) {
            if (cuentas.get(i).equalsIgnoreCase(cuentaIn)) {
                return i;
            }
        }
        return -1;
    }

    public List<String> getCuentas() {
        return cuentas;
    }

    @Override
    public String toString() {
        return "EmailMod{" + "cantidad cuentas=" + cuentas + '}';
    }

}
