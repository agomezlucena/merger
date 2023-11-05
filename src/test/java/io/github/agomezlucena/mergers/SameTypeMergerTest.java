package io.github.agomezlucena.mergers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class SameTypeMergerTest {
    @Test
    @DisplayName("create a SameTypeMerge object will ignore any null normalizer function")
    void test1(){
        SameTypeMerger<String> merger = new SameTypeMerger<>(Collections.emptyList());
        Assertions.assertDoesNotThrow(()-> merger.withFirstCollectionNormalizer(null));
        Assertions.assertDoesNotThrow(()-> merger.withSecondCollectionNormalizer(null));
    }
}