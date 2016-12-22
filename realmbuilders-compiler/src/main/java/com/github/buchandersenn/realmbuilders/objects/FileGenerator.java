package com.github.buchandersenn.realmbuilders.objects;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
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

import io.realm.RealmList;

/**
 * Class responsible for creating the final output files.
 */
class FileGenerator {

    private final Filer filer;
    private final MethodNameFormatter formatter;

    FileGenerator(Filer filer) {
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

        String builderClassName = classData.getSimpleClassName() + "Builder";
        TypeSpec.Builder fileBuilder = TypeSpec.classBuilder(builderClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addJavadoc("This class is a builder for $L.$L\n",
                        classData.getPackageName(), classData.getSimpleClassName());


        // Add a setter method for each field...
        for (Map.Entry<String, TypeName> entry : classData.getFields().entrySet()) {
            String fieldName = entry.getKey();
            TypeName fieldType = entry.getValue();
            ClassName returnClass = ClassName.get(classData.getPackageName(), builderClassName);

            addField(fileBuilder, fieldName, fieldType);
            addMethod(fileBuilder, fieldName, fieldType, returnClass);

            if (fieldType instanceof ParameterizedTypeName) {
                ParameterizedTypeName parameterizedType = (ParameterizedTypeName) fieldType;
                ClassName rawType = parameterizedType.rawType;
                if (rawType.packageName().equals("io.realm") && rawType.simpleName().equals("RealmList")) {
                    TypeName accumulatingType = parameterizedType.typeArguments.get(0);
                    addAccumulatingMethod(fileBuilder, fieldName, accumulatingType, returnClass);
                }
            }
        }

        // Add a build method...
        addBuildMethod(fileBuilder, classData);

        JavaFile javaFile = JavaFile.builder(classData.getPackageName(), fileBuilder.build()).build();
        try {
            javaFile.writeTo(filer);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addField(TypeSpec.Builder fileBuilder, String fieldName, TypeName fieldType) {
        FieldSpec field = FieldSpec.builder(fieldType, fieldName)
                .addModifiers(Modifier.PRIVATE)
                .build();
        fileBuilder.addField(field);
    }

    private void addMethod(TypeSpec.Builder fileBuilder, String fieldName, TypeName fieldType, ClassName returnClass) {
        MethodSpec method = MethodSpec.methodBuilder(formatter.format(fieldName))
                .addModifiers(Modifier.PUBLIC)
                .addParameter(fieldType, fieldName)
                .returns(returnClass)
                .addStatement("this." + fieldName + "=" + fieldName)
                .addStatement("return this")
                .build();
        fileBuilder.addMethod(method);
    }

    private void addAccumulatingMethod(TypeSpec.Builder fileBuilder, String fieldName, TypeName accumulatingType, ClassName returnClass) {
        String baseName = fieldName;
        if (Character.toLowerCase(baseName.charAt(baseName.length() - 1)) == 's') {
            baseName = fieldName.substring(0, baseName.length() - 1);
        }

        String methodName = "add" +
                Character.toTitleCase(baseName.charAt(0)) +
                baseName.substring(1);

        MethodSpec method = MethodSpec.methodBuilder(methodName)
                .addModifiers(Modifier.PUBLIC)
                .addParameter(accumulatingType, baseName)
                .returns(returnClass)
                .addStatement("if (this." + fieldName + "==null) " + fieldName + "= new $T<$T>()", RealmList.class, accumulatingType)
                .addStatement("this." + fieldName + ".add(" + baseName + ")")
                .addStatement("return this")
                .build();
//        if (dogs==null) dogs = new RealmList<Dog>();

        fileBuilder.addMethod(method);
    }

    private void addBuildMethod(TypeSpec.Builder fileBuilder, ClassData classData) {
        ClassName resultClass = ClassName.get(classData.getPackageName(), classData.getSimpleClassName());

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .returns(resultClass)
                .addStatement(resultClass.simpleName() + " result = new " + resultClass.simpleName() + "()");

        // TODO : Support Realm objects using private fields and setters
        // TODO : Generate null-pointer checks for required fields
        for (Map.Entry<String, TypeName> entry : classData.getFields().entrySet()) {
            String fieldName = entry.getKey();
            methodBuilder.addStatement("result." + fieldName + "= this." + fieldName);
        }

        MethodSpec method = methodBuilder
                .addStatement("return result")
                .build();

        fileBuilder.addMethod(method);
    }
}
