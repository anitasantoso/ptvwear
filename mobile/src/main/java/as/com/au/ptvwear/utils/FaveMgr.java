package as.com.au.ptvwear.utils;

import com.google.gson.reflect.TypeToken;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.googlecode.androidannotations.api.Scope;

import java.util.List;

import as.com.au.common.JSONSerializer;
import as.com.au.common.model.Stop;
import as.com.au.ptvwear.prefs.FavePrefs_;

/**
 * Created by Anita on 17/01/2015.
 */
@EBean(scope = Scope.Singleton)
public class FaveMgr {

    @Pref
    FavePrefs_ prefs;
    JSONSerializer<Stop> serializer = new JSONSerializer<Stop>();

    public void remove(Stop stop) {
        List<Stop> faves = getFaves();
        faves.remove(stop);
        setFave(faves);
    }

    public void add(Stop stop) {
        List<Stop> faves = getFaves();
        faves.add(stop);
        setFave(faves);
    }

    public boolean isFave(Stop stop) {
        return getFaves().contains(stop);
    }

    private void setFave(List<Stop> faves) {
        String jsonArrStr = serializer.deserialize(faves, new TypeToken<List<Stop>>(){}.getType());
        prefs.edit().faveStops().put(jsonArrStr).apply();
    }

    public String favesAsJsonString() {
        return prefs.faveStops().get();
    }

    public List<Stop> getFaves() {
        String favesStr = prefs.faveStops().get();
        return serializer.serialize(favesStr, new TypeToken<List<Stop>>(){}.getType());
    }

    public Stop stopById(int stopId) {
        for(Stop stop : getFaves()) {
            if(stop.getStopId() == stopId) {
                return stop;
            }
        }
        return null;
    }
}
