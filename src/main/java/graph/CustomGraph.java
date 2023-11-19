package graph;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import coupling.CouplingMetric;

public class CustomGraph {
    private Map<String, Set<String>> graph = new HashMap<>();

    public void addClass(String className) {
        graph.putIfAbsent(className, new HashSet<>());
    }

    public void addMethodCall(String caller, String callee) {
        addClass(caller); 
        addClass(callee);
        graph.get(caller).add(callee);
    }

    public Set<String> getCalledClasses(String className) {
        return graph.getOrDefault(className, new HashSet<>());
    }

    public Set<String> getClasses() {
        return graph.keySet();
    }

    // Print le graphe
    public void printGraph() {
        for (String classA : graph.keySet()) {
            System.out.println(classA + " appelle : " + graph.get(classA));
        }
    }
    
    public void visualize() {
        Graph graph = new SingleGraph("Graphe de couplage");
        graph.addAttribute("ui.stylesheet", styleSheet());

        // Ajout des nœuds (classes)
        for (String className : this.graph.keySet()) {
            Node node = graph.addNode(className);
            node.addAttribute("ui.label", className);
        }

        CouplingMetric metric = new CouplingMetric();

        for (String caller : this.graph.keySet()) {
            for (String callee : this.graph.get(caller)) {
                String edgeId = caller + "_" + callee;
                Edge edge = graph.addEdge(edgeId, caller, callee, true);

                // Calculez du poids basé sur la métrique de couplage
                double weight = metric.calculateCoupling(this, caller, callee);
                
                edge.addAttribute("ui.label", String.format("%.2f", weight));
            }
        }
        graph.display();
    }

    private String styleSheet() {
        return "node {"
             + "   fill-color: black;"
             + "   size: 20px, 20px;"
             + "   text-alignment: above;"
             + "   text-size: 16;"
             + "}"
             + "edge {"
             + "   fill-color: grey;"
             + "   size: 3px;"
             + "   text-size: 16;"
             + "}";
    }
}
