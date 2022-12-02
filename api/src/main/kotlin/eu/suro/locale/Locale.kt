package eu.suro.locale

import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.introspector.PropertyUtils
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.ConcurrentHashMap

data class Locale constructor(
        private var name: String
){
    var size:Int = 0
    private val YAML: Yaml = newSnakeYaml()
    var messages: ConcurrentHashMap<String, String> = ConcurrentHashMap()
    var listMessages: ConcurrentHashMap<String, List<String>> = ConcurrentHashMap()
    companion object{
        fun newSnakeYaml(): Yaml{
            val constructor = Constructor()
            val propertyUtils = PropertyUtils()
            propertyUtils.isSkipMissingProperties = true
            constructor.propertyUtils = propertyUtils
            return Yaml(constructor)
        }
    }

    init{
        try {
            loadFromSite("core/")
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

    /**
     * Загрузка локализации с github
     * @param path - путь до файла
     * @throws IOException - если не удалось загрузить файл
     */
    @Throws(IOException::class)
    fun loadFromSite(path: String) {
        val oracle = URL("https://raw.githubusercontent.com/hyneo/Localization/main/lang/$path$name.yml")
        val map = YAML.loadAs(InputStreamReader(oracle.openStream()), LinkedHashMap::class.java) ?: return
        size = map.size
        addMessages(map)
    }

    /**
     * Добавление локализации с github
     * @param path - путь до файла
     * @throws IOException - если не удалось загрузить файл
     */
    @Throws(IOException::class)
    fun addFromSite(path: String) {
        val oracle = URL("https://raw.githubusercontent.com/hyneo/Localization/main/lang/$path$name.yml")
        val map = YAML.loadAs(InputStreamReader(oracle.openStream()), LinkedHashMap::class.java) ?: return
        size += map.size
        addMessages(map)
    }

    private fun addMessages(map: java.util.LinkedHashMap<*, *>){
        for (entry in map){
            val key = entry.key.toString()
            val data = entry.value
            if(data is List<*>){
                listMessages[key] = data as List<String>
                continue
            }
            messages[key] = data.toString()
        }
    }
}