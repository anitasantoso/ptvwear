package as.com.au.ptvwear;

import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_timetable)
public class TimetableActivity extends ActionBarActivity {

    @ViewById(R.id.time_tv)
    TextView timeTextView;

    @AfterViews
    void initView() {


    }
}
