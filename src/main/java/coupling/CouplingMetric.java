package coupling;


import java.util.Set;
import graph.CustomGraph;

public class CouplingMetric {
    // Calcule le couplage entre deux classes en utilisant le graphe des appels
    public double calculateCoupling(CustomGraph graph, String classA, String classB) {
        int relationsAB = countRelationsBetweenClasses(graph, classA, classB);  // Nombre d'appels de A vers B et vice versa
        int totalRelations = countTotalRelations(graph);  // Nombre total d'appels dans le graphe

        // Retourne le ratio des relations entre A et B par rapport au total, ou 0 si aucune relation
        return totalRelations == 0 ? 0 : (double) relationsAB / totalRelations;
    }

    // Compte le nombre d'appels entre deux classes spécifiques
    private int countRelationsBetweenClasses(CustomGraph graph, String classA, String classB) {
        int count = 0;
        // Compte les appels de A vers B
        for (String caller : graph.getCalledClasses(classA)) {
            if (caller.equals(classB)) {
                count += graph.getCalledClasses(classA).size();
            }
        }
        // Compte les appels de B vers A
        for (String caller : graph.getCalledClasses(classB)) {
            if (caller.equals(classA)) {
                count += graph.getCalledClasses(classB).size();
            }
        }
        return count;
    }

    // Compte le nombre total d'appels dans le graphe
    private int countTotalRelations(CustomGraph graph) {
        int count = 0;
        for (String className : graph.getClasses()) {
            count += graph.getCalledClasses(className).size();  // Ajoute le nombre d'appels pour chaque classe
        }
        return count;
    }

    // Affiche les métriques de couplage pour toutes les paires de classes dans le graphe
    public void printCouplingMetrics(CustomGraph graph) {
        Set<String> classes = graph.getClasses();
        for (String classA : classes) {
            for (String classB : classes) {
                if (!classA.equals(classB)) {
                    double coupling = calculateCoupling(graph, classA, classB);
                    System.out.println("Couplage entre " + classA + " et " + classB + ": " + coupling);
                }
            }
        }
    }
}

