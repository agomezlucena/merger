package io.github.agomezlucena.errors;

import java.util.Optional;

/**
 * This class have generic functions that throws exceptions based
 */
public class ThrowingFunctions {
    private ThrowingFunctions(){}

    /**
     * Will throw the exception defined by the error payload when the given
     * object is null
     * @param object an object
     * @param payload payload that generates the exception if object is null
     * @param <U> any type
     */
    public static <U> void throwIfNull(U object, ErrorPayload payload) {
        Optional.ofNullable(object)
                .orElseThrow(payload::throwException);
    }
}
