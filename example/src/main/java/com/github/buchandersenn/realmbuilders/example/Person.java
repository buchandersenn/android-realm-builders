package com.github.buchandersenn.realmbuilders.example;

import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

public class Person extends RealmObject {
    public String name;
    public boolean hasDogs;
    public boolean mHasCats;
    public Boolean has_fish;
    public RealmList<Dog> dogs;
    public Dog favoriteDog;
    public Date birthDate;

    @Ignore
    public int random;
}
