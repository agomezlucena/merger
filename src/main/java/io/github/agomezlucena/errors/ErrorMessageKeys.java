package io.github.agomezlucena.errors;

/**
 * This enum represent error message keys.
 */
public enum ErrorMessageKeys {
    /**
     * Represents the key used when the given collection is null
     */
    COLLECTION_IS_NULL("collections.isnull"),
    /**
     * Represent the key used when the hash function is null
     */
    HASH_FUNCTION_IS_NULL("functions.hash.isnull"),
    /**
     * Represent the key used when the given merger function is null
     */
    MERGER_FUNCTION_IS_NULL("functions.merger.isnull"),
    /**
     * Represent the key used when the given normalizer function is null
     */
    FIRST_NORMALIZER_IS_NULL("functions.normalizer.first.isnull"),
    /**
     * Represent the key used when the given normalizer function is null
     */
    SECOND_NORMALIZER_IS_NULL("functions.normalizer.second.isnull"),
    /**
     * Represent the key used when the given parameter is null
     */
    PARAM_IS_NULL("param.isnull");
    final String key;
    ErrorMessageKeys(String key){
        this.key = key;
    }
}