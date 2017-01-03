package com.xyl.architectrue.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * User: ShaudXiao
 * Date: 2017-01-03
 * Time: 16:32
 * Company: zx
 * Description:
 * FIXME
 */


public abstract class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        final BaseViewHolder holder = new BaseViewHolder(view);
        if(clickable()) {
            holder.convertView.setClickable(true);
            holder.convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(v, holder.getLayoutPosition());
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        onBindView(holder, position);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutId(position);
    }

    public abstract int getLayoutId(int position);

    public abstract boolean clickable();

    public abstract void onItemClick(View v, int position);

    public abstract void onBindView(BaseViewHolder holder, int position);

}
