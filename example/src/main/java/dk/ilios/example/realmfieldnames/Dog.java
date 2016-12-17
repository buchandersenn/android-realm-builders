package dk.ilios.example.realmfieldnames;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Dog extends RealmObject {
    public Person owner;
    public String name;
    public int age;
    public Integer weight;
    public @Required Integer registrationNumber;
}
