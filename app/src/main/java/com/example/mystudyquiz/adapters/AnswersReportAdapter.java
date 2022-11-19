package com.example.mystudyquiz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystudyquiz.BR;
import com.example.mystudyquiz.R;
import com.example.mystudyquiz.databinding.AnswersReportItemBinding;
import com.example.mystudyquiz.databinding.QuestionsItemBinding;
import com.example.mystudyquiz.model.Answer;
import com.example.mystudyquiz.model.AnswerReportType;
import com.example.mystudyquiz.model.Question;
import com.example.mystudyquiz.model.QuestionList;

import java.util.Map;

public class AnswersReportAdapter extends RecyclerView.Adapter<AnswersReportAdapter.ViewHolder>  {
    private QuestionList mDataSet;
    private Map<Question, Answer> answers;
    private AnswerReportType answerReportType;

    public AnswersReportAdapter(QuestionList mDataSet, Map<Question, Answer> answers, AnswerReportType answerReportType) {
        this.mDataSet = mDataSet;
        this.answers = answers;
        this.answerReportType = answerReportType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AnswersReportItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.answers_report_item, parent, false);
        return new AnswersReportAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.bind(mDataSet.getQuestionList().get(position),answers != null ? answers.get(mDataSet.getQuestionList().get(position)) : null);
    }

    @Override
    public int getItemCount() {
        return mDataSet.getSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        AnswersReportItemBinding binding;
        public ViewHolder(@NonNull AnswersReportItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Question question,@Nullable Answer answer) {
            binding.setQuestion(question);
            if(answer != null)
                binding.setWrongAnswer(answer);
            switch (answerReportType){
                case ALL_ANSWERS:
                    binding.wrongAnswerLayout.setVisibility(View.GONE);
                    binding.correctAnswerLayout.setVisibility(View.GONE);
                    break;

                case CORRECT_ANSWERS:
                    binding.wrongAnswerLayout.setVisibility(View.GONE);
                    binding.correctAnswerLayout.setVisibility(View.VISIBLE);
                    break;

                case WRONG_ANSWERS:
                    binding.wrongAnswerLayout.setVisibility(View.VISIBLE);
                    binding.correctAnswerLayout.setVisibility(View.VISIBLE);
                    break;

            }
            binding.executePendingBindings();
        }
    }
}
