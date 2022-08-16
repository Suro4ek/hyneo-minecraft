package eu.suro.api.config;

public interface Config {

    void save();

    void setAndSave(String path, Object o);

    void setIfNotExist(String path, Object o);

    <T> T getOrSet(String path, T o);
}
