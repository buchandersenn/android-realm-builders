package dk.nicolai_buch_andersen.realmbuilders.queries;

import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import dk.nicolai_buch_andersen.realmbuilders.AbstractConditionBuilder;
import dk.nicolai_buch_andersen.realmbuilders.BooleanConditionBuilder;
import dk.nicolai_buch_andersen.realmbuilders.ByteArrayConditionBuilder;
import dk.nicolai_buch_andersen.realmbuilders.ByteConditionBuilder;
import dk.nicolai_buch_andersen.realmbuilders.DateConditionBuilder;
import dk.nicolai_buch_andersen.realmbuilders.DoubleConditionBuilder;
import dk.nicolai_buch_andersen.realmbuilders.FloatConditionBuilder;
import dk.nicolai_buch_andersen.realmbuilders.IntegerConditionBuilder;
import dk.nicolai_buch_andersen.realmbuilders.LongConditionBuilder;
import dk.nicolai_buch_andersen.realmbuilders.ShortConditionBuilder;
import dk.nicolai_buch_andersen.realmbuilders.StringConditionBuilder;

/**
 * The Realm Query Builder is a processor that looks at all available Realm model classes
 * and create a companion builder class for constructing instances.
 */
@SupportedAnnotationTypes({
        "io.realm.annotations.RealmClass"
})
public class RealmQueryBuilderProcessor extends AbstractProcessor {

    private static final boolean CONSUME_ANNOTATIONS = false;

    private Set<ClassData> classes = new HashSet<>();
    private Types typeUtils;
    private Messager messager;
    private Elements elementUtils;
    private TypeMirror ignoreAnnotation;
    private TypeMirror requiredAnnotation;
    private TypeMirror realmModelClass;
    private DeclaredType realmListClass;
    private FileGenerator fileGenerator;
    private boolean done = false;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
        ignoreAnnotation = elementUtils.getTypeElement("io.realm.annotations.Ignore").asType();
        requiredAnnotation = elementUtils.getTypeElement("io.realm.annotations.Required").asType();
        realmModelClass = elementUtils.getTypeElement("io.realm.RealmModel").asType();
        realmListClass = typeUtils.getDeclaredType(elementUtils.getTypeElement("io.realm.RealmList"),
                typeUtils.getWildcardType(null, null));
        fileGenerator = new FileGenerator(processingEnv.getFiler(), typeUtils);
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (done) {
            return CONSUME_ANNOTATIONS;
        }

        // Create all proxy classes
        TypeElement realmClassAnnotation = annotations.iterator().next();
        for (Element classElement : roundEnv.getElementsAnnotatedWith(realmClassAnnotation)) {
            ClassData classData = processClass((TypeElement) classElement);
            classes.add(classData);
        }

