package visitors;


import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class LinesByMethodVisitor extends ASTVisitor {
    private CompilationUnit cu;
    private int totalLinesOfCode = 0;
    private int totalMethods = 0;
    private int maxParameters = 0;
    private Map<String, Integer> methodLinesCount = new HashMap<>();

    public LinesByMethodVisitor(CompilationUnit cu) {
        this.cu = cu;
    }

    @Override
    public boolean visit(MethodDeclaration node) {
        totalMethods++;
        Block body = node.getBody();
        if (body != null) {
            int methodLength = cu.getLineNumber(body.getLength() + body.getStartPosition()) -
                cu.getLineNumber(body.getStartPosition()) + 1;
            totalLinesOfCode += methodLength;

            String methodName = node.getName().getFullyQualifiedName();
            methodLinesCount.put(methodName, methodLength);
        }

        int methodParametersCount = node.parameters().size();
        if (methodParametersCount > maxParameters) {
            maxParameters = methodParametersCount;
        }

        return super.visit(node);
    }

    public Map<String, Integer> getMethodLinesCount() {
        return methodLinesCount;
    }

    public int getTotalLinesOfCode() {
        return totalLinesOfCode;
    }

    public int getTotalMethods() {
        return totalMethods;
    }

    public int getMaxParameters() {
        return maxParameters;
    }
}

