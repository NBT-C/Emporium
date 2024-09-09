package me.nbtc.premieremporium.repositories;


import java.util.HashMap;
import java.util.Map;

public class Registry {
    private final Map<Class<?>, Object> repositories = new HashMap<>();

    public <T> void registerRepository(Class<T> repositoryClass, T repository) {
        repositories.put(repositoryClass, repository);
    }

    @SuppressWarnings("unchecked")
    public <T> T getRepository(Class<T> repositoryClass) {
        return (T) repositories.get(repositoryClass);
    }
}