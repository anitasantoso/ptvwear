package as.com.au.ptvwear;

import android.content.Context;
import android.support.wearable.view.WearableListView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import as.com.au.common.model.FaveStop;
import as.com.au.common.model.TransportType;

/**
 * Created by Anita on 17/01/2015.
 */
class FavouriteListAdapter extends WearableListView.Adapter {
    List<FaveStop> items;
    private final Context mContext;
    private final LayoutInflater mInflater;

    public FavouriteListAdapter(Context context, List<FaveStop> items) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        this.items = items;
    }

    public void setItems(List<FaveStop> items) {
        this.items = items;
    }

    public List<FaveStop> getItems() {
        return items;
    }

    // Provide a reference to the type of views you're using
    static class ItemViewHolder extends WearableListView.ViewHolder {
        private TextView textView, descTextView;
        private ImageView imgView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
            descTextView = (TextView) itemView.findViewById(R.id.desc_text_view);
            imgView = (ImageView) itemView.findViewById(R.id.icon_img_view);
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

        FaveStop fave = items.get(position);
        TransportType type = fave.getStop().getTransportType();

        int resId;
        switch (type) {
            case Train:
                resId = R.drawable.ic_train;
                break;
            case Tram:
                resId = R.drawable.ic_tram;
                break;
            case Bus:
                resId = R.drawable.ic_bus;
                break;
            default:
                resId = R.drawable.ic_train;
        }
        itemHolder.imgView.setImageDrawable(mContext.getResources().getDrawable(resId));
        itemHolder.textView.setText(fave.getStop().getLocationName());
        itemHolder.descTextView.setText("To " + fave.getLine().getDirectionName());

        holder.itemView.setTag(position);
    }

    // Return the size of your dataset
    // (invoked by the WearableListView's layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
}