package Pharmacy.model;

import java.io.Serializable;
import java.util.*;

public class DataModels {
    
    public static class Drug implements Serializable, Comparable<Drug> {
        private String id;
        private String name;
        private int quantity;
        private double price;
        private Date expiryDate;
        private int minStockLevel;
        
        public Drug(String id, String name, int quantity, double price, Date expiryDate, int minStockLevel) {
            this.id = id;
            this.name = name;
            this.quantity = quantity;
            this.price = price;
            this.expiryDate = expiryDate;
            this.minStockLevel = minStockLevel;
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public String getName() { return name; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public double getPrice() { return price; }
        public Date getExpiryDate() { return expiryDate; }
        public int getMinStockLevel() { return minStockLevel; }
        
        @Override
        public int compareTo(Drug other) {
            return this.expiryDate.compareTo(other.expiryDate);
        }
        
        @Override
        public String toString() {
            return String.format("Drug{id='%s', name='%s', quantity=%d, price=%.2f}", 
                id, name, quantity, price);
        }
    }
    
    public static class Patient implements Serializable {
        private String id;
        private String name;
        private double walletBalance;
        private List<String> prescriptionHistory;
        
        public Patient(String id, String name, double walletBalance) {
            this.id = id;
            this.name = name;
            this.walletBalance = walletBalance;
            this.prescriptionHistory = new ArrayList<>();
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public String getName() { return name; }
        public double getWalletBalance() { return walletBalance; }
        public void setWalletBalance(double walletBalance) { this.walletBalance = walletBalance; }
        public List<String> getPrescriptionHistory() { return prescriptionHistory; }
        
        public void addPrescription(String prescriptionId) {
            prescriptionHistory.add(prescriptionId);
        }
        
        @Override
        public String toString() {
            return String.format("Patient{id='%s', name='%s', balance=%.2f}", 
                id, name, walletBalance);
        }
    }
    
    public static class Prescription implements Serializable {
        private String id;
        private String patientId;
        private Map<String, Integer> items; // drugId -> quantity
        private Date date;
        private boolean isValid;
        
        public Prescription(String id, String patientId) {
            this.id = id;
            this.patientId = patientId;
            this.items = new HashMap<>();
            this.date = new Date();
            this.isValid = true;
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public String getPatientId() { return patientId; }
        public Map<String, Integer> getItems() { return items; }
        public Date getDate() { return date; }
        public boolean isValid() { return isValid; }
        public void setValid(boolean valid) { isValid = valid; }
        
        public void addItem(String drugId, int quantity) {
            items.put(drugId, quantity);
        }
        
        public double calculateTotal(Map<String, Drug> drugDictionary) {
            double total = 0;
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                Drug drug = drugDictionary.get(entry.getKey());
                if (drug != null) {
                    total += drug.getPrice() * entry.getValue();
                }
            }
            return total;
        }
        
        @Override
        public String toString() {
            return String.format("Prescription{id='%s', patientId='%s', items=%d}", 
                id, patientId, items.size());
        }
    }
    
    public static class Transaction implements Serializable {
        private String id;
        private String prescriptionId;
        private boolean successful;
        private Map<String, Integer> stockChanges; // drugId -> quantity change
        private double walletChange;
        private String errorMessage;
        
        public Transaction(String id, String prescriptionId) {
            this.id = id;
            this.prescriptionId = prescriptionId;
            this.stockChanges = new HashMap<>();
            this.successful = false;
        }
        
        // Getters and Setters
        public String getId() { return id; }
        public String getPrescriptionId() { return prescriptionId; }
        public boolean isSuccessful() { return successful; }
        public void setSuccessful(boolean successful) { this.successful = successful; }
        public Map<String, Integer> getStockChanges() { return stockChanges; }
        public double getWalletChange() { return walletChange; }
        public void setWalletChange(double walletChange) { this.walletChange = walletChange; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
        
        public void addStockChange(String drugId, int change) {
            stockChanges.put(drugId, change);
        }
        
        @Override
        public String toString() {
            return String.format("Transaction{id='%s', prescriptionId='%s', success=%s}", 
                id, prescriptionId, successful);
        }
    }
    
    public static class InventoryAlert implements Serializable, Comparable<InventoryAlert> {
        private String drugId;
        private String drugName;
        private int currentStock;
        private int minStockLevel;
        
        public InventoryAlert(String drugId, String drugName, int currentStock, int minStockLevel) {
            this.drugId = drugId;
            this.drugName = drugName;
            this.currentStock = currentStock;
            this.minStockLevel = minStockLevel;
        }
        
        // Getters
        public String getDrugId() { return drugId; }
        public String getDrugName() { return drugName; }
        public int getCurrentStock() { return currentStock; }
        public int getMinStockLevel() { return minStockLevel; }
        
        @Override
        public int compareTo(InventoryAlert other) {
            return Integer.compare(this.currentStock, other.currentStock);
        }
        
        @Override
        public String toString() {
            return String.format("Alert{%s: %d/%d}", drugName, currentStock, minStockLevel);
        }
    }
}