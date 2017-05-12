package edu.csulb.android.budget;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kyo on 5/8/17.
 */

public class Item {
    private float budget;
    private float remainder;
    private String today;
    private int id;

    public Item() {
        id = 0;
        budget = 0;
        remainder = 0;
        today = "yyyy/MM/dd HH:mm";
    }

    public Item(int id, float remainder, float budget, String today) {
        this.budget = budget;
        this.remainder = remainder;
        this.today = today;
        this.id = id;
    }

    public int getId() { return id; }
    public float getBudget() { return budget; }
    public float getRemainder() { return remainder; }
    public String getToday() { return today; }
}
