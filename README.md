# Realm Builders

This library auto-generate builder classes that for creating and querying Realm data.

For each Realm model class a corresponding `<class>Builder` and `<class>QueryBuilder` is created.

## Installation

Just include the following dependency in your `gradle.build` file

```gradle
compile 'com.github.buchandersenn:android-realm-builders:1.0.0'
apt 'com.github.buchandersenn:android-realm-builders-compiler:1.0.0'
```

This library is compatible with  `Realm 1.1.1` and onwards.

## Usage

The library adds an annotation processor that automatically detects all Realm model classes and
generate two extra classes called `<class>Builder` and `<class>QueryBuilder`. These classes can
then be used as follows:

```java
// Standard Realm Model class
public class Person extends RealmObject {
    private String name;
    private boolean hasDogs; // camel case naming gets converted to uppercase separated by "_"
    private boolean mHasCats; // Hungarian notation is evil but support for m starting prefix.
    private boolean has_fish; // fields already using "_" are just converted as they are.
    private Dog favoriteDog; // linked fields are generated one link away
    private RealmList<Dog> dogs; // linked fields are generated one link away

    @Ignore
    private int random;

    // Getters and setters ...
}

public class Dog extends RealmObject {
    private String name;

    // Getters and setters ...
}


Realm realm = Realm.getDefaultInstance();
...
```

You can also see an example [here](/example).

## About this library

This library was heavily inspired by https://github.com/cmelchior/realmfieldnameshelper
by Christian Melchior.
