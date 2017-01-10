package com.xyl.zhifutopanser.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trello.rxlifecycle.android.ActivityEvent;
import com.xyl.architectrue.rxsupport.RxAppCompatActivity;
import com.xyl.architectrue.utils.LogUtils;
import com.xyl.architectrue.view.MultiStateView;
import com.xyl.zhifutopanser.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * User: ShaudXiao
 * Date: 2017-01-09
 * Time: 15:43
 * Company: zx
 * Description:
 * FIXME
 */


public class AnwersDetailActivity extends RxAppCompatActivity {

    public static final String ANSWERS_URL = "answer_url";
    public static final String ANSWERS_TITLE = "answer_title";
    public static final String ANSWERS_DETAIL = "answer_detail";

    private String mAnswerUrl;
    private String mAnswerTitle;
    private String mAnswerDetail;

    private String mAnswerBodyHtml;

    private TextView tvDetail;
    private MultiStateView mStateView;
    private WebView mWebView;
    private View mPaddingView;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.answers_detail_layout);

        mAnswerUrl = "http://www.zhihu.com" + getIntent().getExtras().getString(ANSWERS_URL);
        mAnswerTitle = getIntent().getExtras().getString(ANSWERS_TITLE);
        mAnswerDetail = getIntent().getExtras().getString(ANSWERS_DETAIL);

        initView();
        initStateView();
        initData(true);
    }

    private void initStateView() {
        LinearLayout linearLayout = (LinearLayout) mStateView.findViewById(R.id.error_layout);
        linearLayout.setClickable(true);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData(true);
            }
        });
    }

    private void initView() {

        mToolbar = (Toolbar) findViewById(R.id.answers_datail_toolbar);
        tvDetail = (TextView) findViewById(R.id.answers_detail_detail);
        mStateView = (MultiStateView) findViewById(R.id.answers_detail_multistateview);
        mWebView = (WebView) findViewById(R.id.answers_detail_webview);
        mPaddingView = findViewById(R.id.answers_detail_padding);

        mToolbar.setTitle(mAnswerTitle);
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back));
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener((v) -> {
            onBackPressed();
        } );

        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setBuiltInZoomControls(false);

        if(TextUtils.isEmpty(mAnswerDetail)) {
            mPaddingView.setVisibility(View.GONE);
            tvDetail.setVisibility(View.GONE);
        } else {
            mPaddingView.setVisibility(View.VISIBLE);
            tvDetail.setVisibility(View.VISIBLE);
            tvDetail.setText(Html.fromHtml(mAnswerDetail).toString());
        }
    }

    private void initData(final boolean needState) {
        if(needState) {
            mStateView.setViewState(MultiStateView.ViewState.LOADING);
        }

        final String url = mAnswerUrl;
        LogUtils.e("url: " + url);
        Observable.create(new Observable.OnSubscribe<Document>() {

            @Override
            public void call(Subscriber<? super Document> subscriber) {

                try {
                    subscriber.onNext(Jsoup.connect(url).timeout(5000)
                            .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                            .get());
                    subscriber.onCompleted();
                }catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        }).map(new Func1<Document, String>() {
            @Override
            public String call(Document document) {
                Element bodyAnswer = document.getElementById("zh-question-answer-wrap");
                Elements bodys = bodyAnswer.select("div.zm-item-answer");
                Elements headElements = document.getElementsByTag("head");
                headElements.iterator().next();
                String head = headElements.iterator().next().outerHtml();

                String html = "";
                if (bodys.iterator().hasNext()) {
                    Iterator iterator = bodys.iterator();
                    if (iterator.hasNext()) {
                        Element element = (Element) iterator.next();
                        String body = "<body>" + element.select("div.zm-item-rich-text.expandable.js-collapse-body").iterator().next().outerHtml() + "</body>";
                        html = "<html lang=\"en\" xmlns:o=\"http://www.w3.org/1999/xhtml\">" + head + body + "</html>";

                        Document docu = Jsoup.parse(html);
                        Elements elements = docu.getElementsByTag("img");
                        Iterator iter = elements.iterator();
                        while (iter.hasNext()) {
                            Element imgElement = (Element) iter.next();
                            String result = imgElement.attr("data-actualsrc");
                            if (TextUtils.isEmpty(result)) {
                                result = imgElement.attr("data-original");
                            }
                            imgElement.attr("src", result);
                        }
                        html = docu.outerHtml();
                        return html;

                    }
                }
                return "";
            }
        }).compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                if(mStateView.getViewState() != MultiStateView.ViewState.CONTENT) {
                    mStateView.setViewState(MultiStateView.ViewState.CONTENT);

                    if(mAnswerBodyHtml.length() > 100000) {
                        toast(getString(R.string.webview_loading));
                    }

                    mWebView.loadDataWithBaseURL("httl://www.zhihu.com" , mAnswerBodyHtml, "text/html", "utf-8", null);
                }
            }

            @Override
            public void onError(Throwable e) {
                if(needState) {
                    mStateView.setViewState(MultiStateView.ViewState.ERROR);
                }
            }

            @Override
            public void onNext(String s) {
                mAnswerBodyHtml = s;
                LogUtils.e(" s = " + s);
            }
        });
    }
}
