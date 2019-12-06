package de.derklaro.privateservers.database.utils;

import java.util.Map;

public abstract class DatabaseExt<T, V> {

    public abstract void load();

    public abstract void insert(T t);

    public abstract void forget(V key);

    public abstract Map<V, T> getAll();

    public abstract void save();
}
