package analyse;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import graph.CustomGraph;
import parser.Parser;
import visitors.FieldDeclarationVisitor;
import visitors.LinesByMethodVisitor;
import visitors.PackageDeclarationVisitor;
import visitors.TypeDeclarationVisitor;

public class Analyzer {
	   private String sourceCode;
	    private CompilationUnit cu;
	    
	    //Champs alimentés par les visiteurs
	    private int totalMethods;
	    private int totalLinesOfCode;
	    private int maxParameters;
	    private Set<String> packages;
	    private Map<String, Integer> methodsPerClass;
	    private Map<String, Integer> attributesPerClass;
	    private Map<String, Integer> methodLinesCount;
	    
	    private CustomGraph customGraph = new CustomGraph();


	    private int totalAttributes = 0;

	    
	    private Map<String, Set<String>> incomingCalls = new HashMap<>();
	    public Map<String, Set<String>> getIncomingCalls() {
	        return incomingCalls;
	    }
	    
	    private Map<String, Set<String>> callGraph = new HashMap<>();
	    public Map<String, Set<String>> getCallGraph() {
	        return callGraph;
	    }

	    public Analyzer(String sourceCode) {
	        this.sourceCode = sourceCode;
	        this.parse();
	        this.analyze();
	    }

	    private void parse() {
	        ASTParser parser = ASTParser.newParser(AST.JLS8);
	        parser.setSource(sourceCode.toCharArray());
	        this.cu = (CompilationUnit) parser.createAST(null);
	    }

	    private void analyze() {
	        // Utilisation de LinesByMethodVisitor pour obtenir les informations liées aux méthodes
	        LinesByMethodVisitor linesVisitor = new LinesByMethodVisitor(cu);
	        cu.accept(linesVisitor);
	        methodLinesCount = linesVisitor.getMethodLinesCount();
	        totalLinesOfCode = linesVisitor.getTotalLinesOfCode();
	        totalMethods = linesVisitor.getTotalMethods();
	        maxParameters = linesVisitor.getMaxParameters();

	        // Utilisation de PackageDeclarationVisitor pour obtenir les informations liées aux packages
	        PackageDeclarationVisitor packageVisitor = new PackageDeclarationVisitor();
	        cu.accept(packageVisitor);
	        packages = packageVisitor.getPackages();

	        // Utilisation de TypeDeclarationVisitor pour obtenir les informations liées aux classes
	        TypeDeclarationVisitor typeVisitor = new TypeDeclarationVisitor();
	        cu.accept(typeVisitor);
	        methodsPerClass = typeVisitor.getMethodsPerClass();
	        attributesPerClass = typeVisitor.getAttributesPerClass();
	        
	     // Utilisation de FieldDeclarationVisitor pour obtenir les informations liées aux attributs
	        FieldDeclarationVisitor fieldVisitor = new FieldDeclarationVisitor();
	        cu.accept(fieldVisitor);
	        totalAttributes = fieldVisitor.getTotalAttributes();


	        
	        cu.accept(new ASTVisitor() {
	            @Override
	            public boolean visit(MethodInvocation node) {
	                String callerMethod = getCurrentMethodName(node); 
	                String calledMethod = node.getName().getFullyQualifiedName();

	                if (!callerMethod.equals("")) {
	                    callGraph
	                        .computeIfAbsent(callerMethod, k -> new HashSet<>())
	                        .add(calledMethod);
	                    incomingCalls
	                        .computeIfAbsent(calledMethod, k -> new HashSet<>())
	                        .add(callerMethod);
	                }
	                customGraph.addMethodCall(callerMethod, calledMethod);
	                return super.visit(node);
	            }

	            // Méthode pour obtenir le nom de la méthode courante à partir d'un nœud
	            private String getCurrentMethodName(ASTNode node) {
	                while (node != null && !(node instanceof MethodDeclaration)) {
	                    node = node.getParent();
	                }

	                if (node == null) {
	                    return "";
	                }

	                MethodDeclaration method = (MethodDeclaration) node;
	                return method.getName().getFullyQualifiedName();
	            }
	        });
	    }



    public int getNumberOfClasses() {
        CompilationUnit cu = Parser.parse(sourceCode);
        TypeDeclarationVisitor visitor = new TypeDeclarationVisitor();
        cu.accept(visitor);
        return visitor.getClassCount();
    }
    
    public int getTotalMethods() {
        return totalMethods;
    }

    public int getTotalLinesOfCode() {
        return totalLinesOfCode;
    }

    public int getTotalAttributes() {
        return totalAttributes;
    }

    public int getNumberOfPackages() {
        return packages.size();
    }
    
    public Set<String> getPackages() {
        return packages;
    }
    
    public Map<String, Integer> getMethodsPerClass() {
        return methodsPerClass;
    }

    public Map<String, Integer> getAttributesPerClass() {
        return attributesPerClass;
    }
    
    public Map<String, Integer> getMethodLinesCount() {
        return methodLinesCount;
    }

    public int getMaxParameters() {
        return maxParameters;
    }
    
    public CustomGraph getCustomGraph() {
        return customGraph;
    }


}