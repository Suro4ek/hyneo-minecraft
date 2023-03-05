package eu.suro

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import org.bukkit.Bukkit


class SpigotMain : SuspendingJavaPlugin(){
    companion object{
        lateinit var instance: SpigotMain
    }
    override suspend fun onEnableAsync() {
        println("[MCCoroutineSamplePlugin] OnEnable on Primary Thread: " + Bukkit.isPrimaryThread())
        instance = this
        eu.suro.metadata.Metadata.ensureSetup()
    }

//    override fun onEnable() {
//
//    }
}
