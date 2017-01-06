package com.xyl.architectrue.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xyl.architectrue.R;

import static com.xyl.architectrue.view.MultiStateView.ViewState.LOADING;

/**
 * User: ShaudXiao
 * Date: 2017-01-04
 * Time: 11:00
 * Company: zx
 * Description:
 * FIXME
 */


public class MultiStateView extends FrameLayout {

    private static final int UNKNOW_VIEW = -1;
    private static final int CONTENT_VIEW = 0;
    private static final int ERROR_VIEW = 1;
    private static final int EMPTY_VIEW = 2;
    private static final int LOADING_VIEW = 3;

    public enum ViewState {
        CONTENT,
        ERROR,
        EMPTY,
        LOADING
    }

    private LayoutInflater mLayoutInflater;

    private View mContentView;
    private View mErrorView;
    private View mEmptyView;
    private View mLoadingView;

    private ViewState mViewState = ViewState.CONTENT;

    public MultiStateView(Context context) {
        super(context);
    }

    public MultiStateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MultiStateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mLayoutInflater = LayoutInflater.from(getContext());
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.MultiStateView);
        if(ta != null) {
            int loadingResId = ta.getResourceId(R.styleable.MultiStateView_msv_loadingView, -1);
            if(loadingResId > -1) {
                mLoadingView = mLayoutInflater.inflate(loadingResId, this, false);
                addView(mLoadingView, mLoadingView.getLayoutParams());
            }

            int emptyResId = ta.getResourceId(R.styleable.MultiStateView_msv_emptyView, -1);
            if(emptyResId > -1) {
                mEmptyView = mLayoutInflater.inflate(emptyResId, this, false);
                addView(mEmptyView, mEmptyView.getLayoutParams());
            }

            int errorResId = ta.getResourceId(R.styleable.MultiStateView_msv_errorView, -1);
            if(errorResId > -1) {
                mErrorView = mLayoutInflater.inflate(errorResId, this, false);
                addView(mErrorView, mErrorView.getLayoutParams());
            }

            int viewState = ta.getInt(R.styleable.MultiStateView_msv_viewState, UNKNOW_VIEW);
            if(viewState != UNKNOW_VIEW) {
                switch (viewState) {
                    case CONTENT_VIEW:
                        mViewState = ViewState.CONTENT;
                        break;
                    case LOADING_VIEW:
                        mViewState = LOADING;
                        break;
                    case EMPTY_VIEW:
                        mViewState = ViewState.EMPTY;
                        break;
                    case ERROR_VIEW:
                        mViewState = ViewState.ERROR;
                        break;
                }
            }

        }

        ta.recycle();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if(mContentView == null) {
            throw new IllegalArgumentException("Content View is not defined!");
        }

        setView();
    }

    /**
     * All of the addView methods have been overridden so that it can obtain the content
     * view via xml. It is NOT recommand to add views into MultiStateView via the addView
     * method, but rather use any of the setViewForState methods to set views for their given
     * ViewState accordingly
     *
     * */

    @Override
    public void addView(View child) {
        if(isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child);
    }

    @Override
    public void addView(View child, int index) {
        if(isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, index);
    }

    @Override
    public void addView(View child, int width, int height) {
        if(isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, width, height);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        if(isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, params);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if(isValidContentView(child)) {
            mContentView = child;
        }
        super.addView(child, index, params);
    }

    public View getView(ViewState state) {
        switch (state) {
            case CONTENT:
                return mContentView;
            case LOADING:
                return mLoadingView;
            case EMPTY:
                return mEmptyView;
            case ERROR:
                return mErrorView;
            default:
                return null;
        }

    }

    public ViewState getViewState() {
        return mViewState;
    }

    public void setViewState(ViewState state) {
        if(state != mViewState) {
            mViewState = state;
            setView();
        }
    }

    public void setView() {
        switch (mViewState) {
            case LOADING:
                if(mLoadingView == null) {
                    throw new NullPointerException("Loading View");
                }
                mLoadingView.setVisibility(View.VISIBLE);
                if(mContentView != null) mContentView.setVisibility(View.GONE);
                if(mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                if(mErrorView != null) mErrorView.setVisibility(View.GONE);
                break;
            case EMPTY:
                if(mEmptyView == null) {
                    throw new NullPointerException("Empty View");
                }
                mErrorView.setVisibility(View.VISIBLE);
                if(mContentView != null) mContentView.setVisibility(View.GONE);
                if(mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                if(mErrorView != null) mErrorView.setVisibility(View.GONE);
                break;
            case ERROR:
                if(mErrorView == null) {
                    throw new NullPointerException("Error View");
                }
                mErrorView.setVisibility(View.VISIBLE);
                if(mContentView != null) mContentView.setVisibility(View.GONE);
                if(mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                if(mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                break;
            case CONTENT:
            default:
                if(mContentView == null) {
                    throw new NullPointerException("Content View");
                }
                mContentView.setVisibility(View.VISIBLE);
                if(mErrorView != null) mErrorView.setVisibility(View.GONE);
                if(mEmptyView != null) mEmptyView.setVisibility(View.GONE);
                if(mLoadingView != null) mLoadingView.setVisibility(View.GONE);
                break;
        }
    }

    public void setViewForState(View view, ViewState state, boolean switchState) {
        switch (state) {
            case LOADING:
                if(mLoadingView != null) removeView(mLoadingView);
                mLoadingView = view;
                addView(mLoadingView);
                break;
            case EMPTY:
                if(mEmptyView != null) removeView(mEmptyView);
                mEmptyView = view;
                addView(mEmptyView);
                break;
            case ERROR:
                if(mErrorView != null) removeView(mErrorView);
                mErrorView = view;
                addView(mErrorView);
                break;
            case CONTENT:
                if(mContentView != null) removeView(mContentView);
                mContentView = view;
                addView(mContentView);
                break;
        }

        if(switchState) {
            setViewState(state);
        }
    }

    public void setViewForState(View view, ViewState state) {
        setViewForState(view, state, false);
    }

    public void setViewForState(int layoutResId, ViewState state, boolean switchState) {
        if(mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(getContext());
        }
        View view = mLayoutInflater.inflate(layoutResId, this, false);
        setViewForState(view, state, switchState);

    }

    public void setViewForState(int layoutResId, ViewState state) {
        setViewForState(layoutResId, state, false);
    }
    /**
     * Checks if the given {@link View} is vaild for the content view
     *
     */

    private boolean isValidContentView(View view) {
        if(mContentView != null && mContentView != view) {
            return false;
        }

        return view != mLoadingView && view != mEmptyView && view != mEmptyView;
    }
}

