![MERGER](assets%2FMERGER-logo.png)

# Merger
Merger is a small library that provides you a fluent api
for merge two collections without modify
the input collections, with an O(n+m) modifiable algorithm.

## Functionalities:
- ### Merge two collections of the same type
    you can merge two collections executing your own logic passing a
    callback that will be executed, and a callback for detect collision
    by default it use hashCode() method.
- ### Merge two collections of different types
    you can merge two collections of different types and execute your own
    logic using the same api but adding another two callbacks for mapping it
    to the uncompleted version of the final object.

## Importing the library to your maven project:
```xml
<dependency>
  <groupId>io.github.agomezlucena</groupId>
  <artifactId>merger</artifactId>
  <version>1.0.0</version>
</dependency>
```
## Usage
### Merging collections of the same type
In this example we are going to merge two collections of strings, and first we are going to deletes duplicates and next we are going to print the resulting collection, and next we are going to concatenate the repeated strings and print the resulting collection too.

```java
import mergers.io.github.agomezlucena.CollectionMergers;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MergerExample {

  public static void main(String[] args) {
    List<String> firstCollection = Arrays.asList("a", "b", "c");
    List<String> secondCollection = Arrays.asList("b", "c", "d");

    Collection<String> withoutDuplicates = merge(
            firstCollection,
            secondCollection,
            first, second -> first
    );
    Collection<String> concatenatedDuplicates = merge(
            firstCollection,
            secondCollection,
            first, second -> first + second
    );

    //will print [a,b,c,d]
    System.out.println(withoutDuplicates.toString());
    //will print [a,bb,cc,d]
    System.out.println(concatenatedDuplicates.toString());
  }

  private static Collection<String> merge(
          Collection<String> firstCollection,
          Collection<String> secondCollection,
          BiFunction<String, String, String> mergeFunction
  ) {
    return CollectionMergers.ofSameType(firstCollection)
            .withSecondCollection(secondCollection)
            .withMergerFunction(mergeFunction)
            .merge();
  }

}
```
### Merging collection of the same type with a custom hash function
In this example we are going to merge the incomplete data of class `PersonalData`
this class includes three fields `personId`, `name` and `location`.

```java
import mergers.io.github.agomezlucena.CollectionMergers;

@Service
@RequiredArgConstructor
public class PersonalDataCompleter implements Completer<PersonalData> {
  @Override
  public Collection<PersonalData> complete(
          Collection<PersonalData> names,
          Collection<PersonalData> locations
  ) {
    return CollectionMergers.<PersonalData>ofSameType(names)
            .withSecondCollection(locations)
            .withHashFunction(it -> Objects.hashCode(it.getPersonId()))
            .withMergerFunction(first, second ->
                    PersonalData
                            .withId(first.getPersonId())
                            .withName(first.getName())
                            .withLocation(second.getLocation())
            )
            .merge();
  }
}
```
### Merging collections of different types
In this example we are going to merge the data of contact of various sellers 
and the average of sales in the month and the contact data is in the class 
`SellerContactInfo` and `SellerAverageSales` and the result will be in `Seller` class.

```java
@Service
@RequiredArgConstructor
public class SellerServiceImp implements SellerService {
  private final SellerContactRepository sellerContactsRepo;
  private final SellerAverageSalesRepository avgSalesRepo;

  @Override
  public Collection<Seller> getSellers() {
    return CollectionMergers.ofTwoTypes(
                    sellerContactsRepo.getSellersInfo(),
                    SellerAverageSales.class,
                    Seller.class
            ).withSecondCollection(avgSalesRepo.getSellersMonthlyAverageSales())
            .firstCollectionNormalizer(this::mapSellerContactInfoToSeller)
            .secondCollectionNormalizer(this::mapSellerAverageSalesToSeller)
            .withHashFunction(this::sellerHash)
            .withMergerFunction(this::mergeSellers)
            .merge();
  }

  private Seller mapSellerContactInfoToSeller(SellerContactInfo info) {
    return new Seller(info.getSellerId(), info.getSellerName());
  }

  private Seller mapSellerAverageSalesToSeller(SellerAverageSales sales) {
    return new Seller(sales.getSellerId(), sales.getAverage());
  }

  private Integer sellerHash(Seller seller) {
    return Objects.hasCode(seller.getSellerId());
  }

  private Seler mergeSellers(Seller first, Seller second) {
    return new Seller(
            Optional.ofNullable(first.getId()).orElse(second.getId()),
            Optional.ofNullable(first.getName()).orElse(second.getName()),
            Optional.ofNullable(first.getAvg()).orElse(second.getAvg())
      );
  }
}
```
## Contributing
if you want to collaborate with this project read [CONTRIBUTING.md](CONTRIBUTING.md)

## Special thanks:
I'd like to thanks to the next projects:
  - Junit project that we use for generating test cases.
  - AssertJ that we use we also use for generating some difficult assertions.
