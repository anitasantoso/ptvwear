package as.com.au.ptvwear.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import as.com.au.ptvwear.R;
import as.com.au.ptvwear.model.Stop;
import as.com.au.ptvwear.utils.FaveMgr_;

/**
 * Created by Anita on 17/01/2015.
 */
public class StopsListAdapter extends BaseAdapter {

    public interface DatasetChangedDelegate<T> {
        public void itemRemoved(T item);
        public void itemAdded(T item);
    }

    Context context;
    List<Stop> stops;
    DatasetChangedDelegate<Stop> delegate;

    public StopsListAdapter(Context context) {
        this.context = context;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public void setDelegate(DatasetChangedDelegate<Stop> delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getCount() {
        return stops == null? 0 : stops.size();
    }

    @Override
    public Object getItem(int position) {
        return stops.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_stop, null);
        }
        TextView typeTxtView = (TextView)convertView.findViewById(R.id.tv_transport_type);
        TextView locTxtView = (TextView)convertView.findViewById(R.id.tv_location_name);
        TextView suburbTxtView = (TextView)convertView.findViewById(R.id.tv_suburb);
        ImageButton faveBtn = (ImageButton) convertView.findViewById(R.id.button_fave);

        final Stop stop = stops.get(position);
        final boolean isFave = FaveMgr_.getInstance_(context).isFave(stop);
        setFaveBtnDrawable(faveBtn, isFave);

        faveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isFave) {
                    FaveMgr_.getInstance_(context).remove(stop);
                    if(delegate != null) {
                        delegate.itemRemoved(stop);
                    }
                } else {
                    FaveMgr_.getInstance_(context).add(stop);
                    if(delegate != null) {
                        delegate.itemAdded(stop);
                    }
                }
                setFaveBtnDrawable((ImageButton)v, !isFave);
            }
        });
        locTxtView.setText(stop.getLocationName());
        suburbTxtView.setText(stop.getSuburb());
        typeTxtView.setText(stop.getTransportType().toString());

        return convertView;
    }

    private void setFaveBtnDrawable(ImageButton b, boolean isFave) {
        b.setImageDrawable(context.getResources()
                .getDrawable(isFave ? R.drawable.ic_fave_on : R.drawable.ic_fave_off));
    }
}
