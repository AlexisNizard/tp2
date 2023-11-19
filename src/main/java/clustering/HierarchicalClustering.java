package clustering;

import java.util.List;
import java.util.Map;
import java.util.Set;

import coupling.CouplingMetric;

// Importez d'autres bibliothèques si nécessaire
import java.util.ArrayList;
import java.util.HashMap;


import graph.CustomGraph;
public class HierarchicalClustering {
	   
	private final double threshold; // Seuil de couplage pour la fusion des clusters
    private final int maxClusters; // Nombre maximal de clusters autorisé

    public HierarchicalClustering(double threshold, int maxClusters) {
        this.threshold = threshold;
        this.maxClusters = maxClusters;
    }

    // Méthode principale pour effectuer le clustering hiérarchique
    public List<Cluster> performClustering(CustomGraph graph, CouplingMetric metric) {
        // Initialisation des clusters, chaque classe est un cluster initial
        Map<String, Cluster> clusters = new HashMap<>();
        for (String vertex : graph.getClasses()) {
            clusters.put(vertex, new Cluster(vertex));
        }

        // Continuer à fusionner les clusters jusqu'à atteindre le nombre maximal
        while (clusters.size() > maxClusters) {
            ClusterPair pairToMerge = findPairToMerge(clusters, graph, metric);
            if (pairToMerge != null) {
                mergeClusters(clusters, pairToMerge); // Fusionner les clusters les plus couplés
            } else {
                break; // Arrêt si aucun cluster n'est trouvé pour fusionner
            }
        }

        return new ArrayList<>(clusters.values()); 
    }

    // Trouver la paire de clusters à fusionner en fonction du couplage
    private ClusterPair findPairToMerge(Map<String, Cluster> clusters, CustomGraph graph, CouplingMetric metric) {
        ClusterPair pairToMerge = null;
        double maxCoupling = -1;

        // Parcourir tous les couples de clusters pour trouver la paire avec le plus grand couplage
        for (String classA : clusters.keySet()) {
            for (String classB : clusters.keySet()) {
                if (!classA.equals(classB)) {
                    double coupling = metric.calculateCoupling(graph, classA, classB);
                    if (coupling > maxCoupling && coupling > this.threshold) {
                        maxCoupling = coupling;
                        pairToMerge = new ClusterPair(classA, classB, clusters.get(classA), clusters.get(classB));
                    }
                }
            }
        }

        return pairToMerge;
    }

    // Fusionner deux clusters en un seul
    private void mergeClusters(Map<String, Cluster> clusters, ClusterPair pairToMerge) {
        Cluster cluster1 = pairToMerge.cluster1;
        Cluster cluster2 = pairToMerge.cluster2;

        cluster1.mergeWith(cluster2); // Fusionner le cluster2 dans le cluster1
        clusters.remove(pairToMerge.cluster2Name); // Retirer le cluster2 de la liste
        clusters.put(pairToMerge.cluster1Name, cluster1); // Mettre à jour la liste avec le cluster fusionné
    }

    private static class ClusterPair {
        String cluster1Name;
        String cluster2Name;
        Cluster cluster1;
        Cluster cluster2;

        ClusterPair(String cluster1Name, String cluster2Name, Cluster cluster1, Cluster cluster2) {
            this.cluster1Name = cluster1Name;
            this.cluster2Name = cluster2Name;
            this.cluster1 = cluster1;
            this.cluster2 = cluster2;
        }
    }

    public static class Cluster {
        private List<String> classes;

        public Cluster() {
            this.classes = new ArrayList<>();
        }
        public Cluster(String initialClass) {
            this.classes = new ArrayList<>();
            this.classes.add(initialClass);
        }
        
        public void mergeWith(Cluster other) {
            this.classes.addAll(other.classes);
        }
        public void addClass(String className) {
            classes.add(className);
        }
        
        public int size() {
            return classes.size();
        }
        public List<String> getClasses() {
            return this.classes;
        }

    }
}
