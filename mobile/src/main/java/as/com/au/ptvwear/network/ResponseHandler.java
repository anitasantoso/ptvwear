package as.com.au.ptvwear.network;

/**
 * Created by Anita on 16/01/2015.
 */
public interface ResponseHandler<T> {

    public void onSuccess(T result);
    public void onError(String error);
}
