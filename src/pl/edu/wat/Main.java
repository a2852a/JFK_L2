package pl.edu.wat;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import javax.tools.*;
import java.io.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {
        final String fileName = "src\\Class.java";
        final String alteredFileName = "src\\ClassAltered.java";
        CompilationUnit cu;
        try (FileInputStream in = new FileInputStream(fileName)) {
            cu = JavaParser.parse(in);
        }


        cu.getChildNodesByType(ClassOrInterfaceDeclaration.class)
                .stream()
                .forEach(Main::changeStrings);

//        new Rewriter().visit(cu, null);
        cu.getClassByName("Class").get().setName("ClassAltered");

        try (FileWriter output = new FileWriter(new File(alteredFileName), false)) {
            output.write(cu.toString());
        }

        File[] files = {new File(alteredFileName)};
        String[] options = {"-d", "out//production//Synthesis"};

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null)) {
            Iterable<? extends JavaFileObject> compilationUnits =
                    fileManager.getJavaFileObjectsFromFiles(Arrays.asList(files));
            compiler.getTask(
                    null,
                    fileManager,
                    diagnostics,
                    Arrays.asList(options),
                    null,
                    compilationUnits).call();

            diagnostics.getDiagnostics().forEach(d -> System.out.println(d.getMessage(null)));
        }
    }


    private static void changeStrings(ClassOrInterfaceDeclaration c) {
        List<FieldDeclaration> fieldDeclList = c.getFields();
        for (FieldDeclaration ff : fieldDeclList) {

            for(VariableDeclarator variable : ff.getVariables()){
                if (
                        !variable.getInitializer().isPresent() &&
                                (variable.getType().asString().equals("String")
                                        ||
                                        variable.getType().asString().equals("java.lang.String")
                                )
                ) {
                    variable.setInitializer("\"pusty ciag\"");
                }
            }


        }
    }


    private static class Rewriter extends VoidVisitorAdapter<Void> {
        @Override
        public void visit(ClassOrInterfaceDeclaration c, Void arg) {
            List<ClassOrInterfaceDeclaration> childClasses = c.getChildNodesByType(ClassOrInterfaceDeclaration.class);
            for (ClassOrInterfaceDeclaration childNode : childClasses) {
                //super.visit(childNode,arg);
                changeStrings(childNode);
            }
            changeStrings(c);

        }
    }
}
