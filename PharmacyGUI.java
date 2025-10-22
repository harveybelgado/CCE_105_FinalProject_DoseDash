package Pharmacy.gui;
import Pharmacy.logic.PharmacyManager;
import Pharmacy.model.DataModels.*;
import Pharmacy.util.FileHandler;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.*;

public class PharmacyGUI extends JFrame {
    private PharmacyManager manager;
    private JTabbedPane tabbedPane;
    
    // Components
    private JTable drugTable, patientTable, transactionTable;
    private JTextArea alertArea, expiryArea;
    private JComboBox<String> patientComboBox, drugComboBox;
    private JSpinner quantitySpinner;
    private JLabel totalLabel, patientBalanceLabel;
    private DefaultTableModel prescriptionModel;
    private Prescription currentPrescription;
    
    public PharmacyGUI(PharmacyManager manager) {
        this.manager = manager;
        initializeGUI();
    }
    
    private void initializeGUI() {
        setTitle("Pharmacy Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        
        // Use a different layout - BorderLayout with sidebar
        setLayout(new BorderLayout());
        
        // Create sidebar with quick actions
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);
        
        // Create main tabbed area
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Prescriptions", createPrescriptionPanel());
        tabbedPane.addTab("Inventory", createInventoryPanel());
        tabbedPane.addTab("Patients", createPatientPanel());
        tabbedPane.addTab("Transactions", createTransactionPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        JLabel statusLabel = new JLabel("Ready");
        statusBar.add(statusLabel);
        add(statusBar, BorderLayout.SOUTH);
        
        // Menu bar
        createSimpleMenuBar();
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        sidebar.setPreferredSize(new Dimension(200, 0));
        
        JLabel title = new JLabel("DoseDash");
        title.setFont(new Font("Century Gothic", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(title);
        sidebar.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Quick action buttons
        JButton newRxBtn = new JButton("New Prescription");
        JButton viewStockBtn = new JButton("Check Stock");
        JButton addPatientBtn = new JButton("Add Patient");
        JButton viewReportsBtn = new JButton("View Reports");
        
        // Style buttons
        for (JButton btn : new JButton[]{newRxBtn, viewStockBtn, addPatientBtn, viewReportsBtn}) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(180, 30));
            btn.setMargin(new Insets(5, 5, 5, 5));
            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        // Add action listeners
        newRxBtn.addActionListener(e -> tabbedPane.setSelectedIndex(0));
        viewStockBtn.addActionListener(e -> tabbedPane.setSelectedIndex(1));
        addPatientBtn.addActionListener(e -> tabbedPane.setSelectedIndex(2));
        viewReportsBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                FileHandler.exportTransactionReport(manager, fileChooser.getSelectedFile().getPath());
                JOptionPane.showMessageDialog(this, "Report exported successfully!");
            }
        });
        
