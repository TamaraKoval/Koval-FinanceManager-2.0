import Analyzer.PurchaseAnalyzer;
import Caregorizator.CategorizatorCSV;
import Purchase.Purchase;
import Storage.StorageForPurchases;
import com.google.gson.Gson;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Main {

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        File categoriesFile = new File("categories.tsv");
        CategorizatorCSV categorizator = new CategorizatorCSV(categoriesFile);
        PurchaseAnalyzer analyzer = new PurchaseAnalyzer(categorizator);
        StorageForPurchases purchaseStorage = new StorageForPurchases();

        System.out.println("Менеджер личных финансов к работе готов");
        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            while(true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    System.out.println("\nУстановлено новое соединение");
                    String message = in.readLine();
                    Purchase currentPurchase = gson.fromJson(message, Purchase.class);
                    purchaseStorage.addToStorage(currentPurchase);
                    analyzer.prepareDataForAnalysis(purchaseStorage);
                    analyzer.doAnalytics();
                    String jsonAnalytics = gson.toJson(analyzer.getCurrentAnalytics());
                    out.println("Вы получили статистику:");
                    out.println(jsonAnalytics);
                } catch (IOException e) {
                    System.out.println("Не могу стартовать сервер");
                    e.printStackTrace();
                }
            }
        }
    }
}
