package Analyzer;

import Caregorizator.Categorizable;
import Purchase.Purchase;
import Storage.StorageForPurchases;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseAnalyzer {

    private Categorizable categorizator;
    private Map<String, Integer> mapForAnalysis;
    private Map<String, Integer> mapForAnalysisYear;
    private Map<String, Integer> mapForAnalysisMonth;
    private Map<String, Integer> mapForAnalysisDay;
    private MaxCategoryReport report;


    public PurchaseAnalyzer(Categorizable categorizator) {
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

        fillMap(mapForAnalysis, storage.getPurchaseList());
        fillMap(mapForAnalysisYear, storage.getPurchaseListYear());
        fillMap(mapForAnalysisMonth, storage.getPurchaseListMonth());
        fillMap(mapForAnalysisDay, storage.getPurchaseListDay());
    }

    protected void fillMap(Map<String, Integer> map, List<Purchase> purchaseList) {
        for (Purchase purchase : purchaseList) {
            int value = map.get(categorizator.getCategory(purchase.getName()));
            value += purchase.getSum();
            map.put(categorizator.getCategory(purchase.getName()), value);
        }
    }

    public void doAnalytics() {
        report.setMaxCategory(findMaxCategory(mapForAnalysis));
        report.setMaxYearCategory(findMaxCategory(mapForAnalysisYear));
        report.setMaxMonthCategory(findMaxCategory(mapForAnalysisMonth));
        report.setMaxDayCategory(findMaxCategory(mapForAnalysisDay));
    }

    protected MaxCategory findMaxCategory(Map<String, Integer> map) {
        MaxCategory maxCategory = new MaxCategory("отсутствует", 0);

        int maxValue = Collections.max(map.values());
        if (maxValue != 0) {
            for (Map.Entry<String, Integer> kv : map.entrySet()) {
                if (kv.getValue().equals(maxValue)) {
                    maxCategory.setCategory(kv.getKey());
                    maxCategory.setSum(kv.getValue());
                }
            }
        }
        return maxCategory;
    }

    public MaxCategoryReport getReport() {
        return report;
    }

    public Map<String, Integer> getMapForAnalysis() {
        return mapForAnalysis;
    }
}
