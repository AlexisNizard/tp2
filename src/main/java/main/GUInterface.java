package main;

import java.util.List;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

public class GUInterface {
	
	Color bgColor = new Color(245, 245, 245); 
	Color btnColor = new Color(70, 130, 180);

	Font regularFont = new Font("SansSerif", Font.PLAIN, 16);
	Font boldFont = new Font("SansSerif", Font.BOLD, 16);

	
    private JFrame frame;
    private JButton btnSelectPath;
    private JButton btnExercice1;
    private JButton btnExercice2;
    private String selectedPath;
    
    
    private JButton btnSetThreshold;
    private int threshold = 0; 
    private JTextArea resultsArea;
    
    public void launch() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private void createAndShowGUI() {
    	
    	
    	   frame = new JFrame("Analyzer");
    	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	    frame.setSize(400, 200);
    	    frame.setLayout(new FlowLayout());

    	    frame.getContentPane().setBackground(bgColor); 

    	    btnSelectPath = new JButton("Sélectionner le chemin du projet");
    	    styleButton(btnSelectPath); 

    	    btnExercice1 = new JButton("Exercice 1");
    	    styleButton(btnExercice1); 
    	    btnExercice2 = new JButton("Exercice 2");
    	    styleButton(btnExercice2);
    	
 

    	    btnSelectPath.addActionListener(new ActionListener() {
    	        @Override
    	        public void actionPerformed(ActionEvent e) {
    	            JFileChooser fileChooser = new JFileChooser();

    	            try {
    	                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    	                SwingUtilities.updateComponentTreeUI(fileChooser);
    	            } catch (Exception ex) {
    	                ex.printStackTrace();
    	            }

    	            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    	            fileChooser.setDialogTitle("Sélectionnez le chemin de votre projet"); 


    	            int returnValue = fileChooser.showOpenDialog(frame);
    	            if (returnValue == JFileChooser.APPROVE_OPTION) {
    	                selectedPath = fileChooser.getSelectedFile().getAbsolutePath();
    	                displayExerciseButtons();
    	            }
    	        }
    	    });


        frame.add(btnSelectPath);
        frame.setVisible(true);
        
        
        resultsArea = new JTextArea(20, 40);
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        CLInterface cli = new CLInterface();
        btnExercice1.addActionListener(new ActionListener() {
         
            @Override
            public void actionPerformed(ActionEvent e) {
            
                Map<String, String> results = cli.runAnalysis(selectedPath,false);
                StringBuilder displayResults = new StringBuilder();
                for (Map.Entry<String, String> entry : results.entrySet()) {
                    displayResults.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
                }

                frame.add(btnSetThreshold);

                resultsArea.setText(displayResults.toString());
                frame.add(scrollPane);
                frame.pack();
            }
        });
        
        btnSetThreshold = new JButton("Choisir le seuil X (Question 11)");
        styleButton(btnSetThreshold); 
        btnSetThreshold.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setThreshold(cli);
            }
        });
        
        
        
        btnExercice2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CLInterface cli = new CLInterface();
                Map<String, Set<String>> callGraph = cli.getCallGraph(selectedPath);
                
                displayCallGraph(callGraph);
            }
        });

      
        

    }
    
    private void displayCallGraph(Map<String, Set<String>> callGraph) {
        StringBuilder graphBuilder = new StringBuilder();
        graphBuilder.append("digraph G {\n");
        callGraph.forEach((caller, callees) -> {
            callees.forEach(callee -> {
                graphBuilder.append("    \"" + caller + "\" -> \"" + callee + "\";\n");
            });
        });
        graphBuilder.append("}");

        String dotSource = graphBuilder.toString();
        try {
        	File imgFile = guru.nidi.graphviz.engine.Graphviz.fromString(dotSource).render(guru.nidi.graphviz.engine.Format.PNG).toFile(File.createTempFile("graph_", ".png"));
            displayImage(imgFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void displayImage(File file) {
        JFrame graphFrame = new JFrame("Graphique d'appel");
        ImageIcon icon = new ImageIcon(file.getAbsolutePath());
        JLabel label = new JLabel(icon);
        JScrollPane scrollPane = new JScrollPane(label);
        graphFrame.add(scrollPane);
        graphFrame.pack();
        graphFrame.setVisible(true);
    }

    
    private void setThreshold(CLInterface cli) {
    	JTextField thresholdField = new JTextField(5);
        thresholdField.setFont(regularFont);
        JLabel label = new JLabel("Insérer le seuil X:");
        label.setFont(boldFont); 
        

        JPanel thresholdPanel = new JPanel();
        thresholdPanel.add(new JLabel("Insérer le seuil X:"));
        thresholdPanel.add(thresholdField);

        int result = JOptionPane.showConfirmDialog(null, thresholdPanel, 
               "Seuil X", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                threshold = Integer.parseInt(thresholdField.getText());
                displayThresholdResults(cli);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Veuillez insérer une valeur numérique valide pour le seuil X.");
            }
        }
    }

    private void displayThresholdResults(CLInterface cli) {
        List<String> classesAboveThreshold = cli.getClassesAboveThreshold(threshold);
        resultsArea.append("11. Classes avec plus de " + threshold + " méthodes: " + classesAboveThreshold.toString() + "\n");
        
    }
    

private void styleButton(JButton button) {
    button.setBackground(btnColor); 
    button.setForeground(Color.WHITE);
    button.setFont(regularFont); 
    button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
    button.setFocusPainted(false); 
    button.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
}


    private void displayExerciseButtons() {
        frame.remove(btnSelectPath);
        frame.add(btnExercice1);
        frame.add(btnExercice2);
        frame.revalidate();
        frame.repaint();
    }
    
    
}
