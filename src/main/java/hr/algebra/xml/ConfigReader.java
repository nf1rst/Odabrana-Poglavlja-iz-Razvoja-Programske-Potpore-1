package hr.algebra.xml;

import hr.algebra.exceptions.ConfigReadException;
import hr.algebra.util.LoggingUtil;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class ConfigReader {
//DOM parser

    private ConfigReader() {}

    //https://stackoverflow.com/questions/428073/what-is-the-best-simplest-way-to-read-in-an-xml-file-in-java-application
    //https://www.baeldung.com/java-xerces-dom-parsing
    public static AppConfig read(){
        try{
            InputStream input = ConfigReader.class.getResourceAsStream("/hr/algebra/configSettings/config.xml");

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);

            doc.getDocumentElement().normalize(); //da makne whitespace izmedu tragova

            String url = getTagValue(doc,"url");
            String username =getTagValue(doc,"username");
            String password =getTagValue(doc,"password");

            int width = Integer.parseInt(getTagValue(doc,"width"));
            int height = Integer.parseInt(getTagValue(doc,"height"));
            String title = getTagValue(doc,"title");
            return new AppConfig(url,username,password,width,height,title);
        }
        catch (Exception e){
            LoggingUtil.logError("Cannot read the config.xml file", e);
            throw new ConfigReadException("Cannot read the config.xml file");
        }
    }

    private static String getTagValue(Document doc, String tag) {
        return doc.getElementsByTagName(tag).item(0).getTextContent();
    }
}
