package com.example.mystudyquiz.fragments;

import static com.example.mystudyquiz.model.QuestionType.BOOLEAN;
import static com.example.mystudyquiz.model.QuestionType.MULTIPLE_CHOICE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;

import com.example.mystudyquiz.utils.AppUtils;
import com.example.mystudyquiz.R;
import com.example.mystudyquiz.databinding.AddNewQuestionFragmentBinding;
import com.example.mystudyquiz.model.Question;
import com.example.mystudyquiz.viewmodel.QuizViewModel;


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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.dropdown_menu_popup_item, getResources().getStringArray(R.array.question_types));

        binding.questionTypeDropdown.setAdapter(adapter);
        binding.questionTypeDropdown.setOnItemClickListener((parent, view, position, id) -> {
            switch (position) {
                case 0:
                    viewModel.setNewQuestionToAddType(MULTIPLE_CHOICE);
                    break;
                case 1:
                    viewModel.setNewQuestionToAddType(BOOLEAN);
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
            return;
        }
        viewModel.setNewQuestionToAdd(new Question(binding.questionTextEdt.getText().toString(), viewModel.getNewQuestionToAddType()));
        Navigation.findNavController(requireView()).navigate(AddNewQuestionFragmentDirections.actionAddNewQuestionFragmentToAddAnswersToQuestionFragment());

    }


    @Override
    public void onResume() {
        super.onResume();
        setQuestionTypes();
    }

    private boolean validateInput() {
        if (binding.questionTextEdt.toString().isEmpty()) {
            AppUtils.showAlertDialog(requireContext(), "Please enter question text");
            return false;
        }

        if (viewModel.getNewQuestionToAddType() == null) {
            AppUtils.showAlertDialog(requireContext(), "Please choose question type");
            return false;
        }
        return true;
    }


}
