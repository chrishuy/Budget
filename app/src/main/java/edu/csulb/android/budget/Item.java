package edu.csulb.android.budget;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kyo on 5/8/17.
 */

public class Item {
    private float income;
    private float saving;
    private float grocery;
    private float bill;
    private float budget;
    private String today;

    public Item() {
        income = 0;
        saving = 0;
        grocery = 0;
        bill = 0;
        budget = 0;
        today = "yyyy/MM/dd HH:mm";
    }

    public Item(float income, float saving, float grocery, float bill, float budget, String today) {
        this.income = income;
        this.saving = saving;
        this.grocery = grocery;
        this.bill = bill;
        this.budget = budget;
        this.today = today;
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
//        this.today = dateFormat.format(new Date());
    }

    public float getIncome() { return income; }
    public float getSaving() { return saving; }
    public float getGrocery() { return grocery; }
    public float getBill() { return bill; }
    public float getBudget() { return budget; }
    public String getToday() { return today; }
}