package Pharmacy.logic;
import Pharmacy.adt.PharmacyADT.*;
import Pharmacy.model.DataModels.*;
import java.util.*;
import java.util.stream.Collectors;

public class PharmacyManager {
    private DrugDictionary drugDictionary;
    private BalancedExpiryTree expiryTree;
    private LowStockHeap lowStockHeap;
    private TransactionStack transactionStack;
    private Map<String, Patient> patients;
    private int transactionCounter;
    private int prescriptionCounter;
    
    public PharmacyManager() {
        this.drugDictionary = new DrugDictionary();
        this.expiryTree = new BalancedExpiryTree();
        this.lowStockHeap = new LowStockHeap();
        this.transactionStack = new TransactionStack();
        this.patients = new HashMap<>();
        this.transactionCounter = 1;
        this.prescriptionCounter = 1;
        
        initializeSampleData();
    }
    
    private void initializeSampleData() {
        Calendar cal = Calendar.getInstance();
        
        Drug drug1 = new Drug("D001", "Paracetamol", 100, 5.50, 
                new Date(cal.getTimeInMillis() + 86400000L * 30), 20);
            Drug drug2 = new Drug("D002", "Amoxicillin", 50, 12.75, 
                new Date(cal.getTimeInMillis() + 86400000L * 60), 15);
            Drug drug3 = new Drug("D003", "Ibuprofen", 75, 8.25, 
                new Date(cal.getTimeInMillis() + 86400000L * 45), 25);
            Drug drug4 = new Drug("D004", "Acetaminophen", 90, 18.50, 
                    new Date(cal.getTimeInMillis() + 86400000L * 45), 20);
            Drug drug5 = new Drug("D005", "Diphenhydramine", 110, 19.00,
            		new Date(cal.getTimeInMillis() + 86400000L * 30), 29);
            Drug drug6 = new Drug("D006", "Fexofenadine", 86, 13.00,
            		new Date(cal.getTimeInMillis() + 86400000L * 30), 39);
            Drug drug7 = new Drug("D007", "Loratadine", 120, 15.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 90), 30);
            Drug drug8 = new Drug("D008", "Cetirizine", 95, 14.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 85), 25);
            Drug drug9 = new Drug("D009", "Aspirin", 200, 6.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 120), 50);
            Drug drug10 = new Drug("D010", "Naproxen", 80, 22.00,
                    new Date(cal.getTimeInMillis() + 86400000L * 75), 20);
            Drug drug11 = new Drug("D011", "Metformin", 150, 8.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 180), 40);
            Drug drug12 = new Drug("D012", "Atorvastatin", 110, 25.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 200), 30);
            Drug drug13 = new Drug("D013", "Lisinopril", 130, 12.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 160), 35);
            Drug drug14 = new Drug("D014", "Levothyroxine", 90, 18.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 150), 25);
            Drug drug15 = new Drug("D015", "Amlodipine", 100, 14.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 170), 30);
            Drug drug16 = new Drug("D016", "Metoprolol", 120, 11.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 140), 35);
            Drug drug17 = new Drug("D017", "Omeprazole", 180, 16.80,
                    new Date(cal.getTimeInMillis() + 86400000L * 130), 45);
            Drug drug18 = new Drug("D018", "Losartan", 95, 19.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 155), 25);
            Drug drug19 = new Drug("D019", "Albuterol", 70, 32.00,
                    new Date(cal.getTimeInMillis() + 86400000L * 100), 20);
            Drug drug20 = new Drug("D020", "Gabapentin", 110, 21.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 125), 30);
            Drug drug21 = new Drug("D021", "Hydrochlorothiazide", 140, 9.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 135), 35);
            Drug drug22 = new Drug("D022", "Sertraline", 125, 23.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 145), 30);
            Drug drug23 = new Drug("D023", "Simvastatin", 105, 20.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 165), 25);
            Drug drug24 = new Drug("D024", "Pantoprazole", 160, 17.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 140), 40);
            Drug drug25 = new Drug("D025", "Fluoxetine", 115, 19.80,
                    new Date(cal.getTimeInMillis() + 86400000L * 150), 30);
            Drug drug26 = new Drug("D026", "Carvedilol", 85, 26.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 120), 20);
            Drug drug27 = new Drug("D027", "Tramadol", 60, 45.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 90), 15);
            Drug drug28 = new Drug("D028", "Warfarin", 75, 38.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 110), 20);
            Drug drug29 = new Drug("D029", "Clopidogrel", 130, 29.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 160), 35);
            Drug drug30 = new Drug("D030", "Insulin Glargine", 45, 125.00,
                    new Date(cal.getTimeInMillis() + 86400000L * 60), 10);
            Drug drug31 = new Drug("D031", "Montelukast", 140, 27.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 130), 35);
            Drug drug32 = new Drug("D032", "Duloxetine", 95, 34.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 140), 25);
            Drug drug33 = new Drug("D033", "Escitalopram", 120, 28.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 150), 30);
            Drug drug34 = new Drug("D034", "Venlafaxine", 100, 31.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 135), 25);
            Drug drug35 = new Drug("D035", "Pregabalin", 80, 42.00,
                    new Date(cal.getTimeInMillis() + 86400000L * 125), 20);
            Drug drug36 = new Drug("D036", "Tamsulosin", 110, 24.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 155), 30);
            Drug drug37 = new Drug("D037", "Finasteride", 90, 33.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 170), 25);
            Drug drug38 = new Drug("D038", "Allopurinol", 130, 15.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 180), 35);
            Drug drug39 = new Drug("D039", "Metronidazole", 150, 12.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 95), 40);
            Drug drug40 = new Drug("D040", "Cephalexin", 140, 18.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 85), 35);
            Drug drug41 = new Drug("D041", "Azithromycin", 120, 22.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 100), 30);
            Drug drug42 = new Drug("D042", "Doxycycline", 110, 16.80,
                    new Date(cal.getTimeInMillis() + 86400000L * 110), 30);
            Drug drug43 = new Drug("D043", "Ciprofloxacin", 95, 19.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 105), 25);
            Drug drug44 = new Drug("D044", "Clindamycin", 85, 26.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 95), 20);
            Drug drug45 = new Drug("D045", "Trimethoprim", 160, 11.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 120), 40);
            Drug drug46 = new Drug("D046", "Acyclovir", 75, 38.00,
                    new Date(cal.getTimeInMillis() + 86400000L * 130), 20);
            Drug drug47 = new Drug("D047", "Valacyclovir", 65, 45.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 125), 15);
            Drug drug48 = new Drug("D048", "Fluconazole", 140, 29.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 140), 35);
            Drug drug49 = new Drug("D049", "Miconazole", 180, 14.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 110), 45);
            Drug drug50 = new Drug("D050", "Hydrocortisone", 200, 8.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 100), 50);
            Drug drug51 = new Drug("D051", "Prednisone", 130, 12.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 90), 35);
            Drug drug52 = new Drug("D052", "Dexamethasone", 95, 17.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 95), 25);
            Drug drug53 = new Drug("D053", "Methylprednisolone", 80, 21.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 85), 20);
            Drug drug54 = new Drug("D054", "Budesonide", 110, 32.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 120), 30);
            Drug drug55 = new Drug("D055", "Fluticasone", 140, 28.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 130), 35);
            Drug drug56 = new Drug("D056", "Salbutamol", 160, 15.80,
                    new Date(cal.getTimeInMillis() + 86400000L * 110), 40);
            Drug drug57 = new Drug("D057", "Formoterol", 90, 41.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 140), 25);
            Drug drug58 = new Drug("D058", "Tiotropium", 70, 56.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 150), 20);
            Drug drug59 = new Drug("D059", "Ipratropium", 120, 23.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 125), 30);
            Drug drug60 = new Drug("D060", "Theophylline", 100, 19.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 135), 25);
            Drug drug61 = new Drug("D061", "Ranitidine", 180, 10.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 120), 45);
            Drug drug62 = new Drug("D062", "Famotidine", 170, 13.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 130), 40);
            Drug drug63 = new Drug("D063", "Cimetidine", 150, 9.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 115), 35);
            Drug drug64 = new Drug("D064", "Lansoprazole", 140, 20.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 140), 35);
            Drug drug65 = new Drug("D065", "Rabeprazole", 120, 24.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 145), 30);
            Drug drug66 = new Drug("D066", "Esomeprazole", 130, 26.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 150), 35);
            Drug drug67 = new Drug("D067", "Dexlansoprazole", 95, 31.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 135), 25);
            Drug drug68 = new Drug("D068", "Metoclopramide", 110, 14.80,
                    new Date(cal.getTimeInMillis() + 86400000L * 125), 30);
            Drug drug69 = new Drug("D069", "Ondansetron", 85, 35.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 140), 20);
            Drug drug70 = new Drug("D070", "Granisetron", 75, 42.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 130), 15);
            Drug drug71 = new Drug("D071", "Prochlorperazine", 100, 18.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 120), 25);
            Drug drug72 = new Drug("D072", "Promethazine", 120, 16.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 110), 30);
            Drug drug73 = new Drug("D073", "Meclizine", 140, 12.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 100), 35);
            Drug drug74 = new Drug("D074", "Dimenhydrinate", 160, 11.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 95), 40);
            Drug drug75 = new Drug("D075", "Scopolamine", 65, 28.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 105), 15);
            Drug drug76 = new Drug("D076", "Loperamide", 200, 7.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 90), 50);
            Drug drug77 = new Drug("D077", "Bismuth Subsalicylate", 180, 9.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 85), 45);
            Drug drug78 = new Drug("D078", "Docusate", 220, 6.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 120), 55);
            Drug drug79 = new Drug("D079", "Bisacodyl", 190, 8.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 110), 45);
            Drug drug80 = new Drug("D080", "Polyethylene Glycol", 170, 15.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 130), 40);
            Drug drug81 = new Drug("D081", "Lactulose", 150, 18.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 140), 35);
            Drug drug82 = new Drug("D082", "Senna", 200, 5.80,
                    new Date(cal.getTimeInMillis() + 86400000L * 100), 50);
            Drug drug83 = new Drug("D083", "Psyllium", 180, 12.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 150), 45);
            Drug drug84 = new Drug("D084", "Methylcellulose", 160, 10.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 145), 40);
            Drug drug85 = new Drug("D085", "Mineral Oil", 140, 7.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 160), 35);
            Drug drug86 = new Drug("D086", "Glycerin", 220, 4.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 180), 55);
            Drug drug87 = new Drug("D087", "Magnesium Citrate", 130, 14.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 95), 35);
            Drug drug88 = new Drug("D088", "Sodium Phosphate", 110, 16.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 90), 30);
            Drug drug89 = new Drug("D089", "Furosemide", 120, 11.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 125), 30);
            Drug drug90 = new Drug("D090", "Hydrochlorothiazide", 140, 9.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 135), 35);
            Drug drug91 = new Drug("D091", "Spironolactone", 100, 17.80,
                    new Date(cal.getTimeInMillis() + 86400000L * 140), 25);
            Drug drug92 = new Drug("D092", "Triamterene", 95, 15.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 130), 25);
            Drug drug93 = new Drug("D093", "Bumetanide", 85, 22.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 120), 20);
            Drug drug94 = new Drug("D094", "Torsemide", 90, 26.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 135), 25);
            Drug drug95 = new Drug("D095", "Mannitol", 70, 34.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 150), 15);
            Drug drug96 = new Drug("D096", "Acetazolamide", 80, 29.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 140), 20);
            Drug drug97 = new Drug("D097", "Methazolamide", 65, 32.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 130), 15);
            Drug drug98 = new Drug("D098", "Dorzolamide", 75, 41.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 125), 20);
            Drug drug99 = new Drug("D099", "Brinzolamide", 60, 48.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 120), 15);
            Drug drug100 = new Drug("D100", "Timolol", 110, 19.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 110), 30);
            Drug drug101 = new Drug("D101", "Latanoprost", 85, 52.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 100), 20);
            Drug drug102 = new Drug("D102", "Bimatoprost", 70, 61.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 95), 15);
            Drug drug103 = new Drug("D103", "Travoprost", 75, 58.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 105), 20);
            Drug drug104 = new Drug("D104", "Unoprostone", 65, 47.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 115), 15);
            Drug drug105 = new Drug("D105", "Pilocarpine", 120, 14.80,
                    new Date(cal.getTimeInMillis() + 86400000L * 90), 30);
            Drug drug106 = new Drug("D106", "Carbachol", 95, 23.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 85), 25);
            Drug drug107 = new Drug("D107", "Echothiophate", 55, 67.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 80), 10);
            Drug drug108 = new Drug("D108", "Demecarium", 50, 72.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 75), 10);
            Drug drug109 = new Drug("D109", "Isofluorophate", 45, 81.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 70), 10);
            Drug drug110 = new Drug("D110", "Physostigmine", 80, 35.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 95), 20);
            Drug drug111 = new Drug("D111", "Neostigmine", 90, 28.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 100), 25);
            Drug drug112 = new Drug("D112", "Pyridostigmine", 85, 31.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 105), 20);
            Drug drug113 = new Drug("D113", "Edrophonium", 70, 42.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 90), 15);
            Drug drug114 = new Drug("D114", "Ambenonium", 60, 53.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 85), 15);
            Drug drug115 = new Drug("D115", "Galantamine", 100, 37.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 110), 25);
            Drug drug116 = new Drug("D116", "Rivastigmine", 95, 44.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 115), 25);
            Drug drug117 = new Drug("D117", "Donepezil", 110, 39.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 120), 30);
            Drug drug118 = new Drug("D118", "Tacrine", 75, 29.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 100), 20);
            Drug drug119 = new Drug("D119", "Memantine", 120, 46.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 130), 30);
            Drug drug120 = new Drug("D120", "Levodopa", 90, 52.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 125), 25);
            Drug drug121 = new Drug("D121", "Carbidopa", 85, 48.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 120), 20);
            Drug drug122 = new Drug("D122", "Entacapone", 95, 57.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 115), 25);
            Drug drug123 = new Drug("D123", "Tolcapone", 70, 63.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 110), 15);
            Drug drug124 = new Drug("D124", "Selegiline", 100, 41.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 105), 25);
            Drug drug125 = new Drug("D125", "Rasagiline", 80, 59.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 100), 20);
            Drug drug126 = new Drug("D126", "Amantadine", 130, 26.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 95), 35);
            Drug drug127 = new Drug("D127", "Benztropine", 110, 34.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 90), 30);
            Drug drug128 = new Drug("D128", "Trihexyphenidyl", 95, 31.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 85), 25);
            Drug drug129 = new Drug("D129", "Procyclidine", 85, 36.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 80), 20);
            Drug drug130 = new Drug("D130", "Biperiden", 75, 39.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 75), 15);
            Drug drug131 = new Drug("D131", "Ethopropazine", 65, 42.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 70), 15);
            Drug drug132 = new Drug("D132", "Orphenadrine", 90, 28.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 65), 25);
            Drug drug133 = new Drug("D133", "Chlorpromazine", 120, 17.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 60), 30);
            Drug drug134 = new Drug("D134", "Fluphenazine", 100, 23.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 55), 25);
            Drug drug135 = new Drug("D135", "Perphenazine", 95, 26.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 50), 25);
            Drug drug136 = new Drug("D136", "Prochlorperazine", 110, 21.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 45), 30);
            Drug drug137 = new Drug("D137", "Trifluoperazine", 85, 29.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 40), 20);
            Drug drug138 = new Drug("D138", "Thioridazine", 75, 32.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 35), 15);
            Drug drug139 = new Drug("D139", "Mesoridazine", 70, 35.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 30), 15);
            Drug drug140 = new Drug("D140", "Pipotiazine", 65, 41.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 25), 15);
            Drug drug141 = new Drug("D141", "Haloperidol", 130, 19.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 20), 35);
            Drug drug142 = new Drug("D142", "Droperidol", 90, 37.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 15), 25);
            Drug drug143 = new Drug("D143", "Pimozide", 80, 44.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 10), 20);
            Drug drug144 = new Drug("D144", "Penfluridol", 70, 52.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 5), 15);
            Drug drug145 = new Drug("D145", "Fluspirilene", 60, 58.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 3), 15);
            Drug drug146 = new Drug("D146", "Clozapine", 95, 63.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 180), 25);
            Drug drug147 = new Drug("D147", "Olanzapine", 110, 57.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 170), 30);
            Drug drug148 = new Drug("D148", "Quetiapine", 120, 49.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 160), 30);
            Drug drug149 = new Drug("D149", "Risperidone", 130, 42.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 150), 35);
            Drug drug150 = new Drug("D150", "Ziprasidone", 100, 55.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 140), 25);
            Drug drug151 = new Drug("D151", "Aripiprazole", 115, 61.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 130), 30);
            Drug drug152 = new Drug("D152", "Paliperidone", 90, 67.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 120), 25);
            Drug drug153 = new Drug("D153", "Iloperidone", 85, 59.75,
                    new Date(cal.getTimeInMillis() + 86400000L * 110), 20);
            Drug drug154 = new Drug("D154", "Lurasidone", 95, 64.50,
                    new Date(cal.getTimeInMillis() + 86400000L * 100), 25);
            Drug drug155 = new Drug("D155", "Asenapine", 80, 71.25,
                    new Date(cal.getTimeInMillis() + 86400000L * 90), 20);
            
            
            addDrug(drug1);
            addDrug(drug2);
            addDrug(drug3);
            addDrug(drug4);
            addDrug(drug5);
            addDrug(drug6);
            addDrug(drug7);
            addDrug(drug8);
            addDrug(drug9);
            addDrug(drug10);
            addDrug(drug11);
            addDrug(drug12);
            addDrug(drug13);
            addDrug(drug14);
            addDrug(drug15);
            addDrug(drug16);
            addDrug(drug17);
            addDrug(drug18);
            addDrug(drug19);
            addDrug(drug20);
            addDrug(drug21);
            addDrug(drug22);
            addDrug(drug23);
            addDrug(drug24);
            addDrug(drug25);
            addDrug(drug26);
            addDrug(drug27);
            addDrug(drug28);
            addDrug(drug29);
            addDrug(drug30);
            addDrug(drug31);
            addDrug(drug32);
            addDrug(drug33);
            addDrug(drug34);
            addDrug(drug35);
            addDrug(drug36);
            addDrug(drug37);
            addDrug(drug38);
            addDrug(drug39);
            addDrug(drug40);
            addDrug(drug41);
            addDrug(drug42);
            addDrug(drug43);
            addDrug(drug44);
            addDrug(drug45);
            addDrug(drug46);
            addDrug(drug47);
            addDrug(drug48);
            addDrug(drug49);
            addDrug(drug50);
            addDrug(drug51);
            addDrug(drug52);
            addDrug(drug53);
            addDrug(drug54);
            addDrug(drug55);
            addDrug(drug56);
            addDrug(drug57);
            addDrug(drug58);
            addDrug(drug59);
            addDrug(drug60);
            addDrug(drug61);
            addDrug(drug62);
            addDrug(drug63);
            addDrug(drug64);
            addDrug(drug65);
            addDrug(drug66);
            addDrug(drug67);
            addDrug(drug68);
            addDrug(drug69);
            addDrug(drug70);
            addDrug(drug71);
            addDrug(drug72);
            addDrug(drug73);
            addDrug(drug74);
            addDrug(drug75);
            addDrug(drug76);
            addDrug(drug77);
            addDrug(drug78);
            addDrug(drug79);
            addDrug(drug80);
            addDrug(drug81);
            addDrug(drug82);
            addDrug(drug83);
            addDrug(drug84);
            addDrug(drug85);
            addDrug(drug86);
            addDrug(drug87);
            addDrug(drug88);
            addDrug(drug89);
            addDrug(drug90);
            addDrug(drug91);
            addDrug(drug92);
            addDrug(drug93);
            addDrug(drug94);
            addDrug(drug95);
            addDrug(drug96);
            addDrug(drug97);
            addDrug(drug98);
            addDrug(drug99);
            addDrug(drug100);
            addDrug(drug101);
            addDrug(drug102);
            addDrug(drug103);
            addDrug(drug104);
            addDrug(drug105);
            addDrug(drug106);
            addDrug(drug107);
            addDrug(drug108);
            addDrug(drug109);
            addDrug(drug110);
            addDrug(drug111);
            addDrug(drug112);
            addDrug(drug113);
            addDrug(drug114);
            addDrug(drug115);
            addDrug(drug116);
            addDrug(drug117);
            addDrug(drug118);
            addDrug(drug119);
            addDrug(drug120);
            addDrug(drug121);
            addDrug(drug122);
            addDrug(drug123);
            addDrug(drug124);
            addDrug(drug125);
            addDrug(drug126);
            addDrug(drug127);
            addDrug(drug128);
            addDrug(drug129);
            addDrug(drug130);
            addDrug(drug131);
            addDrug(drug132);
            addDrug(drug133);
            addDrug(drug134);
            addDrug(drug135);
            addDrug(drug136);
            addDrug(drug137);
            addDrug(drug138);
            addDrug(drug139);
            addDrug(drug140);
            addDrug(drug141);
            addDrug(drug142);
            addDrug(drug143);
            addDrug(drug144);
            addDrug(drug145);
            addDrug(drug146);
            addDrug(drug147);
            addDrug(drug148);
            addDrug(drug149);
            addDrug(drug150);
            addDrug(drug151);
            addDrug(drug152);
            addDrug(drug153);
            addDrug(drug154);
            addDrug(drug155);

        Patient patient1 = new Patient("P001", "Harvey Belgado", 500.0);
        Patient patient2 = new Patient("P002", "Jessa Cantila", 300.0);
        
        patients.put(patient1.getId(), patient1);
        patients.put(patient2.getId(), patient2);
    }
    
    public void addDrug(Drug drug) {
        drugDictionary.addDrug(drug);
        expiryTree.addDrug(drug);
        lowStockHeap.updateAlert(drug.getId(), drug.getName(), 
            drug.getQuantity(), drug.getMinStockLevel());
    }
    
    public Transaction processPrescription(Prescription prescription) {
        String transactionId = "T" + transactionCounter++;
        Transaction transaction = new Transaction(transactionId, prescription.getId());
        
        try {
            // Validate prescription
            if (!prescription.isValid()) {
                throw new Exception("Prescription is invalid");
            }
            
            Patient patient = patients.get(prescription.getPatientId());
            if (patient == null) {
                throw new Exception("Patient not found");
            }
            
            // Check stock and calculate total - FIXED: Use drugDictionary directly
            double total = calculatePrescriptionTotal(prescription);
            Map<String, Integer> tempStockChanges = new HashMap<>();
            
            for (Map.Entry<String, Integer> entry : prescription.getItems().entrySet()) {
                String drugId = entry.getKey();
                int requiredQuantity = entry.getValue(); // FIXED: Corrected variable name
                Drug drug = drugDictionary.getDrug(drugId); // FIXED: Added getDrug method call
                
                if (drug == null) {
                    throw new Exception("Drug not found: " + drugId);
                }
                
                if (drug.getQuantity() < requiredQuantity) {
                    throw new Exception("Insufficient stock for: " + drug.getName());
                }
                
                // Reserve stock temporarily
                tempStockChanges.put(drugId, -requiredQuantity);
            }
            
            // Check wallet balance
            if (patient.getWalletBalance() < total) {
                throw new Exception("Insufficient wallet balance");
            }
            
            // Execute transaction - update stock and wallet
            for (Map.Entry<String, Integer> entry : tempStockChanges.entrySet()) {
                String drugId = entry.getKey();
                int quantityChange = entry.getValue();
                Drug drug = drugDictionary.getDrug(drugId); // FIXED: Added getDrug method call
                
                int newQuantity = drug.getQuantity() + quantityChange;
                drug.setQuantity(newQuantity);
                expiryTree.updateDrugQuantity(drugId, newQuantity);
                lowStockHeap.updateAlert(drugId, drug.getName(), newQuantity, drug.getMinStockLevel());
                
                transaction.addStockChange(drugId, quantityChange);
            }
            
            patient.setWalletBalance(patient.getWalletBalance() - total);
            transaction.setWalletChange(-total);
            transaction.setSuccessful(true);
            patient.addPrescription(prescription.getId());
            
        } catch (Exception e) {
            transaction.setSuccessful(false);
            transaction.setErrorMessage(e.getMessage());
            rollbackTransaction(transaction);
        }
        
        transactionStack.pushTransaction(transaction);
        return transaction;
    }
    
    // NEW METHOD: Calculate prescription total manually
    private double calculatePrescriptionTotal(Prescription prescription) {
        double total = 0.0;
        for (Map.Entry<String, Integer> entry : prescription.getItems().entrySet()) {
            String drugId = entry.getKey();
            int quantity = entry.getValue();
            Drug drug = drugDictionary.getDrug(drugId);
            if (drug != null) {
                total += drug.getPrice() * quantity;
            }
        }
        return total;
    }
    
    private void rollbackTransaction(Transaction transaction) {
        if (!transaction.getStockChanges().isEmpty()) {
            for (Map.Entry<String, Integer> entry : transaction.getStockChanges().entrySet()) {
                String drugId = entry.getKey();
                int quantityChange = entry.getValue();
                Drug drug = drugDictionary.getDrug(drugId); // FIXED: Added getDrug method call
                
                if (drug != null) {
                    int newQuantity = drug.getQuantity() - quantityChange; // Reverse the change
                    drug.setQuantity(newQuantity);
                    expiryTree.updateDrugQuantity(drugId, newQuantity);
                    lowStockHeap.updateAlert(drugId, drug.getName(), newQuantity, drug.getMinStockLevel());
                }
            }
        }
    }
    
    public Prescription createPrescription(String patientId) {
        String prescriptionId = "RX" + prescriptionCounter++;
        return new Prescription(prescriptionId, patientId);
    }
    
    // Getters for ADTs
    public DrugDictionary getDrugDictionary() { return drugDictionary; }
    public BalancedExpiryTree getExpiryTree() { return expiryTree; }
    public LowStockHeap getLowStockHeap() { return lowStockHeap; }
    public TransactionStack getTransactionStack() { return transactionStack; }
    public Map<String, Patient> getPatients() { return patients; }
    
    public Patient getPatient(String patientId) {
        return patients.get(patientId);
    }
    
    public void addPatient(Patient patient) {
        patients.put(patient.getId(), patient);
    }
    
    public List<Drug> getExpiringDrugs(int days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, days);
        Date threshold = cal.getTime();
        return expiryTree.getDrugsExpiringBefore(threshold);
    }
}