        done = fileGenerator.generate(classes);
        return CONSUME_ANNOTATIONS;
    }

    private ClassData processClass(TypeElement classElement) {
        String packageName = getPackageName(classElement);
        String className = classElement.getSimpleName().toString();
        ClassData data = new ClassData(packageName, className);

        // Find all appropriate fields
        for (Element element : classElement.getEnclosedElements()) {
            ElementKind elementKind = element.getKind();
            if (elementKind.equals(ElementKind.FIELD)) {
                VariableElement variableElement = (VariableElement) element;

                Set<Modifier> modifiers = variableElement.getModifiers();
                if (modifiers.contains(Modifier.STATIC)) {
                    continue; // completely ignore any static fields
                }

                // Don't add any fields marked with @Ignore
                List<? extends AnnotationMirror> elementAnnotations = variableElement.getAnnotationMirrors();
                boolean ignoreField = false;
                for (AnnotationMirror elementAnnotation : elementAnnotations) {
                    DeclaredType annotationType = elementAnnotation.getAnnotationType();
                    if (typeUtils.isAssignable(annotationType, ignoreAnnotation)) {
                        ignoreField = true;
                        break;
                    }
                }

                if (!ignoreField) {
                    ClassName linkedFieldType = getLinkedFieldType(element);
                    if (linkedFieldType != null) {
                        data.addLinkedField(element.getSimpleName().toString(), linkedFieldType);
                    } else {
                        data.addField(element.getSimpleName().toString(), getConditionBuilderClassForField(element));
                    }
                }
            }
        }

        return data;
    }

    /**
     * Returns the qualified name of the linked Realm class field or {@code null} if it is not a linked
     * class.
     */
    private ClassName getLinkedFieldType(Element field) {
        if (typeUtils.isAssignable(field.asType(), realmModelClass)) {
            // Object link
            TypeElement typeElement = elementUtils.getTypeElement(field.asType().toString());
            return ClassName.get(typeElement);
//            return ClassName.get(field.asType());
        } else if (typeUtils.isAssignable(field.asType(), realmListClass)) {
            // List link
            TypeMirror fieldType = field.asType();
            List<? extends TypeMirror> typeArguments = ((DeclaredType) fieldType).getTypeArguments();
            if (typeArguments.size() == 0) {
                return null;
            }
            TypeElement typeElement = elementUtils.getTypeElement(typeArguments.get(0).toString());
            return ClassName.get(typeElement);
//            return get(typeArguments.get(0));
        } else {
            return null;
        }
    }

    // TODO : Return boxed or unboxed TypeName; let FileGenerator find the correct condition builder
    private Class<? extends AbstractConditionBuilder> getConditionBuilderClassForField(Element field) {
        // TODO : Handle linked fields
        if (field.asType().getKind() == TypeKind.BOOLEAN || field.asType().toString().equals("java.lang.Boolean")) {
            boolean requiredField = isRequiredField(field);
            return requiredField ? BooleanConditionBuilder.class : BooleanConditionBuilder.Nullable.class;
        } else if (field.asType().getKind() == TypeKind.BYTE || field.asType().toString().equals("java.lang.Byte")) {
            boolean requiredField = isRequiredField(field);
            return requiredField ? ByteConditionBuilder.class : ByteConditionBuilder.Nullable.class;
        } else if (field.asType().getKind() == TypeKind.ARRAY && TypeName.get(field.asType()).equals(ArrayTypeName.of(byte.class))) {
            boolean requiredField = isRequiredField(field);
            return requiredField ? ByteArrayConditionBuilder.class : ByteArrayConditionBuilder.Nullable.class;
        } else if (field.asType().getKind() == TypeKind.SHORT || field.asType().toString().equals("java.lang.Short")) {
            boolean requiredField = isRequiredField(field);
            return requiredField ? ShortConditionBuilder.class : ShortConditionBuilder.Nullable.class;
        } else if (field.asType().getKind() == TypeKind.INT || field.asType().toString().equals("java.lang.Integer")) {
            boolean requiredField = isRequiredField(field);
            return requiredField ? IntegerConditionBuilder.class : IntegerConditionBuilder.Nullable.class;
        } else if (field.asType().getKind() == TypeKind.LONG || field.asType().toString().equals("java.lang.Long")) {
            boolean requiredField = isRequiredField(field);
            return requiredField ? LongConditionBuilder.class : LongConditionBuilder.Nullable.class;
        } else if (field.asType().getKind() == TypeKind.FLOAT || field.asType().toString().equals("java.lang.Float")) {
            boolean requiredField = isRequiredField(field);
            return requiredField ? FloatConditionBuilder.class : FloatConditionBuilder.Nullable.class;
        } else if (field.asType().getKind() == TypeKind.DOUBLE || field.asType().toString().equals("java.lang.Double")) {
            boolean requiredField = isRequiredField(field);
            return requiredField ? DoubleConditionBuilder.class : DoubleConditionBuilder.Nullable.class;
        } else if (field.asType().toString().equals("java.lang.String")) {
            boolean requiredField = isRequiredField(field);
            return requiredField ? StringConditionBuilder.class : StringConditionBuilder.Nullable.class;
        } else if (field.asType().toString().equals("java.util.Date")) {
            boolean requiredField = isRequiredField(field);
            return requiredField ? DateConditionBuilder.class : DateConditionBuilder.Nullable.class;
        }

        return null;
    }

    private boolean isRequiredField(Element field) {
        // Primitive fields are always required...
        if (field.asType().getKind().isPrimitive()) {
            return true;
        }

        boolean requiredField = false;
        List<? extends AnnotationMirror> elementAnnotations = field.getAnnotationMirrors();
        for (AnnotationMirror elementAnnotation : elementAnnotations) {
            DeclaredType annotationType = elementAnnotation.getAnnotationType();
            if (typeUtils.isAssignable(annotationType, requiredAnnotation)) {
                requiredField = true;
                break;
            }
        }
        return requiredField;
    }

    private String getPackageName(TypeElement classElement) {
        Element enclosingElement = classElement.getEnclosingElement();

        if (!enclosingElement.getKind().equals(ElementKind.PACKAGE)) {
            messager.printMessage(Diagnostic.Kind.ERROR,
                    "Could not determine the package name. Enclosing element was: " + enclosingElement.getKind());
            return null;
        }

        PackageElement packageElement = (PackageElement) enclosingElement;
        return packageElement.getQualifiedName().toString();
    }
}
