package traindge.masandroidproject.models;

/**
 * Created by xaidi on 19-05-2017.
 */

public class Month {
    String month;
    int presents;
    int absents;
    int leaves;

    public String getMonth() {
        return month;
    }

    public int getPresents() {
        return presents;
    }

    public int getAbsents() {
        return absents;
    }

    public int getLeaves() {
        return leaves;
    }

    public Month(String month, int presents, int absents, int leaves) {

        this.month = month;
        this.presents = presents;
        this.absents = absents;
        this.leaves = leaves;
    }
}
