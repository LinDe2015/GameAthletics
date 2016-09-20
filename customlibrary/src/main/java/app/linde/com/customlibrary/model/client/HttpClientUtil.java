package app.linde.com.customlibrary.model.client;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import app.linde.com.customlibrary.model.client.cookie.CookieJarImpl;
import app.linde.com.customlibrary.model.client.cookie.restore.PersistentCookieStore;
import app.linde.com.customlibrary.model.client.interfaces.HttpClientStringBack;
import app.linde.com.customlibrary.model.client.interfaces.HttpRunnable;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by LinDe on 2016-09-12 0012.
 * {@link OkHttpClient}
 * 对OkHttp进行二次封装
 * <p/>
 * {@link #init(Context, String)} 初始化
 * {@link #cancelTag(Object)} 取消指定标签的请求
 * {@link #sendGetRequest(String, String, int, Map, HttpClientStringBack)} GET请求
 * {@link #sendPostRequest(String, String, Map, Map, HttpClientStringBack)} POST请求
 */
public final class HttpClientUtil
{
    private static OkHttpClient sOkHttpClient;
    private static Handler sHandler;

    private HttpClientUtil()
    {
    }

    /**
     * 初始化
     *
     * @param applicationContext 上下文
     * @param cacheFileDirectory 缓存目录
     */
    public static void init(@NonNull Context applicationContext, @NonNull String cacheFileDirectory)
    {
        sHandler = new Handler(Looper.getMainLooper());

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        /**
         * 超时设置
         */
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        /**
         * 加载失败后取消重试
         */
        builder.retryOnConnectionFailure(true);
        /**
         * 添加cookie存储
         */
        builder.cookieJar(new CookieJarImpl(new PersistentCookieStore(applicationContext)));
        /**
         * 设置缓存目录
         */
        File file = new File(cacheFileDirectory);
        if (!file.exists())
        {
            //noinspection ResultOfMethodCallIgnored
            file.mkdirs();
        }
        builder.cache(new Cache(file, 100L * 1000 * 1000));
        /**
         * ??
         */
        builder.hostnameVerifier(new HostnameVerifier()
        {
            @Override
            public boolean verify(String hostname, SSLSession session)
            {
                return true;
            }
        });
        sOkHttpClient = builder.build();
    }

    /**
     * GET 请求
     *
     * @param tag              标签
     * @param url              请求地址
     * @param maxAge           缓存时间（秒）
     * @param headerParams     请求头部参数，可为空
     * @param responseCallBack 请求回调
     */
    public static void sendGetRequest(String tag, String url, int maxAge, Map<String, String> headerParams, HttpClientStringBack responseCallBack)
    {
        Request.Builder builder = _createRequestBuilder(tag, url, maxAge, headerParams);
        Request request = builder.get().build();
        _handlerRequest(request, responseCallBack);
    }

    /**
     * POST 请求
     *
     * @param tag              标签
     * @param url              请求地址
     * @param headerParams     请求头部参数，可为空
     * @param postParams       请求POST参数
     * @param responseCallBack 请求回调
     */
    public static void sendPostRequest(String tag, String url, Map<String, String> headerParams, Map<String, String> postParams, final HttpClientStringBack responseCallBack)
    {
        Request.Builder builder = _createRequestBuilder(tag, url, 0, headerParams);
        Request request = builder.post(_createRequestBody(postParams)).build();
        _handlerRequest(request, responseCallBack);
    }

    /**
     * 取消Tag标记的请求
     */
    public static void cancelTag(Object tag)
    {
//        KLog.e("取消请求Tag:" + tag);
        for (Call call : sOkHttpClient.dispatcher().queuedCalls())
        {
//            KLog.e("Tag:" + call.request().tag());
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
        for (Call call : sOkHttpClient.dispatcher().runningCalls())
        {
//            KLog.e("Tag:" + call.request().tag());
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
    }

    private static RequestBody _createRequestBody(Map<String, String> params, File... files)
    {
        if (files == null || files.length == 0)
        {
            FormBody.Builder builder = new FormBody.Builder();
            _addParams(params, builder);
            return builder.build();
        } else
        {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            _addParams(params, builder);

            for (File file : files)
            {
                RequestBody fileBody = RequestBody.create(MediaType.parse(_guessMimeType(file.getName())), file);
                builder.addFormDataPart(file.getName(), file.getName(), fileBody);
            }
            return builder.build();
        }
    }

    private static void _addParams(Map<String, String> params, MultipartBody.Builder builder)
    {
        if (params != null && !params.isEmpty())
        {
            for (String key : params.keySet())
            {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

    private static void _addParams(Map<String, String> params, FormBody.Builder builder)
    {
        if (params != null)
        {
            for (String key : params.keySet())
            {
                builder.add(key, params.get(key));
            }
        }
    }

    private static String _guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private static Request.Builder _createRequestBuilder(String tag, String url, int maxAge, Map<String, String> headerParams)
    {
        Request.Builder builder = new Request.Builder();
        builder.tag(tag)
                .url(url)
                .cacheControl(_createCacheControl(maxAge));
        builder.headers(_createHeaders(headerParams));
        return builder;
    }

    /**
     * 创建请求缓存策略
     *
     * @param maxAge 缓存时间（秒）
     */
    private static CacheControl _createCacheControl(int maxAge)
    {
        if (maxAge > 0)
        {
            CacheControl.Builder builder = new CacheControl.Builder();
            builder.maxAge(maxAge, TimeUnit.SECONDS);
            builder.onlyIfCached();
            return builder.build();
        } else
        {
            return CacheControl.FORCE_NETWORK;
        }
    }

    /**
     * 创建请求头部参数
     */
    private static Headers _createHeaders(Map<String, String> headerParams)
    {
        Headers.Builder headerBuilder = new Headers.Builder();
        headerBuilder.add("Charset", "UTF-8");
        headerBuilder.add("Accept-Encoding", "gzip,deflate");
        if (headerParams != null && !headerParams.isEmpty())
        {
            for (String key : headerParams.keySet())
            {
                headerBuilder.add(key, headerParams.get(key));
            }
        }
        return headerBuilder.build();
    }

    private static void _handlerRequest(Request request, final HttpClientStringBack callback)
    {
        callback.onBefore(request);
        sOkHttpClient.newCall(request).enqueue(new Callback()
        {
            @Override
            public void onFailure(Call call, final IOException e)
            {
//                KLog.e("message:" + e.getMessage());
                if (call.isCanceled())
                {
                    return;
                }
                sHandler.post(new HttpRunnable.NetworkRunnable(callback, e));
            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException
            {
//                KLog.e("responseCode:" + response.code());
                if (call.isCanceled())
                {
                    return;
                }
                final int code = response.code();
                /**
                 * 响应失败
                 */
                if (!response.isSuccessful() || code != 200)
                {
                    sHandler.post(new HttpRunnable.ResponseErrorRunnable(callback, code));
                    return;
                }
                /**
                 * 响应成功
                 */
                String result = null;
                Exception error = null;
                try
                {
                    result = _getResponseResult(response);
                } catch (IOException exception)
                {
                    exception.printStackTrace();
                    error = exception;
                }
                sHandler.post(new HttpRunnable.ResponseRunnable(callback, result, error));
            }
        });
    }

    private static String _getResponseResult(Response response) throws IOException
    {
        String contentType = response.headers().get("Content-Encoding");
        String result;
        if (!TextUtils.isEmpty(contentType) && contentType.equalsIgnoreCase("gzip"))
        {
            // 以GZIP形式压缩后的字符串，需要解压缩

            GZIPInputStream gzip = new GZIPInputStream(new BufferedInputStream(new ByteArrayInputStream(response.body().bytes())));
            //noinspection SpellCheckingInspection
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[256];
            int count;
            while ((count = gzip.read(buffer)) >= 0)
            {
                baos.write(buffer, 0, count);
            }
            byte[] bytes = baos.toByteArray();
            result = new String(bytes, "UTF-8");
            gzip.close();
            baos.close();
            ;
        } else
        {
            // 正常数据，不需要解压缩
            result = new String(response.body().bytes(), "UTF-8");
        }
        return result;
    }
}
