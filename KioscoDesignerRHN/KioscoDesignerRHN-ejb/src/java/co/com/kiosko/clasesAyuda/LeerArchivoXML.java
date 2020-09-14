package co.com.kiosko.clasesAyuda;

//import co.com.kiosko.clasesAyuda.CadenasKioskos;
//import co.com.kiosko.clasesAyuda.interfaz.ILeerArchivoXML;
import co.com.kiosko.clasesAyuda.personalizaModulos.AuditoriaModulo;
import co.com.kiosko.clasesAyuda.personalizaModulos.ConfigModulos;
import co.com.kiosko.clasesAyuda.personalizaModulos.EmailMod;
import co.com.kiosko.clasesAyuda.personalizaModulos.EmpresaModulo;
import co.com.kiosko.clasesAyuda.personalizaModulos.ModuloConfig;
//import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//import javax.ejb.Stateful;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Felipe Triviño, Edwin Hastamorir
 */
//@Stateful
//public class LeerArchivoXML implements ILeerArchivoXML, Serializable {
//public class LeerArchivoXML implements Serializable {
public class LeerArchivoXML {

//    private static LeerArchivoXML instance;
    private List<CadenasKioskos> listaCadenas;
    private ConfigModulos configModulos;

    public LeerArchivoXML() {
//        listaCadenas = new ArrayList<CadenasKioskos>();
//        configModulos = null;
    }

//    private LeerArchivoXML() {
//        listaCadenas = new ArrayList<CadenasKioskos>();
////        configModulos = null;
//    }
//    public static LeerArchivoXML getInstance() {
//        if (instance == null) {
//            synchronized (LeerArchivoXML.class) {
//                instance = new LeerArchivoXML();
//            }
//        }
//        return instance;
//    }
//    @Override
    public List<CadenasKioskos> leerArchivoEmpresasKiosko() {
//        InputStream fXmlFile = null;
        try {
            listaCadenas = new ArrayList<CadenasKioskos>();
//            listaCadenas.clear();
            InputStream fXmlFile = getClass().getResourceAsStream("../archivosConfiguracion/cadenasKioskos.xml");
//            InputStream fXmlFile = getClass().getResourceAsStream("../../archivosConfiguracion/cadenasKioskos.xml");
//            fXmlFile = new FileInputStream("/../../archivosConfiguracion/cadenasKioskos.xml");
//            fXmlFile = new FileInputStream("..//archivosConfiguracion//cadenasKioskos.xml");
            //File fXmlFile = new File("cadenasKioskos.xml");
//            int data = fXmlFile.read();
//            while (data != -1) {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("cadenaKiosko");
//            List<CadenasKioskos> listaCadenas = new ArrayList<CadenasKioskos>();
//            listaCadenas = new ArrayList<CadenasKioskos>();
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    listaCadenas.add(new CadenasKioskos(eElement.getAttribute("id"),
                            eElement.getElementsByTagName("descripcion").item(0).getTextContent(),
                            eElement.getElementsByTagName("cadena").item(0).getTextContent(),
                            eElement.getElementsByTagName("nit").item(0).getTextContent(),
                            eElement.getElementsByTagName("fondo").item(0).getTextContent(),
                            eElement.getElementsByTagName("grupo").item(0).getTextContent(),
                            eElement.getElementsByTagName("emplnomina").item(0).getTextContent(),
                            eElement.getElementsByTagName("esquema").item(0).getTextContent(),
                            eElement.getElementsByTagName("captcha").item(0).getTextContent()
                    ));
                }
            }
            Collections.sort(listaCadenas);
//                data = fXmlFile.read();
//            }
//            fXmlFile.close();
            return listaCadenas;
        } catch (ParserConfigurationException e) {
            System.out.println("Error LeerArchivoXML.leerArchivoEmpresasKiosko");
            System.out.println("Error parseando el archivo. " + e);
            return null;
        } catch (SAXException e) {
            System.out.println("Error LeerArchivoXML.leerArchivoEmpresasKiosko");
            System.out.println("Error SAX. " + e);
            return null;
        } catch (IOException e) {
            System.out.println("Error LeerArchivoXML.leerArchivoEmpresasKiosko");
            System.out.println("Error leyendo el archivo. " + e);
            return null;
        } catch (DOMException e) {
            System.out.println("Error LeerArchivoXML.leerArchivoEmpresasKiosko: ");
            System.out.println("Error en DOM. " + e);
            return null;
        }
//        } finally {
//            if (fXmlFile != null) {
//                try {
//                    fXmlFile.close();
//                } catch (IOException ex) {
//                    System.out.println("leerArchivoEmpresasKiosko: " + ex.getMessage());
//                }
//            }
//        }
    }

//    @Override
    public List<CadenasKioskos> leerArchivoEmpresasKioskoGrupo(String grupo) {
        List<CadenasKioskos> listaCadenasResultado = new ArrayList<CadenasKioskos>();
//        List<CadenasKioskos> listaCadenas;
        listaCadenas = leerArchivoEmpresasKiosko();
        if (grupo != null) {
            if (!grupo.isEmpty()) {
                for (int i = 0; i < listaCadenas.size(); i++) {
                    if (listaCadenas.get(i).getGrupo().equalsIgnoreCase(grupo)) {
                        listaCadenasResultado.add(listaCadenas.get(i));
                    }
                }
                listaCadenas = listaCadenasResultado;
            }
        }
        //System.out.println("lista de XML: "+listaCadenas);
        return listaCadenas;
    }

