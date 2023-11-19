package visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;

public class MethodDeclarationVisitor extends ASTVisitor {
    private int totalMethods = 0;
    private int maxParameters = 0;

    public boolean visit(MethodDeclaration node) {
        totalMethods++;

        int methodParametersCount = node.parameters().size();
        if (methodParametersCount > maxParameters) {
            maxParameters = methodParametersCount;
        }

        return super.visit(node);
    }

    public int getTotalMethods() {
        return totalMethods;
    }

    public int getMaxParameters() {
        return maxParameters;
    }
}

