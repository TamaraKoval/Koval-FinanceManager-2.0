package Purchase;

import java.io.Serializable;

public class Purchase implements Serializable {

    private String title;
    private String date;
    private int sum;

    public Purchase(String title, String date, int sum) {
        this.title = title;
        this.date = date;
        this.sum = sum;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getSum() {
        return sum;
    }
}
