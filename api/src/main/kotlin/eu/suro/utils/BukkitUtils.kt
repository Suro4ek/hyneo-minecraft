package eu.suro.utils

import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask

class BukkitUtils {

    companion object{
        val VERSION : String = Bukkit.getServer()::class.java.name.split("\\.")[3]
        val MIN_COORD = -29999999
        val MAX_COORD = 29999999

        /**
         * Остановить таск с try catch IGNORE<br>
         * (Иногда при остановлении task'ка, выдает Exception)
         * @param task таск
         */
        fun cancelTask(task: BukkitTask?){
            if(task != null){
                try {
                    task.cancel()
                }catch (ignore: Exception) {}
            }
        }

        /**
         * Остановить таск с try catch IGNORE<br>
         * (Иногда при остановлении task'ка, выдает Exception)
         * @param task таск
         */
        fun cancelTask(task: BukkitRunnable?) {
            if (task != null) {
                try {
                    task.cancel()
                } catch (ignore: java.lang.Exception) {
                }
            }
        }

        /**
         * Остановить таск с try catch IGNORE<br>
         * (Иногда при остановлении task'ка, выдает Exception)
         * @param task таск
         */
        fun cancelTask(task: ScheduleBukkit?) {
            if (task != null) {
                try {
                    task.cancel()
                } catch (ignore: java.lang.Exception) {
                }
            }
        }
    }
}