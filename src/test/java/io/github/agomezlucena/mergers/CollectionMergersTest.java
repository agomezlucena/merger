package io.github.agomezlucena.mergers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class CollectionMergersTest {
    private static Stream<Arguments> invalidInputs() {
        return Stream.of(
                arguments(
                        "when you try to obtain a Merger of the same type if you pass null will throw a " +
                        "IllegalArgumentException with message given collection can't be null",
                        IllegalArgumentException.class,
                        (Executable) () -> CollectionMergers.ofSameType(null),
                        "given collection can't be null"
                ),
                arguments(
                        "when you try to obtain a Merger of different types if you pass a null collection will throw " +
                        "IllegalArgumentException with message given collection can't be null",
                        IllegalArgumentException.class,
                        (Executable) () -> CollectionMergers.ofTwoTypes(null, Long.class, Long.class),
                        "given collection can't be null"
                ),
                arguments(
                        "when you try to obtain a Merger of different types if you pass a second collection class as " +
                        "null will throw an IllegalArgumentException with message param: secondCollectionClass can't " +
                        "be null",
                        IllegalArgumentException.class,
                        (Executable) () -> CollectionMergers.ofTwoTypes(new ArrayList<>(), null, Long.class),
                        "param: secondCollectionClass can't be null."
                ),
                arguments(
                        "when you try to obtain a Merger of different types if you pass a null result class will throw" +
                        "an IllegalArgumentsException with message param: resultClass can't be null",
                        IllegalArgumentException.class,
                        (Executable) () -> CollectionMergers.ofTwoTypes(new ArrayList<>(), Long.class, null),
                        "param: resultClass can't be null."
                )
        );
    }

    @ParameterizedTest(name="{0}")
    @MethodSource("invalidInputs")
    @DisplayName("bad way testing")
    void test1(
            @SuppressWarnings("unused") String testName,
            Class<? extends Throwable> expectedErrorClass,
            Executable executable,
            String expectedErrorMessage
    ){
        String obtainedMessage = assertThrows(expectedErrorClass,executable).getMessage();
        assertEquals(expectedErrorMessage,obtainedMessage);
    }

}