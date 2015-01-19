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

/**
 * Created by Anita on 17/01/2015.
 */
class FavouriteListAdapter extends WearableListView.Adapter {
    private List<FaveStop> items;
    private final Context mContext;
    private final LayoutInflater mInflater;

    public FavouriteListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setItems(List<FaveStop> items) {
        this.items = items;
    }

    public FaveStop getStopAt(int position) {
        return items.size() > position? items.get(position) : null;
    }

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

    @Override
    public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        return new ItemViewHolder(mInflater.inflate(R.layout.list_item_fave, null));
    }

    @Override
    public void onBindViewHolder(WearableListView.ViewHolder holder,
                                 int position) {

        int resId;
        String title, desc;
        ItemViewHolder itemHolder = (ItemViewHolder) holder;

        if(items.size() > position) {
            FaveStop fave = items.get(position);

            resId = ResUtil.resIdForTransportType(fave.getStop().getTransportType());
            title = fave.getStop().getLocationName();
            desc = "To " + fave.getLine().getDirectionName();
        } else {
            resId = R.drawable.ic_phone;
            title = "Open on device";
            desc = null;
        }
        itemHolder.imgView.setImageDrawable(mContext.getResources().getDrawable(resId));
        itemHolder.textView.setText(title);

        itemHolder.descTextView.setVisibility(desc == null? View.GONE : View.VISIBLE);
        if(desc != null) {
            itemHolder.descTextView.setText(desc);
        }
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return items.size() /**+ 1**/; // TODO show link
    }
}