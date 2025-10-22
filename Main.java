package Pharmacy;

import Pharmacy.gui.PharmacyGUI;
import Pharmacy.logic.PharmacyManager;
import Pharmacy.util.FileHandler;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Load existing data or create new manager
        PharmacyManager manager = FileHandler.loadData();
        
        // Create and show GUI
        SwingUtilities.invokeLater(() -> {
            PharmacyGUI gui = new PharmacyGUI(manager);
            gui.setVisible(true);
        });
        
        // Add shutdown hook to save data
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            FileHandler.saveData(manager);
            System.out.println("Data saved on shutdown.");
        }));
    }
}