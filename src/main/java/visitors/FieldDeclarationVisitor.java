package visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;

public class FieldDeclarationVisitor extends ASTVisitor {
    private int totalAttributes = 0;

    @Override
    public boolean visit(FieldDeclaration node) {
        totalAttributes += node.fragments().size();
        return super.visit(node);
    }

    public int getTotalAttributes() {
        return totalAttributes;
    }
}
