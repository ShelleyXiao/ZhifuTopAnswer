package com.xyl.zhifutopanser.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.android.FragmentEvent;
import com.xyl.architectrue.view.LoadMoreRecyleView;
import com.xyl.architectrue.view.MultiStateView;
import com.xyl.zhifutopanser.Model.TopQuestion;
import com.xyl.zhifutopanser.R;
import com.xyl.zhifutopanser.activity.AnswersActivity;
import com.xyl.zhifutopanser.adapter.QuestionRecyleViewAdapter;
import com.xyl.zhifutopanser.utils.JSoupUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * User: ShaudXiao
 * Date: 2017-01-05
 * Time: 10:16
 * Company: zx
 * Description:
 * FIXME
 */


public class RecylerViewFragment extends BaseFragment {

    public static final String BUNDLE_KEY = "topic_id";
    private static final int PAGE_SART = 1;
    private int mPage = PAGE_SART;
    private int mTopicId;

    private LoadMoreRecyleView mMoreRecyleView;
    private MultiStateView mStateView;
    private SwipeRefreshLayout mRefreshLayout;

    private List<TopQuestion> mTopQuestions;

    private QuestionRecyleViewAdapter mRecyleViewAdapter ;

    public RecylerViewFragment() {
    }

    public static RecylerViewFragment newInstance(int topicID) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_KEY, topicID);
        RecylerViewFragment fragment = new RecylerViewFragment();
        fragment.setArguments(bundle);

        return  fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle args = getArguments();
        if(null != args) {
            mTopicId = args.getInt(BUNDLE_KEY);
        }

        View view = inflater.inflate(R.layout.recyleview_fragment_layout, container, false);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.fragment_refresh);
        mMoreRecyleView = (LoadMoreRecyleView) view.findViewById(R.id.fragment_recyleview);
        mStateView = (MultiStateView) view.findViewById(R.id.multistateview);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        init();
    }

    @Override
    protected void onFirstUserVisible() {
        super.onFirstUserVisible();

        onUserVisible();
    }

    @Override
    protected void onUserVisible() {
        super.onUserVisible();
        if(getActivity() == null) {
            return;
        }
        if(mTopQuestions == null || mTopQuestions.size() == 0) {
            initData(true, mPage = PAGE_SART);
        }
    }

    private void init() {
        mRefreshLayout.setOnRefreshListener(() -> {
            initData(false, mPage = PAGE_SART);
        });

        mMoreRecyleView.setCanLoadMore(true);
        mMoreRecyleView.setLoadMoreListener(() -> {
            initData(false, ++mPage);
        });
    }

    private void initData(final boolean needState, final int page) {
        if(needState) {
            mStateView.setViewState(MultiStateView.ViewState.LOADING);
        }

        final String url = "https://www.zhihu.com/topic/" + mTopicId + "/top-answers?page=" + page;
        Observable.create(new Observable.OnSubscribe<Document>() {
            @Override
            public void call(Subscriber<? super Document> subscriber) {

                try {
                    subscriber.onNext(Jsoup.connect(url).timeout(5000)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6").get());
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }

            }
        }).map(new Func1<Document, List<TopQuestion>>() {

            @Override
            public List<TopQuestion> call(Document document) {
                return JSoupUtils.getTopList(document);
            }
        }).compose(this.<List<TopQuestion>>bindUntilEvent(FragmentEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<TopQuestion>>() {
                    @Override
                    public void onCompleted() {
                        if(MultiStateView.ViewState.CONTENT != mStateView.getViewState()) {
                            mStateView.setViewState(MultiStateView.ViewState.CONTENT);
                        }
                        if(page == 1) {
                            mRefreshLayout.setRefreshing(false);
                        } else {
                            mMoreRecyleView.setLoadMoreState(LoadMoreRecyleView.STATE_FINISH_LOADMORE);
                        }

                        initRecylerView();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (needState) {
                            mStateView.setViewState(MultiStateView.ViewState.ERROR);
                        }
                        if(page == 1) {
                            mRefreshLayout.setRefreshing(false);
                        } else {
                            mMoreRecyleView.setLoadMoreState(LoadMoreRecyleView.STATE_FINISH_LOADMORE);
                        }

                        if(page > 1) {
                            mPage--;
                        }
                    }

                    @Override
                    public void onNext(List<TopQuestion> o) {
                        if(page == 1) {
                            mTopQuestions.clear();
                            mTopQuestions.addAll(o);
                        } else {
                            mTopQuestions.addAll(o);
                        }
                    }
                });
    }

    private void initRecylerView() {
        if(mMoreRecyleView.getAdapter() == null) {
            mMoreRecyleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            mRecyleViewAdapter = new QuestionRecyleViewAdapter(mTopQuestions);
            mMoreRecyleView.setAdapter(mRecyleViewAdapter);
            mRecyleViewAdapter.setQuestionItemClickListener((position) -> {
                Intent intent = new Intent(getActivity(), AnswersActivity.class);
                intent.putExtra(AnswersActivity.QUESTION_URL, mTopQuestions.get(position).getUrl());
                startActivity(intent);
            });
        }
    }
}
