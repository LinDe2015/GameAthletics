package app.linde.com.customlibrary.model.asyncrefreshlist;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2016-09-13 0013.
 */

final class RecyclerScrollListener<RV extends RecyclerView> extends RecyclerView.OnScrollListener
{
    private final RV mRecyclerView;
    private final int mEveryPageNumber;
    private final OnReloadListener<RV> mReloadListener;

    private int firstVisiblePage, lastVisiblePage;

    RecyclerScrollListener(@NonNull RV recyclerView, int everyPageNumber, OnReloadListener<RV> reloadListener)
    {
        mRecyclerView = recyclerView;
        mEveryPageNumber = everyPageNumber;
        mReloadListener = reloadListener;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState)
    {
        super.onScrollStateChanged(recyclerView, newState);
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy)
    {
        super.onScrolled(recyclerView, dx, dy);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof LinearLayoutManager)
        {
            LinearLayoutManager llm = (LinearLayoutManager) manager;
            onScrolledLinearLayoutManager(llm);
        }
    }

    private void onScrolledLinearLayoutManager(LinearLayoutManager manager)
    {
        int firstVisibleItem = manager.findFirstVisibleItemPosition();
        int lastVisibleItem = manager.findLastVisibleItemPosition();

        int firstPage = firstVisibleItem / mEveryPageNumber + 1;
        int lastPage = lastVisibleItem / mEveryPageNumber + 1;

        if (firstVisiblePage <= 0 && lastVisiblePage <= 0)
        {
            firstVisiblePage = firstPage;
            lastVisiblePage = lastPage;
            onResume();
        } else
        {
            if (firstPage < firstVisiblePage && mReloadListener != null)
            {
                mReloadListener.onReload(mRecyclerView, firstPage, mEveryPageNumber);
            }
            firstVisiblePage = firstPage;
            if (lastPage > lastVisiblePage && mReloadListener != null)
            {
                mReloadListener.onReload(mRecyclerView, lastPage, mEveryPageNumber);
            }
            lastVisiblePage = lastPage;
        }
    }

    final void onResume()
    {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager)
        {
            LinearLayoutManager manager = (LinearLayoutManager) layoutManager;
            int firstVisibleItem = manager.findFirstVisibleItemPosition();
            int lastVisibleItem = manager.findLastVisibleItemPosition();
            int firstPage = (firstVisibleItem / mEveryPageNumber) + 1;
            int lastPage = (lastVisibleItem / mEveryPageNumber) + 1;
            if (mReloadListener != null)
            {
                for (int page = firstPage; page <= lastPage; page++)
                {
                    mReloadListener.onReload(mRecyclerView, page, mEveryPageNumber);
                }
            }
        }
    }
}
