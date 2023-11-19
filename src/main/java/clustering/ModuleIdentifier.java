package clustering;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import graph.CustomGraph;
import clustering.HierarchicalClustering.Cluster;
import coupling.CouplingMetric;

public class ModuleIdentifier {

    // Identifie les modules parmi les clusters en fonction du couplage
    public List<Cluster> identifyModules(List<Cluster> clusters, CouplingMetric metric, CustomGraph graph) {
        // Calcul du couplage moyen sur tous les clusters
        double averageCoupling = calculateAverageCouplingAcrossClusters(clusters, metric, graph);
        double threshold = averageCoupling * 0.5; // Définition d'un seuil basé sur la moyenne

        // Filtre les clusters dont le couplage est supérieur au seuil et les considérer comme des modules
        return clusters.stream()
            .filter(cluster -> calculateAverageCoupling(cluster, metric, graph) > threshold)
            .peek(cluster -> System.out.println("Cluster séléctionné comme module : " + cluster.getClasses()))
            .collect(Collectors.toList());
    }

    // Calcule la moyenne du couplage entre toutes les paires de classes dans tous les clusters
    private double calculateAverageCouplingAcrossClusters(List<Cluster> clusters, CouplingMetric metric, CustomGraph graph) {
        double totalCoupling = 0.0;
        int totalPairs = 0;

        // Parcours de chaque cluster pour calculer le couplage et le nombre de paires
        for (Cluster cluster : clusters) {
            double clusterCoupling = calculateAverageCoupling(cluster, metric, graph);
            int pairCount = cluster.getClasses().size() * (cluster.getClasses().size() - 1) / 2;
            totalCoupling += clusterCoupling * pairCount; // Somme des couplages
            totalPairs += pairCount; // Nombre total de paires
        }

        // Retourne la moyenne du couplage sur tous les clusters
        return totalPairs == 0 ? 0.0 : totalCoupling / totalPairs;
    }

    // Calcule la moyenne du couplage pour un cluster donné
    private double calculateAverageCoupling(Cluster cluster, CouplingMetric metric, CustomGraph graph) {
        if (cluster.size() < 2) {
            return 0.0; // Retourne 0 pour les clusters avec moins de deux classes
        }

        double totalCoupling = 0.0;
        int pairCount = 0;

        // Calcule le couplage pour chaque paire de classes dans le cluster
        List<String> classes = cluster.getClasses();
        for (int i = 0; i < classes.size(); i++) {
            for (int j = i + 1; j < classes.size(); j++) {
                double coupling = metric.calculateCoupling(graph, classes.get(i), classes.get(j));
                totalCoupling += coupling; // Somme des couplages
                pairCount++; // Nombre de paires
            }
        }

        // Retourne la moyenne du couplage pour le cluster
        return totalCoupling / pairCount;
    }
}