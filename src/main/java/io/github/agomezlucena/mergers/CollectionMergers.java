package io.github.agomezlucena.mergers;

import io.github.agomezlucena.errors.ErrorPayload;

import java.util.Collection;

import static io.github.agomezlucena.errors.ErrorMessageKeys.PARAM_IS_NULL;
import static io.github.agomezlucena.errors.ThrowingFunctions.throwIfNull;

/**
 * <p>This class allows you to access to the different type of mergers.</p>
 * <p>By instance you can create a merger of the same type to delete duplicates
 * with this code:</p>
 * <pre><code>CollectionMergers.ofSameType(Arrays.asList("a","b"))
 * .withSecondCollection(Arrays.asList("b","c"))
 * .withMergerFunction(it1,it2 -&gt; it1)
 * .merge()</code></pre>
 * @author Alejandro Gómez Lucena.
 */
public class CollectionMergers {
    /**
     * This method is the gateway for generating a Merger object for two collections of the same type.
     *
     * @param firstCollection a not null collection.
     * @param <T>             the type to of the merged collections.
     * @return a Merger object of the type that you define and the first collection initialized.
     * @throws IllegalArgumentException if firstCollection is null
     */
    public static <T> Merger<T, T, T> ofSameType(Collection<T> firstCollection) {
        return new SameTypeMerger<>(firstCollection);
    }

    /**
     * Allows you to generate a Merger object for two different types.
     *
     * @param firstCollection       a not null collection.
     * @param secondCollectionClass type of the objects of the second collection used only for type inferring
     * @param resultClass           type of the object of resulting collection used only for type inferring
     * @param <T>                   type of object of the resulting collection
     * @param <FC>                  type of object of the first collection
     * @param <SC>                  type of object of the second collection
     * @return a merger object of the specified types with the first collection initialized.
     * @throws IllegalArgumentException if any of the parameters are null
     */
    public static <T, FC, SC> Merger<T, FC, SC> ofTwoTypes(
            Collection<FC> firstCollection,
            Class<SC> secondCollectionClass,
            Class<T> resultClass
    ) {
        throwIfNull(secondCollectionClass, ErrorPayload.of(PARAM_IS_NULL, "secondCollectionClass", IllegalArgumentException.class));
        throwIfNull(resultClass, ErrorPayload.of(PARAM_IS_NULL, "resultClass", IllegalArgumentException.class));
        return new DifferentTypeMerger<>(firstCollection);
    }

    private CollectionMergers(){}
}
