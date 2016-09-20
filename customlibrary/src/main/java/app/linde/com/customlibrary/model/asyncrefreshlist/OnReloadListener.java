package app.linde.com.customlibrary.model.asyncrefreshlist;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2016-09-13 0013.
 */

public interface OnReloadListener<RV extends RecyclerView>
{
    void onReload(RV recyclerView, int page,int everyPageNumber);
}
