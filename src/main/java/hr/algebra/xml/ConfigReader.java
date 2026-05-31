package hr.algebra.xml;

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class ConfigReader {
    //cita config.xml koristeciDOM parser
    //taj DOM parser ucita cijeli XML u memoriju kao stablo
    //dobar je za male datoteke gdje trebas
    // pristupat elementira moze u bilo kojem redoslijedu
    // staticka utility klasa - privatni konstruktor


    private ConfigReader() {}

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
            throw new RuntimeException("Cannot read the config.xml file", e);
        }
    }

    private static String getTagValue(Document doc, String tag) {
        return doc.getElementsByTagName(tag).item(0).getTextContent();
    }
}
