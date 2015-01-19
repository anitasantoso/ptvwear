package as.com.au.ptvwear;

import android.content.Context;
import android.support.wearable.view.GridPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import as.com.au.common.model.Departure;

/**
 * Created by Anita on 17/01/2015.
 */
public class TimetablePagerAdapter extends GridPagerAdapter {

    private final Context mContext;
    List<Departure> items;

    public TimetablePagerAdapter(Context ctx) {
        super();
        mContext = ctx;
    }

    public void setItems(List<Departure> items) {
        this.items = items;
    }

    @Override
    protected Object instantiateItem(ViewGroup container, int row, int col) {

        final View view = LayoutInflater.from(mContext).inflate(R.layout.view_timetable_fullscreen, container, false);
        String title, description;

        if(items != null && items.size() > row) {
            Departure dep = items.get(row);
            DateTime time = dep.getTime();

            // TODO if rolls over the next day
            int duration = Minutes.minutesBetween(new DateTime(), time).getMinutes();
            description = duration > 0? String.format("(in %d mins)", duration) : "(now)";

            // current day
            // DateTimeFormat.forPattern("EEEE").print(time))

            title = DateTimeFormat.forPattern("h:mm a").print(time);
        } else {
            title = "Loading...";
            description = "";
        }

        // TODO show hide up and down arrow
        final TextView timeTextView = (TextView) view.findViewById(R.id.time_tv);
        final TextView durationTextView = (TextView) view.findViewById(R.id.duration_tv);

        timeTextView.setText(title);
        durationTextView.setText(description);

        container.addView(view);
        return view;
    }

    @Override
    protected void destroyItem(ViewGroup container, int row, int col, Object view) {
        container.removeView((View)view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public int getRowCount() {
        // this is a vertical 2D cards
        return items != null? items.size() : 1;
    }

    @Override
    public int getColumnCount(int rowNum) {
        return 1;
    }
}

