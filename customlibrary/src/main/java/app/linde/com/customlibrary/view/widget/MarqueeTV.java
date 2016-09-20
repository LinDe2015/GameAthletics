package app.linde.com.customlibrary.view.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import app.linde.com.customlibrary.R;

/**
 * Created by Administrator on 2016/9/16.
 */

public class MarqueeTV extends TextView implements View.OnFocusChangeListener
{
    public MarqueeTV(Context context)
    {
        super(context);
        init();
    }

    public MarqueeTV(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public MarqueeTV(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MarqueeTV(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init()
    {
        setOnFocusChangeListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            setTextAppearance(R.style.MarqueeTextView);
        } else
        {
            setTextAppearance(getContext(), R.style.MarqueeTextView);
        }
    }

    @Override
    public boolean isFocused()
    {
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {
        if (v == this && !hasFocus)
        {
            requestFocus();
        }
    }
}
