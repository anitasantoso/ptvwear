package as.com.au.ptvwear;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import as.com.au.common.model.Stop;

/**
 * Created by Anita on 17/01/2015.
 */
class FavouriteListAdapter extends WearableListView.Adapter {
    List<Stop> items;
    private final Context mContext;
    private final LayoutInflater mInflater;

    public FavouriteListAdapter(Context context, List<Stop> items) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.items = items;
    }

    public void setItems(List<Stop> items) {
        this.items = items;
    }

    public List<Stop> getItems() {
        return items;
    }

    // Provide a reference to the type of views you're using
    static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView textView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }

    // Create new views for list items
    // (invoked by the WearableListView's layout manager)
    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // Inflate our custom layout for list items
        return new ItemViewHolder(mInflater.inflate(R.layout.list_item_fave, null));
    }

    // Replace the contents of a list item
    // Instead of creating new views, the list tries to recycle existing ones
    // (invoked by the WearableListView's layout manager)
    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder,
                                 int position) {

        ItemViewHolder itemHolder = (ItemViewHolder) holder;
        TextView view = itemHolder.textView;

        Stop stop = items.get(position);
        view.setText(stop.getLocationName());

        holder.itemView.setTag(position);
    }

    // Return the size of your dataset
    // (invoked by the WearableListView's layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
}