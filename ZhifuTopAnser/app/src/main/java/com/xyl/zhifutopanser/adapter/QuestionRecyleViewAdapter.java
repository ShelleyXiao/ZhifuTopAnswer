package com.xyl.zhifutopanser.adapter;

import android.text.TextUtils;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xyl.architectrue.adapter.BaseAdapter;
import com.xyl.architectrue.adapter.BaseViewHolder;
import com.xyl.architectrue.utils.Utils;
import com.xyl.zhifutopanser.Model.TopQuestion;
import com.xyl.zhifutopanser.R;

import java.util.List;

/**
 * User: ShaudXiao
 * Date: 2017-01-05
 * Time: 14:48
 * Company: zx
 * Description:
 * FIXME
 */


public class QuestionRecyleViewAdapter extends BaseAdapter {

    private List<TopQuestion> mTopQuestions;

    private onQuestionItemClickListener mQuestionItemClickListener;

    public QuestionRecyleViewAdapter(List<TopQuestion> topQuestions) {
        mTopQuestions = topQuestions;
    }

    public void setQuestionItemClickListener(onQuestionItemClickListener questionItemClickListener) {
        mQuestionItemClickListener = questionItemClickListener;
    }

    @Override
    public int getLayoutId(int position) {
        return R.layout.quenstion_item_layout;
    }

    @Override
    public boolean clickable() {
        return true;
    }

    @Override
    public void onItemClick(View v, int position) {
        if(null != mQuestionItemClickListener) {
            mQuestionItemClickListener.onQuestionItemClick(position);
        }
    }

    @Override
    public void onBindView(BaseViewHolder holder, int position) {
        TopQuestion question = mTopQuestions.get(position);
        holder.setText(R.id.question_top_title_desc, question.getTitle())
                .setText(R.id.question_top_body, question.getBody())
                .setText(R.id.question_top_vote, question.getVote() + " " + Utils.getContext().getResources().getString(R.string.answer_vote));
        SimpleDraweeView simpleDraweeView = holder.getView(R.id.question_top_img_desc);
        if(TextUtils.isEmpty(question.getImgUrl())) {
            simpleDraweeView.setVisibility(View.GONE);
        } else {
            simpleDraweeView.setVisibility(View.VISIBLE);
            simpleDraweeView.setImageURI(question.getImgUrl());
        }
    }

    @Override
    public int getItemCount() {
        return mTopQuestions == null ? 0 : mTopQuestions.size();
    }

    public interface onQuestionItemClickListener {
        void onQuestionItemClick(int position);
    }
}
