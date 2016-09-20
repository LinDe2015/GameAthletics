package app.linde.com.customlibrary.model.client.interfaces;

import java.io.IOException;

/**
 * Created by Administrator on 2016-09-13 0013.
 */

public class HttpRunnable
{
    public static class NetworkRunnable implements Runnable
    {
        private final HttpClientCallback<?> mHttpClientCallback;
        private final IOException mIOException;

        public NetworkRunnable(HttpClientCallback<?> httpClientCallback, IOException ioException)
        {
            mHttpClientCallback = httpClientCallback;
            mIOException = ioException;
        }

        @Override
        public void run()
        {
            if (mHttpClientCallback == null)
                return;
            try
            {
                mHttpClientCallback.onNetDataError(mIOException);
            } catch (Exception e1)
            {
                e1.printStackTrace();
            }
            try
            {
                mHttpClientCallback.onComplete();
            } catch (Exception e1)
            {
                e1.printStackTrace();
            }
        }
    }

    public static class ResponseErrorRunnable implements Runnable
    {
        private final HttpClientCallback<?> mHttpClientCallback;
        private final int mResponseCode;

        public ResponseErrorRunnable(HttpClientCallback<?> httpClientCallback, int responseCode)
        {
            mHttpClientCallback = httpClientCallback;
            mResponseCode = responseCode;
        }

        @Override
        public void run()
        {
            if (mHttpClientCallback == null)
                return;
            try
            {
                mHttpClientCallback.onResponseError(mResponseCode);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            try
            {
                mHttpClientCallback.onComplete();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static class ResponseRunnable implements Runnable
    {
        private final HttpClientStringBack mHttpClientCallback;
        private final String mResponse;
        private final Exception mException;

        public ResponseRunnable(HttpClientStringBack httpClientCallback, String response, Exception exception)
        {
            mHttpClientCallback = httpClientCallback;
            mResponse = response;
            mException = exception;
        }

        @Override
        public void run()
        {
            if (mException != null)
            {
                try
                {
                    mHttpClientCallback.onNetDataError(mException);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            } else
            {
                try
                {
                    mHttpClientCallback.onResponse(mResponse);
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            try
            {
                mHttpClientCallback.onComplete();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
