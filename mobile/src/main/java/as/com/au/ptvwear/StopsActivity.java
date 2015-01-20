package as.com.au.ptvwear;

import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import as.com.au.common.model.Stop;
import as.com.au.common.model.TransportType;
import as.com.au.ptvwear.fragment.StopListFragment;
import as.com.au.ptvwear.fragment.StopSearchFragment_;
import as.com.au.ptvwear.network.NetworkService;
import as.com.au.ptvwear.network.ResponseHandler;
import as.com.au.ptvwear.utils.AlertUtils;
import as.com.au.ptvwear.utils.LocationService;
import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_stops)
public class StopsActivity extends BackNavActivity {

    private static final String tabTitles[] = new String[]{"Nearby Stops", "Search"};

    @ViewById(R.id.pager)
    ViewPager pager;

    @AfterViews
    void initViews() {

        final ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        setupTabs(actionBar);
        setupViewPager();
    }

    private void setupTabs(final ActionBar actionBar) {
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction fragmentTransaction) {

            }
        };
        for (int i = 0; i < tabTitles.length; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(tabTitles[i])
                            .setTabListener(tabListener));
        }
    }

    private void setupViewPager() {
        pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        loadNearbyStops();
                        return StopListFragment.newFragmentWithType(StopListUpdatedEvent.ItemType.NEARBY);
                    case 1:
                        return new StopSearchFragment_();
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return tabTitles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return tabTitles[position];
            }
        });

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                getSupportActionBar().setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    private void loadNearbyStops() {

        Location loc = LocationService.getLastKnownLocation();
        if (loc == null) {
            AlertUtils.showError(this, "Could not find user location");
            return;
        }

        // home -37.865300, 144.994785
        // flinders st -37.818178, 144.966880
        NetworkService.getInstance().getNearbyStops(loc.getLatitude(), loc.getLongitude(),
                new ResponseHandler<List<Stop>>() {
                    @Override
                    public void onSuccess(List<Stop> result) {

                        Collections.sort(result, new Comparator<Stop>() {
                            @Override
                            public int compare(Stop lhs, Stop rhs) {
                                return lhs.getTransportType() == TransportType.Train ? -1 :
                                        lhs.getTransportType() == TransportType.Tram ? 0 : 1;
                            }
                        });
                        EventBus.getDefault().post(new StopListUpdatedEvent(result, StopListUpdatedEvent.ItemType.NEARBY));
                    }

                    @Override
                    public void onError(String error) {
                        AlertUtils.showError(StopsActivity.this, error);
                    }
                }, TransportType.Train, TransportType.Tram);
    }

    public static class StopListUpdatedEvent {

        public static enum ItemType { NEARBY, SEARCH_RESULT }

        public List<Stop> stops;
        public ItemType itemType;

        public StopListUpdatedEvent(List<Stop> stops, ItemType itemType) {
            this.stops = stops;
            this.itemType = itemType;
        }

        public static ItemType typeFromIndex(int i) {
            for(ItemType type : ItemType.values()) {
                if(type.ordinal() == i) {
                    return type;
                }
            }
            return null;
        }
    }
}
