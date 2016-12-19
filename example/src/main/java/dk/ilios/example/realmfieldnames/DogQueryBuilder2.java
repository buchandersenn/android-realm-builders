package dk.ilios.example.realmfieldnames;

import dk.nicolai_buch_andersen.realmbuilders.AbstractQueryBuilder;
import dk.nicolai_buch_andersen.realmbuilders.BooleanConditionBuilder;
import dk.nicolai_buch_andersen.realmbuilders.IntegerConditionBuilder;
import dk.nicolai_buch_andersen.realmbuilders.StringConditionBuilder;
import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * This class is a query builder for dk.ilios.example.realmfieldnames.Dog
 */
public final class DogQueryBuilder2 extends AbstractQueryBuilder<Dog> {
    public DogQueryBuilder2(Realm realm) {
        this.query = RealmQuery.createQuery(realm, Dog.class);
    }

    public IntegerConditionBuilder<DogQueryBuilder2> age() {
        return new IntegerConditionBuilder<DogQueryBuilder2>("age", this);
    }

    public IntegerConditionBuilder<DogQueryBuilder2> registrationNumber() {
        return new IntegerConditionBuilder<DogQueryBuilder2>("registrationNumber", this);
    }

    public IntegerConditionBuilder<DogQueryBuilder2> weight() {
        return new IntegerConditionBuilder<DogQueryBuilder2>("weight", this);
    }

    public OwnerQueryBuilder owner() {
        return new OwnerQueryBuilder(this.query);
    }

    public final class OwnerQueryBuilder extends AbstractQueryBuilder<Dog> {
        public OwnerQueryBuilder(RealmQuery<Dog> query) {
            this.query = query;
        }

        public StringConditionBuilder.Nullable<DogQueryBuilder2> name() {
            return new StringConditionBuilder.Nullable<DogQueryBuilder2>("owner.name", DogQueryBuilder2.this);
        }

        public BooleanConditionBuilder<DogQueryBuilder2> hasDogs() {
            return new BooleanConditionBuilder<DogQueryBuilder2>("owner.hasDogs", DogQueryBuilder2.this);
        }
    }
}
