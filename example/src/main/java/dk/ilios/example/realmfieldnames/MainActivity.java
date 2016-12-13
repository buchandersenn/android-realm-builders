package dk.ilios.example.realmfieldnames;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dk.ilios.realmfieldnames.R;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder(this)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm = Realm.getInstance(realmConfig);

        Dog spot = new DogBuilder().name("Spot").age(3).build();
        Dog fluffy = new DogBuilder().name("Fluffy").age(5).build();

        Person person = new PersonBuilder()
                .name("John")
                .addDog(spot)
                .addDog(fluffy)
                .favoriteDog(spot)
                .build();

//        new DogQueryBuilder().age().between(0, 3);

        RealmResults<Person> results = realm.where(Person.class)
                .equalTo(PersonFields.NAME, "John")
                .findAll();

        RealmResults<Person> results2 = realm.where(Person.class)
                .equalTo(PersonFields.FAVORITE_DOG.NAME, "Fido")
                .findAll();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
