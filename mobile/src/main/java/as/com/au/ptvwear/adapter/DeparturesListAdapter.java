package as.com.au.ptvwear.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import as.com.au.common.model.Departure;
import as.com.au.ptvwear.R;

/**
 * Created by Anita on 17/01/2015.
 */
public class DeparturesListAdapter extends BaseAdapter {
    Context context;
    List<Departure> items;

    public DeparturesListAdapter(Context context) {
        this.context = context;
    }

    public void setItems(List<Departure> items) {
        this.items = items;
    }

    @Override
    public int getCount() {
        return items != null? items.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_departure, null);
        }
        Departure dep = items.get(position);
        TextView dirNameTextView = (TextView)convertView.findViewById(R.id.direction_name_tv);
        TextView lineTextView = (TextView)convertView.findViewById(R.id.line_name_tv);

        lineTextView.setText(dep.getLine().getLineName());
        dirNameTextView.setText(dep.getLine().getDirectionName());

        return convertView;
    }
}