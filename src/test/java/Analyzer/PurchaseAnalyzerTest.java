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
    void test_prepareDataForAnalysis_keysBeforeFilling() throws ParseException {
        String[] testList = {"еда", "быт", "другое"};
        Mockito.when(categorizator.getListOfCategories()).thenReturn(testList);

        purchaseAnalyzer.prepareDataForAnalysis(storage);

        Assertions.assertTrue(purchaseAnalyzer.getMapForAnalysis().keySet().containsAll(List.of(testList)));;
    }

    @Test
    void test_prepareDataForAnalysis_valuesBeforeFilling() throws ParseException {
        String[] testList = {"еда", "быт", "другое"};
        Mockito.when(categorizator.getListOfCategories()).thenReturn(testList);

        purchaseAnalyzer.prepareDataForAnalysis(storage);
        Set<Integer> setOfGivenValues = new HashSet<>(purchaseAnalyzer.getMapForAnalysis().values());

        Assertions.assertTrue(setOfGivenValues.containsAll(List.of(0)));;
    }

    @Test
    void test_prepareDataForAnalysis_MaxMustBeZeroWithEmptyStorage() throws ParseException {
        int expectedSum = 0;

        String[] testList = {"еда", "быт", "другое"};
        Mockito.when(categorizator.getListOfCategories()).thenReturn(testList);
        purchaseAnalyzer.prepareDataForAnalysis(storage);

        int checkSum = Collections.max(purchaseAnalyzer.getMapForAnalysis().values());;

        Assertions.assertEquals(expectedSum, checkSum);
    }

    @Test
    void test_prepareDataForAnalysis_fillAnalyzerWithStorage() throws ParseException {
        int expectedSum = 100;

        String[] testList = {"еда", "быт", "другое"};
        Mockito.when(categorizator.getListOfCategories()).thenReturn(testList);
        Mockito.when(categorizator.getCategory("сухарик")).thenReturn("еда");

        Purchase purchase = new Purchase("сухарик", "2023.03.21", 100);
        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(purchase);
        Mockito.when(storage.getPurchaseList()).thenReturn(purchaseList);

        purchaseAnalyzer.prepareDataForAnalysis(storage);

        int checkSum = purchaseAnalyzer.getMapForAnalysis().get(categorizator.getCategory(purchase.getTitle()));

        Assertions.assertEquals(expectedSum, checkSum);
    }

    @Test
    void test_findMaxCategory_withoutStorage() throws ParseException {
        MaxCategory expectedMaxCategory = new MaxCategory("отсутствует", 0);

        String[] testList = {"еда", "быт", "другое"};
        Mockito.when(categorizator.getListOfCategories()).thenReturn(testList);

        purchaseAnalyzer.prepareDataForAnalysis(storage);

        MaxCategory recievedMaxCategory = purchaseAnalyzer.findMaxCategory(purchaseAnalyzer.getMapForAnalysis());

        Assertions.assertEquals(expectedMaxCategory, recievedMaxCategory);
    }

    @Test
    void test_findMaxCategory_withStorage() throws ParseException {
        MaxCategory expectedMaxCategory = new MaxCategory("еда", 100);

        String[] testList = {"еда", "быт", "другое"};
        Mockito.when(categorizator.getListOfCategories()).thenReturn(testList);
        Mockito.when(categorizator.getCategory("сухарик")).thenReturn("еда");

        Purchase purchase = new Purchase("сухарик", "2023.03.21", 100);
        List<Purchase> purchaseList = new ArrayList<>();
        purchaseList.add(purchase);
        Mockito.when(storage.getPurchaseList()).thenReturn(purchaseList);

        purchaseAnalyzer.prepareDataForAnalysis(storage);

        MaxCategory recievedMaxCategory = purchaseAnalyzer.findMaxCategory(purchaseAnalyzer.getMapForAnalysis());

        Assertions.assertEquals(expectedMaxCategory, recievedMaxCategory);
    }
}