import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import Purchase.Purchase;
import com.google.gson.Gson;

public class Client {

    public static void main(String[] args) {
        Gson gson = new Gson();

        System.out.println("Добро пожаловать в менеджер покупок!");

        try (Socket socket = new Socket("localhost", 8989);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            Scanner scanner = new Scanner(System.in);

            Purchase purchase = getPurchase(scanner);
            if (purchase == null) {
                throw new RuntimeException("Вы вводите некорректные данные! Пройдите проверку на бота и подключитесь снова");
            }
            String jsonPurchase = gson.toJson(purchase);
            out.println(jsonPurchase);

            System.out.println("\nИдет пересылка данных на сервер...\n");
            String answerFromServer = in.readLine();
            if (answerFromServer == null) {
                System.out.println("Сервер не отвечает, перезайдите в программу");
            } else {
                System.out.println(answerFromServer);
            }
        } catch (IOException e) {
            System.out.println("Не могу подключиться к серверу");
            e.printStackTrace();
        }
    }

    private static Purchase getPurchase(Scanner scanner) {
        Purchase data = null;
        for (int k = 0; k < 3; ) {
            System.out.println("Введите продукт: ");
            String title = scanner.nextLine();

            System.out.println("Введите сумму: ");
            int sum;
            try {
                sum = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException exception) {
                System.out.println("Вы вводите не цифры, а что-то другое! Повторите попытку");
                k++;
                continue;
            }

            System.out.println("Вы сделали покупку сегодня? (да/нет)");
            String dateAnswer = scanner.nextLine().toLowerCase();
            String date = null;
            switch (dateAnswer) {
                case "да":
                    date = getToday();
                    break;
                case "нет":
                    System.out.println("Введите дату в формате: год.месяц.число. Например: 2022.03.22");
                    String draftDate = scanner.nextLine();
                    if (!isOkFormat(draftDate)) {
                        System.out.println("Вы вводите нечитаемую дату (либо дату не по формату), повторите попытку");
                        k++;
                    } else {
                        date = replaceSigns(draftDate);
                    }
                    break;
                default:
                    System.out.println("Пожалуйста, определитесь с датой! И повторите попытку");
                    k++;
            }

            data = (date != null) ? new Purchase(title, date, sum) : null;
            if (data != null) break;
        }
        return data;
    }

    private static String getToday() {
        String date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
        date = simpleDateFormat.format(new Date());
        return date;
    }

    private static boolean isOkFormat(String draftDate) {
        return draftDate.matches("(19|20)\\d\\d[\\W_](0[1-9]|1[012])[\\W_](0[1-9]|[12][0-9]|3[01])");
    }

    private static String replaceSigns(String str) {
        return str.replaceAll("[\\W_]", ".");
    }
}

