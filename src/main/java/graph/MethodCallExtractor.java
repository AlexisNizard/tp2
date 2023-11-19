package graph;

import spoon.Launcher;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.filter.TypeFilter;
import spoon.reflect.code.CtInvocation;

public class MethodCallExtractor {

    // Extrait les appels de methodes du code source situé dans projectPath
    public CustomGraph extractMethodCalls(String projectPath) {
        CustomGraph graph = new CustomGraph();
        try {
            Launcher launcher = new Launcher();
            launcher.addInputResource(projectPath);  // Ajout du chemin du projet à Spoon pour l'analyse
            launcher.buildModel();  // Construction du modèle Spoon du code source
            CtModel model = launcher.getModel();  // Récupération du modèle Spoon pour l'analyse

            // Parcours de toutes les classes dans le modèle
            for (CtClass<?> ctClass : model.getElements(new TypeFilter<>(CtClass.class))) {
                String className = ctClass.getQualifiedName();  // Obtention du nom de la classe
                // Vérification pour s'assurer de ne pas inclure les classes de bibliothèques standard
                if (shouldIncludeClass(className)) {
                    // Parcours des invocations de méthodes dans chaque classe
                    for (CtInvocation<?> invocation : ctClass.getElements(new TypeFilter<>(CtInvocation.class))) {
                        CtTypeReference<?> declaringTypeRef = invocation.getExecutable().getDeclaringType();
                        // Vérifie si l'invocation a une référence de type déclarant (pour éviter les références nulles)
                        if (declaringTypeRef != null) {
                            String targetClassName = declaringTypeRef.getQualifiedName();
                            // Ajout de l'appel de méthode au graphe si la classe appelée doit être incluse
                            if (shouldIncludeClass(targetClassName)) {
                                graph.addMethodCall(className, targetClassName);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'extraction des appels de méthode: " + e.getMessage());
        }

        return graph;
    }

    // Filtre les classes appartenant aux bibliothèques standard Java
    private boolean shouldIncludeClass(String className) {
        return !className.startsWith("java.");
    }
}
