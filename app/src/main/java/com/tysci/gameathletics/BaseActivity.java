package com.tysci.gameathletics;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import app.linde.com.customlibrary.model.mpermissions.MPermissionsActivity;
import app.linde.com.customlibrary.view.widget.loading.LoadingLayout;
import butterknife.ButterKnife;

/**
 * Created by LinDe
 * on 2016/9/16.
 */
public abstract class BaseActivity extends MPermissionsActivity
{
    protected LoadingLayout mLoadingLayout;

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentViewBeforeBindWidgets();
        ButterKnife.bind(this);
        afterBindWidgets(savedInstanceState);
        if (registerEventBus())
            EventBus.getDefault().register(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID)
    {
        this.setContentView(getLayoutInflater().inflate(layoutResID, null));
    }

    @Override
    public void setContentView(View view)
    {
        mLoadingLayout = new LoadingLayout(this);
        mLoadingLayout.setContentView(view);
        super.setContentView(mLoadingLayout);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    protected boolean registerEventBus()
    {
        return false;
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        if (registerEventBus())
            EventBus.getDefault().unregister(this);
    }

    /**
     * 用{@link ButterKnife}绑定控件前的操作
     * <p/>
     * 注意：此处必须调用{@link #setContentView(int)}或 {@link #setContentView(View)}
     * 否则程序会报错
     */
    protected abstract void setContentViewBeforeBindWidgets();

    /**
     * 用{@link ButterKnife}绑定控件后
     */
    protected abstract void afterBindWidgets(Bundle savedInstanceState);
}
