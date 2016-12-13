//package dk.nicolai_buch_andersen.realmquerybuilders.builders;
//
//import io.realm.Realm;
//import io.realm.RealmModel;
//import io.realm.RealmQuery;
//
//public class AbstractQueryBuilder<T extends RealmModel> {
//    Realm realm;
//    RealmQuery<T> query;
//
//    protected IntegerConditionBuilder createIntegerConditionBuilder(String fieldName) {
//        return new IntegerConditionBuilder(fieldName, this);
//    }
//}
