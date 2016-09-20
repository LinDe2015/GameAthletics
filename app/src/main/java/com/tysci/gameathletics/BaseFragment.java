package com.tysci.gameathletics;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import app.linde.com.customlibrary.model.mpermissions.MPermissionsFragment;
import butterknife.ButterKnife;

/**
 * Created by LinDe
 * on 2016/9/16.
 */
public abstract class BaseFragment extends MPermissionsFragment
{
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
