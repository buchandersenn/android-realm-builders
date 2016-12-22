package com.github.buchandersenn.realmbuilders.queries;

import com.github.buchandersenn.realmbuilders.AbstractConditionBuilder;
import com.github.buchandersenn.realmbuilders.AbstractQueryBuilder;
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

import io.realm.Realm;
import io.realm.RealmQuery;

/**
 * Class responsible for creating the final output files.
 */
class FileGenerator {
    private final Filer filer;
    private final MethodNameFormatter methodNameFormatter;
    private final ClassNameFormatter classNameFormatter;

    FileGenerator(Filer filer) {
        this.filer = filer;
        this.methodNameFormatter = new MethodNameFormatter();
        this.classNameFormatter = new ClassNameFormatter();
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

    private boolean generateFile(ClassData currentClassData, Set<ClassData> allClassData) {
        String builderClassName = currentClassData.getSimpleClassName() + "QueryBuilder";
        ClassName builderClass = ClassName.get(currentClassData.getPackageName(), builderClassName);

        ClassName rawSuperClass = ClassName.get(AbstractQueryBuilder.class);
        ClassName realmModelClass = ClassName.get(currentClassData.getPackageName(), currentClassData.getSimpleClassName());
        TypeName builderSuperClass = ParameterizedTypeName.get(rawSuperClass, realmModelClass);

        TypeSpec.Builder fileBuilder = TypeSpec.classBuilder(builderClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(builderSuperClass)
                .addJavadoc("This class is a query builder for $L.$L\n",
                        currentClassData.getPackageName(), currentClassData.getSimpleClassName());


        addConstructor(fileBuilder, realmModelClass);
        addLogicMethods(fileBuilder, builderClass);

        // Add a query method for each field...
        for (Map.Entry<String, Class<? extends AbstractConditionBuilder>> entry : currentClassData.getFields().entrySet()) {
            String fieldName = entry.getKey();
            Class<? extends AbstractConditionBuilder> conditionClass = entry.getValue();

            if (conditionClass == null) {
                continue;
            }

            ClassName conditionBuilder = ClassName.get(conditionClass);
            addQueryMethod(fileBuilder, builderClass, conditionBuilder, fieldName);
        }

        // Add a query method for each linked field...
        for (Map.Entry<String, ClassName> entry : currentClassData.getLinkedFields().entrySet()) {
            String fieldName = entry.getKey();
            ClassName fieldType = entry.getValue();

            if (fieldType == null) {
                continue;
            }

            // Find type in list of known classes
            ClassData nestedClassData = null;
            for (ClassData classData : allClassData) {
                if (classData.getPackageName().equals(fieldType.packageName()) && classData.getSimpleClassName().equals(fieldType.simpleName())) {
                    nestedClassData = classData;
                    break;
                }
            }

            // Build query method and a nested query class for the linked field...
            if (nestedClassData != null) {
                String nestedBuilderSimpleName = classNameFormatter.format(fieldName) + "QueryBuilder";
                addLinkedQueryMethod(fileBuilder, builderClass, fieldName, nestedBuilderSimpleName);
                addLinkedQueryNestedClass(fileBuilder, builderClass, builderSuperClass, nestedBuilderSimpleName, realmModelClass, fieldName, nestedClassData);
            }
        }

        JavaFile javaFile = JavaFile.builder(currentClassData.getPackageName(), fileBuilder.build()).build();
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

    // TODO : Add caching of the condition builder for each field in a private variable, so as not to build a new class each time the method is invoked
    // TODO : Also add caching for nested queries
    private void addQueryMethod(TypeSpec.Builder fileBuilder, ClassName builderClass, ClassName conditionBuilder, String fieldName) {
        TypeName parameterizedConditionBuilder = ParameterizedTypeName.get(conditionBuilder, builderClass);
        MethodSpec method = MethodSpec.methodBuilder(methodNameFormatter.format(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(parameterizedConditionBuilder)
                .addStatement("return new $T($S, this)", parameterizedConditionBuilder, fieldName)
                .build();
        fileBuilder.addMethod(method);
    }

    private void addLinkedQueryMethod(TypeSpec.Builder fileBuilder, ClassName builderClass, String fieldName, String nestedClassName) {
        ClassName nestedBuilderClass = ClassName.get(builderClass.packageName(), builderClass.simpleName() + "." + nestedClassName);
        MethodSpec method = MethodSpec.methodBuilder(methodNameFormatter.format(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .returns(nestedBuilderClass)
                .addStatement("return new $T(this.query)", nestedBuilderClass)
                .build();
        fileBuilder.addMethod(method);
    }

    private void addLinkedQueryNestedClass(TypeSpec.Builder fileBuilder, ClassName builderClass, TypeName builderSuperClass, String nestedBuilderSimpleName, ClassName realmModelClass, String fieldName, ClassData classData) {
        TypeSpec.Builder nestedClassBuilder = TypeSpec.classBuilder(nestedBuilderSimpleName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .superclass(builderSuperClass);

        // Add constructor to nested class...
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(ParameterizedTypeName.get(ClassName.get(RealmQuery.class), realmModelClass), "query")
                .addStatement("this.query = query")
                .build();
        nestedClassBuilder.addMethod(constructor);

        // Add query methods to nested class...
        for (Map.Entry<String, Class<? extends AbstractConditionBuilder>> entry : classData.getFields().entrySet()) {
            String nestedFieldName = entry.getKey();
            Class<? extends AbstractConditionBuilder> conditionClass = entry.getValue();

            if (conditionClass == null) {
                continue;
            }

            ClassName conditionBuilder = ClassName.get(conditionClass);

            TypeName parameterizedConditionBuilder = ParameterizedTypeName.get(conditionBuilder, builderClass);
            MethodSpec method = MethodSpec.methodBuilder(methodNameFormatter.format(nestedFieldName))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(parameterizedConditionBuilder)
                    .addStatement("return new $T($S, $T.this)", parameterizedConditionBuilder, fieldName + "." + nestedFieldName, builderClass)
                    .build();
            nestedClassBuilder.addMethod(method);
        }

        fileBuilder.addType(nestedClassBuilder.build());
    }
}
