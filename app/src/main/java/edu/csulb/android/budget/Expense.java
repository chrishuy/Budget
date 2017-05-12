package edu.csulb.android.budget;

/**
 * Created by kyo on 5/11/17.
 */

public class Expense {
    private float spent;
    private String item;
    private String today;
    private int id;

    public Expense() {
        this.spent = 0;
        this.item = "";
        this.today = "yyyy/MM/dd HH:mm";
        this.id = 0;
    }

    public Expense(int id, float spent, String item, String today) {
        this.spent = spent;
        this.item = item;
        this.today = today;
        this.id = id;
    }

    public float getSpent() { return spent; }
    public String getItem() { return item; }
    public String getToday() { return today; }
    public int getId() { return id; }
}
