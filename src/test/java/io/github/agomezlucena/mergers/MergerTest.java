package io.github.agomezlucena.mergers;

import io.github.agomezlucena.entities.Seller;
import io.github.agomezlucena.entities.SellerLastWeekSales;
import io.github.agomezlucena.entities.SellerWithLastWeekSales;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MergerTest {
    @Test
    @DisplayName("if comes two collections with different values must return a collection that is the concatenation of the first with the second")
    void test1() {
        Collection<String> expectedCollection = Arrays.asList("a", "b", "c", "d", "e");
        Collection<String> givenFirstCollection = Arrays.asList("a", "b");
        Collection<String> givenSecondCollection = Arrays.asList("c", "d", "e");
        BiFunction<String, String, String> givenCollisionFunction = (first, second) -> first;
        Collection<String> obtainedValue = CollectionMergers.ofSameType(givenFirstCollection)
                .withSecondCollection(givenSecondCollection)
                .withMergerFunction(givenCollisionFunction)
                .merge();
        assertThat(obtainedValue).containsAll(expectedCollection);
    }

    @Test
    @DisplayName("if comes two collections with some repeated value will merge the values based on the passed collision function")
    void test2() {
        Collection<String> expectedCollection = Arrays.asList("a", "bb", "cc", "dd", "e");
        Collection<String> givenFirstCollection = Arrays.asList("a", "b", "c", "d");
        Collection<String> givenSecondCollection = Arrays.asList("b", "c", "d", "e");
        BiFunction<String, String, String> givenCollisionFunction = (first, second) -> first + second;
        Collection<String> obtainedValue = CollectionMergers.ofSameType(givenFirstCollection)
                .withSecondCollection(givenSecondCollection)
                .withMergerFunction(givenCollisionFunction)
                .merge();
        assertThat(obtainedValue).containsAll(expectedCollection);
    }

    @Test
    @DisplayName("if comes two collections of objects will merge as expected")
    void test3() {
        UUID firstId = UUID.randomUUID();
        UUID secondId = UUID.randomUUID();
        Map<UUID, Seller> results = new HashMap<>();

        results.put(firstId, new Seller(firstId, "alex", "casa"));
        results.put(secondId, new Seller(secondId, "zoe", "casa"));

        Collection<Seller> givenFirstCollection = Arrays.asList(
                new Seller(firstId, null, "casa"),
                new Seller(secondId, "zoe", null)
        );
        Collection<Seller> givenSecondCollection = Arrays.asList(
                new Seller(firstId, "alex", null),
                new Seller(secondId, null, "casa")
        );
        Function<Seller, Integer> givenHashFunction = it -> Objects.hash(it.getSellerId());
        BiFunction<Seller, Seller, Seller> givenCollisionFunction = (first, second) ->
                new Seller(
                        first.getSellerId(),
                        Optional.ofNullable(first.getName()).orElse(second.getName()),
                        Optional.ofNullable(first.getLocation()).orElse(second.getLocation())
                );

        Collection<Seller> obtainedValues = CollectionMergers.ofSameType(givenFirstCollection)
                .withSecondCollection(givenSecondCollection)
                .withHashFunction(givenHashFunction)
                .withMergerFunction(givenCollisionFunction)
                .merge();

        obtainedValues.forEach(obtainedValue -> {
            if (!results.containsKey(obtainedValue.getSellerId()))
                fail("clave invalida, no se encuentra entre los resultados.");
            Seller expectedValue = results.get(obtainedValue.getSellerId());
            assertEquals(expectedValue.getName(), obtainedValue.getName());
            assertEquals(expectedValue.getLocation(), obtainedValue.getLocation());
        });
    }

    @Test
    @DisplayName("you can merge two differents types of data passing two normalizing functions")
    void test4() {
        UUID firstId = UUID.randomUUID();
        UUID secondId = UUID.randomUUID();
        Map<UUID, SellerWithLastWeekSales> results = new HashMap<>();
        results.put(firstId, new SellerWithLastWeekSales(firstId, "alex", "casa", 1000.50));
        results.put(secondId, new SellerWithLastWeekSales(secondId, "zoe", "casa", 15000d));

        Collection<Seller> sellersBaseInfoCollection = Arrays.asList(
                new Seller(firstId, "alex", "casa"),
                new Seller(secondId, "zoe", "casa")
        );

        Collection<SellerLastWeekSales> sellersSales = Arrays.asList(
                new SellerLastWeekSales(firstId, 1000.50),
                new SellerLastWeekSales(secondId, 15000d)
        );

        Function<SellerWithLastWeekSales, Integer> givenHashFunction = it -> Objects.hash(it.getSellerId());

        Collection<SellerWithLastWeekSales> result = CollectionMergers.ofTwoTypes(
                        sellersBaseInfoCollection,
                        SellerLastWeekSales.class,
                        SellerWithLastWeekSales.class
                )
                .withSecondCollection(sellersSales)
                .withFirstCollectionNormalizer(sellerNormalizerFunction())
                .withSecondCollectionNormalizer(sellerLastWeekSalesNormalizerFunction())
                .withHashFunction(givenHashFunction)
                .withMergerFunction(sellersCollisionFunction())
                .merge();

        result.forEach(obtainedValue -> {
            if (!results.containsKey(obtainedValue.getSellerId()))
                fail("clave invalida, no se encuentra entre los resultados.");
            SellerWithLastWeekSales expectedValue = results.get(obtainedValue.getSellerId());
            assertEquals(expectedValue.getName(), obtainedValue.getName());
            assertEquals(expectedValue.getLocation(), obtainedValue.getLocation());
            assertEquals(expectedValue.getAmountInEuros(), obtainedValue.getAmountInEuros());
        });
    }


    private Function<Seller,SellerWithLastWeekSales> sellerNormalizerFunction(){
        return it -> new SellerWithLastWeekSales(it.getSellerId(), it.getName(), it.getLocation(), null);
    }

    private Function<SellerLastWeekSales,SellerWithLastWeekSales> sellerLastWeekSalesNormalizerFunction(){
        return it -> new SellerWithLastWeekSales(it.getSellerId(), null, null, it.getAmountInEuros());
    }
    private  BiFunction<SellerWithLastWeekSales, SellerWithLastWeekSales, SellerWithLastWeekSales> sellersCollisionFunction(){
        return  (first, second) -> new SellerWithLastWeekSales(
                first.getSellerId(),
                Optional.ofNullable(first.getName()).orElse(second.getName()),
                Optional.ofNullable(first.getLocation()).orElse(second.getLocation()),
                Optional.ofNullable(first.getAmountInEuros()).orElse(second.getAmountInEuros())
        );
    }

}
