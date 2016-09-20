package app.linde.com.customlibrary.view.widget.loading;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2016/9/16.
 */

public interface CreateLoadingView
{
    View onCreateLoadingView(Context context);

    View onCreateErrorView(Context context);

    View onCreateEmptyView(Context context);
}
