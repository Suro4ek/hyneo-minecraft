package eu.suro.utils;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import javax.annotation.Nullable;
import java.util.Objects;

@UtilityClass
public class BukkitUtils {

    public static final String VERSION = Bukkit.getServer().getClass().getName().split("\\.")[3]; //версия пакетов

    public static final int MIN_COORD = -29999999;
    public static final int MAX_COORD = 29999999;


    /**
     * Остановить таск с try catch IGNORE<br>
     * (Иногда при остановлении task'ка, выдает Exception)
     * @param task таск
     */
    public static void cancelTask(@Nullable BukkitTask task) {
        if(task != null) {
            try {
                task.cancel();
            } catch(Exception ignore) {}
        }
    }

    /**
     * Остановить таск с try catch IGNORE<br>
     * (Иногда при остановлении task'ка, выдает Exception)
     * @param task таск
     */
    public static void cancelTask(@Nullable BukkitRunnable task) {
        if(task != null) {
            try {
                task.cancel();
            } catch(Exception ignore) {}
        }
    }

    /**
     * Остановить таск с try catch IGNORE<br>
     * (Иногда при остановлении task'ка, выдает Exception)
     * @param task таск
     */
    public static void cancelTask(@Nullable ScheduleBukkit task) {
        if(task != null) {
            try {
                task.cancel();
            } catch(Exception ignore) {}
        }
    }
}