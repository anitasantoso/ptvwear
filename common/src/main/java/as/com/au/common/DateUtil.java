package as.com.au.common;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by Anita on 20/01/2015.
 */
public class DateUtil {

    public static String formatToTime(DateTime dt) {
        return DateTimeFormat.forPattern("h:mm a").print(dt);
    }

    public static int getDifferenceInMins(DateTime dt, DateTime other) {
        Minutes waitTimeInMins = Minutes.minutesBetween(dt, other);
        return Math.abs(waitTimeInMins.getMinutes());
    }

    public static int getMinsToNow(DateTime dt) {
        return getDifferenceInMins(dt, new DateTime());
    }
}
