package co.com.kiosko.utilidadesUI;

/**
 *
 * @author Edwin
 */
public class OpcionLista {

    private String valorMostrar;
    private String valor;

    public OpcionLista() {
    }

    public OpcionLista(String valorMostrar, String valor) {
        this.valorMostrar = valorMostrar;
        this.valor = valor;
    }

    public String getValorMostrar() {
        return valorMostrar;
    }

    public void setValorMostrar(String valorMostrar) {
        this.valorMostrar = valorMostrar;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (this.valor != null ? this.valor.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OpcionLista other = (OpcionLista) obj;
        if ((this.valor == null) ? (other.valor != null) : !this.valor.equals(other.valor)) {
            return false;
        }
        return true;
    }
    
    

    @Override
    public String toString() {
        return "OpcionesLista{" + "valorMostrar=" + valorMostrar + ", valor=" + valor + '}';
    }

}
