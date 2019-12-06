package de.derklaro.privateservers.utility;

import java.util.Arrays;
import java.util.Objects;

public final class Validate {

    public static <T> boolean notNull(T... ts) {
        return Arrays.stream(ts).noneMatch(Objects::isNull);
    }
}
