package co.com.kiosko.reportes;

import java.io.File;
import java.io.Serializable;
import java.sql.Connection;
//import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@Stateless
public class IniciarReporte implements IniciarReporteInterface, Serializable {

    //@Override
    private Connection inicarC(EntityManager em) {
        Connection conexion = null;
        try {
//            em.getTransaction().begin();
            conexion = em.unwrap(java.sql.Connection.class);
//            em.getTransaction().commit();
        } catch (Exception e) {
            System.out.println("Error: " + this.getClass().getName() + ".iniciarC()");
            System.out.println("Causa: " + e);
        }
        return conexion;
    }

    @Override
    public String ejecutarReporte(String nombreReporte, String rutaReporte, String rutaGenerado, String nombreArchivo, String tipoReporte, Map parametros, EntityManager em) {
        Connection conexion = null;
        try {
            /*
             * System.out.println("Valores: " + parametros.values());
             * System.out.println("INICIARREPORTE NombreReporte: " +
             * nombreReporte); System.out.println("INICIARREPORTE rutaReporte: "
             * + rutaReporte); System.out.println("INICIARREPORTE rutaGenerado:
             * " + rutaGenerado); System.out.println("INICIARREPORTE
             * nombreArchivo: " + nombreArchivo);
             * System.out.println("INICIARREPORTE tipoReporte: " + tipoReporte);
             * System.out.println("INICIARREPORTE parametros: " + parametros);
             */
            File archivo = new File(rutaReporte + nombreReporte + ".jasper");
            conexion = inicarC(em);
            JasperReport masterReport;
            masterReport = (JasperReport) JRLoader.loadObject(archivo);
            JasperPrint imprimir = JasperFillManager.fillReport(masterReport, parametros, conexion);
            String outFileName = rutaGenerado + nombreArchivo;
            Exporter exporter = null;

            if (tipoReporte.equals("PDF")) {
                exporter = new JRPdfExporter();
            }

            if (exporter != null) {
                List<JasperPrint> jpl = new ArrayList<JasperPrint>();
                jpl.add(imprimir);
                exporter.setExporterInput(SimpleExporterInput.getInstance(jpl));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outFileName));
                exporter.exportReport();
            }
            //cerrarConexion();
//            if (em.getTransaction().isActive()) {
//                System.out.println("Cerrando Transaccion reporte.");
//                em.getTransaction().commit();
//            }
            return outFileName;
        } catch (JRException e) {
            System.out.println("Error: IniciarReporte.ejecutarReporte: " + e);
            //System.out.println(e.getStackTrace());
            System.out.println("************************************");
            //e.printStackTrace();
            if (e.getCause() != null) {
                return "Error: INICIARREPORTE " + e.toString() + "\n" + e.getCause().toString();
            } else {
                return "Error: INICIARREPORTE " + e.toString();
            }
        }
    }

    /*@Override
    public void cerrarConexion() {
        Connection conexion = null;
        try {
            conexion.close();
        } catch (SQLException se) {
            System.out.println("Error SQL al cerrar: " + se);
            System.out.println("Error causa: " + se.getCause());
        } catch (Exception e){
            System.out.println("Error general al cerrar: " + e);
            System.out.println("Error causa: " + e.getCause());
        }
    }*/
}
