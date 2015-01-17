package as.com.au.ptvwear.utils;

import com.google.gson.reflect.TypeToken;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.googlecode.androidannotations.api.Scope;

import java.util.List;

import as.com.au.common.JSONSerializer;
import as.com.au.common.model.FaveStop;
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

    public void add(FaveStop fave) {
        List<FaveStop> faves = getFaves();
        faves.add(fave);
        setFave(faves);
    }

//    public boolean isFave(FaveStop stop) {
//        return getFaves().contains(stop);
//    }

    private void setFave(List<FaveStop> faves) {
        String jsonArrStr = serializer.deserialize(faves, new TypeToken<List<FaveStop>>(){}.getType());
        prefs.edit().faveStops().put(jsonArrStr).apply();
    }

    public String favesAsJsonString() {
        return prefs.faveStops().get();
    }

    public List<FaveStop> getFaves() {
        String favesStr = prefs.faveStops().get();
        return serializer.serialize(favesStr, new TypeToken<List<FaveStop>>(){}.getType());
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
