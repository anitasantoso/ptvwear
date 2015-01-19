package as.com.au.ptvwear.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.EditText;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

import as.com.au.ptvwear.R;
import as.com.au.ptvwear.StopsActivity;

/**
 * Created by Anita on 19/01/2015.
 */
@EFragment(R.layout.fragment_stop_search)
public class StopSearchFragment extends Fragment {

    @ViewById(R.id.from_text_view)
    EditText fromTextView;

    @ViewById(R.id.to_text_view)
    EditText toTextView;

    @ViewById(R.id.search_button)
    Button searchButton;

    @AfterViews
    void onCreateView() {

        // list view
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.list_view_container, StopListFragment.newFragmentWithType(StopsActivity.StopListUpdatedEvent.ItemType.SEARCH_RESULT));
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}
