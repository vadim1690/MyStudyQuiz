package com.example.mystudyquiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;

import com.example.mystudyquiz.AppUtils;
import com.example.mystudyquiz.R;
import com.example.mystudyquiz.databinding.AddAnswersToQuestionFragmentBinding;
import com.example.mystudyquiz.databinding.AddNewQuestionFragmentBinding;
import com.example.mystudyquiz.model.Answer;
import com.example.mystudyquiz.model.AnswerList;
import com.example.mystudyquiz.model.BooleanQuestion;
import com.example.mystudyquiz.model.MultipleChoiceQuestion;
import com.example.mystudyquiz.model.Question;
import com.example.mystudyquiz.viewmodel.QuizViewModel;

import java.util.Arrays;


public class AddAnswersToQuestionFragment extends Fragment {
    private AddAnswersToQuestionFragmentBinding binding;
    private QuizViewModel viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = AddAnswersToQuestionFragmentBinding.inflate(inflater, container, false);
        //set variables in Binding
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewModel(view);
        setBinding();
        setViewByQuestionType();
        setOnClickListeners();

    }

    private void setViewByQuestionType() {
        switch (viewModel.getNewQuestionToAdd().getQuestionType()) {
            case BOOLEAN:
                binding.multipleChoiceAnswersLayout.setVisibility(View.GONE);
                binding.booleanAnswersLayout.setVisibility(View.VISIBLE);
                break;
            case MULTIPLE_CHOICE:
                binding.multipleChoiceAnswersLayout.setVisibility(View.VISIBLE);
                binding.booleanAnswersLayout.setVisibility(View.GONE);
                break;
        }
    }

    private void setBinding() {
        binding.setQuiz(viewModel.getSelectedQuiz());
        binding.setQuestion(viewModel.getNewQuestionToAdd());
    }


    private void setViewModel(View view) {
        NavBackStackEntry backStackEntry = Navigation.findNavController(view).getBackStackEntry(R.id.my_quizzes_navigation);
        viewModel = new ViewModelProvider(backStackEntry).get(QuizViewModel.class);

    }

    private void setOnClickListeners() {
        binding.createBtn.setOnClickListener(v -> createNewQuestion());
        binding.booleanAnswerTrue.setOnClickListener(v -> booleanAnswerTrueClicked());
        binding.booleanAnswerFalse.setOnClickListener(v -> booleanAnswerFalseClicked());
    }

    private void booleanAnswerFalseClicked() {
        binding.booleanAnswerFalse.setIcon(AppCompatResources.getDrawable(requireContext(), R.drawable.correct));
        binding.booleanAnswerTrue.setIcon(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_wrong));
        binding.booleanAnswerFalse.setSelected(true);
        binding.booleanAnswerTrue.setSelected(false);
    }

    private void booleanAnswerTrueClicked() {
        binding.booleanAnswerTrue.setIcon(AppCompatResources.getDrawable(requireContext(), R.drawable.correct));
        binding.booleanAnswerFalse.setIcon(AppCompatResources.getDrawable(requireContext(), R.drawable.ic_wrong));
        binding.booleanAnswerFalse.setSelected(false);
        binding.booleanAnswerTrue.setSelected(true);
    }

    private void createNewQuestion() {
        Question question = viewModel.getNewQuestionToAdd();
        Answer correct = null;
        switch (question.getQuestionType()) {
            case MULTIPLE_CHOICE:
                MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) question;
                correct = new Answer(binding.correctAnswer1TextEdt.getText().toString());
                multipleChoiceQuestion.setAnswerList(getMultipleChoiceAnswerList());
                break;

            case BOOLEAN:
                BooleanQuestion booleanQuestion = (BooleanQuestion) question;
                correct = getBooleanAnswer(true);
                booleanQuestion.setWrongBooleanAnswer(getBooleanAnswer(false));
                break;

        }

        question.setCorrectAnswer(correct);
        viewModel.addNewQuestionToCurrentQuiz(question);
        finishQuizCreation();
    }

    private AnswerList getMultipleChoiceAnswerList() {
        return new AnswerList(
                Arrays.asList(
                        new Answer(binding.correctAnswer1TextEdt.getText().toString()),
                        new Answer(binding.answer2TextEdt.getText().toString()),
                        new Answer(binding.answer3TextEdt.getText().toString()),
                        new Answer(binding.answer4TextEdt.getText().toString())
                ));
    }

    private Answer getBooleanAnswer(boolean getCorrect) {
        if (binding.booleanAnswerTrue.isSelected())
            return getCorrect ? new Answer(String.valueOf(true)) : new Answer(String.valueOf(false));
        else if (binding.booleanAnswerFalse.isSelected())
            return getCorrect ? new Answer(String.valueOf(false)) : new Answer(String.valueOf(true));
        return null;
    }

    private void finishQuizCreation() {
        Navigation.findNavController(requireView()).navigate(AddAnswersToQuestionFragmentDirections.actionAddAnswersToQuestionFragmentToQuestionsFragment());
    }

}
