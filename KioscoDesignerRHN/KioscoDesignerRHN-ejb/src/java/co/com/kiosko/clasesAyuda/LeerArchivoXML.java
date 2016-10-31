package co.com.kiosko.clasesAyuda;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
 * @author Felipe Triviño
 */
public class LeerArchivoXML {

    public List<CadenasKioskos> leerArchivoEmpresasKiosko() {
        try {
            InputStream fXmlFile = getClass().getResourceAsStream("../archivosConfiguracion/cadenasKioskos.xml");
            //File fXmlFile = new File("cadenasKioskos.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("cadenaKiosko");
            List<CadenasKioskos> listaCadenas = new ArrayList<CadenasKioskos>();

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    listaCadenas.add(new CadenasKioskos(eElement.getAttribute("id"),
                            eElement.getElementsByTagName("descripcion").item(0).getTextContent(),
                            eElement.getElementsByTagName("cadena").item(0).getTextContent(),
                            eElement.getElementsByTagName("nit").item(0).getTextContent(),
                            eElement.getElementsByTagName("fondo").item(0).getTextContent(),
                            eElement.getElementsByTagName("grupo").item(0).getTextContent()
                    ));
                }
            }
            Collections.sort(listaCadenas);
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
    }

    public List<CadenasKioskos> leerArchivoEmpresasKioskoGrupo(String grupo) {
        List<CadenasKioskos> listaCadenasResultado = new ArrayList<CadenasKioskos>();
        List<CadenasKioskos> listaCadenas;
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

    public List<String> obtenerGruposEmpresasKiosko() {
        List<String> listaGrupos = new ArrayList<String>();
        List<CadenasKioskos> listaCadenas;
        listaCadenas = leerArchivoEmpresasKiosko();
        int contador;
        for (int i = 0; i < listaCadenas.size(); i++) {
            contador=0;
            for (int j=0; j< listaGrupos.size(); j++){
                if (listaCadenas.get(i).getGrupo().equalsIgnoreCase( listaGrupos.get(j) ) ){
                    contador++;
                }
            }
            if (contador == 0){
                listaGrupos.add(listaCadenas.get(i).getGrupo());
            }
        }
        return listaGrupos;
    }
}