//    @Override
    public List<String> obtenerGruposEmpresasKiosko() {
        List<String> listaGrupos = new ArrayList<String>();
//        List<CadenasKioskos> listaCadenas;
        listaCadenas = leerArchivoEmpresasKiosko();
        int contador;
        for (int i = 0; i < listaCadenas.size(); i++) {
            contador = 0;
            for (int j = 0; j < listaGrupos.size(); j++) {
                if (listaCadenas.get(i).getGrupo().equalsIgnoreCase(listaGrupos.get(j))) {
                    contador++;
                }
            }
            if (contador == 0) {
                listaGrupos.add(listaCadenas.get(i).getGrupo());
            }
        }
        return listaGrupos;
    }

//    @Override
    public ConfigModulos leerArchivoConfigModulos() {
        try {
//            System.out.println("ATTRIBUTE_NODE: " + Node.ATTRIBUTE_NODE); //2
//            System.out.println("ELEMENT_NODE: " + Node.ELEMENT_NODE); //1
//            System.out.println("DOCUMENT_NODE: " + Node.DOCUMENT_NODE); //9
//            System.out.println("ENTITY_NODE: " + Node.ENTITY_NODE); //6
//            System.out.println("ENTITY_NODE: " + Node.TEXT_NODE); //3
            InputStream fXmlFile = getClass().getResourceAsStream("../archivosConfiguracion/configModulos.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("configModulos");
//            ConfigModulos configModulos = null;
            configModulos = null;
            for (int h = 0; h < nList.getLength(); h++) {
                Element eConfMod = (Element) nList.item(h);
                if (eConfMod.getNodeType() == Node.ELEMENT_NODE) {
                    configModulos = new ConfigModulos();
                    NodeList ndsModCon = eConfMod.getElementsByTagName("moduloConfig");
                    for (int i = 0; i < ndsModCon.getLength(); i++) {
                        Element eModCon = (Element) ndsModCon.item(i);
                        NodeList ndsEmpMod = eModCon.getElementsByTagName("empresaModulo");
                        ModuloConfig modulo = new ModuloConfig(eModCon.getAttribute("nombre"));
                        configModulos.adicionarModulo(modulo);
                        for (int j = 0; j < ndsEmpMod.getLength(); j++) {
                            Element eEmpMod = (Element) ndsEmpMod.item(j);
                            NodeList ndsAudMod = eEmpMod.getElementsByTagName("auditoriaModulo");
                            EmpresaModulo empresa = new EmpresaModulo(eEmpMod.getAttribute("nit"));
                            modulo.adicionarEmpresa(empresa);
                            for (int k = 0; k < ndsAudMod.getLength(); k++) {
                                Element eAudMod = (Element) ndsAudMod.item(k);
                                NodeList ndsEmailMod = eAudMod.getElementsByTagName("emailsMod");
                                AuditoriaModulo auditoria = new AuditoriaModulo();
                                auditoria.setCodigo(eAudMod.getAttribute("codigo"));
                                empresa.adicionarAuditorias(auditoria);
                                for (int l = 0; l < ndsEmailMod.getLength(); l++) {
                                    Element eEmailMod = (Element) ndsEmailMod.item(l);
                                    NodeList ndsCuentas = eEmailMod.getElementsByTagName("cuenta");
                                    EmailMod emailMod = new EmailMod();
                                    auditoria.adicionarCorreoModulo(emailMod);
                                    for (int s = 0; s < ndsCuentas.getLength(); s++) {
                                        if (ndsCuentas.item(s).getNodeType() == 1) {
                                            emailMod.adicionarCuenta(ndsCuentas.item(s).getTextContent());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
//                Node nNode = nList.item(0);
            }
//            System.out.println("resul: "+configModulos);
            return configModulos;
        } catch (ParserConfigurationException e) {
            System.out.println("Error LeerArchivoXML.leerArchivoConfigModulos");
            System.out.println("Error parseando el archivo. " + e);
            return null;
        } catch (SAXException e) {
            System.out.println("Error LeerArchivoXML.leerArchivoConfigModulos");
            System.out.println("Error SAX. " + e);
            return null;
        } catch (IOException e) {
            System.out.println("Error LeerArchivoXML.leerArchivoConfigModulos");
            System.out.println("Error leyendo el archivo. " + e);
            return null;
        } catch (DOMException e) {
            System.out.println("Error LeerArchivoXML.leerArchivoConfigModulos: ");
            System.out.println("Error en DOM. " + e);
            return null;
        }
    }

//    @Override
    public List<String> getCuentasAudOp(String modulo, long empresa, String opcion) {
//        configModulos = leerArchivoConfigModulos();
        for (ModuloConfig moduloConfig : configModulos.getModulos()) {
            if (moduloConfig.getNombre().equalsIgnoreCase(modulo)) {
                for (EmpresaModulo empresaModulo : moduloConfig.getEmpresas()) {
                    if (empresaModulo.getNit().equalsIgnoreCase( String.valueOf(empresa) )){
                        for(AuditoriaModulo auditoriaModulo : empresaModulo.getAuditorias()){
                            System.out.println("auditoriaModulo: "+auditoriaModulo.getCodigo());
                            if (auditoriaModulo.getCodigo().equalsIgnoreCase(opcion)){
                                System.out.println(auditoriaModulo.getCodigo()+": "+auditoriaModulo.getEmailMods().size());
                                for(EmailMod emailMod : auditoriaModulo.getEmailMods() ){
                                    System.out.println("cuentas: "+emailMod.getCuentas());
                                    return emailMod.getCuentas();
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

//    @Override
    public List<CadenasKioskos> getListaCadenas() {
        return listaCadenas;
    }

//    @Override
    public ConfigModulos getConfigModulos() {
        return configModulos;
    }

}
