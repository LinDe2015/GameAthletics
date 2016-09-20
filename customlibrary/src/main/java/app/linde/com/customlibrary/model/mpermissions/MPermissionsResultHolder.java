package app.linde.com.customlibrary.model.mpermissions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/9/20.
 */
final class MPermissionsResultHolder
{

    private ArrayList<String> granted;
    private ArrayList<String> denied;
    private ArrayList<String> notAskAgain;

    MPermissionsResultHolder(Activity activity, IMPermissions permissions)
    {
        checkResult(activity, permissions);
        result(activity, permissions);
    }

    MPermissionsResultHolder(Fragment fragment, IMPermissions permissions)
    {
        checkResult(fragment, permissions);
        result(fragment.getActivity(), permissions);
    }

    private void checkResult(Activity activity, IMPermissions permissions)
    {
        ArrayList<String> allPermissions = permissions.getMPermissionsNeedRequest();
        granted = new ArrayList<>();
        denied = new ArrayList<>();
        notAskAgain = new ArrayList<>();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            permissions.onMPermissionsGranted(allPermissions);
        } else
        {
            for (String permission : allPermissions)
            {
                if (permission == null) continue;
                {
                    if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
                    {
                        granted.add(permission);
                    } else
                    {
                        if (activity.shouldShowRequestPermissionRationale(permission))
                        {
                            denied.add(permission);
                        } else
                        {
                            notAskAgain.add(permission);
                        }
                    }
                }
            }
        }
    }

    private void checkResult(Fragment fragment, IMPermissions permissions)
    {
        ArrayList<String> allPermissions = permissions.getMPermissionsNeedRequest();
        granted = new ArrayList<>();
        denied = new ArrayList<>();
        notAskAgain = new ArrayList<>();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        {
            permissions.onMPermissionsGranted(allPermissions);
        } else
        {
            for (String permission : allPermissions)
            {
                if (permission == null) continue;
                {
                    if (fragment.getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED)
                    {
                        granted.add(permission);
                    } else
                    {
                        if (fragment.shouldShowRequestPermissionRationale(permission))
                        {
                            denied.add(permission);
                        } else
                        {
                            notAskAgain.add(permission);
                        }
                    }
                }
            }
        }
    }

    private void result(final Activity activity, final IMPermissions permissions)
    {
        if (!granted.isEmpty())
            permissions.onMPermissionsGranted(granted);
        if (!denied.isEmpty())
            permissions.onMPermissionsDenied(denied);
        if (!notAskAgain.isEmpty())
        {
            MPermissionsTips tips = permissions.userDoNotAskAgain();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setCancelable(false);
            builder.setTitle(tips.getTitle());
            builder.setMessage(tips.getMessage());
            builder.setPositiveButton(tips.getPositiveButton(), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    activity.startActivityForResult(intent, MPermissionsUtil.REQUEST_PERMISSION_SETTING);
                }
            });
            builder.setNegativeButton(tips.getNegativeButton(), new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    ArrayList<String> result = new ArrayList<>();
                    result.addAll(denied);
                    result.addAll(notAskAgain);
                    permissions.onMPermissionsDenied(result);
                }
            });
        }
    }
}
