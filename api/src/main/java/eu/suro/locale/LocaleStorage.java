package eu.suro.locale;

import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class LocaleStorage {

    static Locale RU_LOCALE = new Locale("ru");
    static Locale EN_LOCALE = new Locale("en");

    public static void updateLocales(String path) { //обновление локализаций
        for (Language language : Language.values()) {
            try {
                language.getLocale().loadFromSite(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}