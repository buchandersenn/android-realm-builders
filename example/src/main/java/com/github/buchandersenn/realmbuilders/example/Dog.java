package com.github.buchandersenn.realmbuilders.example;

import io.realm.RealmObject;
import io.realm.annotations.Required;

public class Dog extends RealmObject {
    public String name;
    public int age;
    public Integer weight;
    public @Required Integer registrationNumber;
    public Person owner;
}
