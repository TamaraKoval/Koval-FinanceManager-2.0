package Analyzer;

public class MaxCategory {

    private String category;
    private int sum;

    public MaxCategory(String category, int sum) {
        this.category = category;
        this.sum = sum;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaxCategory that = (MaxCategory) o;
        return sum == that.sum && category.equals(that.category);
    }
}
