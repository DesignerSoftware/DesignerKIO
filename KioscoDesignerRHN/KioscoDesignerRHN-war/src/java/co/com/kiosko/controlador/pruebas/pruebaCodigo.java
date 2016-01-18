package co.com.kiosko.controlador.pruebas;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author 908036
 */
public class pruebaCodigo {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);
        // Match the given input against this pattern
        Matcher matcher = pattern.matcher("3%&$&@gmail.com.co");
        System.out.println("RESULTADO: " + matcher.matches());
    }
}
