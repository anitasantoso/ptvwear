package as.com.au.ptvwear.utils;

import as.com.au.common.model.TransportType;
import as.com.au.ptvwear.R;

/**
 * Created by Anita on 19/01/2015.
 */
public class ResUtil {

    public static int resIdForTransportType(TransportType type) {
        if(type == TransportType.Train) {
            return R.drawable.ic_train;
        } else if(type == TransportType.Tram) {
            return R.drawable.ic_tram;
        }
        throw new UnsupportedOperationException("Not implemented");
    }
}
