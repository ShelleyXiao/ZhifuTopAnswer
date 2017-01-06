package com.xyl.architectrue.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import static com.xyl.architectrue.view.LoadMoreRecyleView.LAYOUT_MANAGER_LAYOUT.GRID;
import static com.xyl.architectrue.view.LoadMoreRecyleView.LAYOUT_MANAGER_LAYOUT.LINEAR;
import static com.xyl.architectrue.view.LoadMoreRecyleView.LAYOUT_MANAGER_LAYOUT.STAGGERED_GRID;

/**
 * User: ShaudXiao
 * Date: 2017-01-04
 * Time: 09:51
 * Company: zx
 * Description:
 * FIXME
 */


public class LoadMoreRecyleView extends RecyclerView {

    //监听到底部的接口
    private onLoadMoreListener mLoadMoreListener;

    //正在加载
    public static final int STATE_START_LOADMORE = 0x01;

    //加载完毕
    public static final int STATE_FINISH_LOADMORE = 0X02;

    //是否可以加载
    public boolean canLoadMore = true;

    //  加载更多状态
    private int loadMoreState ;


    //layoutmanager类型
    private LAYOUT_MANAGER_LAYOUT layoutManagerType;

    //最后一个item位置
    private int[] lastPosition;

    //最后一个可见item位置
    private int lastVisiableItemPosition;

    //当前滑动的状态
    private int currentScrollState = 0;

    public static enum LAYOUT_MANAGER_LAYOUT  {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }

    public LoadMoreRecyleView(Context context) {
        super(context);
    }

    public LoadMoreRecyleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadMoreRecyleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setOnScrollListener(OnScrollListener listener) {
        super.setOnScrollListener(listener);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        LayoutManager layoutManager = getLayoutManager();
        if(layoutManager != null) {
            if(layoutManager instanceof LinearLayoutManager) {
                layoutManagerType = LINEAR;
            } else if(layoutManager instanceof GridLayoutManager) {
                layoutManagerType = GRID;
            } else if(layoutManager instanceof StaggeredGridLayoutManager) {
                layoutManagerType = STAGGERED_GRID;
            } else {
                throw new RuntimeException("not support layoutmanager!");
            }
        }

        switch (layoutManagerType) {
            case LINEAR:
                lastVisiableItemPosition = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                break;
            case GRID:
                lastVisiableItemPosition = ((GridLayoutManager)layoutManager).findLastVisibleItemPosition();
                break;
            case STAGGERED_GRID:
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager)layoutManager;
                if(lastPosition == null) {
                    lastPosition = new int[manager.getSpanCount()];
                }
                manager.findLastVisibleItemPositions(lastPosition);
                lastVisiableItemPosition = findMax(lastPosition);
                break;
        }
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);
        if(!canLoadMore) {
            return;
        }
        if(loadMoreState == STATE_START_LOADMORE) {
            return;
        }
        currentScrollState = state;
        LayoutManager manager = getLayoutManager();
        int visiableItem = manager.getChildCount();
        int toatalItem = manager.getItemCount();
        if(visiableItem > 0 && currentScrollState == RecyclerView.SCROLL_STATE_IDLE
                && visiableItem >= toatalItem -1) {
            loadMoreState = STATE_START_LOADMORE;
            mLoadMoreListener.loadMore();
        }

    }

    public void setLoadMoreState(int loadMoreState) {
        this.loadMoreState = loadMoreState;
    }

    public onLoadMoreListener getLoadMoreListener() {
        return mLoadMoreListener;
    }

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public void setLoadMoreListener(onLoadMoreListener loadMoreListener) {
        mLoadMoreListener = loadMoreListener;
    }

    public interface onLoadMoreListener {
        void loadMore();
    }

    private int findMax(int[] position) {
        int max = position[0];
        for(int value : position) {
            if(value > max) {
                max = value;
            }
        }

        return max;
    }
}
