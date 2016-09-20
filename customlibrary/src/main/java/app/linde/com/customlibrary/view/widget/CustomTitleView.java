package app.linde.com.customlibrary.view.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import app.linde.com.customlibrary.R;

/**
 * Created by LinDe
 * on 2016/9/16.
 */

public class CustomTitleView extends RelativeLayout
{
    private MarqueeTV mMarqueeTV;

    private FrameLayout mLeftFrameLayout;
    private FrameLayout mCenterFrameLayout;
    private FrameLayout mRightFrameLayout;

    public CustomTitleView(Context context)
    {
        this(context, null);
    }

    public CustomTitleView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomTitleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context)
    {
        LayoutInflater.from(context).inflate(R.layout.layout_title_view, this, true);

        mMarqueeTV = (MarqueeTV) findViewById(R.id.tv_title_text);
        mLeftFrameLayout = (FrameLayout) findViewById(R.id.frame_title_left);
        mCenterFrameLayout = (FrameLayout) findViewById(R.id.frame_title_center);
        mRightFrameLayout = (FrameLayout) findViewById(R.id.frame_title_right);
    }

    public void setTitleVisibility(boolean isShow)
    {
        mMarqueeTV.setVisibility(isShow ? VISIBLE : GONE);
    }

    public <Text> void setTitleText(Text text)
    {
        if (mMarqueeTV == null)
            return;
        if (text == null)
            mMarqueeTV.setText("");
        else if (text instanceof Integer)
        {
            String result;
            try
            {
                result = getResources().getString((Integer) text);
            } catch (Exception e)
            {
                result = text.toString();
            }
            mMarqueeTV.setText(result);
        } else
        {
            mMarqueeTV.setText(text.toString());
        }
    }

    public void addLeftView(View view)
    {
        _addView(0, view);
    }

    public void addCenterView(View view)
    {
        _addView(1, view);
    }

    public void addRightView(View view)
    {
        _addView(2, view);
    }

    private void _addView(int flag, View view)
    {
        FrameLayout frameLayout;
        switch (flag)
        {
            case 0:
                frameLayout = mLeftFrameLayout;
                break;
            case 1:
                frameLayout = mCenterFrameLayout;
                break;
            case 2:
                frameLayout = mRightFrameLayout;
                break;
            default:
                return;
        }
        if (frameLayout == null || view == null)
            return;
        if (frameLayout.getChildCount() > 0)
            frameLayout.removeAllViews();
        frameLayout.addView(view);
    }
}
