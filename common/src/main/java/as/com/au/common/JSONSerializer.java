package as.com.au.common;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Anita on 17/01/2015.
 */
public class JSONSerializer<T> {

    public String deserialize(List<T> list, final Type type) {
        Gson gson = new Gson();
        JsonElement elm = gson.toJsonTree(list, type);
        if(elm.getAsJsonArray() != null) {
            return elm.getAsJsonArray().toString();
        }
        return null;
    }

    public List<T> serialize(String jsonStr, final Type type) {
        List<T> faves = null;
        if(!jsonStr.isEmpty()) {
            Gson gson = new Gson();
            faves = gson.fromJson(jsonStr, type);
        }
        return faves;
    }
}
