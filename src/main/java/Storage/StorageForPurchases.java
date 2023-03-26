package Storage;

import Purchase.Purchase;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class StorageForPurchases implements Storable<Purchase>, Serializable {

    private static final long serialVersionUID = 1L;

    private List<Purchase> purchaseList;

    public StorageForPurchases() {
        purchaseList = new ArrayList<>();
    }

    @Override
    public void addToStorage(Purchase purchase) throws ParseException {
        purchaseList.add(purchase);
    }

    public List<Purchase> getPurchaseList() {
        return purchaseList;
    }

    public void saveBin(File file) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(this);
        }
    }

    public static StorageForPurchases loadFromBin(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (StorageForPurchases) ois.readObject();
        }
    }
}
