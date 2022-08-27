package eu.suro.event.bungee.single;



import eu.suro.event.bungee.SingleSubscription;
import eu.suro.event.functional.FunctionalHandlerList;
import eu.suro.utils.Delegates;
import net.md_5.bungee.api.plugin.Event;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

public interface SingleHandlerList<T extends Event> extends FunctionalHandlerList<T, SingleSubscription<T>> {

    @Nonnull
    @Override
    default SingleHandlerList<T> consumer(@Nonnull Consumer<? super T> handler) {
        Objects.requireNonNull(handler, "handler");
        return biConsumer(Delegates.consumerToBiConsumerSecond(handler));
    }

    @Nonnull
    @Override
    SingleHandlerList<T> biConsumer(@Nonnull BiConsumer<SingleSubscription<T>, ? super T> handler);
}