        return sidebar;
    }
    
    private void createSimpleMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        
        JMenuItem saveItem = new JMenuItem("Save Data");
        JMenuItem loadItem = new JMenuItem("Load Data");
        JMenuItem exitItem = new JMenuItem("Exit");
        
        saveItem.addActionListener(e -> {
            FileHandler.saveData(manager);
            JOptionPane.showMessageDialog(this, "Data saved successfully!");
        });
        
        loadItem.addActionListener(e -> {
            manager = FileHandler.loadData();
            refreshAllTabs();
            JOptionPane.showMessageDialog(this, "Data loaded successfully!");
        });
        
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
    
    private JPanel createPrescriptionPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(new JLabel("Prescription Management"));
        panel.add(headerPanel, BorderLayout.NORTH);
        
        // Main content
        JPanel contentPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        // Left - Prescription form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createTitledBorder("Create Prescription"));
        
        // Form fields
        JPanel fieldPanel = new JPanel(new GridLayout(5, 2, 5, 5));
        fieldPanel.add(new JLabel("Patient:"));
        patientComboBox = new JComboBox<>();
        refreshPatientComboBox();
        fieldPanel.add(patientComboBox);
        
        fieldPanel.add(new JLabel("Drug:"));
        drugComboBox = new JComboBox<>();
        refreshDrugComboBox();
        fieldPanel.add(drugComboBox);
        
        fieldPanel.add(new JLabel("Quantity:"));
        quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        fieldPanel.add(quantitySpinner);
        
        fieldPanel.add(new JLabel("Total:"));
        totalLabel = new JLabel("0.00");
        fieldPanel.add(totalLabel);
        
        fieldPanel.add(new JLabel("Patient Balance:"));
        patientBalanceLabel = new JLabel("0.00");
        fieldPanel.add(patientBalanceLabel);
        
        formPanel.add(fieldPanel);
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addItemButton = new JButton("Add Item");
        JButton processButton = new JButton("Process");
        JButton clearButton = new JButton("Clear");
        
        buttonPanel.add(addItemButton);
        buttonPanel.add(processButton);
        buttonPanel.add(clearButton);
        formPanel.add(buttonPanel);
        
        // Right - Prescription items table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Prescription Items"));
        String[] columns = {"Drug ID", "Drug Name", "Quantity", "Price", "Subtotal"};
        prescriptionModel = new DefaultTableModel(columns, 0);
        JTable prescriptionTable = new JTable(prescriptionModel);
        JScrollPane tableScroll = new JScrollPane(prescriptionTable);
        tablePanel.add(tableScroll, BorderLayout.CENTER);
        
        contentPanel.add(formPanel);
        contentPanel.add(tablePanel);
        panel.add(contentPanel, BorderLayout.CENTER);
        
        // Event listeners
        patientComboBox.addActionListener(e -> updatePatientBalance());
        addItemButton.addActionListener(e -> addItemToPrescription());
        processButton.addActionListener(e -> processPrescription());
        clearButton.addActionListener(e -> clearPrescription());
        
        return panel;
    }
    
    private JPanel createInventoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Drug table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Drug Inventory"));
        String[] columns = {"ID", "Name", "Quantity", "Price", "Min Stock", "Expiry Date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        drugTable = new JTable(model);
        refreshDrugTable();
        JScrollPane tableScroll = new JScrollPane(drugTable);
        tablePanel.add(tableScroll, BorderLayout.CENTER);
        
        // Alert areas
        JPanel alertPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        
        JPanel stockAlertPanel = new JPanel(new BorderLayout());
        stockAlertPanel.setBorder(BorderFactory.createTitledBorder("Low Stock Alerts"));
        alertArea = new JTextArea(10, 25);
        alertArea.setEditable(false);
        JScrollPane alertScroll = new JScrollPane(alertArea);
        stockAlertPanel.add(alertScroll, BorderLayout.CENTER);
        
        JPanel expiryAlertPanel = new JPanel(new BorderLayout());
        expiryAlertPanel.setBorder(BorderFactory.createTitledBorder("Expiring Soon (30 days)"));
        expiryArea = new JTextArea(10, 25);
        expiryArea.setEditable(false);
        JScrollPane expiryScroll = new JScrollPane(expiryArea);
        expiryAlertPanel.add(expiryScroll, BorderLayout.CENTER);
        
        alertPanel.add(stockAlertPanel);
        alertPanel.add(expiryAlertPanel);
        
        // Layout
        panel.add(tablePanel, BorderLayout.CENTER);
        panel.add(alertPanel, BorderLayout.SOUTH);
        
        refreshInventoryData();
        return panel;
    }
    
    private JPanel createPatientPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Patient table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Patients"));
        String[] columns = {"ID", "Name", "Wallet Balance", "Prescription Count"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        patientTable = new JTable(model);
        refreshPatientTable();
        JScrollPane tableScroll = new JScrollPane(patientTable);
        tablePanel.add(tableScroll, BorderLayout.CENTER);
        
        // Add patient form
        JPanel formPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add New Patient"));
        
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField balanceField = new JTextField();
        JButton addButton = new JButton("Add Patient");
        
        formPanel.add(new JLabel("ID:"));
        formPanel.add(new JLabel("Name:"));
        formPanel.add(new JLabel("Initial Balance:"));
        formPanel.add(idField);
        formPanel.add(nameField);
        formPanel.add(balanceField);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        
        addButton.addActionListener(e -> {
            try {
                String id = idField.getText();
                String name = nameField.getText();
                double balance = Double.parseDouble(balanceField.getText());
                
                if (!id.isEmpty() && !name.isEmpty()) {
                    manager.addPatient(new Patient(id, name, balance));
                    refreshPatientTable();
                    refreshPatientComboBox();
                    idField.setText("");
                    nameField.setText("");
                    balanceField.setText("");
                    JOptionPane.showMessageDialog(this, "Patient added successfully!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter valid balance!");
            }
        });
        
        panel.add(tablePanel, BorderLayout.CENTER);
        panel.add(formPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Transaction table
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Transaction History"));
        String[] columns = {"ID", "Prescription ID", "Status", "Wallet Change", "Error Message"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        transactionTable = new JTable(model);
        refreshTransactionTable();
        JScrollPane tableScroll = new JScrollPane(transactionTable);
        tablePanel.add(tableScroll, BorderLayout.CENTER);
        
        // Rollback button
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton rollbackButton = new JButton("Rollback Last Transaction");
        rollbackButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, 
                "Are you sure you want to rollback the last transaction?", 
                "Confirm Rollback", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Rollback feature to be implemented");
            }
        });
        buttonPanel.add(rollbackButton);
        
        panel.add(tablePanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    // ========== EXISTING FUNCTIONALITY METHODS (UNCHANGED) ==========
    
    private void refreshPatientComboBox() {
        patientComboBox.removeAllItems();
        manager.getPatients().values().forEach(patient -> 
            patientComboBox.addItem(patient.getId() + " - " + patient.getName()));
    }
    
    private void refreshDrugComboBox() {
        drugComboBox.removeAllItems();
        manager.getDrugDictionary().getAllDrugs().forEach(drug -> 
            drugComboBox.addItem(drug.getId() + " - " + drug.getName()));
    }
    
    private void updatePatientBalance() {
        if (patientComboBox.getSelectedItem() != null) {
            String selected = patientComboBox.getSelectedItem().toString();
            String patientId = selected.split(" - ")[0];
            Patient patient = manager.getPatient(patientId);
            if (patient != null) {
                patientBalanceLabel.setText(String.format("%.2f", patient.getWalletBalance()));
            }
        }
    }
    
    private void addItemToPrescription() {
        if (patientComboBox.getSelectedItem() == null || drugComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Please select both patient and drug!");
            return;
        }
        
        String patientSelected = patientComboBox.getSelectedItem().toString();
        String patientId = patientSelected.split(" - ")[0];
        
        if (currentPrescription == null) {
            currentPrescription = manager.createPrescription(patientId);
        }
        
        String drugSelected = drugComboBox.getSelectedItem().toString();
        String drugId = drugSelected.split(" - ")[0];
        int quantity = (Integer) quantitySpinner.getValue();
        
        // Add to prescription
        currentPrescription.addItem(drugId, quantity);
        
        // Add to table
        Drug drug = manager.getDrugDictionary().getDrug(drugId);
        if (drug == null) {
            JOptionPane.showMessageDialog(this, "Drug not found!");
            return;
        }
        
        double subtotal = drug.getPrice() * quantity;
        prescriptionModel.addRow(new Object[]{
            drugId, drug.getName(), quantity, 
            String.format("%.2f", drug.getPrice()), 
            String.format("%.2f", subtotal)
        });
        
        // Update total
        updateTotal();
    }
    
    private void updateTotal() {
        if (currentPrescription != null) {
            double total = 0.0;
            for (Map.Entry<String, Integer> entry : currentPrescription.getItems().entrySet()) {
                String drugId = entry.getKey();
                int quantity = entry.getValue();
                Drug drug = manager.getDrugDictionary().getDrug(drugId);
                if (drug != null) {
                    total += drug.getPrice() * quantity;
                }
            }
            totalLabel.setText(String.format("%.2f", total));
        }
    }
    
    private void processPrescription() {
        if (currentPrescription == null || currentPrescription.getItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items in prescription!");
            return;
        }
        
        Transaction transaction = manager.processPrescription(currentPrescription);
        
        if (transaction.isSuccessful()) {
            JOptionPane.showMessageDialog(this, "Prescription processed successfully!");
            clearPrescription();
            refreshAllTabs();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Failed to process prescription: " + transaction.getErrorMessage());
        }
    }
    
    private void clearPrescription() {
        prescriptionModel.setRowCount(0);
        totalLabel.setText("0.00");
        currentPrescription = null;
    }
    
    private void refreshDrugTable() {
        DefaultTableModel model = (DefaultTableModel) drugTable.getModel();
        model.setRowCount(0);
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        manager.getDrugDictionary().getAllDrugs().forEach(drug -> {
            model.addRow(new Object[]{
                drug.getId(),
                drug.getName(),
                drug.getQuantity(),
                String.format("%.2f", drug.getPrice()),
                drug.getMinStockLevel(),
                sdf.format(drug.getExpiryDate())
            });
        });
    }
    
    private void refreshPatientTable() {
        DefaultTableModel model = (DefaultTableModel) patientTable.getModel();
        model.setRowCount(0);
        
        manager.getPatients().values().forEach(patient -> {
            model.addRow(new Object[]{
                patient.getId(),
                patient.getName(),
                String.format("%.2f", patient.getWalletBalance()),
                patient.getPrescriptionHistory().size()
            });
        });
    }
    
    private void refreshTransactionTable() {
        DefaultTableModel model = (DefaultTableModel) transactionTable.getModel();
        model.setRowCount(0);
        
        manager.getTransactionStack().getAllTransactions().forEach(transaction -> {
            model.addRow(new Object[]{
                transaction.getId(),
                transaction.getPrescriptionId(),
                transaction.isSuccessful() ? "SUCCESS" : "FAILED",
                String.format("%.2f", transaction.getWalletChange()),
                transaction.getErrorMessage()
            });
        });
    }
    
    private void refreshInventoryData() {
        refreshDrugTable();
        
        // Update low stock alerts
        alertArea.setText("");
        manager.getLowStockHeap().getAllAlerts().forEach(alert -> {
            alertArea.append(String.format("- %s: %d (min: %d)%n", 
                alert.getDrugName(), alert.getCurrentStock(), alert.getMinStockLevel()));
        });
        
        // Update expiring drugs
        expiryArea.setText("");
        manager.getExpiringDrugs(30).forEach(drug -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            expiryArea.append(String.format("- %s: %s%n", 
                drug.getName(), sdf.format(drug.getExpiryDate())));
        });
    }
    
    private void refreshAllTabs() {
        refreshPatientComboBox();
        refreshDrugComboBox();
        refreshDrugTable();
        refreshPatientTable();
        refreshTransactionTable();
        refreshInventoryData();
    }
}