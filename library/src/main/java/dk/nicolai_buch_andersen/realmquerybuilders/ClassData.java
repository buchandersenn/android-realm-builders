package dk.nicolai_buch_andersen.realmquerybuilders;

import java.util.Map;
import java.util.TreeMap;

import javax.lang.model.type.TypeKind;

/**
 * Class responsible for keeping track of the metadata for each Realm model class.
 */
class ClassData {
    private String packageName;
    private String simpleClassName;
    private TreeMap<String, TypeKind> fields = new TreeMap<>(); // <fieldName, fieldType>

    ClassData(String packageName, String simpleClassName) {
        this.packageName = packageName;
        this.simpleClassName = simpleClassName;
    }

    void addField(String field, TypeKind type) {
        fields.put(field, type);
    }

    Map<String, TypeKind> getFields() {
        return fields;
    }

    String getSimpleClassName() {
        return simpleClassName;
    }

    String getPackageName() {
        return packageName;
    }
}
