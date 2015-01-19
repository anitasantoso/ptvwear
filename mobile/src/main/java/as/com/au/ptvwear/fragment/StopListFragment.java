package as.com.au.ptvwear.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.FragmentArg;
import com.googlecode.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import as.com.au.common.model.Stop;
import as.com.au.ptvwear.R;
import as.com.au.ptvwear.StopDetailsActivity_;
import as.com.au.ptvwear.StopsActivity;
import as.com.au.ptvwear.adapter.StopsListAdapter;
import de.greenrobot.event.EventBus;

/**
 * Created by Anita on 19/01/2015.
 */

@EFragment(R.layout.fragment_stop_list)
public class StopListFragment extends Fragment {

    @ViewById(R.id.stops_list_view)
    ListView stopsListView;

    @FragmentArg
    int itemTypeIndex;

    StopsActivity.StopListUpdatedEvent.ItemType itemType;

    StopsListAdapter listAdapter;

    public static StopListFragment newFragmentWithType(StopsActivity.StopListUpdatedEvent.ItemType type) {
        StopListFragment frag = new StopListFragment_();
        Bundle b = new Bundle();
        b.putInt("itemTypeIndex", type.ordinal());
        frag.setArguments(b);
        return frag;
    }

    @AfterViews
    void initView() {

        itemType = StopsActivity.StopListUpdatedEvent.typeFromIndex(itemTypeIndex);
        stopsListView.setAdapter(listAdapter = new StopsListAdapter(getActivity()));
        stopsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // pass Stop object
                EventBus.getDefault().postSticky(listAdapter.getItem(position));

                Intent intent = new Intent(getActivity(), StopDetailsActivity_.class);
                startActivity(intent);
            }
        });

        // get data
        List<Stop> stops = EventBus.getDefault().getStickyEvent(ArrayList.class);
        EventBus.getDefault().removeStickyEvent(ArrayList.class);

        if (stops != null) {
            updateListView(stops);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        // TODO show spinner here while waiting for data
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    private void updateListView(List<Stop> stops) {
        listAdapter.setStops(stops);
        listAdapter.notifyDataSetChanged();
    }

    public void onEventMainThread(StopsActivity.StopListUpdatedEvent event) {
        if (itemType == event.itemType) {
            updateListView(event.stops);
        }
    }
}
