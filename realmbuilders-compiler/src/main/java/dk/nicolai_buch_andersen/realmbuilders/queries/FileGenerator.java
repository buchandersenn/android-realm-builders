package dk.nicolai_buch_andersen.realmbuilders.queries;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Types;

import dk.nicolai_buch_andersen.realmbuilders.AbstractConditionBuilder;
import dk.nicolai_buch_andersen.realmbuilders.AbstractQueryBuilder;
import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Class responsible for creating the final output files.
 */
class FileGenerator {

    private final Filer filer;
    private final MethodNameFormatter formatter;

    FileGenerator(Filer filer, Types typeUtils) {
        this.filer = filer;
        this.formatter = new MethodNameFormatter();
    }

    /**
     * Generates all the builders.
     * @param fileData Files to create.
     * @return {@code true} if the files where generated, {@code false} if not.
     */
    boolean generate(Set<ClassData> fileData) {
        for (ClassData classData : fileData) {
            if (!generateFile(classData, fileData)) {
                return false;
            }
        }

        return true;
    }

    private boolean generateFile(ClassData classData, Set<ClassData> classPool) {

        String builderClassName = classData.getSimpleClassName() + "QueryBuilder";
        ClassName builderClass = ClassName.get(classData.getPackageName(), builderClassName);

        ClassName rawSuperClass = ClassName.get(AbstractQueryBuilder.class);
        ClassName realmModelClass = ClassName.get(classData.getPackageName(), classData.getSimpleClassName());
        TypeName parameterizedSuperClass = ParameterizedTypeName.get(rawSuperClass, realmModelClass);

        TypeSpec.Builder fileBuilder = TypeSpec.classBuilder(builderClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(parameterizedSuperClass)
                .addJavadoc("This class is a query builder for $L.$L\n",
                        classData.getPackageName(), classData.getSimpleClassName());


        addConstructor(fileBuilder, realmModelClass);
        addLogicMethods(fileBuilder, builderClass);

        // Add a query method for each field...
        for (Map.Entry<String, Class<? extends AbstractConditionBuilder>> entry : classData.getFields().entrySet()) {
            String fieldName = entry.getKey();
            Class<? extends AbstractConditionBuilder> fieldType = entry.getValue();

            if (fieldType == null) {
                continue;
            }

            ClassName conditionBuilder = ClassName.get(fieldType);
            addQueryMethod(fileBuilder, builderClass, conditionBuilder, fieldName);
        }

        JavaFile javaFile = JavaFile.builder(classData.getPackageName(), fileBuilder.build()).build();
        try {
            javaFile.writeTo(filer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addConstructor(TypeSpec.Builder fileBuilder, ClassName realmModelClass) {
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(Realm.class, "realm")
                .addStatement("this.query = $T.createQuery(realm,$T.class)", RealmQuery.class, realmModelClass)
                .build();
        fileBuilder.addMethod(constructor);
    }

    private void addLogicMethods(TypeSpec.Builder fileBuilder, ClassName builderClass) {
        MethodSpec not = MethodSpec.methodBuilder("not")
                .addModifiers(Modifier.PUBLIC)
                .returns(builderClass)
                .addStatement("this.query.not()")
                .addStatement("return this")
                .build();
        fileBuilder.addMethod(not);

        MethodSpec or = MethodSpec.methodBuilder("or")
                .addModifiers(Modifier.PUBLIC)
                .returns(builderClass)
                .addStatement("this.query.or()")
                .addStatement("return this")
                .build();
        fileBuilder.addMethod(or);

        MethodSpec beginGroup = MethodSpec.methodBuilder("beginGroup")
                .addModifiers(Modifier.PUBLIC)
                .returns(builderClass)
                .addStatement("this.query.beginGroup()")
                .addStatement("return this")
                .build();
        fileBuilder.addMethod(beginGroup);

        MethodSpec endGroup = MethodSpec.methodBuilder("endGroup")
                .addModifiers(Modifier.PUBLIC)
                .returns(builderClass)
                .addStatement("this.query.endGroup()")
                .addStatement("return this")
                .build();
        fileBuilder.addMethod(endGroup);
    }

    private void addQueryMethod(TypeSpec.Builder fileBuilder, ClassName builderClass, ClassName conditionBuilder, String fieldName) {
        TypeName parameterizedConditionBuilder = ParameterizedTypeName.get(conditionBuilder, builderClass);
        MethodSpec method = MethodSpec.methodBuilder(formatter.format(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(parameterizedConditionBuilder)
                .addStatement("return new $T($S, this)", parameterizedConditionBuilder, fieldName)
                .build();
        fileBuilder.addMethod(method);
    }
}
