package eu.suro.utils;

import eu.suro.SpigotMain;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


public class ScheduleBukkit {

    private static Plugin plugin  = SpigotMain.getInstance();
    private static BukkitScheduler bukkitScheduleBukkitr = Bukkit.getScheduler();
    private ScheduleBukkitRunnable ScheduleBukkit;

    public ScheduleBukkit(Runnable runnable) {
        this.ScheduleBukkit = new ScheduleBukkitRunnable(runnable);
    }

    public ScheduleBukkit(Consumer<ScheduleBukkit> consumer) {
        this.ScheduleBukkit = new ScheduleBukkitRunnable(() -> consumer.accept(this));
    }

    /**
     * Создать таск
     * @param runnable код
     * @return таск
     */
    public static ScheduleBukkit build(Runnable runnable) {
        return new ScheduleBukkit(runnable);
    }


    public static long toTicks(long value, TimeUnit time) {
        return time.toSeconds(value) * 20;
    }

    public static ScheduleBukkit create(Runnable runnable) {
        return new ScheduleBukkit(runnable);
    }
    /**
     * Запустить таск
     * @param runnable таск
     * @return bukkit task
     */
    public static BukkitTask run(Runnable runnable) {
        return bukkitScheduleBukkitr.runTask(plugin, runnable);
    }

    /**
     * Запустить таск асинхронно
     * @param runnable таск
     * @return bukkit task
     */
    public static BukkitTask runAsync(Runnable runnable) {
        return bukkitScheduleBukkitr.runTaskAsynchronously(plugin, runnable);
    }

    /**
     * Выполнить позже
     * @param runnable таск
     * @param delay    через сколько выполнить
     * @param time     тип времени
     * @return bukkit task
     */
    public static BukkitTask later(Runnable runnable, long delay, TimeUnit time) {
        return bukkitScheduleBukkitr.runTaskLater(plugin, runnable, time.toSeconds(delay) * 20);
    }

    /**
     * Выполнить позже async
     * @param runnable таск
     * @param delay    через сколько выполнить
     * @param time     тип времени
     * @return bukkit task
     */
    public static BukkitTask laterAsync(Runnable runnable, long delay, TimeUnit time) {
        return bukkitScheduleBukkitr.runTaskLaterAsynchronously(plugin, runnable, time.toSeconds(delay) * 20);
    }


    /**
     * Запустить таймер
     * @param runnable таск
     * @param delay    через сколько выполнить
     * @param period   через сколько повторять
     * @param time     тип времени
     * @return bukkit task
     */
    public static BukkitTask timer(Runnable runnable, long delay, long period, TimeUnit time) {
        return bukkitScheduleBukkitr.runTaskTimer(plugin, runnable, time.toSeconds(delay) * 20, time.toSeconds(period) * 20);
    }

    /**
     * Запустить таймер async
     * @param runnable таск
     * @param delay    через сколько выполнить
     * @param period   через сколько повторять
     * @param time     тип времени
     * @return bukkit task
     */
    public static BukkitTask timerAsync(Runnable runnable, long delay, long period, TimeUnit time) {
        return bukkitScheduleBukkitr.runTaskTimerAsynchronously(plugin, runnable, time.toSeconds(delay) * 20, time.toSeconds(period) * 20);
    }

    /**
     * Выполнить позже
     * @param runnable таск
     * @param delay    через сколько выполнить
     * @return bukkit task
     */
    public static BukkitTask later(Runnable runnable, long delay) {
        return bukkitScheduleBukkitr.runTaskLater(plugin, runnable, delay);
    }

    /**
     * Запустить таймер
     * @param runnable таск
     * @param delay    через сколько выполнить
     * @param period   через сколько повторять тиков
     * @return bukkit task
     */
    public static BukkitTask timer(Runnable runnable, long delay, long period) {
        return bukkitScheduleBukkitr.runTaskTimer(plugin, runnable, delay, period);
    }

    /**
     * Останавливает таймер.
     * @param runnableID - айди таймера, который будем останавливать.
     */
    public static void stopTimer(int runnableID) {
        bukkitScheduleBukkitr.cancelTask(runnableID);
    }

    /**
     * Установить условие выполенния
     * @param predicate условие выполнения
     * @return this
     */
    public ScheduleBukkit predicate(PredicateZero predicate) {
        this.ScheduleBukkit.predicate = predicate;
        return this;
    }

    /**
     * Установить количество срабатываний
     * @param count сколько раз нужно таймеру сработать
     * @return this
     */
    public ScheduleBukkit count(int count) {
        this.ScheduleBukkit.count = count;
        return this;
    }

    /**
     * Запустить таймер
     * @param delay  через сколько первое срабатывание
     * @param period период между запусками
     * @return this
     */
    public ScheduleBukkit timer(long delay, long period, TimeUnit time) {
        this.ScheduleBukkit.runTaskTimer(plugin, this.toTime(delay, time), this.toTime(period, time));
        return this;
    }

    /**
     * Запустить таймер
     * @param delay  через сколько первое срабатывание
     * @param period период между запусками
     * @return this
     */
    public ScheduleBukkit timer(long delay, long period) {
        this.ScheduleBukkit.runTaskTimer(plugin, delay, period);
        return this;
    }

    /**
     * Запустить таск позже
     * @param delay через сколько срабатывание
     * @return this
     */
    public ScheduleBukkit later(long delay, TimeUnit time) {
        this.ScheduleBukkit.runTaskLater(plugin, this.toTime(delay, time));
        return this;
    }

    /**
     * Запустить таск позже
     * @param delay через сколько срабатывание
     * @return this
     */
    public ScheduleBukkit later(long delay) {
        this.ScheduleBukkit.runTaskLater(plugin, delay);
        return this;
    }

    /**
     * Запустить таск
     * @return this
     */
    public ScheduleBukkit run() {
        this.ScheduleBukkit.runTask(plugin);
        return this;
    }

    private long toTime(long ticks, TimeUnit time) {
        return time.toSeconds(ticks) * 20;
    }

    /**
     * Выключить таск
     */
    public void cancel() {
        BukkitUtils.cancelTask(this.ScheduleBukkit);
    }

    private class ScheduleBukkitRunnable extends BukkitRunnable {

        private Runnable      runnable;
        private PredicateZero predicate;
        private int     count = -1;
        private int     i     = 0;
        private boolean timer = false;

        ScheduleBukkitRunnable(Runnable runnable) {
            this.runnable = runnable;
        }

        @Override
        public void run() {
            this.checkCount();
            if(this.hasPredicate()) {
                runnable.run();
            } else {
                ScheduleBukkit.this.cancel();
            }
        }

        private boolean hasPredicate() {
            return predicate == null || predicate.test();
        }

        private void checkCount() {
            if(timer && count != -1 && ++i >= count) {
                ScheduleBukkit.this.cancel();
            }
        }

        @Override
        public synchronized BukkitTask runTaskTimer(Plugin plugin, long delay, long period) throws IllegalArgumentException, IllegalStateException {
            this.timer = true;
            return super.runTaskTimer(plugin, delay, period);
        }
    }

}