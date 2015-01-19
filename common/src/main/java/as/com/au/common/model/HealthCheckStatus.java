package as.com.au.common.model;

/**
 * Created by Anita on 19/01/2015.
 */
public class HealthCheckStatus {

    public boolean securityTokenOK;
    public boolean clientClockOK;
    public boolean memcacheOK;
    public boolean databaseOK;

    // if any of these is false
    public boolean hasError() {
        return !securityTokenOK /**|| clientClockOK **/|| !memcacheOK || !databaseOK;
    }

    public String getError() {
        String errorMsg;
        if (!securityTokenOK) {
            errorMsg = "Invalid security token";
        }
        // TODO why is this false?
        /**
         else if(!clientClockOK) {
         errorMsg = "Time is not synchronised";
         }
         **/
        else if (!memcacheOK || !databaseOK) {
            errorMsg = "Server is unavailable";
        } else {
            errorMsg = "Unknown error";
        }
        return errorMsg;
    }
}
