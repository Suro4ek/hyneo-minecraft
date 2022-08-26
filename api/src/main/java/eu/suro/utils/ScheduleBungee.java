package eu.suro.utils;
import eu.suro.BungeeMain;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.api.scheduler.TaskScheduler;

import java.util.concurrent.TimeUnit;

public class ScheduleBungee {

    private static TaskScheduler scheduler = ProxyServer.getInstance().getScheduler();
    private static BungeeMain plugin = BungeeMain.getInstance();

    private ScheduleBungee() {
    }

    /**
     * Запустить таск асинхронно
     */
    public static ScheduledTask runAsync(Runnable runnable) {
        return scheduler.runAsync(plugin, runnable);
    }

    /**
     * Выполнить позже
     * @param runnable такс
     * @param delay    через сколько выполнить
     * @param time     тип времени
     * @return bukkit task
     */
    public static ScheduledTask later(Runnable runnable, long delay, TimeUnit time) {
        return scheduler.schedule(plugin, runnable, delay, time);
    }

    /**
     * Запустить таймер
     * @param runnable таск
     * @param delay    через сколько выполнить
     * @param period   через сколько повторять
     * @param time     тип времени
     * @return bukkit task
     */
    public static ScheduledTask timer(Runnable runnable, long delay, long period, TimeUnit time) {
        return scheduler.schedule(plugin, runnable, delay, period, time);
    }

    public static TaskScheduler getScheduler(){
        return scheduler;
    }
}