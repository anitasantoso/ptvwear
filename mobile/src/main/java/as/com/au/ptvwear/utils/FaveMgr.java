package as.com.au.ptvwear.utils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.sharedpreferences.Pref;
import com.googlecode.androidannotations.api.Scope;

import java.util.ArrayList;
import java.util.List;

import as.com.au.ptvwear.model.Stop;
import as.com.au.ptvwear.prefs.FavePrefs_;

/**
 * Created by Anita on 17/01/2015.
 */
@EBean(scope = Scope.Singleton)
public class FaveMgr {

    @Pref
    FavePrefs_ prefs;

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
        Gson gson = new Gson();
        JsonElement elm = gson.toJsonTree(faves, new TypeToken<List<Stop>>(){}.getType());
        String jsonArrStr = elm.getAsJsonArray().toString();

        prefs.edit().faveStops().put(jsonArrStr).apply();
    }

    public List<Stop> getFaves() {
        String favesStr = prefs.faveStops().get();
        List<Stop> faves = new ArrayList<Stop>();
        if(!favesStr.isEmpty()) {
            Gson gson = new Gson();
            faves = gson.fromJson(favesStr, new TypeToken<List<Stop>>(){}.getType());
        }
        return faves;
    }
}
