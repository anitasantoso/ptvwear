package as.com.au.ptvwear.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import as.com.au.common.model.Stop;
import as.com.au.ptvwear.R;
import as.com.au.ptvwear.utils.FaveMgr_;

/**
 * Created by Anita on 17/01/2015.
 */
public class FaveStopsListAdapter extends StopsListAdapter {

    public interface DatasetChangedDelegate<T> {
        public void itemRemoved(T item);
        public void itemAdded(T item);
    }

    DatasetChangedDelegate<Stop> delegate;

    public FaveStopsListAdapter(Context context) {
        super(context);
    }

    public void setDelegate(DatasetChangedDelegate<Stop> delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getListItemResource() {
        return R.layout.list_item_stop_fave;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = super.getView(position, convertView, parent);

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

        return convertView;
    }

    private void setFaveBtnDrawable(ImageButton b, boolean isFave) {
        b.setImageDrawable(context.getResources()
                .getDrawable(isFave ? R.drawable.ic_fave_on : R.drawable.ic_fave_off));
    }
}
