package Pharmacy.adt;
import Pharmacy.model.DataModels.*;
import java.util.*;
import java.util.stream.Collectors;

public class PharmacyADT {
    
    // Balanced BST for managing drugs by expiry date
    public static class BalancedExpiryTree {
        private TreeMap<Date, List<Drug>> expiryTree;
        
        public BalancedExpiryTree() {
            this.expiryTree = new TreeMap<>();
        }
        
        public void addDrug(Drug drug) {
            expiryTree.computeIfAbsent(drug.getExpiryDate(), k -> new ArrayList<>()).add(drug);
        }
        
        public void removeDrug(Drug drug) {
            List<Drug> drugs = expiryTree.get(drug.getExpiryDate());
            if (drugs != null) {
                drugs.remove(drug);
                if (drugs.isEmpty()) {
                    expiryTree.remove(drug.getExpiryDate());
                }
            }
        }
        
        public List<Drug> getDrugsExpiringBefore(Date date) {
            return expiryTree.headMap(date).values().stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        }
        
        public Drug getNextExpiringDrug() {
            if (expiryTree.isEmpty()) return null;
            List<Drug> drugs = expiryTree.firstEntry().getValue();
            return drugs.isEmpty() ? null : drugs.get(0);
        }
        
        public void updateDrugQuantity(String drugId, int newQuantity) {
            for (List<Drug> drugs : expiryTree.values()) {
                for (Drug drug : drugs) {
                    if (drug.getId().equals(drugId)) {
                        drug.setQuantity(newQuantity);
                        return;
                    }
                }
            }
        }
        
        public int size() {
            return expiryTree.values().stream().mapToInt(List::size).sum();
        }
    }
    
    // Min-Heap for low stock alerts
    public static class LowStockHeap {
        private PriorityQueue<InventoryAlert> minHeap;
        
        public LowStockHeap() {
            this.minHeap = new PriorityQueue<>();
        }
        
        public void addAlert(InventoryAlert alert) {
            minHeap.offer(alert);
        }
        
        public void removeAlert(String drugId) {
            minHeap.removeIf(alert -> alert.getDrugId().equals(drugId));
        }
        
        public InventoryAlert getMostCriticalAlert() {
            return minHeap.peek();
        }
        
        public List<InventoryAlert> getAllAlerts() {
            return new ArrayList<>(minHeap);
        }
        
        public void updateAlert(String drugId, String drugName, int currentStock, int minStockLevel) {
            removeAlert(drugId);
            if (currentStock <= minStockLevel * 1.5) { // Only alert if stock is low
                addAlert(new InventoryAlert(drugId, drugName, currentStock, minStockLevel));
            }
        }
        
        public int size() {
            return minHeap.size();
        }
    }
    
    // Stack for transaction rollback
    public static class TransactionStack {
        private Stack<Transaction> stack;
        
        public TransactionStack() {
            this.stack = new Stack<>();
        }
        
        public void pushTransaction(Transaction transaction) {
            stack.push(transaction);
        }
        
        public Transaction popTransaction() {
            return stack.isEmpty() ? null : stack.pop();
        }
        
        public Transaction peekTransaction() {
            return stack.isEmpty() ? null : stack.peek();
        }
        
        public boolean isEmpty() {
            return stack.isEmpty();
        }
        
        public int size() {
            return stack.size();
        }
        
        public List<Transaction> getAllTransactions() {
            return new ArrayList<>(stack);
        }
    }
    
    // Hash Table for quick drug lookup
    public static class DrugDictionary {
        private Map<String, Drug> dictionary;
        
        public DrugDictionary() {
            this.dictionary = new HashMap<>();
        }
        
        public void addDrug(Drug drug) {
            dictionary.put(drug.getId(), drug);
        }
        
        // FIXED: Added proper getDrug method
        public Drug getDrug(String drugId) {
            return dictionary.get(drugId);
        }
        
        public void removeDrug(String drugId) {
            dictionary.remove(drugId);
        }
        
        public boolean containsDrug(String drugId) {
            return dictionary.containsKey(drugId);
        }
        
        public Collection<Drug> getAllDrugs() {
            return dictionary.values();
        }
        
        public int size() {
            return dictionary.size();
        }
        
        public void updateDrugQuantity(String drugId, int newQuantity) {
            Drug drug = dictionary.get(drugId);
            if (drug != null) {
                drug.setQuantity(newQuantity);
            }
        }
        
        // NEW METHOD: Get the underlying map for compatibility
        public Map<String, Drug> getDictionary() {
            return dictionary;
        }
    }
}