package dk.ilios.example.realmfieldnames;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class Person extends RealmObject {

    public String name;
    public boolean hasDogs; // camel case naming gets converted to uppercase separated by "_"
    public boolean mHasCats; // Hungarian notation is evil and not supported (yet).
    public Boolean has_fish; // fields already using "_" are just converted as they are.
    public RealmList<Dog> dogs;
    public Dog favoriteDog;
    public Date birthDate;

    @Ignore
    public int random;
}
