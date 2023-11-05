package io.github.agomezlucena.errors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ThrowingFunctionsTest {
    @Test
    @DisplayName("throwIfNull will not throw if the passed object is not null")
    void test1() {
        assertDoesNotThrow(() -> ThrowingFunctions.throwIfNull(
                        1L,
                        ErrorPayload.of(
                                ErrorMessageKeys.COLLECTION_IS_NULL,
                                IllegalArgumentException.class
                        )
                )
        );
    }

    @Test
    @DisplayName("throwIfNull will not throw if the passed object is not null")
    void test2() {
        assertThrows(
                IllegalArgumentException.class,
                () -> ThrowingFunctions.throwIfNull(
                        null,
                        ErrorPayload.of(
                                ErrorMessageKeys.COLLECTION_IS_NULL,
                                IllegalArgumentException.class
                        )
                )
        );
    }
}