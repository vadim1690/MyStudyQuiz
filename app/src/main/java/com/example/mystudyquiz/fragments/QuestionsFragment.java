package com.example.mystudyquiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;

import com.example.mystudyquiz.databinding.QuestionsFragmentBinding;
import com.example.mystudyquiz.adapters.QuestionAdapter;
import com.example.mystudyquiz.R;
import com.example.mystudyquiz.viewmodel.QuizViewModel;

public class QuestionsFragment extends Fragment {
    private QuestionsFragmentBinding binding;
    private QuizViewModel quizViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = QuestionsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewModel(view);
        setQuestions();
        setOnClickListeners();

    }

    private void setOnClickListeners() {
        binding.startQuizBtn.setOnClickListener(v -> startQuizClicked());
        binding.addQuestionBtn.setOnClickListener(v -> addQuestionClicked());
    }

    private void addQuestionClicked() {
        Navigation.findNavController(requireView()).navigate(QuestionsFragmentDirections.actionQuestionsFragmentToAddNewQuestionFragment());
    }

    private void setQuestions() {
        binding.setQuestionsAdapter(new QuestionAdapter(quizViewModel.getSelectedQuiz().getQuestions()));
        binding.quizNameTv.setText(quizViewModel.getSelectedQuiz().getName());
    }

    private void setViewModel(View view) {
        NavBackStackEntry backStackEntry = Navigation.findNavController(view).getBackStackEntry(R.id.my_quizzes_navigation);
        quizViewModel = new ViewModelProvider(backStackEntry).get(QuizViewModel.class);
    }

    private void startQuizClicked() {
        Navigation.findNavController(requireView()).navigate(QuestionsFragmentDirections.actionQuestionsFragmentToAnsweringQuizFragment());
    }


}
