package hr.algebra.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


//https://medium.com/@ibrahim-akay/logback-slf4j-powerful-and-flexible-logging-solution-69e6b6523c17

public class LoggingUtil {
    private static Logger logger = LoggerFactory.getLogger(LoggingUtil.class);

    private LoggingUtil(){}

    public static void logInfo(String message){
        logger.info(message);
    }

    public static void logError(String message,Object ... args){
        logger.error(message,args);
    }

    public static void logInfo(String action, String entity, String identificator, String username){
        logger.info(action, entity, identificator, username);
    }

    public static void logInfo(String action, String entity, String username){
        logger.info(action, entity, username);
    }

}
