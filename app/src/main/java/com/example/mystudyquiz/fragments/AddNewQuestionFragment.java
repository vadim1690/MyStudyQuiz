package com.example.mystudyquiz.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;

import com.example.mystudyquiz.AppUtils;
import com.example.mystudyquiz.R;
import com.example.mystudyquiz.databinding.AddNewQuestionFragmentBinding;
import com.example.mystudyquiz.databinding.AddNewQuizFragmentBinding;
import com.example.mystudyquiz.model.BooleanQuestion;
import com.example.mystudyquiz.model.MultipleChoiceQuestion;
import com.example.mystudyquiz.model.Question;
import com.example.mystudyquiz.model.QuestionType;
import com.example.mystudyquiz.model.Quiz;
import com.example.mystudyquiz.viewmodel.QuizViewModel;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;


public class AddNewQuestionFragment extends Fragment {
    private AddNewQuestionFragmentBinding binding;
    private QuizViewModel viewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = AddNewQuestionFragmentBinding.inflate(inflater, container, false);
        //set variables in Binding
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewModel(view);
        setBinding();
        setQuestionTypes();
        setOnClickListeners();

    }

    private void setBinding() {
        binding.setQuiz(viewModel.getSelectedQuiz());
    }

    private void setQuestionTypes() {
        String[] types = new String[]{"Multiple choice(4 Options)", "Boolean(True/False)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_menu_popup_item, types);
        binding.questionTypeDropdown.setAdapter(adapter);
        binding.questionTypeDropdown.setOnItemClickListener((parent, view, position, id) -> {
            switch (position){
                case 0:
                    viewModel.setNewQuestionToAddType(QuestionType.MULTIPLE_CHOICE);
                    break;
                case 1:
                    viewModel.setNewQuestionToAddType(QuestionType.BOOLEAN);
                    break;
            }

        });

    }

    private void setViewModel(View view) {
        NavBackStackEntry backStackEntry = Navigation.findNavController(view).getBackStackEntry(R.id.my_quizzes_navigation);
        viewModel = new ViewModelProvider(backStackEntry).get(QuizViewModel.class);

    }

    private void setOnClickListeners() {
        binding.continueBtn.setOnClickListener(v -> continueToAddingAnswers());

    }

    private void continueToAddingAnswers() {
        if (!validateInput()) {
            AppUtils.showAlertDialog(requireContext(), "Please enter question text");
            return;
        }
        viewModel.setNewQuestionToAdd(getQuestionBySelectedType());
        Navigation.findNavController(requireView()).navigate(AddNewQuestionFragmentDirections.actionAddNewQuestionFragmentToAddAnswersToQuestionFragment());

    }

    private Question getQuestionBySelectedType() {
        switch (viewModel.getNewQuestionToAddType()) {
            case MULTIPLE_CHOICE: return new MultipleChoiceQuestion(binding.questionTextEdt.getText().toString());

            case BOOLEAN: return new BooleanQuestion(binding.questionTextEdt.getText().toString());
        }

        return null;
    }

    private boolean validateInput() {
        return !binding.questionTextEdt.toString().isEmpty();
    }
}
