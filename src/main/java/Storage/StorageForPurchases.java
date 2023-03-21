package Storage;

import Purchase.Purchase;

import java.util.ArrayList;
import java.util.List;

public class StorageForPurchases implements Storable<Purchase> {

    private List<Purchase> purchaseList;

    public StorageForPurchases() {
        purchaseList = new ArrayList<>();;
    }

    @Override
    public void addToStorage(Purchase purchase) {
        purchaseList.add(purchase);
    }

    public List<Purchase> getPurchaseList() {
        return purchaseList;
    }

    //тут буду актуализировать данные
}
