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
        File storage = new File("storage.bin");
        StorageForPurchases purchaseStorage;
        if (storage.exists()) {
            purchaseStorage = StorageForPurchases.loadFromBin(storage);
        } else {
            purchaseStorage = new StorageForPurchases();
        }

        File categoriesFile = new File("categories.tsv");
        CategorizatorCSV categorizator = new CategorizatorCSV(categoriesFile);
        PurchaseAnalyzer analyzer = new PurchaseAnalyzer(categorizator);

        System.out.println("Менеджер личных финансов к работе готов");
        try (ServerSocket serverSocket = new ServerSocket(8989)) {
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    System.out.println("\nУстановлено новое соединение");

                    while (true) {
                        String purchaseMessage = in.readLine();
                        if (purchaseMessage == null) {
                            System.out.println("Соединение сброшено");
                            break;
                        }
                        if (!purchaseMessage.equals("0")) {
                            Purchase currentPurchase = gson.fromJson(purchaseMessage, Purchase.class);
                            purchaseStorage.addToStorage(currentPurchase);
                        } else {
                            purchaseStorage.saveBin(storage);
                            out.println("Сформировать статистику? (да/нет):");
                            String answer = in.readLine();
                            switch (answer) {
                                case "да":
                                    analyzer.prepareDataForAnalysis(purchaseStorage);
                                    analyzer.doAnalytics();
                                    String jsonAnalytics = gson.toJson(analyzer.getReport());
                                    out.println(jsonAnalytics);
                                    break;
                                case "нет":
                                    out.println("Мы сохранили статистику, вы сможете запросить еще в следующий раз!");
                                    break;
                                default:
                                    out.println("Извините, мы не поняли ваш ответ, вы сможете запросить статистику в следующий раз!");
                            }
                            break;
                        }
                    }
                } catch (IOException | ParseException e) {
                    System.out.println("Не могу стартовать сервер");
                    e.printStackTrace();
                }
            }
        }
    }
}