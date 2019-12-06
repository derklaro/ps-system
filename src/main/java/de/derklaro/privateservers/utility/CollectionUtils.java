package de.derklaro.privateservers.utility;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class CollectionUtils {

    private CollectionUtils() {
        throw new UnsupportedOperationException();
    }

    public static <T> T filter(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).findFirst().orElse(null);
    }

    public static <T> Collection<T> filterAll(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <T> T filter(T[] collection, Predicate<T> predicate) {
        return Arrays.stream(collection).filter(predicate).findFirst().orElse(null);
    }

    public static <T> Collection<T> filterAll(T[] collection, Predicate<T> predicate) {
        return Arrays.stream(collection).filter(predicate).collect(Collectors.toList());
    }
}
