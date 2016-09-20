package app.linde.com.customlibrary.model.client.interfaces;

import okhttp3.Request;

/**
 * Created by Administrator on 2016-09-13 0013.
 */

interface HttpClientCallback<T>
{
    void onBefore(Request request);

    void onComplete();

    void onNetDataError(Exception e);

    void onResponseError(int responseCode);

    void onResponse(T response);
}
