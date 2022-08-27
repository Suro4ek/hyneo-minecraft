package eu.suro.event.bungee;


import eu.suro.event.Subscription;
import net.md_5.bungee.api.plugin.Event;

import java.util.Set;

import javax.annotation.Nonnull;

/**
 * Represents a subscription to a set of events.
 *
 * @param <T> the handled type
 */
public interface MergedSubscription<T> extends Subscription {

    /**
     * Gets the handled class
     *
     * @return the handled class
     */
    @Nonnull
    Class<? super T> getHandledClass();

    /**
     * Gets a set of the individual event classes being listened to
     *
     * @return the individual classes
     */
    @Nonnull
    Set<Class<? extends Event>> getEventClasses();

}