package com.xyl.architectrue.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.IdRes;
import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

/**
 * User: ShaudXiao
 * Date: 2017-01-03
 * Time: 16:10
 * Company: zx
 * Description:
 * FIXME
 */


public class BaseViewHolder extends RecyclerView.ViewHolder {

    protected View convertView;
    protected SparseArray<View> mViews;

    public BaseViewHolder(View itemView) {
        super(itemView);
        this.convertView = itemView;
        mViews = new SparseArray<>();
    }

    /*
    * 通过id获取对应控件，如果没有则加入mViews，有则从mViews中获取
    *
    * */
    public <T extends View> T getView(@IdRes int resId) {
        View view = mViews.get(resId);
        if(null == view) {
            view = convertView.findViewById(resId);
            mViews.put(resId, view);
        }

        return (T)view;
    }

    public View getConvertView() {
        return convertView;
    }

    public BaseViewHolder setBgColor(@IdRes int resId, int color) {
        getView(resId).setBackgroundColor(color);
        return this;
    }

    public BaseViewHolder setBgDrawable(@IdRes int resId, Drawable drawable) {
        getView(resId).setBackground(drawable);
        return this;
    }

    public BaseViewHolder setTextColor(@IdRes int resId, int color) {
        ((TextView)getView(resId)).setTextColor(color);
        return this;
    }

    public BaseViewHolder setText(@IdRes int resId, String text) {
        ((TextView)getView(resId)).setText(text);
        return this;
    }

    public BaseViewHolder setTextSize(@IdRes int resId, int spSize) {
        ((TextView)getView(resId)).setTextSize(spSize);
        return this;
    }

    public BaseViewHolder setVisibility(@IdRes int resId, @Visibility int visibilty) {
        switch (visibilty) {
            case VISIBLE:
                getView(resId).setVisibility(View.VISIBLE);
                break;
            case INVISIBLE:
                getView(resId).setVisibility(View.INVISIBLE);
                break;
            case GONE:
                getView(resId).setVisibility(View.GONE);
                break;

        }


        return this;
    }

    @IntDef({VISIBLE, INVISIBLE, GONE})
    public @interface Visibility {

    }

    public static final int VISIBLE = 0x00000000;
    public static final int INVISIBLE = 0x00000004;
    public static final int GONE = 0x00000008;

}
