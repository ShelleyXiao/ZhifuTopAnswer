package com.xyl.zhifutopanser.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.xyl.architectrue.adapter.BaseAdapter;
import com.xyl.zhifutopanser.R;

/**
 * User: ShaudXiao
 * Date: 2017-01-04
 * Time: 17:16
 * Company: zx
 * Description:
 * FIXME
 */


public class SelectPopupWindow extends PopupWindow {

    private final View mMenuView;
    private final RecyclerView mRecyclerView;

    public SelectPopupWindow(Context context) {
        super(context);

        LayoutInflater inflater = LayoutInflater.from(context);
        mMenuView = inflater.inflate(R.layout.popupwindow_layout, null);
        mRecyclerView = (RecyclerView) mMenuView.findViewById(R.id.pop_recycle);

        this.setContentView(mMenuView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);

        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        setOutsideTouchable(false);

    }

    public void setAdapter(BaseAdapter adapter) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mRecyclerView.getContext(), 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setAdapter(adapter);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
