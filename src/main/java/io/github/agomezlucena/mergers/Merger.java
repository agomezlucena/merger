package io.github.agomezlucena.mergers;

import io.github.agomezlucena.errors.ErrorMessageKeys;
import io.github.agomezlucena.errors.ErrorPayload;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.github.agomezlucena.errors.ThrowingFunctions.throwIfNull;

/**
 * This class allow you to merge two collections based in a hash function to define the identity
 * and a merger function to execute the merge logic.
 * By default, if you don't define any hash function will Object.hashCode
 *
 * @param <T>  the type of the resulting collection.
 * @param <FC> the type of the first collection.
 * @param <SC> the type of the second collection.
 * @author Alejandro Gómez Lucena.
 */
public abstract class Merger<T, FC, SC> {
    protected final Collection<FC> firstCollection;
    protected Collection<SC> secondCollection;
    protected Function<T, Integer> hashFunction = Object::hashCode;
    protected BiFunction<T, T, T> mergerFunction;

    /**
     * Allows you to create a Merger object with a not null collection of objects of type FC
     * @param firstCollection not null collection
     * @throws IllegalArgumentException if firstCollection is null
     */
    protected Merger(Collection<FC> firstCollection) {
        throwIfNull(firstCollection, ErrorPayload.of(ErrorMessageKeys.COLLECTION_IS_NULL, IllegalArgumentException.class));
        this.firstCollection = firstCollection;
    }

    /**
     * will set the second collection if not null
     * @param secondCollection a not null collection of the required type
     * @return a Merger object with the second collection initialized
     * @throws IllegalArgumentException if secondCollection is null.
     */
    public Merger<T, FC, SC> withSecondCollection(Collection<SC> secondCollection){
        throwIfNull(secondCollection,ErrorPayload.of(ErrorMessageKeys.COLLECTION_IS_NULL,IllegalArgumentException.class));
        this.secondCollection = secondCollection;
        return this;
    }

    /**
     * will set first collection normalizer function with a not null function that map FC to T.
     * @param firstCollectionNormalizer a not null function that map FC -> T.
     * @return a Merger object with the firstCollectionNormalizedFunction set.
     */
    public abstract Merger<T, FC, SC> withFirstCollectionNormalizer(Function<FC, T> firstCollectionNormalizer);
    /**
     * will set first collection normalizer function with a not null function that map SC to T.
     * @param secondCollectionNormalizer a not null function that map SC -> T.
     * @return a Merger object with the secondCollectionNormalizer set.
     */
    public abstract Merger<T, FC, SC> withSecondCollectionNormalizer(Function<SC, T> secondCollectionNormalizer);

    /**
     * This method will initialize the hash function different to the default
     * Object.hashCode if you use any other object or need a specific identity
     * definition use this method before merging the collections.
     *
     * @param hashFunction a function that map the value to an integer
     * @return a Merger object with the given hash function
     */
    public Merger<T, FC, SC> withHashFunction(Function<T, Integer> hashFunction) {
        throwIfNull(hashFunction,ErrorPayload.of(ErrorMessageKeys.HASH_FUNCTION_IS_NULL,IllegalArgumentException.class));
        this.hashFunction = hashFunction;
        return this;
    }

    /**
     * This method will initialize the merger function.
     *
     * @param mergerFunction function in where you include your merge logic.
     * @return a Merger object with the given merge function.
     */
    public Merger<T, FC, SC> withMergerFunction(BiFunction<T, T, T> mergerFunction) {
        throwIfNull(mergerFunction, ErrorPayload.of(ErrorMessageKeys.MERGER_FUNCTION_IS_NULL, IllegalArgumentException.class));
        this.mergerFunction = mergerFunction;
        return this;
    }

    /**
     * Will execute the merge in both collection, will return a new one
     * with the merged data.
     *
     * @return a collection with result of merging both collections.
     */
    public Collection<T> merge() {
        throwIfNull(secondCollection, ErrorPayload.of(ErrorMessageKeys.COLLECTION_IS_NULL, IllegalStateException.class));
        throwIfNull(mergerFunction, ErrorPayload.of(ErrorMessageKeys.MERGER_FUNCTION_IS_NULL, IllegalStateException.class));
        return executeMerge(new ConcurrentHashMap<>());
    }

    /**
     * this method must be overridden for allowing different ways of merge the data.
     *
     * @param collisionMap a map for discerning duplicates.
     * @return the collection with the merged data.
     */
    protected abstract Collection<T> executeMerge(Map<Integer, T> collisionMap);

    /**
     * this method generate a consumer that execute the client custom logic for merge
     * @param collisionMap a map for discerning duplicates.
     * @return a Consumer function that execute the client business logic for merge
     */
    protected Consumer<T> mergeFunction(Map<Integer, T> collisionMap) {
        return (it) -> {
            Integer hash = hashFunction.apply(it);
            if (!collisionMap.containsKey(hash)) {
                collisionMap.put(it.hashCode(), it);
                return;
            }
            final T oldElement = collisionMap.get(hash);
            collisionMap.put(hash, mergerFunction.apply(oldElement, it));
        };
    }

}
