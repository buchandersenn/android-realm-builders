package com.github.buchandersenn.realmbuilders.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmModel;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm realm;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);

        final Dog spot = new DogBuilder().name("Spot").registrationNumber(123).age(3).build();
        final Dog fluffy = new DogBuilder().name("Fluffy").registrationNumber(456).age(5).build();
        final Dog killer = new DogBuilder().name("Killer").registrationNumber(789).age(8).build();

        final Person john = new PersonBuilder()
                .name("John")
                .addDog(spot)
                .addDog(fluffy)
                .hasDogs(true)
                .hasCats(false)
                .favoriteDog(spot)
                .build();

        final Person jane = new PersonBuilder()
                .name("Jane")
                .addDog(killer)
                .favoriteDog(killer)
                .hasDogs(true)
                .has_fish(false)
                .hasCats(true)
                .build();

        spot.owner = john;
        fluffy.owner = john;
        killer.owner = jane;

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
                realm.copyToRealm(john);
                realm.copyToRealm(jane);
            }
        });

        Dog dogNamedKiller = new DogQueryBuilder(realm).name().equalTo("Killer").findFirst();
        appendResult("dog named Killer", dogNamedKiller);

        RealmResults<Dog> dogsOwnedByJohn = new DogQueryBuilder(realm).owner().name().equalTo("John").findAll();
        appendResults("dogs owned by John", dogsOwnedByJohn);

        Number youngestDogOwnedByJohn = new DogQueryBuilder(realm)
                .owner().name().equalTo("John")
                .age().min();
        appendResult("youngest dog owned by John", youngestDogOwnedByJohn);

        RealmResults<Dog> complexDogs = new DogQueryBuilder(realm)
                .age().greaterThanOrEqualTo(3)
                .beginGroup()
                    .registrationNumber().between(0, 200)
                    .or()
                    .registrationNumber().between(500, 1000)
                .endGroup()
                .findAll();
        appendResults("complex dog query", complexDogs);

        Person personNamedJohn = new PersonQueryBuilder(realm)
                .name().isNotEmpty()
                .name().contains("John")
                .hasDogs().equalTo(true)
                .hasCats().equalTo(false)
                .has_fish().equalTo(null)
                .has_fish().isNull()
                .findFirst();
        appendResult("Person where name contains 'John'", personNamedJohn);

        Person personNamedMartin = new PersonQueryBuilder(realm)
                .name().equalTo("Martin")
                .findFirst();
        appendResult("Person named 'Martin'", personNamedMartin);
    }

    private void appendResult(String caption, Object result) {
        textView.append(caption + ":\n");

        if (result == null) {
            textView.append("No such element in Realm\n");
        } else {
            textView.append(result + "\n");
        }

        textView.append("\n");
    }

    private void appendResults(String caption, RealmResults<? extends RealmModel> results) {
        textView.append(caption + ":\n");
        for (RealmModel model : results) {
            textView.append(model + "\n");
        }
        textView.append("\n");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
