package Analyzer;

import Caregorizator.Categorizable;
import Purchase.Purchase;
import Storage.StorageForPurchases;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.util.*;

class PurchaseAnalyzerTest {

    PurchaseAnalyzer purchaseAnalyzer;

    Categorizable categorizator = Mockito.mock(Categorizable.class);
    StorageForPurchases storage = Mockito.mock(StorageForPurchases.class);

    @BeforeEach
    void beforeEach() {
        purchaseAnalyzer = new PurchaseAnalyzer(categorizator);
    }

    @Test
    void test_fillMap() {
        int extectedSum = 100;

        Purchase purchase = new Purchase("сухарик", "2023.03.26", 100);
        Map<String, Integer> mapForAnalysis = new HashMap<>();
        Mockito.when(categorizator.getCategory("сухарик")).thenReturn("еда");

        purchaseAnalyzer.fillMap(mapForAnalysis, purchase);

        Assertions.assertEquals(extectedSum, mapForAnalysis.get(categorizator.getCategory(purchase.getTitle())));
    }

    @Test
    void test_getAnalytics_fillAnalyzerWithStorage() throws ParseException {
        int expectedSum = 100;

        Mockito.when(categorizator.getCategory("сухарик")).thenReturn("еда");

        Purchase purchase = new Purchase("сухарик", "2023.03.26", 100);
        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(purchase);
        Mockito.when(storage.getPurchaseList()).thenReturn(purchaseList);

        purchaseAnalyzer.getAnalytics(storage, purchase.getDate());

        int checkSum = purchaseAnalyzer.getMapForAnalysis().get(categorizator.getCategory(purchase.getTitle()));

        Assertions.assertEquals(expectedSum, checkSum);
    }

    @Test
    void test_findMaxCategory() {
        MaxCategory expectedMaxCategory = new MaxCategory("быт", 200);

        Purchase firstPurchase = new Purchase("сухарик", "2023.03.26", 100);
        Purchase secondPurchase = new Purchase("мыло", "2023.03.26", 200);
        Map<String, Integer> mapForAnalysis = new HashMap<>();
        Mockito.when(categorizator.getCategory("сухарик")).thenReturn("еда");
        Mockito.when(categorizator.getCategory("мыло")).thenReturn("быт");

        purchaseAnalyzer.fillMap(mapForAnalysis, firstPurchase);
        purchaseAnalyzer.fillMap(mapForAnalysis, secondPurchase);
        MaxCategory recievedMaxCategory = purchaseAnalyzer.findMaxCategory(mapForAnalysis);

        Assertions.assertEquals(expectedMaxCategory, recievedMaxCategory);
    }

    @Test
    void test_getAnalytics_wholeMap() throws ParseException {
        MaxCategory expectedMaxCategory = new MaxCategory("быт", 200);

        Purchase firstPurchase = new Purchase("сухарик", "2023.03.26", 100);
        Purchase secondPurchase = new Purchase("мыло", "2023.03.01", 200);
        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(firstPurchase);
        purchaseList.add(secondPurchase);
        Mockito.when(categorizator.getCategory("сухарик")).thenReturn("еда");
        Mockito.when(categorizator.getCategory("мыло")).thenReturn("быт");
        Mockito.when(storage.getPurchaseList()).thenReturn(purchaseList);

        purchaseAnalyzer.getAnalytics(storage, "2023.03.26");
        MaxCategory recievedMaxCategory = purchaseAnalyzer.findMaxCategory(purchaseAnalyzer.getMapForAnalysis());

        Assertions.assertEquals(expectedMaxCategory, recievedMaxCategory);
    }

    @Test
    void test_getAnalytics_mapYear() throws ParseException {
        MaxCategory expectedMaxCategory = new MaxCategory("быт", 200);

        Purchase firstPurchase = new Purchase("сухарик", "2023.03.26", 100);
        Purchase secondPurchase = new Purchase("мыло", "2023.03.01", 200);
        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(firstPurchase);
        purchaseList.add(secondPurchase);
        Mockito.when(categorizator.getCategory("сухарик")).thenReturn("еда");
        Mockito.when(categorizator.getCategory("мыло")).thenReturn("быт");
        Mockito.when(storage.getPurchaseList()).thenReturn(purchaseList);

        purchaseAnalyzer.getAnalytics(storage, "2023.03.26");
        MaxCategory recievedMaxCategory = purchaseAnalyzer.findMaxCategory(purchaseAnalyzer.getMapForAnalysisYear());

        Assertions.assertEquals(expectedMaxCategory, recievedMaxCategory);
    }

    @Test
    void test_getAnalytics_mapMonth() throws ParseException {
        MaxCategory expectedMaxCategory = new MaxCategory("быт", 200);

        Purchase firstPurchase = new Purchase("сухарик", "2023.03.26", 100);
        Purchase secondPurchase = new Purchase("мыло", "2023.03.01", 200);
        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(firstPurchase);
        purchaseList.add(secondPurchase);
        Mockito.when(categorizator.getCategory("сухарик")).thenReturn("еда");
        Mockito.when(categorizator.getCategory("мыло")).thenReturn("быт");
        Mockito.when(storage.getPurchaseList()).thenReturn(purchaseList);

        purchaseAnalyzer.getAnalytics(storage, "2023.03.26");
        MaxCategory recievedMaxCategory = purchaseAnalyzer.findMaxCategory(purchaseAnalyzer.getMapForAnalysisMonth());

        Assertions.assertEquals(expectedMaxCategory, recievedMaxCategory);
    }

    @Test
    void test_getAnalytics_mapDay() throws ParseException {
        MaxCategory expectedMaxCategory = new MaxCategory("еда", 100);

        Purchase firstPurchase = new Purchase("сухарик", "2023.03.26", 100);
        Purchase secondPurchase = new Purchase("мыло", "2023.03.01", 200);
        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(firstPurchase);
        purchaseList.add(secondPurchase);
        Mockito.when(categorizator.getCategory("сухарик")).thenReturn("еда");
        Mockito.when(categorizator.getCategory("мыло")).thenReturn("быт");
        Mockito.when(storage.getPurchaseList()).thenReturn(purchaseList);

        purchaseAnalyzer.getAnalytics(storage, "2023.03.26");
        MaxCategory recievedMaxCategory = purchaseAnalyzer.findMaxCategory(purchaseAnalyzer.getMapForAnalysisDay());

        Assertions.assertEquals(expectedMaxCategory, recievedMaxCategory);
    }
}