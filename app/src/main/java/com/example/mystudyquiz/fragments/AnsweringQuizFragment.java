package com.example.mystudyquiz.fragments;

import android.graphics.Color;
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
import com.example.mystudyquiz.model.Answer;
import com.example.mystudyquiz.model.MultipleChoiceQuestion;
import com.example.mystudyquiz.model.Question;
import com.example.mystudyquiz.viewmodel.QuizViewModel;
import com.google.android.material.button.MaterialButton;


public class AnsweringQuizFragment extends Fragment {
    private AnsweringQuizFragmentBinding binding;
    private QuizViewModel viewModel;
    private OnBackPressedCallback callback;
    private Answer selectedAnswer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = AnsweringQuizFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewModel();
        setOnBackPressedCallback();
        setClickListeners();
        setQuiz();

    }

    private void setClickListeners() {
        binding.skipBtn.setOnClickListener(view -> skipClicked());
        binding.nextBtn.setOnClickListener(view -> answerSelected(selectedAnswer));
        binding.answerButton1.setOnClickListener(view -> multipleChoiceButtonClicked(binding.answerButton1));
        binding.answerButton2.setOnClickListener(view -> multipleChoiceButtonClicked(binding.answerButton2));
        binding.answerButton3.setOnClickListener(view -> multipleChoiceButtonClicked(binding.answerButton3));
        binding.answerButton4.setOnClickListener(view -> multipleChoiceButtonClicked(binding.answerButton4));
    }

    private void multipleChoiceButtonClicked(MaterialButton answerButton) {
        selectedAnswer = viewModel.getAnswerByText(answerButton.getText().toString());
        Answer correctAnswer = viewModel.getCurrentCorrectAnswer();
        if(selectedAnswer.equals(correctAnswer)){
            // button green
            answerButton.setBackgroundColor(Color.GREEN);
        }else{
            // button red
            answerButton.setBackgroundColor(Color.RED);
            setCorrectButton(correctAnswer);
        }
        disableMultipleChoiceButtons();
    }

    private void disableMultipleChoiceButtons() {
        binding.answerButton1.setEnabled(false);
        binding.answerButton2.setEnabled(false);
        binding.answerButton3.setEnabled(false);
        binding.answerButton4.setEnabled(false);
        changeSkipButtonToNext();

    }

    private void changeSkipButtonToNext() {
        binding.skipBtn.setVisibility(View.GONE);
        binding.nextBtn.setVisibility(View.VISIBLE);
    }

    private void changeNextButtonToSkip() {
        binding.skipBtn.setVisibility(View.VISIBLE);
        binding.nextBtn.setVisibility(View.GONE);
    }

    private void setCorrectButton(Answer correctAnswer) {
        if(binding.answerButton1.getText().toString().equalsIgnoreCase(correctAnswer.getText()))
            binding.answerButton1.setBackgroundColor(Color.GREEN);
        if(binding.answerButton2.getText().toString().equalsIgnoreCase(correctAnswer.getText()))
            binding.answerButton2.setBackgroundColor(Color.GREEN);
        if(binding.answerButton3.getText().toString().equalsIgnoreCase(correctAnswer.getText()))
            binding.answerButton3.setBackgroundColor(Color.GREEN);
        if(binding.answerButton4.getText().toString().equalsIgnoreCase(correctAnswer.getText()))
            binding.answerButton4.setBackgroundColor(Color.GREEN);

    }

    /**
     * sets on back pressed callback to handle pressing back option during a quiz.
     */
    private void setOnBackPressedCallback() {
        callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // TODO: Show dialog to the user if he is sure he wants to leave the quiz.
                this.remove();
                Navigation.findNavController(requireView()).navigate(AnsweringQuizFragmentDirections.actionAnsweringQuizFragmentToMyQuizzes());
                //Navigation.findNavController(requireView()).popBackStack(R.id.my_quizzes,false);
                //Navigation.findNavController(requireView()).clearBackStack(R.id.my_quizzes);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
    }

    /**
     * Initialize the view model and set all the relevant value needed.
     */
    private void setViewModel() {
        viewModel = new ViewModelProvider(Navigation.findNavController(requireView()).getBackStackEntry(R.id.my_quizzes_navigation)).get(QuizViewModel.class);
    }

    /**
     * sets the view for a new quiz
     */
    private void setQuiz() {
        binding.quizNameTv.setText(viewModel.getSelectedQuiz().getName());
        bindQuestionToView(viewModel.getNextQuestionForQuiz());
    }

    private void skipClicked() {
        answerSelected(null);
    }


    private void finishQuiz() {
        callback.remove();
        Navigation.findNavController(requireView()).navigate(AnsweringQuizFragmentDirections.actionAnsweringQuizFragmentToFinishedQuizFragment());
    }

    /**
     * binds the current question to the view
     *
     * @param question the current question to bind.
     */
    private void bindQuestionToView(Question question) {
        restoreButtons();
        selectedAnswer = null;
        if(question instanceof MultipleChoiceQuestion)
            binding.setMultipleChoiceQuestion((MultipleChoiceQuestion) question);
    }

    private void restoreButtons() {
        binding.answerButton1.setBackgroundColor(Color.parseColor("#D6D6D6"));
        binding.answerButton2.setBackgroundColor(Color.parseColor("#D6D6D6"));
        binding.answerButton3.setBackgroundColor(Color.parseColor("#D6D6D6"));
        binding.answerButton4.setBackgroundColor(Color.parseColor("#D6D6D6"));
        binding.answerButton1.setEnabled(true);
        binding.answerButton2.setEnabled(true);
        binding.answerButton3.setEnabled(true);
        binding.answerButton4.setEnabled(true);
        changeNextButtonToSkip();
    }

    private void answerSelected(Answer answer) {
        viewModel.computeSelectedAnswer(answer);
        continueTheQuiz();
    }

    private void continueTheQuiz() {
        if (viewModel.isQuizFinished())
            finishQuiz();
        else
            bindQuestionToView(viewModel.getNextQuestionForQuiz());
    }
}
