package eu.suro.utils

import eu.suro.SpigotMain
import org.bukkit.Bukkit
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class ScheduleBukkit {
    private val schedule: ScheduleBukkitRunnable
    constructor(consumer: Consumer<ScheduleBukkit>){
        schedule = ScheduleBukkitRunnable { consumer.accept(this) }
    }
    constructor(runnable: Runnable){
        schedule = ScheduleBukkitRunnable(runnable)
    }
    companion object{
        private val bukkitScheduleBukkitr: BukkitScheduler = Bukkit.getScheduler()
        private val plugin: SpigotMain = SpigotMain.instance!!
        /**
         * Создать таск
         * @param runnable код
         * @return таск
         */
        fun build(runnable: Runnable): ScheduleBukkit = ScheduleBukkit(runnable)

        private fun toTicks(value: Long, time: TimeUnit) = time.toSeconds(value) * 20

        fun create(runnable: Runnable): ScheduleBukkit = ScheduleBukkit(runnable)

        /**
         * Запустить таск
         * @param runnable таск
         * @return bukkit task
         */
        fun run(runnable: Runnable): BukkitTask = bukkitScheduleBukkitr.runTask(plugin, runnable)

        /**
         * Запустить таск асинхронно
         * @param runnable таск
         * @return bukkit task
         */
        fun runAsync(runnable: Runnable) = bukkitScheduleBukkitr.runTaskAsynchronously(plugin, runnable)

        /**
         * Выполнить позже
         * @param runnable таск
         * @param delay    через сколько выполнить
         * @param time     тип времени
         * @return bukkit task
         */
        fun later(runnable: Runnable, delay: Long, time: TimeUnit) =
            bukkitScheduleBukkitr.runTaskLater(plugin, runnable, toTicks(delay, time))

        /**
         * Выполнить позже async
         * @param runnable таск
         * @param delay    через сколько выполнить
         * @param time     тип времени
         * @return bukkit task
         */
        fun laterAsync(runnable: Runnable, delay: Long, time: TimeUnit) =
            bukkitScheduleBukkitr.runTaskLaterAsynchronously(plugin, runnable, toTicks(delay, time))

        /**
         * Запустить таймер
         * @param runnable таск
         * @param delay    через сколько выполнить
         * @param period   через сколько повторять
         * @param time     тип времени
         * @return bukkit task
         */
        fun timer(runnable: Runnable, delay: Long, period: Long, time: TimeUnit) =
            bukkitScheduleBukkitr.runTaskTimer(plugin, runnable, toTicks(delay, time), toTicks(period, time))

        /**
         * Запустить таймер async
         * @param runnable таск
         * @param delay    через сколько выполнить
         * @param period   через сколько повторять
         * @param time     тип времени
         * @return bukkit task
         */
        @JvmStatic fun timerAsync(runnable: Runnable, delay: Long, period: Long, time: TimeUnit) =
            bukkitScheduleBukkitr.runTaskTimerAsynchronously(plugin, runnable, toTicks(delay, time), toTicks(period, time))

        /**
         * Выполнить позже
         * @param runnable таск
         * @param delay    через сколько выполнить
         * @return bukkit task
         */
        fun later(runnable: Runnable, delay: Long) = bukkitScheduleBukkitr.runTaskLater(plugin, runnable, delay)

        /**
         * Запустить таймер
         * @param runnable таск
         * @param delay    через сколько выполнить
         * @param period   через сколько повторять тиков
         * @return bukkit task
         */
        fun timer(runnable: Runnable, delay: Long, period: Long) = bukkitScheduleBukkitr.runTaskTimer(plugin, runnable, delay, period)

        /**
         * Останавливает таймер.
         * @param runnableID - айди таймера, который будем останавливать.
         */
        fun stopTimer(runnableID: Int) = bukkitScheduleBukkitr.cancelTask(runnableID)
    }

    /**
     * Установить условие выполенния
     * @param predicate условие выполнения
     * @return this
     */
    fun predicate(predicate: PredicateZero): ScheduleBukkit {
        schedule.predicate = predicate
        return this
    }

    /**
     * Установить количество срабатываний
     * @param count сколько раз нужно таймеру сработать
     * @return this
     */
    fun count(count: Int): ScheduleBukkit {
        schedule.count = count
        return this
    }

    /**
     * Запустить таймер
     * @param delay  через сколько первое срабатывание
     * @param period период между запусками
     * @return this
     */
    fun timer(delay: Long, period: Long, time: TimeUnit): ScheduleBukkit {
        schedule.runTaskTimer(plugin, toTime(delay, time), toTime(period, time))
        return this
    }

    /**
     * Запустить таймер
     * @param delay  через сколько первое срабатывание
     * @param period период между запусками
     * @return this
     */
    fun timer(delay: Long, period: Long): ScheduleBukkit {
        schedule.runTaskTimer(plugin, delay, period)
        return this
    }

    /**
     * Запустить таск позже
     * @param delay через сколько срабатывание
     * @return this
     */
    fun later(delay: Long, time: TimeUnit): ScheduleBukkit {
        schedule.runTaskLater(plugin, toTime(delay, time))
        return this
    }

    /**
     * Запустить таск позже
     * @param delay через сколько срабатывание
     * @return this
     */
    fun later(delay: Long): ScheduleBukkit {
        schedule.runTaskLater(plugin, delay)
        return this
    }

    /**
     * Запустить таск
     * @return this
     */
    fun run(): ScheduleBukkit {
        schedule.runTask(plugin)
        return this
    }

    /**
     * Выключить таск
     */
    fun cancel(){
        BukkitUtils.cancelTask(schedule)
    }

    private fun toTime(ticks: Long, time: TimeUnit): Long {
        return time.toSeconds(ticks) * 20
    }

    private class ScheduleBukkitRunnable internal constructor(private val runnable: Runnable) : BukkitRunnable() {
        var predicate: PredicateZero? = null
        var count = -1
        private var i = 0
        private var timer = false
        override fun run() {
            checkCount()
            if (hasPredicate()) {
                runnable.run()
            } else {
                this.cancel()
            }
        }

        private fun hasPredicate(): Boolean {
            return predicate == null || predicate!!.test()
        }

        private fun checkCount() {
            if (timer && count != -1 && ++i >= count) {
               this.cancel()
            }
        }

        @Synchronized
        @Throws(IllegalArgumentException::class, IllegalStateException::class)
        override fun runTaskTimer(plugin: Plugin, delay: Long, period: Long): BukkitTask {
            timer = true
            return super.runTaskTimer(plugin, delay, period)
        }
    }
}