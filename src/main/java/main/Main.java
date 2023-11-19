package main;

import java.util.Scanner;
import java.util.List;

import graph.CustomGraph;
import graph.MethodCallExtractor;
import clustering.HierarchicalClustering;
import clustering.HierarchicalClustering.Cluster;
import coupling.CouplingMetric;

public class Main {
    private static final String PROJECT_PATH = "/home/alexis1/eclipse-workspace/WORKSPACE_REST/versionDistribueHotelsREST"; // Path du projet
    public static double threshold = 0.01; // Seuil de couplage
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Cluster> clusters = null;

        while (true) {
            System.out.println("\n================== MENU ==================");
            System.out.println("1 - Visualiser le graphe de couplage entre les classes de l'application");
            System.out.println("2 - Effectuer le clustering hiérarchique");
            System.out.println("3 - Quitter");
            System.out.print("Choisissez une option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    visualizeMethodCalls();
                    break;
                case 2:
                    clusters = performHierarchicalClustering();
                    break;
                case 3:
                    System.out.println("Fin du programme.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Option invalide. Veuillez réessayer.");
            }
        }
    }

    private static void visualizeMethodCalls() {
        MethodCallExtractor extractor = new MethodCallExtractor();
        CustomGraph graph = extractor.extractMethodCalls(PROJECT_PATH);
        graph.printGraph();
        
        CouplingMetric metric = new CouplingMetric();
        metric.printCouplingMetrics(graph); 
        Scanner scanner = new Scanner(System.in);
        System.out.print("\n\nVoulez vous un affichage du graph ? [y/n] ");

        char choice = scanner.next().charAt(0);
        if (choice == 'y' || choice == 'Y') {
            graph.visualize();
        }
    
    }

    private static List<Cluster> performHierarchicalClustering() {
        MethodCallExtractor extractor = new MethodCallExtractor();
        CustomGraph graph = extractor.extractMethodCalls(PROJECT_PATH);
        
        int maxClusters = graph.getClasses().size() / 2; // Nombre maximum de clusters (M/2)

        CouplingMetric metric = new CouplingMetric(); // Initialisation de la métrique de couplage

        HierarchicalClustering clustering = new HierarchicalClustering(threshold, maxClusters);
        List<Cluster> clusters = clustering.performClustering(graph, metric); // Utilisation de la métrique de couplage

        clusters.forEach(cluster -> System.out.println("Cluster: " + cluster.getClasses()));
        return clusters;
    }

}
