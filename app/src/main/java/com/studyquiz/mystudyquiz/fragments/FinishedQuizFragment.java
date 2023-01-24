package com.studyquiz.mystudyquiz.fragments;

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

import com.studyquiz.mystudyquiz.R;
import com.studyquiz.mystudyquiz.databinding.AnsweringQuizFragmentBinding;
import com.studyquiz.mystudyquiz.databinding.FinishedQuizFragmentBinding;
import com.studyquiz.mystudyquiz.model.AnswerReportType;
import com.studyquiz.mystudyquiz.viewmodel.QuizViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Locale;

public class FinishedQuizFragment extends Fragment {
    private static final String EVENT_TRY_AGAIN_AFTER_QUIZ_FINISHED = "EVENT_TRY_AGAIN_AFTER_QUIZ_FINISHED";

    private FinishedQuizFragmentBinding binding;
    private QuizViewModel viewModel;
    private OnBackPressedCallback callback;


    private FirebaseAnalytics mFirebaseAnalytics;

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
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext());
        Bundle bundle = new Bundle();
        bundle.putString("action", "try_again");
        mFirebaseAnalytics.logEvent(EVENT_TRY_AGAIN_AFTER_QUIZ_FINISHED, bundle);
        viewModel.currentQuizStartOver();
        callback.remove();
        Navigation.findNavController(requireView()).popBackStack(R.id.answeringQuizFragment, false);
    }

    private void finishClicked() {
        viewModel.clearViewModel();
        callback.remove();
        Navigation.findNavController(requireView()).popBackStack(R.id.my_quizzes, false);
    }

    private void openAnswersReport(AnswerReportType type) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext());
        Bundle bundle = new Bundle();
        bundle.putString("action", "try_again");
        mFirebaseAnalytics.logEvent(EVENT_TRY_AGAIN_AFTER_QUIZ_FINISHED, bundle);
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