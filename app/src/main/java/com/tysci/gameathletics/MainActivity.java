package com.tysci.gameathletics;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
{
    @Override
    protected void setContentViewBeforeBindWidgets()
    {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void afterBindWidgets(Bundle savedInstanceState)
    {
    }

    @Override
    public void onMPermissionsGranted(ArrayList<String> granted)
    {
    }

    @Override
    public void onMPermissionsDenied(ArrayList<String> denied)
    {
    }
}
