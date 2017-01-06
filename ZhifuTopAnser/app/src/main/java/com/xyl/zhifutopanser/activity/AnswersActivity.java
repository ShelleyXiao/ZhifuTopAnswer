package com.xyl.zhifutopanser.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.xyl.architectrue.adapter.BaseAdapter;
import com.xyl.architectrue.adapter.BaseViewHolder;
import com.xyl.architectrue.rxsupport.RxAppCompatActivity;
import com.xyl.architectrue.view.MultiStateView;
import com.xyl.zhifutopanser.Model.AnswersModel;
import com.xyl.zhifutopanser.R;
import com.xyl.zhifutopanser.utils.JSoupUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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
 * Time: 17:04
 * Company: zx
 * Description:
 * FIXME
 */


public class AnswersActivity extends RxAppCompatActivity {

    public static final String QUESTION_URL = "question_url";

    private SwipeRefreshLayout mRefreshLayout;
    private MultiStateView mStateView;
    private RecyclerView mRecyclerView;
    private Toolbar mToolbar;

    private List<AnswersModel> mAnswersModels;

    private String mQuestionUrl;
    private String title;
    private String detail;

    private LinearLayoutManager manager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        mQuestionUrl = getIntent().getStringExtra(QUESTION_URL);

        initView();


    }

    private void initView() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.answers_refresh);
        mStateView = (MultiStateView) findViewById(R.id.answers_multistateview);
        mRecyclerView = (RecyclerView) findViewById(R.id.answers_recyleview);
        mToolbar = (Toolbar) findViewById(R.id.answers_toolbar);

        setTitle(false);

        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener((v) -> {
            intiData(false);
        });

        intiData(true);

    }

    private void intiData(final boolean needState) {
        if(needState) {
            mStateView.setViewState(MultiStateView.ViewState.LOADING);
        }

        final String url = mQuestionUrl;
        Observable.create(new Observable.OnSubscribe<Document>() {

            @Override
            public void call(Subscriber<? super Document> subscriber) {

                try {
                    subscriber.onNext(Jsoup.connect(url + "#zh-question-collapsed-wrap")
                            .timeout(5000)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .get());
                    subscriber.onCompleted();
                }catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        }).map(new Func1<Document, List<AnswersModel>>() {
            @Override
            public List<AnswersModel> call(Document document) {
                Element titleLink = document.getElementById("zh-question-title");
                Element detailLink = document.getElementById("zh-question-detail");
                String title = titleLink.select("span.zm-editable-content").text();
                String detailHtml = detailLink.select("div.zm-editable-content").html();
                AnswersActivity.this.title = title;
                detail = detailHtml;

                return JSoupUtils.getTopAnswers(document);
            }
        }).compose(this.<List<AnswersModel>>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<AnswersModel>>() {
                    @Override
                    public void onCompleted() {
                        if(mStateView.getViewState() != MultiStateView.ViewState.CONTENT) {
                            mStateView.setViewState(MultiStateView.ViewState.CONTENT);

                            mRefreshLayout.setRefreshing(false);

                            initRecylerView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(needState) {
                            mStateView.setViewState(MultiStateView.ViewState.ERROR);
                        }
                        mRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onNext(List<AnswersModel> o) {
                        mAnswersModels.clear();
                        mAnswersModels.addAll(o);
                    }
                });

    }

    private void initRecylerView() {
        if(mRecyclerView.getAdapter() == null) {
            manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(manager);
            mRecyclerView.setAdapter(new BaseAdapter() {
                @Override
                public int getLayoutId(int position) {
                    if(position == 0) {
                        return R.layout.answers_list_top_item_layout;
                    }
                    return R.layout.answers_list_item_layout;
                }

                @Override
                public boolean clickable() {
                    return true;
                }

                @Override
                public void onItemClick(View v, int position) {

                }

                @Override
                public void onBindView(BaseViewHolder holder, int position) {
                    if(position == 0) {
                        holder.setText(R.id.item_fr_answer_top_title, title)
                                .setVisibility(R.id.item_fr_answer_top_body, TextUtils.isEmpty(detail) ? BaseViewHolder.GONE : BaseViewHolder.VISIBLE)
                                .setText(R.id.item_fr_answer_top_body, Html.fromHtml(detail).toString())
                                .setText(R.id.item_fr_answer_top_count, mAnswersModels.size() +  getString(R.string.answers_count_prev)
                                        + mAnswersModels.size() + getString(R.string.answers_count));
                    } else {
                        AnswersModel answer = mAnswersModels.get(position - 1);
                        holder.setText(R.id.item_fr_answer_author,
                                TextUtils.isEmpty(answer.getAuthor()) ? getString(R.string.ansy_name) : answer.getAuthor())
                                .setText(R.id.item_fr_answer_body, answer.getContent())
                                .setText(R.id.item_fr_answer_vote, answer.getVote() + getString(R.string.answer_vote));

                    }
                }

                @Override
                public int getItemCount() {
                    return mAnswersModels.size() + 1;
                }
            });


            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if(manager.findFirstVisibleItemPosition() != 0) {
                        setTitle(true);
                    } else {
                        setTitle(false);
                    }
                }
            });
        } else {
            mRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void setTitle(boolean show) {
        if(show) {
            mToolbar.setTitle(title);
        } else {
            mToolbar.setTitle("");
        }
    }
}
