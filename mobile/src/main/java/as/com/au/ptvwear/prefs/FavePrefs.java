package as.com.au.ptvwear.prefs;

import com.googlecode.androidannotations.annotations.sharedpreferences.DefaultString;
import com.googlecode.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created by Anita on 17/01/2015.
 */
@SharedPref(SharedPref.Scope.UNIQUE)
public interface FavePrefs {

    @DefaultString("")
    String faveStops();
}
