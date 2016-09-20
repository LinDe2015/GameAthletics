package app.linde.com.customlibrary.model.mpermissions;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/20.
 */

public interface IMPermissions
{
    ArrayList<String> getMPermissionsNeedRequest();

    MPermissionsTips shouldShowRequestPermissionRationale();

    MPermissionsTips userDoNotAskAgain();

    void onMPermissionsGranted(ArrayList<String> granted);

    void onMPermissionsDenied(ArrayList<String> denied);
}
