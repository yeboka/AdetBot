package kz.adet;

//FIXME write tests for this class
public class TimeEvaluateService {

    private final int needTimeHour;
    private final int needTimeMinute;

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
}
