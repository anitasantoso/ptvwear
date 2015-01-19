package as.com.au.ptvwear;

import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import java.util.List;

import as.com.au.common.model.Departure;
import as.com.au.common.model.FaveStop;
import as.com.au.ptvwear.service.NetworkService;
import as.com.au.ptvwear.service.ResponseHandler;
import as.com.au.ptvwear.utils.AlertUtils;
import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_timetable)
public class TimetableActivity extends ActionBarActivity {

    @ViewById(R.id.time_tv)
    TextView timeTextView;

    @ViewById(R.id.desc_tv)
    TextView descTextView;

    @AfterInject
    void init() {
        //EventBus.getDefault().registerSticky(this);
    }

    @AfterViews
    void initView() {
        FaveStop fave = EventBus.getDefault().getStickyEvent(FaveStop.class);
        if (fave != null) {

            NetworkService.getInstance().getNextDeparture(fave.getStop(),
                    fave.getLine().getLineId(),
                    fave.getLine().getDirectionId(),

                    new ResponseHandler<List<Departure>>() {

                        @Override
                        public void onSuccess(List<Departure> result) {
                            if (result != null && !result.isEmpty()) {
                                Departure dep = result.get(0);

                            }
                        }

                        @Override
                        public void onError(String error) {
                            AlertUtils.showError(TimetableActivity.this, error);
                        }
                    });
        }
    }

    public void onDestroy() {
        super.onDestroy();
        //EventBus.getDefault().unregister(this);
    }
}
