package hr.algebra.xml;


//zapisuje korisnicke akcije u XML loh datoteku
//koristi DOm parser za kreiranje i azuirranje XML-a

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ActionLogger {
    private static final String LOG_FILE ="action_log.xml";
    private static final DateTimeFormatter DATEFORMAT= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ActionLogger() {}

    public static void log(String username, String action, String entity, String details){
        try{
            Document doc = getOrCreateDocument();

            Element actionElement = doc.createElement("action");

            Element timeEl = doc.createElement("timeStamp");
            timeEl.setTextContent(LocalDateTime.now().format(DATEFORMAT));

            Element userEl = doc.createElement("username");
            userEl.setTextContent(username);

            Element actionEl = doc.createElement("action");
            actionEl.setTextContent(action);

            Element entityEl = doc.createElement("entity");
            entityEl.setTextContent(entity);

            Element detailsEl = doc.createElement("details");
            detailsEl.setTextContent(details);

            actionElement.appendChild(timeEl);
            actionElement.appendChild(userEl);
            actionElement.appendChild(actionEl);
            actionElement.appendChild(entityEl);
            actionElement.appendChild(detailsEl);

            //dodaj na kraj root elementa
            doc.getDocumentElement().appendChild(actionElement);

            saveDocument(doc);
        }
        catch (Exception e){
            System.err.println("Logging error " + e.getMessage());
        }
    }

    private static void saveDocument(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        // za ljepse formatiranje pri ispisu
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(new File(LOG_FILE));
        transformer.transform(source,result);
    }

    private static Document getOrCreateDocument() throws ParserConfigurationException, IOException, SAXException {
        File file = new File(LOG_FILE);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        if(file.exists()) return builder.parse(file);
        else{
            Document doc = builder.newDocument();
            Element root = doc.createElement("action_log");
            doc.appendChild(root);
            return doc;
        }
    }
}
