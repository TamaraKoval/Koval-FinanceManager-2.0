package Analyzer;

import Purchase.Purchase;
import Caregorizator.CategorizatorCSV;
import Storage.StorageForPurchases;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PurchaseAnalyzer {

    private CategorizatorCSV categorizator;
    private Map<String, Integer> mapForAnalysis;
    private Map<String, MaxCategory> currentAnalytics;

    public PurchaseAnalyzer(CategorizatorCSV categorizator) {
        this.categorizator = categorizator;
        mapForAnalysis = new HashMap<>();
        currentAnalytics = new HashMap<>();
    }

    public void prepareDataForAnalysis(StorageForPurchases storage) {
        for (String cat : categorizator.getListOfCategories()) {
            mapForAnalysis.put(cat, 0);
        }
        for (Purchase purchase : storage.getPurchaseList()) {
            int value = mapForAnalysis.get(categorizator.getCategory(purchase.getName()));
            value += purchase.getSum();
            mapForAnalysis.put(categorizator.getCategory(purchase.getName()), value);
        }
    }

    public void doAnalytics() {
        int maxValue = Collections.max(mapForAnalysis.values());
        for (Map.Entry<String, Integer> kv : mapForAnalysis.entrySet()) {
            if (kv.getValue().equals(maxValue)) {
                MaxCategory maxCategory = new MaxCategory(kv.getKey(), kv.getValue());
                currentAnalytics.put("MaxCategory", maxCategory);
            }
        }
    }

    public Map<String, MaxCategory> getCurrentAnalytics() {
        return currentAnalytics;
    }
}
