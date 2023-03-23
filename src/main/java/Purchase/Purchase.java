package Purchase;

import java.io.Serializable;

public class Purchase implements Serializable {

    private String name;
    private String date;
    private int sum;

    public Purchase(String name, String date, int sum) {
        this.name = name;
        this.date = date;
        this.sum = sum;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public int getSum() {
        return sum;
    }
}
