package dk.jensbot.KajbotDiscordLauncher;

import org.apache.log4j.Logger;

public class LogHelper {
    private static Logger logger = Logger.getLogger("[Launcher]");


    /**
     * helper class d() to log debug level information.
     */
    public static void debug(Class cls, String message) {
        logger.debug(cls + ": " + message);
    }


    /**
     * helper class i() to log info level information.
     */
    public static void info(String message) {
        logger.info(message);
    }

    /**
     * helper class w() to log warning level information.
     */
    public static void warning(Class cls, String message) {
        logger.warn(cls + ": " + message);
    }


    /**
     * helper class e() to log error information.
     */
    public static void error(Class cls, String message) {
        logger.error(cls + ": " + message);
    }


    /**
     * helper class t() to log trace information.
     */
    public static void trace(Class cls, String message) {
        logger.trace(cls + ": " + message);
    }
}