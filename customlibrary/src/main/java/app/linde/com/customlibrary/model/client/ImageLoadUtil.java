package app.linde.com.customlibrary.model.client;

import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by Administrator on 2016-09-12 0012.
 */

public class ImageLoadUtil
{
    private static String sImageHeader = null;

    public static void initImageHeader(String imageHeader)
    {
        sImageHeader = imageHeader;
    }

    public static void loadImage(ImageView imageView, int defaultRes, String url)
    {
        Glide.with(imageView.getContext())
                .load(sImageHeader + url)
                .asBitmap()
                .placeholder(defaultRes)
                .into(imageView);
    }

    public static void loadImage(ImageView imageView, int res)
    {
        Glide.with(imageView.getContext())
                .load(res)
                .asBitmap()
                .into(imageView);
    }
}
