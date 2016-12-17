package dk.nicolai_buch_andersen.realmbuilders.queries;

import java.util.Map;
import java.util.TreeMap;

import dk.nicolai_buch_andersen.realmbuilders.AbstractConditionBuilder;

/**
 * Class responsible for keeping track of the metadata for each Realm model class.
 */
class ClassData {
    private String packageName;
    private String simpleClassName;
    private TreeMap<String, Class<? extends AbstractConditionBuilder>> fields = new TreeMap<>(); // <fieldName, fieldType>

    ClassData(String packageName, String simpleClassName) {
        this.packageName = packageName;
        this.simpleClassName = simpleClassName;
    }

    void addField(String field, Class<? extends AbstractConditionBuilder> type) {
        fields.put(field, type);
    }

    Map<String, Class<? extends AbstractConditionBuilder>> getFields() {
        return fields;
    }

    String getSimpleClassName() {
        return simpleClassName;
    }

    String getPackageName() {
        return packageName;
    }
}
