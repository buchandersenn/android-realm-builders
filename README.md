# Realm Builders

This library auto-generates builder classes for creating and querying Realm data.
For each Realm model class two corresponding classes are created:

* `<class>Builder`
* `<class>QueryBuilder`

THE LIBRARY IS STILL A WORK IN PROGRESS. NOT YET RELEASED.

## Installation

Just include the following dependency in your `gradle.build` file:

```gradle
compile 'com.github.buchandersenn:android-realm-builders:1.0.0'
apt 'com.github.buchandersenn:android-realm-builders-compiler:1.0.0'
```

This library is compatible with  `Realm 1.1.1` and onwards.

## Usage

The library adds an annotation processor that automatically detects all Realm model classes and
generates two extra classes called `<class>Builder` and `<class>QueryBuilder.` These classes can
then be used as follows:

```java
// Standard Realm Model class
public class Dog extends RealmObject {
    public String name;
    public int age;
}

public class Person extends RealmObject {
    public String name;
    public boolean hasDogs;
    public boolean mHasCats;
    public Boolean has_fish;
    public Dog favoriteDog;
    public RealmList<Dog> dogs;

    @Ignore
    public int random;
}


Realm realm = Realm.getDefaultInstance();

// Create model objects using the object builders...
final Dog spot = new DogBuilder().name("Spot").age(3).build();
final Dog fluffy = new DogBuilder().name("Fluffy").age(5).build();
final Dog killer = new DogBuilder().name("Killer").age(8).build();

final Person john = new PersonBuilder()
        .name("John")
        .addDog(spot)
        .addDog(fluffy)
        .favoriteDog(spot)
        .hasDogs(true)
        .hasCats(false)
        .build();

final Person jane = new PersonBuilder()
        .name("Jane")
        .addDog(killer)
        .favoriteDog(killer)
        .hasDogs(true)
        .has_fish(false)
        .hasCats(true)
        .build();

// Persist the created objects, attaching them to the realm...
realm.executeTransaction(new Realm.Transaction() {
    @Override
    public void execute(Realm realm) {
        realm.copyToRealm(john);
        realm.copyToRealm(jane);
    }
});

// Execute simple and chained queries using the query builders...
Dog dogNamedKiller = new DogQueryBuilder(realm)
    .name().equalTo("Killer")
    .findFirst();

RealmResults<Person> personsOwningPuppies = new PersonQueryBuilder(realm)
    .dogs().age().lessThanOrEqualTo(3)
    .findAll();
```

The main benefits are the removal of all the error-prone field name strings
and the added type safety in the queries. Consider the standard Realm query:

```java
Dog dogNamedKiller = realm.where(Dog.class)
    .equalTo("age", "8")
    .equalTo("nome", "Killer")
    .findFirst();
```

This query compiles fine, but throws an exception at runtime due to type mismatch
(string vs. integer for the age) and a misspelled field name ("nome" vs "name").
If this is a seldom used query, or if your tests aren't quite fine-grained enough,
such code can easily make its way into production, and then break your app at runtime.

You can avoid these types of errors if you use the auto-generated query builders instead:

```java
Dog dogNamedKiller = new DogQueryBuilder(realm)
    .age().equalTo("Killer") // Won't compile - age is an integer and can't be compared to a string
    .findFirst();

RealmResults<Person> personsOwningPuppies = new PersonQueryBuilder(realm)
    .dogs().name().lessThanOrEqualTo(3) // Won't compile - string fields doesn't support lessThanOrEqualTo
    .findAll();
```

In general the methods beginsWith(), endsWith() and contains() are only available for String fields,
while between(), lessThan(), lessThanOrEqualTo(), greaterThan() and greaterThanOrEqualTo() are
available for numerical fields. The methods min() and max() are available for both numeric and
date fields; sum() and average() are available for numeric fields only.

You can see more examples in the example app [here](/example).

## Tips and Tricks

To construct objects with default or computed values, consider adding factory methods to
the Realm model classes. Some examples:

```java
public class Person extends RealmObject {
    public @PrimaryKey String uuid;
    public String name;
    public boolean hasDogs;
    public RealmList<Dog> dogs;

    public static PersonBuilder builder() {
        new PersonBuilder();
    }

    public static PersonBuilder buildWith(String name) {
        return Person.builder()
            .uuid(UUID.randomUUID().toString())
            .name(name);
    }

    public static Person createPerson(String name, Dog dog) {
        return Person.buildWith(name)
            .hasDogs(dog != null)
            .addDog(dog)
            .build();
    }
}
```

If you find yourself often executing similar queries then you can add them to
the Realm model classes as well:

```java
public class Person extends RealmObject {
    public @PrimaryKey String uuid;
    ...

    public static Person lookup(Realm realm, String uuid) {
        return new PersonQueryBuilder(realm)
            .uuid().equalTo(uuid)
            .findFirst();
    }
}
```

## Caveats

The `<class>Builder` classes are mostly a "proof of concept" as of yet. They have a number
of limitations:

* They do not support Realm model classes with private fields and setter methods
* They do not conserve the initialized values of the fields in the Realm classes
* The build methods do not check for null values in required fields when building the end result
* They lack a lot of other features found in Google's AutoValue builders

The `<class>QueryBuilder` classes also have a few limitations:

* The `findAllSorted(String fieldName)` and related methods still takes field name strings as arguments
* The `distinct(String fieldName)` and related methods still takes field name strings as arguments

To avoid typing the field names and risk spelling mistakes, use the auto-generated field name
constants in the `<class>Fields` classes, e.g. `PersonFields.NAME`.

## About this library

This library started out as a clone of https://github.com/cmelchior/realmfieldnameshelper
by Christian Melchio, and grew from there. Thanks Christian!
