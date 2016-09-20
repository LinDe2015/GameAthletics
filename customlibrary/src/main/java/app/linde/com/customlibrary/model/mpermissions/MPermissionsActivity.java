package app.linde.com.customlibrary.model.mpermissions;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/20.
 */
public abstract class MPermissionsActivity extends AppCompatActivity implements IMPermissions
{
    @Override
    public ArrayList<String> getMPermissionsNeedRequest()
    {
        ArrayList<String> permissions = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
        {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissions;
    }

    @Override
    public MPermissionsTips shouldShowRequestPermissionRationale()
    {
        return new MPermissionsTips("授权提醒", "我们需要确保获得读写SD卡的权限以存储部分数据", "授权", "拒绝");
    }

    @Override
    public MPermissionsTips userDoNotAskAgain()
    {
        return new MPermissionsTips("警告", "您已经拒绝了SD卡读写权限的授予，可能会导致电竞崩溃", "前往设置", "残忍拒绝");
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        startRequestMPermissions();
    }

    private void startRequestMPermissions()
    {
        final ArrayList<String> allPermissions = getMPermissionsNeedRequest();
        // API低于23的版本不需要做这些麻烦的权限设置
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            onMPermissionsGranted(allPermissions);
            return;
        }
        final ArrayList<String> deniedPermissions = MPermissionsUtil.getDeniedPermissions(this, allPermissions);
        if (deniedPermissions.isEmpty())
        {
            onMPermissionsGranted(getMPermissionsNeedRequest());
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            for (String permission : allPermissions)
            {
                if (shouldShowRequestPermissionRationale(permission))
                {
                    MPermissionsTips tips = shouldShowRequestPermissionRationale();
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false);
                    builder.setTitle(tips.getTitle());
                    builder.setMessage(tips.getMessage());
                    builder.setPositiveButton(tips.getPositiveButton(), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            ActivityCompat.requestPermissions(MPermissionsActivity.this, allPermissions.toArray(new String[allPermissions.size()]), MPermissionsUtil.REQUEST_STATUS_CODE);
                        }
                    });
                    builder.setNegativeButton(tips.getNegativeButton(), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            onMPermissionsDenied(deniedPermissions);
                        }
                    });
                    return;
                }
            }
        }
        ActivityCompat.requestPermissions(this, allPermissions.toArray(new String[allPermissions.size()]), MPermissionsUtil.REQUEST_STATUS_CODE);
    }

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        new MPermissionsResultHolder(this, this);
    }
}
