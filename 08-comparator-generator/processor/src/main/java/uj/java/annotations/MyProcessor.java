package uj.java.annotations;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@SupportedAnnotationTypes({"uj.java.annotations.MyComparable"})
@SupportedSourceVersion(SourceVersion.RELEASE_16)
public class MyProcessor extends AbstractProcessor {

    private final String ARG_1_NAME = "d1";
    private final String ARG_2_NAME = "d2";

    @Override
    public boolean process(Set<? extends TypeElement> annotations,
                           RoundEnvironment roundEnv) {
        for (var annotation : annotations) {
            var elements = roundEnv.getElementsAnnotatedWith(annotation);
            for (var element : elements) {
                try {
                    process(element);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    private void process(Element element) throws IOException {
        var simpleClassName = element.getSimpleName().toString();
        var className = ((TypeElement) element).getQualifiedName().toString();
        var packageName = packageName(className, simpleClassName);

        JavaFileObject builderFile = processingEnv.getFiler().createSourceFile(simpleClassName + "Comparator");
        try (PrintWriter out = new PrintWriter(builderFile.openWriter())) {
            if (packageName != null) {
                writePackage(out, packageName);
                writeClassComparator(out, "public", simpleClassName, createMethod(
                        "public", "int", "compare", ifExpressions((TypeElement) element),
                        className + " " + ARG_1_NAME, className + " " + ARG_2_NAME));
            }
        }
    }

    private void writePackage(PrintWriter out, String packageName) {
        out.print("package " + packageName);
        out.println(";");
        out.println();
    }

    private void writeClassComparator(PrintWriter out, String modifier, String className, String... methods) {
        out.print(modifier + " class " + className + "Comparator");
        out.println(" {");
        out.println();
        for (var method : methods) {
            out.println(method);
        }
        out.println("}");
    }

    private String createMethod(String modifier, String type, String methodName, String inside, String... args) {
        var result = "     " + modifier + " " + type + " " + methodName + "(";

        var arguments = new StringBuilder();
        for (var arg : args) {
            arguments.append(arg).append(", ");
        }
        arguments.delete(arguments.length() - 2, arguments.length() - 1);

        result += arguments + ") {\n" + inside + "\n     }";
        return result;
    }

    private String ifExpressions(TypeElement element) {
        var correctElements = new ArrayList<Triple>();
        for (var el : element.getEnclosedElements()) {
            fillCorrectElements(correctElements, el);
        }
        correctElements.sort(Comparator.comparingInt(Triple::value));

        return insideCompareMethod(correctElements);
    }

    private void fillCorrectElements(List<Triple> correctElements, Element el) {
        if (el.asType().getKind().isPrimitive() && !el.getModifiers().contains(Modifier.PRIVATE)) {
            correctElements.add(new Triple(
                    processingEnv.getTypeUtils().boxedClass(processingEnv.getTypeUtils().getPrimitiveType(el.asType().getKind())).getSimpleName().toString(),
                    el.getSimpleName().toString(),
                    addElementValueToTriple(el))
            );
        }
    }

    private int addElementValueToTriple(Element el) {
        if (el.getAnnotation(ComparePriority.class) != null) {
            return el.getAnnotation(ComparePriority.class).value();
        }
        return Integer.MAX_VALUE;
    }

    private String insideCompareMethod(List<Triple> correctElements) {
        var result = new StringBuilder("          int result = 0; \n");
        for (var el : correctElements) {
            result.append(ifExpressionsInCompareMethod(pascalCase(el.type()), el.nameField(), ARG_1_NAME, ARG_2_NAME));
        }
        result.append("          return result;");

        return result.toString();
    }

    private String ifExpressionsInCompareMethod(String type, String typeField, String arg1, String arg2) {
        return "          if ((result = " + type + ".compare(" + arg1 + "." + typeField + ", " + arg2 + "." + typeField + ")) != 0) { \n" + "              return result; \n " + "         } \n";
    }

    private String pascalCase(String big) {
        var result = big.toLowerCase(Locale.ROOT);
        result = result.substring(0, 1).toUpperCase() + result.substring(1);
        return result;
    }

    private String packageName(String className, String simpleClassName) {
        return className.split("\\." + simpleClassName)[0];
    }
}
