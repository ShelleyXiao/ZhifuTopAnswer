package com.xyl.zhifutopanser.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.trello.rxlifecycle.android.FragmentEvent;
import com.xyl.zhifutopanser.Model.TopicModel;
import com.xyl.zhifutopanser.R;
import com.xyl.zhifutopanser.adapter.TabAdapter;
import com.xyl.zhifutopanser.adapter.TopicSelectMenuAdapter;
import com.xyl.zhifutopanser.utils.AllTopicUtils;
import com.xyl.zhifutopanser.view.SelectPopupWindow;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * User: ShaudXiao
 * Date: 2017-01-04
 * Time: 16:59
 * Company: zx
 * Description:
 * FIXME
 */


public class MainFragment extends BaseFragment implements TopicSelectMenuAdapter.onMenuItemSelectedListener
            , View.OnClickListener{

    private SelectPopupWindow mTopicMenuPopupWindow;
    private TopicSelectMenuAdapter mMenuAdapter;
    private TabLayout mTabLayout;
    private ImageView mExpendMenuImage;
    private ViewPager mViewPager;
    private TabAdapter mTabAdapter;
    private View line;

    private List<TopicModel> mTopics = new ArrayList<>();
    private List<RecylerViewFragment> mFragments = new ArrayList<>();

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_main, container, false);
        initView(contentView);
        return contentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        init();
    }

    private void initView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tab);
        mExpendMenuImage = (ImageView) view.findViewById(R.id.expand);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPaper);
        line = view.findViewById(R.id.line);

    }

    private void initViewpaper() {
        if(mTabAdapter == null) {
            mTabAdapter = new TabAdapter(getChildFragmentManager(), mTopics, mFragments);
            mViewPager.setAdapter(mTabAdapter);;
            mViewPager.setOffscreenPageLimit(mTopics.size());

            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            mTabLayout.setupWithViewPager(mViewPager);
            mExpendMenuImage.setClickable(true);;
            mExpendMenuImage.setOnClickListener(this);
        } else {
            mTabAdapter.notifyDataSetChanged();
        }
    }

    private void initTopicPopupMenu() {
        if(mTopicMenuPopupWindow == null) {
            mTopicMenuPopupWindow = new SelectPopupWindow(getContext());
            mMenuAdapter = new TopicSelectMenuAdapter(mTopics, this);
            mTopicMenuPopupWindow.setAdapter(mMenuAdapter);

        }
        mTopicMenuPopupWindow.showAsDropDown(line);
    }

    @Override
    public void onMenuSelected(int position) {
        choosePosition(position);
        mTopicMenuPopupWindow.dismiss();
    }

    @Override
    public void onClick(View v) {
        initTopicPopupMenu();
    }

    private void init() {
        mTopics.clear();
        mFragments.clear();
        mTabLayout.removeAllViews();;
        mTopics.addAll(AllTopicUtils.getInstance().getAllTopic(getActivity(), getString(R.string.title)));

        Observable.from(mTopics)
                .map(new Func1<TopicModel, TopicModel>() {
                    @Override
                    public TopicModel call(TopicModel topicModel) {
                        mFragments.add( RecylerViewFragment.newInstance(topicModel.getTopicId()));
                        return topicModel;
                    }
                })
                .compose(this.<TopicModel>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TopicModel>() {
                    @Override
                    public void onCompleted() {
                        initViewpaper();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TopicModel topicModel) {
                        mTabLayout.addTab(mTabLayout.newTab().setText(topicModel.getTopicName()));
                    }
                });
    }

    private void choosePosition(int position) {
        mTabLayout.setScrollPosition(position, 0, true);
        mViewPager.setCurrentItem(position);
    }
}
