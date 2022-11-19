package com.example.mystudyquiz.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystudyquiz.BR;
import com.example.mystudyquiz.databinding.QuizzesItemBinding;
import com.example.mystudyquiz.model.Quiz;
import com.example.mystudyquiz.R;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {


    private List<Quiz> mDataSet;
    private QuizClickedCallback quizClickedCallback;

    public interface QuizClickedCallback {
        void onQuizClicked(Quiz quiz);

    }

    public QuizAdapter(List<Quiz> mDataSet, QuizClickedCallback quizClickedCallback) {
        this.mDataSet = mDataSet;
        this.quizClickedCallback = quizClickedCallback;
    }

    @NonNull
    @Override
    public QuizAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuizzesItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.quizzes_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizAdapter.ViewHolder holder, int position) {
        holder.bind(mDataSet.get(position));
        holder.binding.getRoot().setOnClickListener(view -> {
            if (quizClickedCallback != null)
                quizClickedCallback.onQuizClicked(mDataSet.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public QuizzesItemBinding binding;

        public ViewHolder(@NonNull QuizzesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }

        public void bind(Object obj) {
            binding.setVariable(BR.quiz, obj);
            binding.executePendingBindings();
        }
    }
}
