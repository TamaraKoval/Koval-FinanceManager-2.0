package Analyzer;

import Purchase.Purchase;
import Caregorizator.CategorizatorCSV;
import Storage.StorageForPurchases;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PurchaseAnalyzer {

    private CategorizatorCSV categorizator;
    private Map<String, Integer> mapForAnalysis;
    private Map<String, Integer> mapForAnalysisYear;
    private Map<String, Integer> mapForAnalysisMonth;
    private Map<String, Integer> mapForAnalysisDay;
    private MaxCategoryReport report;


    public PurchaseAnalyzer(CategorizatorCSV categorizator) {
        this.categorizator = categorizator;
        mapForAnalysis = new HashMap<>();
        mapForAnalysisYear = new HashMap<>();
        mapForAnalysisMonth = new HashMap<>();
        mapForAnalysisDay = new HashMap<>();
        report = new MaxCategoryReport();
    }

    public void prepareDataForAnalysis(StorageForPurchases storage) throws ParseException {
        storage.refreshStorage();

        for (String cat : categorizator.getListOfCategories()) {
            mapForAnalysis.put(cat, 0);
            mapForAnalysisYear.put(cat, 0);
            mapForAnalysisMonth.put(cat, 0);
            mapForAnalysisDay.put(cat, 0);
        }
        for (Purchase purchase : storage.getPurchaseList()) {
            int value = mapForAnalysis.get(categorizator.getCategory(purchase.getName()));
            value += purchase.getSum();
            mapForAnalysis.put(categorizator.getCategory(purchase.getName()), value);
        }

        for (Purchase purchase : storage.getPurchaseListYear()) {
            int value = mapForAnalysisYear.get(categorizator.getCategory(purchase.getName()));
            value += purchase.getSum();
            mapForAnalysisYear.put(categorizator.getCategory(purchase.getName()), value);
        }

        for (Purchase purchase : storage.getPurchaseListMonth()) {
            int value = mapForAnalysisMonth.get(categorizator.getCategory(purchase.getName()));
            value += purchase.getSum();
            mapForAnalysisMonth.put(categorizator.getCategory(purchase.getName()), value);
        }

        for (Purchase purchase : storage.getPurchaseListDay()) {
            int value = mapForAnalysisDay.get(categorizator.getCategory(purchase.getName()));
            value += purchase.getSum();
            mapForAnalysisDay.put(categorizator.getCategory(purchase.getName()), value);
        }
    }

    public void doAnalytics() {
        int maxValue = Collections.max(mapForAnalysis.values());
        for (Map.Entry<String, Integer> kv : mapForAnalysis.entrySet()) {
            if (kv.getValue().equals(maxValue)) {
                MaxCategory maxCategory = new MaxCategory(kv.getKey(), kv.getValue());
                report.setMaxCategory(maxCategory);
            }
        }

        maxValue = Collections.max(mapForAnalysisYear.values());
        for (Map.Entry<String, Integer> kv : mapForAnalysisYear.entrySet()) {
            if (kv.getValue().equals(maxValue)) {
                MaxCategory maxCategory = new MaxCategory(kv.getKey(), kv.getValue());
                report.setMaxYearCategory(maxCategory);
            }
        }

        maxValue = Collections.max(mapForAnalysisMonth.values());
        for (Map.Entry<String, Integer> kv : mapForAnalysisMonth.entrySet()) {
            if (kv.getValue().equals(maxValue)) {
                MaxCategory maxCategory = new MaxCategory(kv.getKey(), kv.getValue());
                report.setMaxMonthCategory(maxCategory);
            }
        }

        maxValue = Collections.max(mapForAnalysisDay.values());
        for (Map.Entry<String, Integer> kv : mapForAnalysisDay.entrySet()) {
            if (kv.getValue().equals(maxValue)) {
                MaxCategory maxCategory = new MaxCategory(kv.getKey(), kv.getValue());
                report.setMaxDayCategory(maxCategory);
            }
        }
    }

    public MaxCategoryReport getReport() {
        return report;
    }
}
