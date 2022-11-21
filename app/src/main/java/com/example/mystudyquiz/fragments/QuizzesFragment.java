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

import com.example.mystudyquiz.R;
import com.example.mystudyquiz.adapters.QuizAdapter;
import com.example.mystudyquiz.databinding.QuizzesFragmentBinding;
import com.example.mystudyquiz.model.Quiz;
import com.example.mystudyquiz.viewmodel.QuizViewModel;

public class QuizzesFragment extends Fragment {
    private QuizzesFragmentBinding binding;
    private QuizViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = QuizzesFragmentBinding.inflate(inflater, container, false);
        //set variables in Binding
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewModel(view);
        binding.addQuizBtn.setOnClickListener(v -> addNewQuizClicked());

    }

    private void addNewQuizClicked() {
        Navigation.findNavController(requireView()).navigate(QuizzesFragmentDirections.actionMyQuizzesToAddNewQuizFragment());
    }

    private void setViewModel(View view) {
        NavBackStackEntry backStackEntry = Navigation.findNavController(view).getBackStackEntry(R.id.my_quizzes_navigation);
        viewModel = new ViewModelProvider(backStackEntry).get(QuizViewModel.class);
        viewModel.getQuizzes().observe(requireActivity(), quizzes -> binding.setQuizAdapter(new QuizAdapter(quizzes, QuizzesFragment.this::quizClicked)));
    }

    /**
     * sets the quiz as the current one and pass it to the next window.
     *
     * @param quiz the selected quiz.
     */
    private void quizClicked(Quiz quiz) {
        viewModel.setSelectedQuiz(quiz);
        Navigation.findNavController(requireView()).navigate(QuizzesFragmentDirections.actionMyQuizzesToQuestionsFragment());
    }


}
