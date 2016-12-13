package dk.nicolai_buch_andersen.realmquerybuilders;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;

//import dk.nicolai_buch_andersen.realmquerybuilders.builders.AbstractQueryBuilder;
//import dk.nicolai_buch_andersen.realmquerybuilders.builders.IntegerConditionBuilder;

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

        String builderClassName = classData.getSimpleClassName() + "QueryBuilder";
        TypeSpec.Builder fileBuilder = TypeSpec.classBuilder(builderClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                .superclass(AbstractQueryBuilder.class)
                .addJavadoc("This class is a query builder for $L.$L\n",
                        classData.getPackageName(), classData.getSimpleClassName());


        // Add a setter method for each field...
        for (Map.Entry<String, TypeKind> entry : classData.getFields().entrySet()) {
            String fieldName = entry.getKey();
            TypeKind fieldType = entry.getValue();
            ClassName returnClass = ClassName.get(classData.getPackageName(), builderClassName);

            switch (fieldType) {
                case INT:
                    addIntegerQueryMethod(fileBuilder, fieldName, returnClass);
                    break;
                default:

            }

            // TODO : Handle linked fields
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

    private void addIntegerQueryMethod(TypeSpec.Builder fileBuilder, String fieldName, ClassName returnClass) {
        MethodSpec method = MethodSpec.methodBuilder(formatter.format(fieldName))
                .addModifiers(Modifier.PUBLIC)
//                .returns(IntegerConditionBuilder.class)
                //.addStatement("return createIntegerConditionBuilder($S)", fieldName)
                .build();
        fileBuilder.addMethod(method);
    }
}
