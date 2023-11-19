package visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import java.util.HashMap;
import java.util.Map;

public class TypeDeclarationVisitor extends ASTVisitor {
    private Map<String, Integer> methodsPerClass = new HashMap<>();
    private Map<String, Integer> attributesPerClass = new HashMap<>();
    private int classCount = 0;
    
    public boolean visit(TypeDeclaration node) {
    	 if (!node.isInterface()) {  
             classCount++;
         }
    	 
        String className = node.getName().getFullyQualifiedName();

        int methodCount = node.getMethods().length;
        methodsPerClass.put(className, methodCount);

        int attributeCount = node.getFields().length;
        attributesPerClass.put(className, attributeCount);

        return super.visit(node);
    }

    public Map<String, Integer> getMethodsPerClass() {
        return methodsPerClass;
    }

    public Map<String, Integer> getAttributesPerClass() {
        return attributesPerClass;
    }
    
    public int getClassCount() {
        return classCount;
    }
}



