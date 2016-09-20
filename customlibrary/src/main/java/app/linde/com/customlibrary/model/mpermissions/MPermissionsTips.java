package app.linde.com.customlibrary.model.mpermissions;

import android.text.TextUtils;

/**
 * Created by Administrator on 2016/9/20.
 */

public final class MPermissionsTips
{
    private String title;
    private String message;
    private String positiveButton;
    private String negativeButton;

    public MPermissionsTips(String title, String message, String positiveButton, String negativeButton)
    {
        this.title = title;
        this.message = message;
        this.positiveButton = positiveButton;
        this.negativeButton = negativeButton;
        checkEmpty();
    }

    private void checkEmpty()
    {
        if (TextUtils.isEmpty(title)) title = "授权提醒";
        if (TextUtils.isEmpty(message)) message = "我们需要获得读写SD卡的权限来存储数据";
        if (TextUtils.isEmpty(positiveButton)) positiveButton = "授权";
        if (TextUtils.isEmpty(negativeButton)) negativeButton = "拒绝";
    }

    public String getTitle()
    {
        return title;
    }

    public String getMessage()
    {
        return message;
    }

    public String getPositiveButton()
    {
        return positiveButton;
    }

    public String getNegativeButton()
    {
        return negativeButton;
    }
}
