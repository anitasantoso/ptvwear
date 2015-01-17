package as.com.au.ptvwear.utils;

import com.google.gson.reflect.TypeToken;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.googlecode.androidannotations.api.Scope;

import java.util.ArrayList;
import java.util.List;

import as.com.au.common.JSONSerializer;
import as.com.au.common.model.FaveStop;
import as.com.au.common.model.Line;
import as.com.au.common.model.Stop;
import as.com.au.ptvwear.prefs.FavePrefs_;

/**
 * Created by Anita on 17/01/2015.
 */
@EBean(scope = Scope.Singleton)
public class FaveMgr {

    @Pref
    FavePrefs_ prefs;
    JSONSerializer<FaveStop> serializer = new JSONSerializer<FaveStop>();

    public void remove(FaveStop fave) {
        List<FaveStop> faves = getFaves();
        faves.remove(fave);
        setFave(faves);
    }

    // TODO check duplicate
    public void add(FaveStop fave) {
        List<FaveStop> faves = getFaves();
        faves.add(fave);
        setFave(faves);
    }

    public void setTitle(String faveId, String title) {
        List<FaveStop> faves = getFaves();
        for(FaveStop fave : faves) {
            if(fave.getFaveId().equals(faveId)) {
                fave.setTitle(title);
            }
        }
        setFave(faves);
    }

    private void setFave(List<FaveStop> faves) {
        String jsonArrStr = serializer.deserialize(faves, new TypeToken<List<FaveStop>>(){}.getType());
        prefs.edit().faveStops().put(jsonArrStr).apply();
    }

    public String favesAsJsonString() {
        return prefs.faveStops().get();
    }

    public List<FaveStop> getFaves() {
        String favesStr = prefs.faveStops().get();
        List<FaveStop> faves = serializer.serialize(favesStr, new TypeToken<List<FaveStop>>(){}.getType());
        if(faves == null) {
            faves = new ArrayList<FaveStop>();
        }
        return faves;
    }

    public boolean isFavourite(Stop stop, Line line) {
        for(FaveStop fave : getFaves()) {
            if(fave.getStop().equals(stop) && fave.getLine().equals(line)) {
                return true;
            }
        }
        return false;
    }

    public FaveStop faveById(String faveId) {
        for(FaveStop fave : getFaves()) {
            if(fave.getFaveId().equals(faveId)) {
                return fave;
            }
        }
        return null;
    }
}
