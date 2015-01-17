package as.com.au.ptvwear;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.view.Gravity;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import as.com.au.common.model.Departure;

/**
 * Created by Anita on 17/01/2015.
 */
public class GridPagerAdapter extends FragmentGridPagerAdapter {

    private final Context mContext;
    List<Departure> items;

    public GridPagerAdapter(Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;
    }

    public void setItems(List<Departure> items) {
        this.items = items;
    }

    @Override
    public Fragment getFragment(int row, int col) {

        String title = null, description = null;
        if(items != null && items.size() > col) {
            Departure dep = items.get(col);
            DateTime time = dep.getTime();

            // TODO if more than a day
            Minutes waitTimeInMins = Minutes.minutesBetween(new DateTime(), time);

            title = String.format("Next %s", dep.getStop().getTransportType().toString());

            StringBuffer desc = new StringBuffer();
            desc.append(DateTimeFormat.forPattern("EEEE").print(time))
                    .append(" ")
                    .append(DateTimeFormat.forPattern("h:mm a").print(time))
                    .append("\n")
                    .append(String.format("(in %s mins)",
                            String.valueOf(waitTimeInMins.getMinutes())));
            description = desc.toString();
        } else {
            title = "Next";
            description = "Please wait...";
        }

        CardFragment fragment = CardFragment.create(title, description, R.drawable.ic_launcher);
        fragment.setCardGravity(Gravity.BOTTOM);
        fragment.setExpansionEnabled(false);
        return fragment;
    }

    @Override
    public android.graphics.drawable.Drawable getBackgroundForPage(int row, int column) {
        return mContext.getResources().getDrawable(R.drawable.background1);
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount(int rowNum) {
        return items != null? items.size() : 1;
    }
}

