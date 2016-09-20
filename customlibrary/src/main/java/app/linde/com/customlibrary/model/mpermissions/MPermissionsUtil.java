package app.linde.com.customlibrary.model.mpermissions;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by LinDe on 2016-09-12 0012.
 * <p/>
 * 对于Android api 23 以上的手机，需要动态获取危险权限
 */
final class MPermissionsUtil
{
    static final int REQUEST_STATUS_CODE = -6666;
    static final int REQUEST_PERMISSION_SETTING = -8888;

    static ArrayList<String> getDeniedPermissions(@NonNull Context context, @NonNull ArrayList<String> permissions)
    {
        ArrayList<String> permissionList = new ArrayList<>();
        for (String permission : permissions)
        {
            if (permission == null || ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED)
                continue;
            permissionList.add(permission);
        }
        return permissionList;
    }
}