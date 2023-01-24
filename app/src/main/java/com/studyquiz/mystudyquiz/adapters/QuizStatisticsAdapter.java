package com.studyquiz.mystudyquiz.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.studyquiz.mystudyquiz.R;
import com.studyquiz.mystudyquiz.data.entities.QuizStatistic;
import com.studyquiz.mystudyquiz.databinding.QuizStatisticsItemBinding;


import java.util.List;

public class QuizStatisticsAdapter extends RecyclerView.Adapter<QuizStatisticsAdapter.ViewHolder> {


    private List<QuizStatistic> mDataSet;


    public QuizStatisticsAdapter(List<QuizStatistic> mDataSet) {
        this.mDataSet = mDataSet;
    }

    @NonNull
    @Override
    public QuizStatisticsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuizStatisticsItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.quiz_statistics_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public QuizStatisticsItemBinding binding;

        public ViewHolder(@NonNull QuizStatisticsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.detailsLayout.setVisibility(View.GONE);
            binding.quizNameBtn.setOnClickListener(view -> {
                if(binding.detailsLayout.getVisibility() == View.VISIBLE)
                    binding.detailsLayout.setVisibility(View.GONE);
                else
                    binding.detailsLayout.setVisibility(View.VISIBLE);
            });
        }




        public void bind(QuizStatistic obj) {
            binding.setQuizStatistics(obj);
            binding.executePendingBindings();
        }

    }
}
