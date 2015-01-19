package as.com.au.ptvwear;

import as.com.au.common.model.TransportType;

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
        /**
         * switch (type) {
         case Train:
         resId = R.drawable.ic_train;
         break;
         case Tram:
         resId = R.drawable.ic_tram;
         break;
         case Bus:
         resId = R.drawable.ic_bus;
         break;
         default:
         resId = R.drawable.ic_train;
         }
         */
        throw new UnsupportedOperationException("Not implemented");
    }
}
