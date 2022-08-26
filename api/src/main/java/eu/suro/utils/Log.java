package eu.suro.utils;

import javax.annotation.Nonnull;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Log {

    public static Logger logger;

    public static void init(Logger logger){
        Log.logger = logger;
    }

    public static void info(@Nonnull String s) {
        Log.logger.info(s);
    }

    public static void warn(@Nonnull String s) {
        Log.logger.warning(s);
    }

    public static void severe(@Nonnull String s) {
        Log.logger.severe(s);
    }

    public static void warn(@Nonnull String s, Throwable t) {
        Log.logger.log(Level.WARNING, s, t);
    }

    public static void severe(@Nonnull String s, Throwable t) {
        Log.logger.log(Level.SEVERE, s, t);
    }

}
