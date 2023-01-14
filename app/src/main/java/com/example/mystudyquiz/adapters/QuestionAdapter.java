package com.example.mystudyquiz.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import com.example.mystudyquiz.R;
import com.example.mystudyquiz.databinding.QuestionsItemBinding;
import com.example.mystudyquiz.model.Question;
import com.example.mystudyquiz.model.QuestionList;
import com.example.mystudyquiz.model.QuestionType;
import com.example.mystudyquiz.model.Quiz;
import com.google.android.material.card.MaterialCardView;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private QuestionList mDataSet;
    private Quiz quiz;
    private Context context;
    private QuestionDeleteCallback questionDeleteCallback;

    public interface QuestionDeleteCallback{
        void onQuestionDelete(Question question);
    }

    public QuestionAdapter(QuestionList mDataSet, Quiz selectedQuiz,Context context,QuestionDeleteCallback questionDeleteCallback) {
        this.mDataSet = mDataSet;
        quiz = selectedQuiz;
        this.context = context;
        this.questionDeleteCallback = questionDeleteCallback;
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
        ((MaterialCardView) holder.binding.getRoot()).setCardBackgroundColor(quiz.getBackgroundColor());

        holder.binding.getRoot().setOnLongClickListener(view -> {
            YoYo.with(Techniques.Pulse)
                    .duration(500)
                    .playOn(holder.binding.getRoot());
            PopupMenu popupMenu = new PopupMenu(context, holder.binding.getRoot());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                popupMenu.setForceShowIcon(true);
            }
            popupMenu.inflate(R.menu.quiz_long_click_menu);
            popupMenu.setOnMenuItemClickListener(menuItem -> {

                if (menuItem.getItemId() == R.id.delete_quiz)
                    questionDeleteCallback.onQuestionDelete(mDataSet.getQuestionList().get(position));
                return true;
            });
            popupMenu.show();
            return true;
        });

        holder.binding.questionTypeImage.setImageResource(mDataSet.getQuestionList().get(position).getQuestionType() == QuestionType.BOOLEAN ? R.drawable.ic_true_or_false :R.drawable.ic_multiple_choice );
        if (quiz.getBackgroundColor() == Color.WHITE ||
                quiz.getBackgroundColor() == Color.CYAN ||
                quiz.getBackgroundColor() == Color.LTGRAY ||
                quiz.getBackgroundColor() == Color.YELLOW ||
                quiz.getBackgroundColor() == Color.GREEN) {
            holder.binding.questionTv.setTextColor(Color.BLACK);
            holder.binding.questionTypeImage.getDrawable().setTint(Color.BLACK);
        } else {
            holder.binding.questionTv.setTextColor(Color.WHITE);
            holder.binding.questionTypeImage.getDrawable().setTint(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet.getSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        QuestionsItemBinding binding;

        public ViewHolder(@NonNull QuestionsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(Question obj) {
            binding.setQuestion(obj);
            binding.executePendingBindings();
        }
    }
}
