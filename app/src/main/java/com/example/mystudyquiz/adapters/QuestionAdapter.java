package com.example.mystudyquiz.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystudyquiz.BR;
import com.example.mystudyquiz.R;
import com.example.mystudyquiz.databinding.QuestionsItemBinding;
import com.example.mystudyquiz.model.QuestionList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder>{
    private QuestionList mDataSet;
    public QuestionAdapter(QuestionList mDataSet) {
        this.mDataSet = mDataSet;
    }

    @NonNull
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuestionsItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.questions_item, parent, false);
        return new QuestionAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.ViewHolder holder, int position) {
        holder.bind(mDataSet.getQuestionList().get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.getSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        QuestionsItemBinding binding;
        public ViewHolder(@NonNull QuestionsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(Object obj) {
            binding.setVariable(BR.question, obj);
            binding.executePendingBindings();
        }
    }
}
