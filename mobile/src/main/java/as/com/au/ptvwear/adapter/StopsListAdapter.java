package as.com.au.ptvwear.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import as.com.au.common.model.Stop;
import as.com.au.ptvwear.R;

/**
 * Created by Anita on 17/01/2015.
 */
public class StopsListAdapter extends BaseAdapter {

    Context context;
    List<Stop> stops;

    public StopsListAdapter(Context context) {
        this.context = context;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_stop_chevron, null);
        }
        TextView typeTxtView = (TextView)convertView.findViewById(R.id.tv_transport_type);
        TextView locTxtView = (TextView)convertView.findViewById(R.id.tv_location_name);
        TextView suburbTxtView = (TextView)convertView.findViewById(R.id.tv_suburb);

        final Stop stop = stops.get(position);

        locTxtView.setText(stop.getLocationName());
        suburbTxtView.setText(stop.getSuburb());
        typeTxtView.setText(stop.getTransportType().toString());

        return convertView;
    }
}
