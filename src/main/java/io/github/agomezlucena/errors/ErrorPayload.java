package io.github.agomezlucena.errors;

import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;

/**
 * <p>This class generate the error with the message formatted if required.</p>
 * <p>The Exception class passed <strong>must</strong> to declare a constructor with only a String argument</p>
 * @author Alejandro Gómez Lucena
 */
public class ErrorPayload {
    private final Class<? extends RuntimeException> errorClass;
    private final ErrorMessageKeys key;
    private final String messageParam;

    /**
     * Generates a ErrorPayload object
     *
     * @param key        any of the ErrorMessageKeys
     * @param errorClass any RuntimeException
     * @return an ErrorPayload object that later will generate an exception from the given
     * class with the error message referenced by the given key, if the message have any
     * substitution will replace it with null by instance if you have the string
     * <em>'param: %s must not be null'</em> will create the message <em>'param: null must not be null'</em>
     */
    public static ErrorPayload of(ErrorMessageKeys key, Class<? extends RuntimeException> errorClass) {
        return new ErrorPayload(errorClass, key);
    }

    /**
     * Generates a ErrorPayload object
     *
     * @param key          any of the ErrorMessageKey
     * @param messageParam the string with value that we expect
     * @param errorClass   any RuntimeException
     * @return an ErrorPayload object that later will generate an exception from the given class
     * formatting with the message with the string that you pass.
     */
    public static ErrorPayload of(ErrorMessageKeys key, String messageParam, Class<? extends RuntimeException> errorClass) {
        return new ErrorPayload(errorClass, key, messageParam);
    }

    private ErrorPayload(Class<? extends RuntimeException> errorClass, ErrorMessageKeys key) {
        this(errorClass, key, null);
    }

    private ErrorPayload(Class<? extends RuntimeException> errorClass, ErrorMessageKeys key, String messageParam) {
        this.errorClass = errorClass;
        this.key = key;
        this.messageParam = messageParam;
    }

    /**
     * Return the configured exception with the required message based in its error key.
     * @return the expected exception but when can't instantiate the expected exception
     * will return an IllegalStateException with message 'can't create ExceptionName exception'
     */
    public RuntimeException throwException() {
        try {
            return errorClass.getDeclaredConstructor(String.class).newInstance(getMessage());
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            return new IllegalStateException(getMessage("errors.exception.instantiate",errorClass.getSimpleName()),e);
        }
    }

    private String getMessage() {
        return getMessage(key.key,messageParam);
    }

    private String getMessage(String key,String param){
        String message = ResourceBundle.getBundle("errors").getString(key);
        if (message.contains("%s")) return String.format(message, param);
        return message;
    }
}
