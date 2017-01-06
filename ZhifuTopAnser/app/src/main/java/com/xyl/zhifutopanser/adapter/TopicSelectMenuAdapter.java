package com.xyl.zhifutopanser.adapter;

import android.view.View;

import com.xyl.architectrue.adapter.BaseAdapter;
import com.xyl.architectrue.adapter.BaseViewHolder;
import com.xyl.zhifutopanser.Model.TopicModel;
import com.xyl.zhifutopanser.R;

import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-01-05
 * Time: 09:57
 * Company: zx
 * Description:
 * FIXME
 */


public class TopicSelectMenuAdapter extends BaseAdapter {

    private List<TopicModel> mTopicLists;

    public TopicSelectMenuAdapter(List<TopicModel> topicLists, onMenuItemSelectedListener menuItemSelectedListener) {
        mTopicLists = topicLists;
        mMenuItemSelectedListener = menuItemSelectedListener;
    }

    private onMenuItemSelectedListener mMenuItemSelectedListener;

    @Override
    public int getLayoutId(int position) {
        return R.layout.popup_menu_item;
    }

    @Override
    public boolean clickable() {
        return true;
    }

    @Override
    public void onItemClick(View v, int position) {
        if(null != mMenuItemSelectedListener) {
            mMenuItemSelectedListener.onMenuSelected(position);
        }
    }

    @Override
    public void onBindView(BaseViewHolder holder, int position) {
        holder.setText(R.id.menu_item, mTopicLists.get(position).getTopicName());
    }

    @Override
    public int getItemCount() {
        return mTopicLists != null ? mTopicLists.size() : 0;
    }

    public void setMenuItemSelectedListener(onMenuItemSelectedListener menuItemSelectedListener) {
        mMenuItemSelectedListener = menuItemSelectedListener;
    }

    public interface onMenuItemSelectedListener {
        public void onMenuSelected(int position);
    }
}
