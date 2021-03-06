package edu.csulb.android.budget;

/**
 * This class is used to calculate the weekly budget
 */
public class WeeklyCalculator {
    public static float weekBudget(float income, float bills, float grocery, float save) {
        float result, savings, weekIncome;

        //Gives saving percentage
        savings = save/100;

        //Gives weekly income adjusted from bills and groceries and savings percentage
        weekIncome = income - bills;
        weekIncome = weekIncome* (1-savings) / 4;

        //Budget for each week
        result = weekIncome - grocery;
        return (float)(Math.round(result * 100.0) / 100.0);
    }
}
