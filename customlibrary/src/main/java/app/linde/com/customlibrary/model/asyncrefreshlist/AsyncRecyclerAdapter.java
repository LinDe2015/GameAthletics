package app.linde.com.customlibrary.model.asyncrefreshlist;

import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016-09-13 0013.
 */

public abstract class AsyncRecyclerAdapter<RV extends RecyclerView, Bean, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
{
    private static final int sDefaultEveryPageNumber = 10;
    private final RecyclerScrollListener<RV> mRecyclerScrollListener;

    protected ArrayList<Bean> mDataList;

    public AsyncRecyclerAdapter(RV recyclerView, OnReloadListener<RV> reloadListener)
    {
        this(recyclerView, reloadListener, sDefaultEveryPageNumber);
    }

    public AsyncRecyclerAdapter(RV recyclerView, OnReloadListener<RV> reloadListener, int everyPageNumber)
    {
        //noinspection unchecked
        mRecyclerScrollListener = new RecyclerScrollListener(recyclerView, everyPageNumber, reloadListener);
        recyclerView.addOnScrollListener(mRecyclerScrollListener);
    }

    @Override
    public int getItemCount()
    {
        return mDataList == null ? 0 : mDataList.size();
    }

    @SafeVarargs
    public final void addDataList(boolean append, Bean... beans)
    {
        if (mDataList == null)
        {
            mDataList = new ArrayList<>();
        }
        if (!append)
        {
            mDataList.clear();
        }
        if (beans == null || beans.length == 0)
        {
            notifyDataSetChanged();
            return;
        }
        Collections.addAll(mDataList, beans);
        notifyDataSetChanged();
    }

    public final void addDataList(boolean append, List<Bean> beans)
    {
        if (mDataList == null)
            mDataList = new ArrayList<>();
        if (!append)
            mDataList.clear();
        if (beans == null || beans.isEmpty())
        {
            notifyDataSetChanged();
            return;
        }
        for (Bean bean : beans)
        {
            mDataList.add(bean);
        }
        notifyDataSetChanged();
    }

    public final void addDataList(boolean append, JSONArray array, Class<Bean> cls)
    {
        if (mDataList == null)
        {
            mDataList = new ArrayList<>();
        }
        if (!append)
        {
            mDataList.clear();
            notifyDataSetChanged();
        }
        if (array == null || array.isEmpty())
        {
            return;
        }
        Bean bean;
        for (int i = 0, size = array.size(); i < size; i++)
        {
            if (cls == JSONObject.class)
                //noinspection unchecked
                bean = (Bean) array.getJSONObject(i);
            else
                bean = array.getObject(i, cls);
            mDataList.add(bean);
        }
        notifyDataSetChanged();
        notifyItemRangeChanged(0, getItemCount());
    }

    public final void onReloadDataList(List<Bean> dataList, int page, int everyPageNumber)
    {
        if (dataList == null || dataList.isEmpty() || dataList.size() > everyPageNumber)
            return;
        final int start = (page - 1) * everyPageNumber;
        final int end = page * everyPageNumber;
        final int size = dataList.size();
        Bean tmp;
        Bean add;
        for (int i = start, index = 0; i < end && index < size; i++, index++)
        {
            add = dataList.get(index);
            if (add == null)
                continue;
            tmp = mDataList.get(i);
            try
            {
                mDataList.remove(i);
            } catch (Exception e)
            {
                return;
            }
            try
            {
                mDataList.add(i, add);
            } catch (Exception e)
            {
                mDataList.add(i, tmp);
                return;
            }
        }
    }

    public final void onReloadDataList(JSONArray array, Class<Bean> cls, int page, int everyPageNumber)
    {
        if (array == null || array.isEmpty() || array.size() > everyPageNumber)
            return;
        final int start = (page - 1) * everyPageNumber;
        final int end = page * everyPageNumber;
        final int size = array.size();
        Bean tmp;
        Bean add;
        for (int i = start, index = 0; i < end && index < size; i++, index++)
        {
            if (cls == JSONObject.class)
                //noinspection unchecked
                add = (Bean) array.getJSONObject(i);
            else
                add = array.getObject(i, cls);
            if (add == null)
                continue;
            tmp = mDataList.get(i);
            try
            {
                mDataList.remove(i);
            } catch (Exception e)
            {
                return;
            }
            try
            {
                mDataList.add(i, add);
            } catch (Exception e)
            {
                mDataList.add(i, tmp);
                return;
            }
        }
    }

    public final void onResume()
    {
        mRecyclerScrollListener.onResume();
    }
}
