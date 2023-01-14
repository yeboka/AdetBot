package kz.adet;

public class TimeEvaluateService {

    private int needTimeHour;
    private int needTimeMinute;

    TimeEvaluateService (int needTimeHour, int needTimeMinute) {
        this.needTimeHour = needTimeHour;
        this.needTimeMinute = needTimeMinute;
    }

    public long getMillisToSleep (long currTimeInMillis) {
        long needTimeInMillis = (long) needTimeHour * 60 * 60 * 1000 + (long) needTimeMinute * 60 * 1000;

        long diff = Math.abs(needTimeInMillis - currTimeInMillis);


        if (currTimeInMillis > needTimeInMillis)
            return 24 * 60 * 60 * 1000 - diff;            //from all millis in a day subtract difference
        else
            return diff;
    }

    public int getNeedTimeHour() {
        return needTimeHour;
    }

    public int getNeedTimeMinute() {
        return needTimeMinute;
    }

    public void setNeedTimeHour(int needTimeHour) {
        this.needTimeHour = needTimeHour;
    }

    public void setNeedTimeMinute(int needTimeMinute) {
        this.needTimeMinute = needTimeMinute;
    }
}
