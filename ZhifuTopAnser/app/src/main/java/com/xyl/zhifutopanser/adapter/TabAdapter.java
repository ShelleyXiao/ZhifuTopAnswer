package com.xyl.zhifutopanser.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.xyl.zhifutopanser.Model.TopicModel;
import com.xyl.zhifutopanser.fragment.RecylerViewFragment;

import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-01-05
 * Time: 10:17
 * Company: zx
 * Description:
 * FIXME
 */


public class TabAdapter extends FragmentPagerAdapter {

    private List<TopicModel> mTopics;
    private List<RecylerViewFragment> mFragments;

    public TabAdapter(FragmentManager fm, List<TopicModel> topics, List<RecylerViewFragment> fragments) {
        super(fm);
        mFragments = fragments;
        mTopics = topics;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTopics.get(position).getTopicName();
    }
}
