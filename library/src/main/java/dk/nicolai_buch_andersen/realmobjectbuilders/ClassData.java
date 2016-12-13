package dk.nicolai_buch_andersen.realmobjectbuilders;

import com.squareup.javapoet.TypeName;

import java.util.Map;
import java.util.TreeMap;

/**
 * Class responsible for keeping track of the metadata for each Realm model class.
 */
class ClassData {
    private String packageName;
    private String simpleClassName;
    private TreeMap<String, TypeName> fields = new TreeMap<>(); // <fieldName, fieldType>

    ClassData(String packageName, String simpleClassName) {
        this.packageName = packageName;
        this.simpleClassName = simpleClassName;
    }

    void addField(String field, TypeName type) {
        fields.put(field, type);
    }

    Map<String, TypeName> getFields() {
        return fields;
    }

    String getSimpleClassName() {
        return simpleClassName;
    }

    String getPackageName() {
        return packageName;
    }
}
