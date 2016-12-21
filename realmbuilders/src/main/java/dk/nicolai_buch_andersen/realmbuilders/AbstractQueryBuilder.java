package dk.nicolai_buch_andersen.realmbuilders;

import java.util.Date;

import io.realm.RealmModel;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

// TODO : Add support for pretty-printing queries (for debug)
public class AbstractQueryBuilder<T extends RealmModel> {
    protected RealmQuery<T> query;

    public boolean isValid() {
        return query.isValid();
    }

    public T findFirst() {
        return (T)query.findFirst();
    }

    public T findFirstAsync() {
        return (T)query.findFirstAsync();
    }

    public RealmResults<T> findAll() {
        return query.findAll();
    }

    public RealmResults<T> findAllAsync() {
        return query.findAllAsync();
    }

    public RealmResults<T> findAllSorted(String fieldName) {
        return query.findAllSorted(fieldName);
    }

    public RealmResults<T> findAllSorted(String fieldName, Sort sortOrder) {
        return query.findAllSorted(fieldName, sortOrder);
    }

    public RealmResults<T> findAllSorted(String[] fieldNames, Sort[] sortOrders) {
        return query.findAllSorted(fieldNames, sortOrders);
    }

    public RealmResults<T> findAllSorted(String fieldName1, Sort sortOrder1, String fieldName2, Sort sortOrder2) {
        return query.findAllSorted(fieldName1, sortOrder1, fieldName2, sortOrder2);
    }

    public RealmResults<T> findAllSortedAsync(String fieldName) {
        return query.findAllSortedAsync(fieldName);
    }

    public RealmResults<T> findAllSortedAsync(String fieldName, Sort sortOrder) {
        return query.findAllSortedAsync(fieldName, sortOrder);
    }

    public RealmResults<T> findAllSortedAsync(String[] fieldNames, Sort[] sortOrders) {
        return query.findAllSortedAsync(fieldNames, sortOrders);
    }

    public RealmResults<T> findAllSortedAsync(String fieldName1, Sort sortOrder1, String fieldName2, Sort sortOrder2) {
        return query.findAllSortedAsync(fieldName1, sortOrder1, fieldName2, sortOrder2);
    }

    public RealmResults<T> distinct(String fieldName) {
        return query.distinct(fieldName);
    }

    public RealmResults<T> distinct(String firstFieldName, String... remainingFieldNames) {
        return query.distinct(firstFieldName, remainingFieldNames);
    }

    public RealmResults<T> distinctAsync(String fieldName) {
        return query.distinctAsync(fieldName);
    }

    public Number max(String fieldName) {
        return query.max(fieldName);
    }

    public Date maximumDate(String fieldName) {
        return query.maximumDate(fieldName);
    }

    public Number min(String fieldName) {
        return query.min(fieldName);
    }

    public Date minimumDate(String fieldName) {
        return query.minimumDate(fieldName);
    }

    public double average(String fieldName) {
        return query.average(fieldName);
    }

    public Number sum(String fieldName) {
        return query.sum(fieldName);
    }

    public long count() {
        return query.count();
    }
}