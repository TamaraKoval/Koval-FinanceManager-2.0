package Analyzer;

import Categorizator.Categorizable;
import Purchase.Purchase;
import Storage.StorageForPurchases;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PurchaseAnalyzer {

    private Categorizable categorizator;
    private MaxCategoryReport report;
    Map<String, Integer> mapForAnalysis;
    Map<String, Integer> mapForAnalysisYear;
    Map<String, Integer> mapForAnalysisMonth;
    Map<String, Integer> mapForAnalysisDay;

    public PurchaseAnalyzer(Categorizable categorizator) {
        this.categorizator = categorizator;
        report = new MaxCategoryReport();
    }

    public void getAnalytics(StorageForPurchases storage, String dateForAnalysis) throws ParseException {
        mapForAnalysis = new HashMap<>();
        mapForAnalysisYear = new HashMap<>();
        mapForAnalysisMonth = new HashMap<>();
        mapForAnalysisDay = new HashMap<>();

        String checkingYear = dateForAnalysis.substring(0, 4);
        String checkingMonth = dateForAnalysis.substring(0, 7);

        for (Purchase purchase : storage.getPurchaseList()) {
            fillMap(mapForAnalysis, purchase);
            if (purchase.getDate().startsWith(checkingYear)) {
                fillMap(mapForAnalysisYear, purchase);
                if (purchase.getDate().startsWith(checkingMonth)) {
                    fillMap(mapForAnalysisMonth, purchase);
                    if (purchase.getDate().equals(dateForAnalysis)) {
                        fillMap(mapForAnalysisDay, purchase);
                    }
                }
            }
        }
        report.setMaxCategory(findMaxCategory(mapForAnalysis));
        report.setMaxYearCategory(findMaxCategory(mapForAnalysisYear));
        report.setMaxMonthCategory(findMaxCategory(mapForAnalysisMonth));
        report.setMaxDayCategory(findMaxCategory(mapForAnalysisDay));

    }

    protected void fillMap(Map<String, Integer> map, Purchase purchase) {
        String currentCategory = categorizator.getCategory(purchase.getTitle());
        if (map.containsKey(currentCategory)) {
            int value = map.get(currentCategory);
            value += purchase.getSum();
            map.put(currentCategory, value);
        } else {
            map.put(currentCategory, purchase.getSum());
        }
    }

    protected MaxCategory findMaxCategory(Map<String, Integer> map) {
        MaxCategory maxCategory = new MaxCategory();
        if (map.size() == 1) {
            for (Map.Entry<String, Integer> kv : map.entrySet()) {
                maxCategory.setCategory(kv.getKey());
                maxCategory.setSum(kv.getValue());
            }
        } else {
            int maxValue = Collections.max(map.values());
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

    public Map<String, Integer> getMapForAnalysisYear() {
        return mapForAnalysisYear;
    }

    public Map<String, Integer> getMapForAnalysisMonth() {
        return mapForAnalysisMonth;
    }

    public Map<String, Integer> getMapForAnalysisDay() {
        return mapForAnalysisDay;
    }
}
