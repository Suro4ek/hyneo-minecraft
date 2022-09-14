package eu.suro.locale;

import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class LocaleStorage {

    static Locale RU_LOCALE = new Locale("ru");
    static Locale EN_LOCALE = new Locale("en");

    /**
     * Загрузка локализации
     * @param path - путь до файла
     */
    public static void updateLocales(String path) { //обновление локализаций
        for (Language language : Language.values()) {
            try {
                language.getLocale().loadFromSite(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Добавление локализации
     * @param path - путь до файла
     */
    public static void addLocales(String path){
        for (Language language : Language.values()) {
            try {
                language.getLocale().loadFromSite(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}