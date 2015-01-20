package as.com.au.ptvwear;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import java.util.List;

import as.com.au.common.DateUtil;
import as.com.au.common.model.Departure;
import as.com.au.common.model.FaveStop;
import as.com.au.ptvwear.network.NetworkService;
import as.com.au.ptvwear.network.ResponseHandler;
import as.com.au.ptvwear.utils.AlertUtils;
import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_timetable)
public class TimetableActivity extends ActionBarActivity {

    @ViewById(R.id.time_tv)
    TextView timeTextView;

    @ViewById(R.id.desc_tv)
    TextView descTextView;

    @ViewById(R.id.timetable_list_view)
    ListView timetableListView;

    TimetableListAdapter listAdapter;

    @AfterInject
    void init() {
        //EventBus.getDefault().registerSticky(this);
    }

    @AfterViews
    void initView() {

        timetableListView.setAdapter(listAdapter = new TimetableListAdapter());
        FaveStop fave = EventBus.getDefault().getStickyEvent(FaveStop.class);
        if (fave != null) {

            // get next 5 departures
            NetworkService.getInstance().getNextDeparture(fave.getStop(),
                    fave.getLine().getLineId(),
                    fave.getLine().getDirectionId(), 5,

                    new ResponseHandler<List<Departure>>() {

                        @Override
                        public void onSuccess(List<Departure> result) {

                            if(!result.isEmpty() && result.size() > 1) {
                                setNextDeparture(result.get(0));

                                listAdapter.setItems(result.subList(1, result.size()-1));
                                listAdapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onError(String error) {
                            AlertUtils.showError(TimetableActivity.this, error);
                        }
                    });
        }
    }

    private void setNextDeparture(Departure dep) {
        timeTextView.setText(DateUtil.formatToTime(dep.getTime()));
        descTextView.setText(String.format("(in %d mins)", DateUtil.getMinsToNow(dep.getTime())));
    }

    public void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }

    class TimetableListAdapter extends BaseAdapter {

        List<Departure> departures;

        public void setItems(List<Departure> departures) {
            this.departures = departures;
        }

        @Override
        public int getCount() {
            return departures != null? departures.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return departures.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_timetable, null);
            }
            TextView timeTextView = (TextView)convertView.findViewById(R.id.time_tv);
            TextView durationTextView = (TextView)convertView.findViewById(R.id.duration_tv);

            Departure dep = departures.get(position);
            timeTextView.setText(DateUtil.formatToTime(dep.getTime()));
            durationTextView.setText(String.format("(in %d mins)", DateUtil.getMinsToNow(dep.getTime())));
            return convertView;
        }
    }
}
