import Analyzer.PurchaseAnalyzer;
import Caregorizator.CategorizatorCSV;
import Purchase.Purchase;
import Storage.StorageForPurchases;
import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Gson gson = new Gson();

        File storage = new File("data.bin");
        StorageForPurchases purchaseStorage;
        if (storage.exists()) {
            purchaseStorage = StorageForPurchases.loadFromBin(storage);
        } else {
            purchaseStorage = new StorageForPurchases();
        }

        File categoriesFile = new File("categories.tsv");
        CategorizatorCSV categorizator = new CategorizatorCSV(categoriesFile);
        PurchaseAnalyzer analyzer = new PurchaseAnalyzer(categorizator);

        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String purchaseMessage = in.readLine();
                    if (purchaseMessage != null) {
                        Purchase currentPurchase = gson.fromJson(purchaseMessage, Purchase.class);
                        purchaseStorage.addToStorage(currentPurchase);
                        purchaseStorage.saveBin(storage);

                        String dateForAnalysis = currentPurchase.getDate();
                        analyzer.getAnalytics(purchaseStorage, dateForAnalysis);
                        String jsonAnalytics = gson.toJson(analyzer.getReport());
                        out.println(jsonAnalytics);
                    }
                } catch (IOException | ParseException e) {
                    System.out.println("Не могу стартовать сервер");
                    e.printStackTrace();
                }
            }
        }
    }
}