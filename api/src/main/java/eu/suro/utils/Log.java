package eu.suro.utils;

import org.slf4j.Logger;
import org.slf4j.Marker;

import javax.annotation.Nonnull;
import java.util.logging.Level;

public final class Log {

    public static Logger logger;

    public static void init(Logger logger){
        Log.logger = logger;
    }

    public static void info(@Nonnull String s) {
        Log.logger.info(s);
    }

    public static void warn(@Nonnull String s) {
        Log.logger.warn(s);
    }

    public static void severe(@Nonnull String s) {
        Log.logger.info(s);
    }

    public static void warn(@Nonnull String s, Throwable t) {
        Log.logger.warn(Marker.ANY_MARKER, s, t);
    }

    public static void severe(@Nonnull String s, Throwable t) {
        Log.logger.info(Marker.ANY_MARKER, s, t);
    }

}
