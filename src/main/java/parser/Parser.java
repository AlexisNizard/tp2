package parser;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Parser {
    public static CompilationUnit parse(String source) {
        ASTParser parser = ASTParser.newParser(AST.JLS8);  // Tu peux ajuster la version en fonction de ta JDK
        parser.setSource(source.toCharArray());
        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        return (CompilationUnit) parser.createAST(null);
    }
}