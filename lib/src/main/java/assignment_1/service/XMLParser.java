package assignment_1.service;

import java.io.File;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import assignment_1.exceptions.QueryNotFoundException;
import assignment_1.model.QueryObject;

public class XMLParser{
    
    private final String filePath;

    // public constructor
    public XMLParser(String str){
        filePath = str;
    }

    /**
     *
     * @param queryId queryId of the query requested from the XML file
     * @return
     */


    public QueryObject getQueryObject(String queryId){
        // Instantiate the Factory
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            // The following line is to help process the XML file securely

            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            // parse the XML file into Document object
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(filePath));

            // the following line is used to normalise the Document
            // This would help reduce any formatting related anomalies in the documents
            // such as unwanted/extra whitespaces etc.
            document.getDocumentElement().normalize();

            // It makes a NodeList object of all sql tags
            NodeList list = document.getElementsByTagName("sql");
            for (int temp = 0; temp < list.getLength(); temp++) {

                // Check for the first tag with id attribute equal to queryId in the parameters.
                Node node = list.item(temp);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    
                    Element element = (Element) node;
                    if(element.getAttribute("id").equals(queryId)){
                        String id = element.getAttribute("id");
                        String paramType = element.getAttribute("paramType");
                        String sqlQuery = element.getTextContent().trim();
                        

                        // Creating a Query object to store the parsed result
                        return new QueryObject(id, paramType, sqlQuery
                        );
                    }
                }
            }
            // Handling exception in case no query is found with the given id
            throw(new QueryNotFoundException(queryId, this.filePath));

        } catch (ParserConfigurationException | SAXException | IOException e) {
           throw(new RuntimeException(e));
        }

    }
}