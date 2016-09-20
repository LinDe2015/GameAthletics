package app.linde.com.customlibrary.view.widget.loading;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2016/9/16.
 */

public final class LoadingLayout extends FrameLayout
{
    private View mLoadingLayout;
    private View mContentLayout;

    private CreateLoadingView mCreateLoadingView;

    public LoadingLayout(Context context)
    {
        this(context, null);
    }

    public LoadingLayout(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        mCreateLoadingView = new SampleCreateLoadingView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LoadingLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes)
    {
        super(context, attrs, defStyleAttr, defStyleRes);
        mCreateLoadingView = new SampleCreateLoadingView();
    }

    public void setCreateLoadingView(CreateLoadingView createLoadingView)
    {
        if (createLoadingView != null)
            mCreateLoadingView = createLoadingView;
    }

    public void setContentView(View contentView)
    {
        if (contentView == null)
            return;
        mContentLayout = contentView;
        mContentLayout.setVisibility(VISIBLE);
        if (getChildCount() > 0)
            removeAllViews();
        addView(mContentLayout);
    }

    public void showLoading()
    {
        _show(0);
    }

    public void showError()
    {
        _show(1);
    }

    public void showEmpty()
    {
        _show(2);
    }

    private void _show(int index)
    {
        if (mCreateLoadingView != null)
        {
            switch (index)
            {
                case 0:
                    mLoadingLayout = mCreateLoadingView.onCreateLoadingView(getContext());
                    break;
                case 1:
                    mLoadingLayout = mCreateLoadingView.onCreateErrorView(getContext());
                    break;
                case 2:
                    mLoadingLayout = mCreateLoadingView.onCreateEmptyView(getContext());
                    break;
            }
            if (mLoadingLayout == null)
                return;
            mContentLayout.setVisibility(GONE);
            mLoadingLayout.setVisibility(VISIBLE);
            addView(mLoadingLayout);
        }
    }

    public void hideLoading()
    {
        mContentLayout.setVisibility(VISIBLE);
        removeView(mLoadingLayout);
        mLoadingLayout = null;
    }
}
