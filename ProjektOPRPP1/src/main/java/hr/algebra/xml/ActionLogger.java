package hr.algebra.xml;


//zapisuje korisnicke akcije u XML loh datoteku
//koristi DOm parser za kreiranje i azuirranje XML-a

import org.slf4j.LoggerFactory;
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
import java.util.logging.Logger;

public class ActionLogger {

    private static final String LOG_FILE ="action_log.xml";
    private static final DateTimeFormatter DATEFORMAT= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private ActionLogger() {}

}
