package com.example.mystudyquiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.mystudyquiz.R;
import com.example.mystudyquiz.databinding.AnsweringQuizFragmentBinding;
import com.example.mystudyquiz.databinding.FinishedQuizFragmentBinding;
import com.example.mystudyquiz.model.AnswerReportType;
import com.example.mystudyquiz.viewmodel.QuizViewModel;
import com.owl93.dpb.CircularProgressView;

import java.util.Locale;

public class FinishedQuizFragment extends Fragment {
    private FinishedQuizFragmentBinding binding;
    private QuizViewModel viewModel;
    private OnBackPressedCallback callback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FinishedQuizFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewModel();
        setProgressCircular();
        setOnClickListeners();
        setOnBackPressedCallback();
        //setClickListeners();


    }

    private void setOnBackPressedCallback() {
        callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // TODO: Show dialog to the user if he is sure he wants to leave the quiz.
                this.remove();
                finishClicked();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
    }

    private void setOnClickListeners() {
        binding.totalQuestionsCardView.setOnClickListener(view -> openAnswersReport(AnswerReportType.ALL_ANSWERS));
        binding.correctQuestionsCardView.setOnClickListener(view -> openAnswersReport(AnswerReportType.CORRECT_ANSWERS));
        binding.wrongQuestionsCardView.setOnClickListener(view -> openAnswersReport(AnswerReportType.WRONG_ANSWERS));
        binding.finishBtn.setOnClickListener(view -> finishClicked());
        binding.tryAgainBtn.setOnClickListener(view -> tryAgainClicked());
    }

    private void tryAgainClicked() {
        viewModel.currentQuizStartOver();
        callback.remove();
        Navigation.findNavController(requireView()).navigate(FinishedQuizFragmentDirections.actionFinishedQuizFragmentToAnsweringQuizFragment());
    }

    private void finishClicked() {
        callback.remove();
        Navigation.findNavController(requireView()).navigate(FinishedQuizFragmentDirections.actionFinishedQuizFragmentToMyQuizzes());
    }

    private void openAnswersReport(AnswerReportType type) {
        if (viewModel.setAnswersReportType(type))
            Navigation.findNavController(requireView()).navigate(FinishedQuizFragmentDirections.actionFinishedQuizFragmentToAnswersReportBottomDialog());
    }

    private void setProgressCircular() {
        binding.progressCircular.setProgress(viewModel.getCorrectAnswersCounter());
        binding.progressCircular.setMaxValue(viewModel.getSelectedQuiz().getSize());
        binding.progressCircular.setTextEnabled(true);
        double score = ((double) viewModel.getCorrectAnswersCounter() / (double) viewModel.getSelectedQuiz().getSize()) * 100;
        //binding.progressCircular.setText(String.format(Locale.getDefault(), "%d/%d", viewModel.getCorrectAnswersCounter(), viewModel.getSelectedQuiz().getSize()));
        binding.progressCircular.setText(String.format(Locale.getDefault(), "Your Score is %d", (int) score)); // TODO: Convert to string resource

    }

    private void setViewModel() {
        viewModel = new ViewModelProvider(Navigation.findNavController(requireView()).getBackStackEntry(R.id.my_quizzes_navigation)).get(QuizViewModel.class);
        binding.setViewModel(viewModel);
    }


}