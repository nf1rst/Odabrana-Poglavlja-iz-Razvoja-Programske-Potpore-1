package hr.algebra.util;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="activityLog")
public class XMLLoggingUtil {

    public XMLLoggingUtil(){}

    private static String LOG_FILE="userActivities.xml";
    private static DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");


    @XmlRootElement(name="errorLogs")
    public static class ErrorLog {
        private List<LogEntry> log=new ArrayList<LogEntry>();

        public void setLog(List<LogEntry> log) {
            this.log = log;
        }

        public List<LogEntry> getLog() {
            return log;
        }
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    public static class LogEntry{
        private String time;
        private String level;
        private String action;
        private String message;
        private String entity;
        private String username;
        private String identity;

        public LogEntry(){}

        public LogEntry(String level, String action, String message, String entity, String username, String identity) {
            this.time = LocalDateTime.now().format(FORMAT);
            this.level = level;
            this.action = action;
            this.message = message;
            this.entity = entity;
            this.username = username;
            this.identity = identity;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setEntity(String entity) {
            this.entity = entity;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setIdentity(String identity) {
            this.identity = identity;
        }

        public String getTime() {
            return time;
        }

        public String getLevel() {
            return level;
        }

        public String getAction() {
            return action;
        }

        public String getMessage() {
            return message;
        }

        public String getEntity() {
            return entity;
        }

        public String getUsername() {
            return username;
        }

        public String getIdentity() {
            return identity;
        }
    }

    public static void log(String level, String action, String message, String entity, String username, String identity){
        //dovuci sve postojece, apendaj novi i ponovno spremi u file
        try{
            JAXBContext context = JAXBContext.newInstance(ErrorLog.class);
            ErrorLog log =  readExistingLogs(context);

            log.getLog().add(new LogEntry(level, action, message, entity, username, identity));

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(log, new File(LOG_FILE));
        }
        catch (JAXBException e){
            LoggingUtil.logError("XML log fail", e);
        }

    }

    private static ErrorLog readExistingLogs(JAXBContext context) {
        File file = new File(LOG_FILE);
        if(!file.exists())return new ErrorLog();
        try{
            return (ErrorLog) context.createUnmarshaller().unmarshal(file);
        }
        catch (JAXBException e){
            LoggingUtil.logError("XML log file read eror", e);
            return new ErrorLog();
        }
    }


}
