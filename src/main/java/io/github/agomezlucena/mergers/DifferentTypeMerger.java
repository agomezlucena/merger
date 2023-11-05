package io.github.agomezlucena.mergers;

import io.github.agomezlucena.errors.ErrorMessageKeys;
import io.github.agomezlucena.errors.ErrorPayload;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

import static io.github.agomezlucena.errors.ThrowingFunctions.throwIfNull;

/**
 * This class allows to merge collections of different type using
 * two normalizing function that will map both collection to the same type.
 *
 * @param <T>  type of the object of the resulting collection.
 * @param <FC> type of the object of the first collection.
 * @param <SC> type of the object of the second collection.
 * @author Alejandro Gómez Lucena
 */
class DifferentTypeMerger<T, FC, SC> extends Merger<T, FC, SC> {
    private Function<FC, T> firstCollectionNormalizer;
    private Function<SC, T> secondCollectionNormalizer;

    protected DifferentTypeMerger(Collection<FC> firstCollectionNormalizer) {
        super(firstCollectionNormalizer);
    }

    /**
     * This method allows you to define a normalizer function for the first collection.
     * This method is an intermediate operation for building a Merger
     *
     * @param firstCollectionNormalizer a not null function that will map an FC type Object to a T Object
     * @return a DifferentTypeMerger with the normalizer function for the first collection initialized.
     * @see Merger
     */
    @Override
    public Merger<T, FC, SC> withFirstCollectionNormalizer(Function<FC, T> firstCollectionNormalizer) {
        throwIfNull(firstCollectionNormalizer, ErrorPayload.of(ErrorMessageKeys.FIRST_NORMALIZER_IS_NULL, IllegalArgumentException.class));
        this.firstCollectionNormalizer = firstCollectionNormalizer;
        return this;
    }

    /**
     * This method allows you to define a normalizer function for the second collection
     *
     * @param secondCollectionNormalizer a not null function than will map an SC type Object to a T Object
     * @return a DifferentTYpeMerger as a Merge object with the normalizer function for the second collection
     * initialized.
     * @see Merger
     */
    @Override
    public Merger<T, FC, SC> withSecondCollectionNormalizer(Function<SC, T> secondCollectionNormalizer) {
        throwIfNull(secondCollectionNormalizer, ErrorPayload.of(ErrorMessageKeys.SECOND_NORMALIZER_IS_NULL, IllegalArgumentException.class));
        this.secondCollectionNormalizer = secondCollectionNormalizer;
        return this;
    }

    /**
     * will check both normalizer functions to not be null and
     * map both collection to the same type objects using theirs normalizer
     * functions and then merge them.
     *
     * @param collisionMap a map for discerning duplicates and execute the merger functions in them.
     * @return will return the values of collisionMap.
     * @throws IllegalStateException if you don't provide some of the normalizer functions.
     */
    @Override
    protected Collection<T> executeMerge(Map<Integer, T> collisionMap) {
        throwIfNull(firstCollectionNormalizer, ErrorPayload.of(ErrorMessageKeys.FIRST_NORMALIZER_IS_NULL, IllegalStateException.class));
        throwIfNull(secondCollectionNormalizer, ErrorPayload.of(ErrorMessageKeys.SECOND_NORMALIZER_IS_NULL, IllegalStateException.class));

        this.firstCollection.parallelStream()
                .map(firstCollectionNormalizer)
                .forEach(it -> collisionMap.put(hashFunction.apply(it), it));

        this.secondCollection.parallelStream()
                .map(secondCollectionNormalizer)
                .forEach(mergeFunction(collisionMap));

        return collisionMap.values();
    }
}
