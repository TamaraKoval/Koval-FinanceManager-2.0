package Storage;

import Purchase.Purchase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StorageForPurchases implements Storable<Purchase> {

    private List<Purchase> purchaseList;
    private List<Purchase> purchaseListYear;
    private List<Purchase> purchaseListMonth;
    private List<Purchase> purchaseListDay;

    public StorageForPurchases() {
        purchaseList = new ArrayList<>();
        purchaseListYear = new ArrayList<>();
        purchaseListMonth = new ArrayList<>();
        purchaseListDay = new ArrayList<>();
    }

    @Override
    public void addToStorage(Purchase purchase) throws ParseException {
        purchaseList.add(purchase);

        Calendar now = Calendar.getInstance();
        Calendar purchaseDate = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        purchaseDate.setTime(dateFormat.parse(purchase.getDate()));

        if (now.get(Calendar.YEAR) == purchaseDate.get(Calendar.YEAR)) {
            purchaseListYear.add(purchase);
            if (now.get(Calendar.MONTH) == purchaseDate.get(Calendar.MONTH)) {
                purchaseListMonth.add(purchase);
                if (now.get(Calendar.DAY_OF_MONTH) == purchaseDate.get(Calendar.DAY_OF_MONTH)) {
                    purchaseListDay.add(purchase);
                }
            }
        }
    }

    public void refreshStorage() throws ParseException {
        Calendar now = Calendar.getInstance();
        Calendar purchaseDate = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");

        List<Purchase> filteredPurchaseListYear = new ArrayList<>();
        for (Purchase purchase : purchaseListYear) {
            purchaseDate.setTime(dateFormat.parse(purchase.getDate()));
            if (now.get(Calendar.YEAR) == purchaseDate.get(Calendar.YEAR)) {
                filteredPurchaseListYear.add(purchase);
            }
        }
        purchaseListYear = filteredPurchaseListYear;

        List<Purchase> filteredPurchaseListMonth = new ArrayList<>();
        for (Purchase purchase : purchaseListMonth) {
            purchaseDate.setTime(dateFormat.parse(purchase.getDate()));
            if ((now.get(Calendar.MONTH) == purchaseDate.get(Calendar.MONTH)) &&
                    (now.get(Calendar.YEAR) == purchaseDate.get(Calendar.YEAR))) {
                filteredPurchaseListMonth.add(purchase);
            }
        }
        purchaseListMonth = filteredPurchaseListMonth;

        List<Purchase> filteredPurchaseListDay = new ArrayList<>();
        for (Purchase purchase : purchaseListDay) {
            purchaseDate.setTime(dateFormat.parse(purchase.getDate()));
            if (now.get(Calendar.DAY_OF_MONTH) == purchaseDate.get(Calendar.DAY_OF_MONTH) &&
                    (now.get(Calendar.MONTH) == purchaseDate.get(Calendar.MONTH)) &&
                    (now.get(Calendar.YEAR) == purchaseDate.get(Calendar.YEAR))) {
                filteredPurchaseListDay.add(purchase);
            }
        }
        purchaseListDay = filteredPurchaseListDay;
    }

    public List<Purchase> getPurchaseList() {
        return purchaseList;
    }

    public List<Purchase> getPurchaseListYear() {
        return purchaseListYear;
    }

    public List<Purchase> getPurchaseListMonth() {
        return purchaseListMonth;
    }

    public List<Purchase> getPurchaseListDay() {
        return purchaseListDay;
    }
}
