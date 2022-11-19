package com.example.mystudyquiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.mystudyquiz.R;
import com.example.mystudyquiz.adapters.AnswersReportAdapter;
import com.example.mystudyquiz.databinding.AnswersReportBottomDialogBinding;
import com.example.mystudyquiz.model.AnswerReportType;
import com.example.mystudyquiz.model.QuestionList;
import com.example.mystudyquiz.viewmodel.QuizViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AnswersReportBottomDialog extends BottomSheetDialogFragment {
    private AnswersReportBottomDialogBinding binding;
    private QuizViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AnswersReportBottomDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setViewModel();
        setAnswersReports();
    }

    private void setAnswersReports() {
        switch (viewModel.getAnswersReportType()) {
            case ALL_ANSWERS:
                binding.answersReportsRv.setAdapter(new AnswersReportAdapter(viewModel.getSelectedQuiz().getQuestions(), null, AnswerReportType.ALL_ANSWERS));
                break;

            case CORRECT_ANSWERS:
                binding.answersReportsRv.setAdapter(new AnswersReportAdapter(new QuestionList(viewModel.getCorrectAnswers().keySet()), viewModel.getCorrectAnswers(), AnswerReportType.CORRECT_ANSWERS));
                break;

            case WRONG_ANSWERS:
                binding.answersReportsRv.setAdapter(new AnswersReportAdapter(new QuestionList(viewModel.getWrongAnswers().keySet()), viewModel.getWrongAnswers(), AnswerReportType.WRONG_ANSWERS));
                break;
        }

    }

    private void setViewModel() {
        viewModel = new ViewModelProvider(NavHostFragment.findNavController(this).getBackStackEntry(R.id.my_quizzes_navigation)).get(QuizViewModel.class);
    }


}
