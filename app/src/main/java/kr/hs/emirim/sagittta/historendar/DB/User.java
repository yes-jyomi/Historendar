package kr.hs.emirim.sagittta.historendar.DB;

public class User {
    public int NUM;  // num, pk
    public String EVENT; //
    public String DAY01; //
    public String DAY02; //


    public void setNUM(int num) {
        this.NUM = num;
    }

    public int getNUM() {
        return NUM;
    }

    public void setEVENT(String event) {
        this.EVENT = event;
    }

    public String getEVENT() {
        return EVENT;
    }

    public void setDAY01(String day01) {
        this.DAY01 = day01;
    }

    public String getDAY01() {
        return DAY01;
    }

    public void setDAY02(String day02) {
        this.DAY02 = day02;
    }

    public String getDAY02() {
        return DAY02;
    }
}
