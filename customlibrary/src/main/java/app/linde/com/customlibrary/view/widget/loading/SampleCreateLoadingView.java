package app.linde.com.customlibrary.view.widget.loading;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by LinDe
 * on 2016/9/16.
 */
class SampleCreateLoadingView implements CreateLoadingView
{
    @Override
    public View onCreateLoadingView(Context context)
    {
        return createTextView(context, "努力加载中...");
    }

    @Override
    public View onCreateErrorView(Context context)
    {
        return createTextView(context, "网络连接中断");
    }

    @Override
    public View onCreateEmptyView(Context context)
    {
        return createTextView(context, "暂无相关数据");
    }

    private TextView createTextView(Context context, String text)
    {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setBackgroundColor(Color.WHITE);
        tv.setTextColor(Color.BLACK);
        tv.setTextSize(15);
        tv.setGravity(Gravity.CENTER);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return tv;
    }
}
