/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package co.com.kiosko.clasesAyuda;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author 908036
 */
public class clasePrueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {

            File fXmlFile = new File("cadenasKioskos.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("cadenaKiosko");

            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    System.out.println("descripcion : " + eElement.getElementsByTagName("descripcion").item(0).getTextContent());
                    System.out.println("cadena : " + eElement.getElementsByTagName("cadena").item(0).getTextContent());

                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }
}
