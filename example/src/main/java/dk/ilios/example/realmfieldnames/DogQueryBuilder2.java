//package dk.ilios.example.realmfieldnames;
//
//import dk.nicolai_buch_andersen.realmbuilders.AbstractQueryBuilder;
//import dk.nicolai_buch_andersen.realmbuilders.IntegerConditionBuilder;
//import dk.nicolai_buch_andersen.realmbuilders.IntegerConditionBuilder;
//import io.realm.Realm;
//import io.realm.RealmQuery;
//
///**
// * This class is a query builder for dk.ilios.example.realmfieldnames.Dog
// */
//public final class DogQueryBuilder2 extends AbstractQueryBuilder<Dog> {
//  public DogQueryBuilder2(Realm realm) {
//    this.query = RealmQuery.createQuery(realm, Dog.class);
//    Dog first = findFirst();
//  }
//
//  public IntegerConditionBuilder<DogQueryBuilder> age() {
//    return new IntegerConditionBuilder<DogQueryBuilder>("age", null);
//  }
//
//  public IntegerConditionBuilder<DogQueryBuilder> registrationNumber() {
//    return new IntegerConditionBuilder<DogQueryBuilder>("registrationNumber", null);
//  }
//
//  public IntegerConditionBuilder<DogQueryBuilder> weight() {
//    return new IntegerConditionBuilder<DogQueryBuilder>("weight", null);
//  }
//}
