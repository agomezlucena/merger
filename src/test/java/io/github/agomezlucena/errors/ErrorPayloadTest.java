package io.github.agomezlucena.errors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorPayloadTest {
    @Test
    @DisplayName("if try to instantiate an exception and can't do it will throw a IllegalStateException")
    void test1(){
        String expectedMessage = "can't create TestException exception";

        RuntimeException error = ErrorPayload.of(ErrorMessageKeys.HASH_FUNCTION_IS_NULL,TestException.class)
                .throwException();
        assertEquals(IllegalStateException.class,error.getClass());
        assertEquals(expectedMessage,error.getMessage());
    }
    private static class TestException extends RuntimeException{
        private TestException(String message){
            super(message);
        }
    }
}