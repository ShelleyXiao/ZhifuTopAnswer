package com.xyl.zhifutopanser.adapter;

import android.view.View;

import com.xyl.architectrue.adapter.BaseAdapter;
import com.xyl.architectrue.adapter.BaseViewHolder;
import com.xyl.zhifutopanser.Model.AnswersModel;

import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-01-06
 * Time: 10:47
 * Company: zx
 * Description:
 * FIXME
 */


public class AnswersAdapter extends BaseAdapter {

    private List<AnswersModel> mAnswerDatas;

    public AnswersAdapter(List<AnswersModel> datas) {
        this.mAnswerDatas = datas;
    }

    @Override
    public int getLayoutId(int position) {
        return 0;
    }

    @Override
    public boolean clickable() {
        return false;
    }

    @Override
    public void onItemClick(View v, int position) {

    }

    @Override
    public void onBindView(BaseViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
