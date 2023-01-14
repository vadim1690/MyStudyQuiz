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
import com.example.mystudyquiz.databinding.QuizzesItemBinding;
import com.example.mystudyquiz.model.Quiz;
import com.example.mystudyquiz.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class QuizAdapter extends RecyclerView.Adapter<QuizAdapter.ViewHolder> {


    private List<Quiz> mDataSet;
    private Context context;
    private QuizClickedCallback quizClickedCallback;
    private QuizDeleteCallback quizDeleteCallback;

    public interface QuizClickedCallback {
        void onQuizClicked(Quiz quiz);

    }

    public interface QuizDeleteCallback {
        void onQuizDelete(Quiz quiz);

    }

    public QuizAdapter(List<Quiz> mDataSet,Context context, QuizClickedCallback quizClickedCallback, QuizDeleteCallback quizDeleteCallback) {
        this.mDataSet = mDataSet;
        this.context = context;
        this.quizClickedCallback = quizClickedCallback;
        this.quizDeleteCallback = quizDeleteCallback;
    }

    @NonNull
    @Override
    public QuizAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuizzesItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.quizzes_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Quiz quiz = mDataSet.get(position);
        holder.bind(quiz);

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
                    quizDeleteCallback.onQuizDelete(quiz);
                return true;
            });
            popupMenu.show();
            return true;
        });

        holder.binding.getRoot().setOnClickListener(view -> {
            if (quizClickedCallback != null)
                quizClickedCallback.onQuizClicked(quiz);
        });
        ((MaterialCardView)holder.binding.getRoot()).setCardBackgroundColor(quiz.getBackgroundColor());
        if(quiz.getBackgroundColor() == Color.WHITE ||
                quiz.getBackgroundColor() == Color.CYAN ||
                quiz.getBackgroundColor() == Color.LTGRAY ||
                quiz.getBackgroundColor() == Color.YELLOW ||
                quiz.getBackgroundColor() == Color.GREEN){
            holder.binding.questionsText.setTextColor(Color.BLACK);
            holder.binding.quizNameTv.setTextColor(Color.BLACK);
            holder.binding.quizSizeTv.setTextColor(Color.BLACK);
        }else{
            holder.binding.questionsText.setTextColor(Color.WHITE);
            holder.binding.quizNameTv.setTextColor(Color.WHITE);
            holder.binding.quizSizeTv.setTextColor(Color.WHITE);
        }


    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public QuizzesItemBinding binding;

        public ViewHolder(@NonNull QuizzesItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }


        public void bind(Quiz obj) {
            binding.setQuiz(obj);
            binding.executePendingBindings();
        }

    }
}
