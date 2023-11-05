package io.github.agomezlucena.mergers;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * This class allows you to merge to collections of the same type.
 * Doesn't require any normalizer function because
 * @param <T> the type that you need to merge
 */
class SameTypeMerger<T> extends Merger<T, T, T> {

    SameTypeMerger(Collection<T> firstCollection) {
        super(firstCollection);
    }

    /**
     * this method will return the same object without initializing the first collection normalizer function,
     * because isn't required, for this kind of jobs.
     * @param firstCollectionNormalizer in this case is ignored.
     * @return the same merge object.
     */
    @Override
    public Merger<T, T, T> withFirstCollectionNormalizer(Function<T, T> firstCollectionNormalizer) {
        return this;
    }

    /**
     * this method will return the same object without initializing the second collection normalizer function,
     * because isn't required, for this kind of jobs.
     * @param secondCollectionNormalizer in this case is ignored.
     * @return the same merge object.
     */
    @Override
    public Merger<T, T, T> withSecondCollectionNormalizer(Function<T, T> secondCollectionNormalizer) {
       return this;
    }

    /**
     * This method will merge both collections with the passed hash and merge functions
     * will ignore any normalizer function.
     * @return a collection with the data merged based on the given hash and merge functions.
     */
    @Override
    protected Collection<T> executeMerge(Map<Integer, T> collisionMap) {
        firstCollection.parallelStream().forEach(it -> collisionMap.put(hashFunction.apply(it), it));
        secondCollection.parallelStream().forEach(mergeFunction(collisionMap));
        return collisionMap.values();
    }

}
