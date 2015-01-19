package as.com.au.ptvwear.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import as.com.au.common.model.FaveStop;
import as.com.au.common.model.Line;
import as.com.au.common.model.Stop;
import as.com.au.ptvwear.R;
import as.com.au.ptvwear.utils.FaveMgr;
import as.com.au.ptvwear.utils.FaveMgr_;
import as.com.au.ptvwear.utils.ResUtil;

/**
 * Created by Anita on 17/01/2015.
 */
public class FaveStopsListAdapter extends BaseAdapter {

    public interface DatasetChangedDelegate<T> {
        public void itemRemoved(T item);

        public void itemAdded(T item);
    }

    DatasetChangedDelegate<Stop> delegate;
    Context context;
    List<FaveStop> items;
    FaveMgr faveMgr;

    public FaveStopsListAdapter(Context context) {
        this.context = context;
        faveMgr = FaveMgr_.getInstance_(context);
    }

    public void setDelegate(DatasetChangedDelegate<Stop> delegate) {
        this.delegate = delegate;
    }

    public void setItems(List<FaveStop> stops) {
        this.items = stops;
    }

    @Override
    public int getCount() {
        return items != null ? items.size() : 0;
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
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_stop_fave, null);
        }
        ImageView iconImgView = (ImageView) convertView.findViewById(R.id.type_icon_img_view);
        TextView typeTxtView = (TextView) convertView.findViewById(R.id.tv_transport_type);
        TextView locTxtView = (TextView) convertView.findViewById(R.id.tv_location_name);
        TextView suburbTxtView = (TextView) convertView.findViewById(R.id.tv_suburb);

        final FaveStop fave = items.get(position);
        final Stop stop = fave.getStop();
        final Line line = fave.getLine();

        iconImgView.setImageDrawable(context.getResources().getDrawable(ResUtil.resIdForTransportType(stop.getTransportType())));
        locTxtView.setText(stop.getLocationName());
        suburbTxtView.setText("To " + line.getDirectionName());

//        typeTxtView.setText(stop.getTransportType().toString());
        typeTxtView.setVisibility(View.GONE);

        ImageButton faveBtn = (ImageButton) convertView.findViewById(R.id.button_fave);
        ImageButton editBtn = (ImageButton) convertView.findViewById(R.id.button_edit);

        // set title
        TextView titleTextView = (TextView) convertView.findViewById(R.id.title_tv);
        String title = fave.getTitle();

        if (title == null) {
            title = stop.getTransportType().toString();
        }
        titleTextView.setText(title);

        setFaveBtnDrawable(faveBtn, true);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogPrompt(fave);
            }
        });
        faveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faveMgr.remove(fave);
                reloadData();
            }
        });

        return convertView;
    }

    private void reloadData() {
        // reload
        setItems(faveMgr.getFaves());
        notifyDataSetChanged();
    }

    private void showDialogPrompt(final FaveStop fave) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle("Edit");
        alert.setMessage("Enter summary for this stop e.g. train to city");

        final EditText input = new EditText(context);
        input.setText(fave.getTitle());
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                faveMgr.setTitle(fave.getFaveId(), value);

                reloadData();
            }
        });

        alert.setNegativeButton("Cancel", null);
        alert.show();
    }

    private void setFaveBtnDrawable(ImageButton b, boolean isFave) {
        b.setImageDrawable(context.getResources()
                .getDrawable(isFave ? R.drawable.ic_fave_on : R.drawable.ic_fave_off));
    }
}
