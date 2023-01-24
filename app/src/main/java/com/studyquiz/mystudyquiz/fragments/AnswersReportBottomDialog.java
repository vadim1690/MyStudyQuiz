package com.studyquiz.mystudyquiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.studyquiz.mystudyquiz.R;
import com.studyquiz.mystudyquiz.adapters.AnswersReportAdapter;
import com.studyquiz.mystudyquiz.databinding.AnswersReportBottomDialogBinding;
import com.studyquiz.mystudyquiz.model.AnswerReportType;
import com.studyquiz.mystudyquiz.model.QuestionList;
import com.studyquiz.mystudyquiz.viewmodel.QuizViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.analytics.FirebaseAnalytics;

public class AnswersReportBottomDialog extends BottomSheetDialogFragment {

    private static final String EVENT_OPEN_ANSWERS_REPORT = "EVENT_OPEN_ANSWERS_REPORT";
    private AnswersReportBottomDialogBinding binding;
    private QuizViewModel viewModel;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AnswersReportBottomDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext());
        Bundle bundle = new Bundle();
        bundle.putString("action", "open");
        mFirebaseAnalytics.logEvent(EVENT_OPEN_ANSWERS_REPORT, bundle);

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

    private Callback callback;
    public interface Callback{
        void call();
    }
    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
