package com.github.buchandersenn.realmbuilders.queries;

import com.github.buchandersenn.realmbuilders.AbstractConditionBuilder;
import com.squareup.javapoet.ClassName;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class responsible for keeping track of the metadata for each Realm model class.
 */
class ClassData {
    private String packageName;
    private String simpleClassName;
    private TreeMap<String, Class<? extends AbstractConditionBuilder>> fields = new TreeMap<>(); // <fieldName, condition builder class>
    private TreeMap<String, ClassName> linkedFields = new TreeMap<>(); // <fieldName, linked field type>

    ClassData(String packageName, String simpleClassName) {
        this.packageName = packageName;
        this.simpleClassName = simpleClassName;
    }

    void addField(String field, Class<? extends AbstractConditionBuilder> type) {
        fields.put(field, type);
    }

    void addLinkedField(String field, ClassName type) {
        linkedFields.put(field, type);
    }

    Map<String, Class<? extends AbstractConditionBuilder>> getFields() {
        return fields;
    }

    Map<String, ClassName> getLinkedFields() {
        return linkedFields;
    }

    String getSimpleClassName() {
        return simpleClassName;
    }

    String getPackageName() {
        return packageName;
    }
}
