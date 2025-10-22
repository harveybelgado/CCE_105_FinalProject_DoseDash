package Pharmacy.util;
import Pharmacy.logic.PharmacyManager;
import java.io.*;
import java.util.logging.*;
import java.util.Date;  // ADD THIS IMPORT

public class FileHandler {
    private static final String DATA_FILE = "data/pharmacy_data.ser";
    private static final Logger logger = Logger.getLogger(FileHandler.class.getName());
    
    static {
        // Create data directory if it doesn't exist
        new File("data").mkdirs();
    }
    
    public static void saveData(PharmacyManager manager) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_FILE))) {
            oos.writeObject(manager);
            logger.info("Data saved successfully to: " + DATA_FILE);
        } catch (IOException e) {
            logger.severe("Error saving data: " + e.getMessage());
        }
    }
    
    public static PharmacyManager loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            logger.info("No saved data found. Creating new PharmacyManager.");
            return new PharmacyManager();
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(DATA_FILE))) {
            PharmacyManager manager = (PharmacyManager) ois.readObject();
            logger.info("Data loaded successfully from: " + DATA_FILE);
            return manager;
        } catch (IOException | ClassNotFoundException e) {
            logger.severe("Error loading data: " + e.getMessage());
            return new PharmacyManager();
        }
    }
    
    public static void exportTransactionReport(PharmacyManager manager, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("=== Pharmacy Transaction Report ===");
            writer.println("Generated on: " + new Date());
            writer.println("====================================");
            writer.println();
            
            writer.println("Drug Inventory:");
            writer.println("---------------");
            manager.getDrugDictionary().getAllDrugs().forEach(drug -> 
                writer.printf("- %s: %d in stock%n", drug.getName(), drug.getQuantity()));
            
            writer.println();
            writer.println("Low Stock Alerts:");
            writer.println("-----------------");
            manager.getLowStockHeap().getAllAlerts().forEach(alert -> 
                writer.printf("- %s: %d (min: %d)%n", 
                    alert.getDrugName(), alert.getCurrentStock(), alert.getMinStockLevel()));
            
            writer.println();
            writer.println("Recent Transactions:");
            writer.println("-------------------");
            manager.getTransactionStack().getAllTransactions().forEach(transaction -> 
                writer.printf("- %s: %s%n", transaction.getId(), 
                    transaction.isSuccessful() ? "SUCCESS" : "FAILED - " + transaction.getErrorMessage()));
            
            logger.info("Report exported to: " + filename);
        } catch (IOException e) {
            logger.severe("Error exporting report: " + e.getMessage());
        }
    }
}