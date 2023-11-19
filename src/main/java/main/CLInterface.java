package main;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import analyse.Analyzer;

public class CLInterface {
	
	private Map<String, Integer> methodsPerClass = new HashMap<>();


    private List<Path> getAllJavaFiles(Path dirPath) throws IOException {
        List<Path> javaFiles = new ArrayList<>();
        Files.walk(dirPath)
             .filter(Files::isRegularFile)
             .filter(p -> p.toString().endsWith(".java"))
             .forEach(javaFiles::add);
        return javaFiles;
    }

    
    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bienvenue, voulez-vous lancer l'interface graphique ? [Y/N]");
        String response = scanner.nextLine().toUpperCase();

        if (response.equals("Y")) {
            new GUInterface().launch();
        } else {
            System.out.println("Entrez le chemin d'accès complet:");
            String path = scanner.nextLine().trim();

            boolean continueLoop = true;
            while (continueLoop) { // Ici commence la boucle
                System.out.println("Choisissez l'exercice:");
                System.out.println("1) Exercice 1");
                System.out.println("2) Exercice 2");
                System.out.println("3) Générer un graphe de couplage pondéré");
                System.out.println("4) Quitter"); // Option pour quitter la boucle
                response = scanner.nextLine().toUpperCase();

                if (response.equals("1")) {
                    runAnalysis(path, true);
                } else if (response.equals("2")) {
                    printCallGraph(path);
                } else if (response.equals("3")) {
                    generateAndPrintWeightedCouplingGraph(path);
                } else if (response.equals("4")) {
                    continueLoop = false; // Termine la boucle
                } else {
                    System.out.println("Choix invalide. Veuillez réessayer.");
                }
            }
        }
    }


    
    public Map<String, Set<String>> getCallGraph(String path) {
        Map<String, Set<String>> combinedGraph = new HashMap<>();
        try {
            List<Path> javaFiles = getAllJavaFiles(Paths.get(path));

            for (Path javaFile : javaFiles) {
                String sourceCode = new String(Files.readAllBytes(javaFile));
                Analyzer analyzer = new Analyzer(sourceCode);
                combinedGraph.putAll(analyzer.getCallGraph());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return combinedGraph;
    }


    public void printCallGraph(String path) {
        try {
            List<Path> javaFiles = getAllJavaFiles(Paths.get(path));

            for (Path javaFile : javaFiles) {
                String sourceCode = new String(Files.readAllBytes(javaFile));
                Analyzer analyzer = new Analyzer(sourceCode);
                Map<String, Set<String>> callGraph = analyzer.getCallGraph();
                Map<String, Set<String>> incomingCalls = analyzer.getIncomingCalls();

                callGraph.forEach((caller, callees) -> {
                    System.out.println(caller);
                    System.out.println("  Appels entrants:");
                    if (incomingCalls.get(caller) != null) {
                        incomingCalls.get(caller).forEach(callerMethod -> {
                            System.out.println("    <- " + callerMethod);
                        });
                    } else {
                        System.out.println("    (Aucun)");
                    }
                    System.out.println("  Appels sortants:");
                    callees.forEach(callee -> {
                        System.out.println("    -> " + callee);
                    });
                });
            }

        } catch (IOException e) {
            System.out.println("Error processing the directory: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    public Map<String, String> runAnalysis(String path, boolean displayResults) {
        Map<String, String> results = new LinkedHashMap<>();  // Pour maintenir l'ordre d'insertion

    

        Map<String, Integer> attributesPerClass = new HashMap<>();
        Map<String, Integer> allMethodsLineCounts = new HashMap<>();
      


        int totalClasses = 0;
        int totalMethods = 0;
        int totalLinesOfCode = 0;
        int totalAttributes = 0;
        int totalPackages = 0;
        int globalMaxParameters = 0;


        try {
            List<Path> javaFiles = getAllJavaFiles(Paths.get(path));

			
			Set<String> allPackages = new HashSet<>();
			
			for (Path javaFile : javaFiles) {
			    String sourceCode = new String(Files.readAllBytes(javaFile));
			    Analyzer analyzer = new Analyzer(sourceCode);
			    totalClasses += analyzer.getNumberOfClasses();
			    totalMethods += analyzer.getTotalMethods();
			    totalLinesOfCode += analyzer.getTotalLinesOfCode();
			    totalAttributes += analyzer.getTotalAttributes();
			    allPackages.addAll(analyzer.getPackages());
			    

	            methodsPerClass.putAll(analyzer.getMethodsPerClass());
	            attributesPerClass.putAll(analyzer.getAttributesPerClass());
	            
	            allMethodsLineCounts.putAll(analyzer.getMethodLinesCount());
	            globalMaxParameters = Math.max(globalMaxParameters, analyzer.getMaxParameters());
			}
			
			totalPackages = allPackages.size();
			
			 // Les 10% des classes qui possèdent le plus grand nombre de méthodes.
	        List<String> topMethodClasses = getTopPercentClasses(methodsPerClass, 10);

	        //  Les 10% des classes qui possèdent le plus grand nombre d’attributs.
	        List<String> topAttributeClasses = getTopPercentClasses(attributesPerClass, 10);

	        //  Les classes qui font partie en même temps des deux catégories précédentes.
	        Set<String> commonClasses = new HashSet<>(topMethodClasses);
	        commonClasses.retainAll(topAttributeClasses);
	        
	        List<String> top10PercentMethods = getTopPercentMethods(allMethodsLineCounts, 10);

	        
	        if (displayResults) {
		        printResults(
		            totalClasses, totalMethods, totalLinesOfCode, 
		            totalAttributes, totalPackages, topMethodClasses, 
		            topAttributeClasses, commonClasses, top10PercentMethods, 
		            globalMaxParameters, methodsPerClass
		        );
		    }
		
			
			
	        storeResults(results,
		                totalClasses, totalMethods, totalLinesOfCode, 
		                totalAttributes, totalPackages, topMethodClasses, 
		                topAttributeClasses, commonClasses, top10PercentMethods, 
		                globalMaxParameters
		            );


        } catch (IOException e) {
            System.out.println("Error processing the directory: " + e.getMessage());
            e.printStackTrace();
        }
        
 

        return results;
    }
    

private void printResults(
    int totalClasses, int totalMethods, int totalLinesOfCode,
    int totalAttributes, int totalPackages, List<String> topMethodClasses,
    List<String> topAttributeClasses, Set<String> commonClasses,
    List<String> top10PercentMethods, int globalMaxParameters, 
    Map<String, Integer> methodsPerClass
) {
	System.out.println("1. Nombre de classes : " + totalClasses);
	System.out.println("2. Nombre de lignes de code : " + totalLinesOfCode);
	System.out.println("3. Nombre de méthodes : " + totalMethods);
	//System.out.println("Nombre d'attributs : " + totalAttributes);
	System.out.println("4. Nombre de packages : " + totalPackages);
	System.out.println("5. Moyenne de méthodes par classe : " + String.format("%.2f", (double) totalMethods / totalClasses));
	System.out.println("6. Moyenne de lignes de code par méthode : " + String.format("%.2f", (double) totalLinesOfCode / totalMethods));
	System.out.println("7. Moyenne d'attributs par classe : " + String.format("%.2f", (double) totalAttributes / totalClasses));
	System.out.println("8. 10% des classes avec le plus de méthodes : " + topMethodClasses);
	System.out.println("9. 10% des classes avec le plus d'attributs : " + topAttributeClasses);
	System.out.println("10. Classes dans les deux catégories : " + commonClasses);

	Scanner scanner = new Scanner(System.in);
	System.out.print("Veuillez maintenant entrez le seuil pour les méthodes (X) : ");
	int seuil = scanner.nextInt();
	List<String> classesAuDelaDuSeuil = methodsPerClass.entrySet().stream()
	    .filter(entry -> entry.getValue() > seuil)
	    .map(Map.Entry::getKey)
	    .collect(Collectors.toList());
	System.out.println("11. Classes avec plus de " + seuil + " méthodes : " + classesAuDelaDuSeuil);

	System.out.println("12. 10% des méthodes avec le plus de lignes de code : " + top10PercentMethods);
	System.out.println("13. Nombre maximum de paramètres dans une méthode : " + globalMaxParameters);

}

private void storeResults(Map<String, String> results,
    int totalClasses, int totalMethods, int totalLinesOfCode,
    int totalAttributes, int totalPackages, List<String> topMethodClasses,
    List<String> topAttributeClasses, Set<String> commonClasses,
    List<String> top10PercentMethods, int globalMaxParameters
) {
	results.put("1. Nombre de classes", Integer.toString(totalClasses));
	results.put("2. Nombre de lignes de code", Integer.toString(totalLinesOfCode));
	results.put("3. Nombre de méthodes", Integer.toString(totalMethods));
	//results.put("Nombre d'attributs", Integer.toString(totalAttributes));
	results.put("4. Nombre de packages", Integer.toString(totalPackages));
	results.put("5. Moyenne de méthodes par classe", String.format("%.2f", (double) totalMethods / totalClasses));
	results.put("6. Moyenne de lignes de code par méthode", String.format("%.2f", (double) totalLinesOfCode / totalMethods));
	results.put("7. Moyenne d'attributs par classe", String.format("%.2f", (double) totalAttributes / totalClasses));
	results.put("8. 10% des classes avec le plus de méthodes", topMethodClasses.toString());
	results.put("9. 10% des classes avec le plus d'attributs", topAttributeClasses.toString());
	results.put("10. Classes dans les deux catégories", commonClasses.toString());
	results.put("12. 10% des méthodes avec le plus de lignes de code", top10PercentMethods.toString());
	results.put("13. Nombre maximum de paramètres dans n'importe quelle méthode", Integer.toString(globalMaxParameters));

}
    
    
    
    private List<String> getTopPercentClasses(Map<String, Integer> map, double percent) {
        int limit = (int) Math.ceil(map.size() * percent / 100);
        limit = Math.max(1, limit);

        return map.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .limit(limit)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }
    
    private List<String> getTopPercentMethods(Map<String, Integer> methodLinesCount, double percent) {
        int limit = (int) Math.ceil(methodLinesCount.size() * percent / 100);
        limit = Math.max(1, limit);

        return methodLinesCount.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // trier par ordre décroissant de lignes de code
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    
    public List<String> getClassesAboveThreshold(int threshold) {
        return methodsPerClass.entrySet().stream()
            .filter(entry -> entry.getValue() > threshold)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }



    public static void main(String[] args) {
        new CLInterface().start();
    }
    
    
    public double calculerCouplage(String nomClasseA, String nomClasseB, Map<String, Set<String>> grapheAppel) {
        int totalRelations = 0;
        int relationsEntreAetB = 0;

        // Pour toutes les méthodes dans le graphe d'appel
        for (Map.Entry<String, Set<String>> entry : grapheAppel.entrySet()) {
            String methodeActuelle = entry.getKey();
            Set<String> appels = entry.getValue();

            // Vérifier si la méthode actuelle appartient à la classe A ou B
            boolean appartientA = methodeActuelle.startsWith(nomClasseA + ".");
            boolean appartientB = methodeActuelle.startsWith(nomClasseB + ".");

            for (String methodeAppelee : appels) {
                // Si la méthode actuelle appartient à A et la méthode appelée à B, ou inversement, c'est un couplage direct.
                if ((appartientA && methodeAppelee.startsWith(nomClasseB + ".")) || (appartientB && methodeAppelee.startsWith(nomClasseA + "."))) {
                    relationsEntreAetB++;
                }

                // Comptabiliser la relation pour le total général
                totalRelations++;
            }
        }

        // Calculer la métrique de couplage en fonction des nombres obtenus
        double couplage = (double) relationsEntreAetB / totalRelations;

        return couplage;
    }
    
    
    public void generateAndPrintWeightedCouplingGraph(String path) {
        try {
            // Récupérer le graphe d'appel pour toutes les classes
            Map<String, Set<String>> callGraph = getCallGraph(path);

            // Un set pour stocker les noms uniques des classes
            Set<String> classNames = new HashSet<>();

            // Extraire les noms des classes à partir des clés du graphe d'appel
            for (String method : callGraph.keySet()) {
                String className = method.substring(0, method.lastIndexOf('.'));
                classNames.add(className);
            }

            // Liste pour stocker les résultats du couplage
            List<Map.Entry<String, Double>> couplingResults = new ArrayList<>();

            // Calculer le couplage pour chaque paire de classes
            for (String classA : classNames) {
                for (String classB : classNames) {
                    if (!classA.equals(classB)) {
                        double coupling = calculerCouplage(classA, classB, callGraph);
                        String pair = classA + " - " + classB;
                        couplingResults.add(new AbstractMap.SimpleEntry<>(pair, coupling));
                    }
                }
            }

            // Imprimer le graphe de couplage
            System.out.println("Graphe de couplage pondéré:");
            for (Map.Entry<String, Double> entry : couplingResults) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
        } catch (Exception e) {
            System.out.println("Une erreur est survenue lors de la génération du graphe de couplage: " + e.getMessage());
            e.printStackTrace();
        }
    }

    
    
}
