package com.example.mystudyquiz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;

import com.example.mystudyquiz.Constants;
import com.example.mystudyquiz.utils.AppUtils;
import com.example.mystudyquiz.R;
import com.example.mystudyquiz.databinding.AddAnswersToQuestionFragmentBinding;
import com.example.mystudyquiz.model.Answer;
import com.example.mystudyquiz.model.Question;
import com.example.mystudyquiz.viewmodel.QuizViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.Arrays;


public class AddAnswersToQuestionFragment extends Fragment {
    private AddAnswersToQuestionFragmentBinding binding;
    private QuizViewModel viewModel;

    private InterstitialAd mInterstitialAd;

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
        initializeAds();

    }

    private void initializeAds() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(requireContext(), "ca-app-pub-3940256099942544/1033173712", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        mInterstitialAd = null;
                    }
                });
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
                if (!validateMultiChoiceAnswers()) {
                    AppUtils.showAlertDialog(requireContext(), "Please enter text for all 4 answers text fields");
                    return;
                }
                correct = new Answer(binding.correctAnswer1TextEdt.getText().toString(), question.getQuestionId());
                question.setAnswerList(Arrays.asList(
                        correct,
                        new Answer(binding.answer2TextEdt.getText().toString(), viewModel.getNewQuestionToAdd().getQuestionId()),
                        new Answer(binding.answer3TextEdt.getText().toString(), viewModel.getNewQuestionToAdd().getQuestionId()),
                        new Answer(binding.answer4TextEdt.getText().toString(), viewModel.getNewQuestionToAdd().getQuestionId())
                ));
                break;

            case BOOLEAN:
                if (!validateBooleanAnswers()) {
                    AppUtils.showAlertDialog(requireContext(), "Please select correct and wrong answers by clicking them ");
                    return;
                }
                Answer wrong = getBooleanAnswer(false);
                correct = getBooleanAnswer(true);
                question.setAnswerList(Arrays.asList(correct, wrong));
                break;
        }

        if (correct != null) {
            question.setCorrectAnswer(correct);
        }
        viewModel.addNewQuestionToCurrentQuiz(question);
        finishQuizCreation();
    }

    private boolean validateMultiChoiceAnswers() {
        return !TextUtils.isEmpty(binding.correctAnswer1TextEdt.getText()) &&
                !TextUtils.isEmpty(binding.answer2TextEdt.getText()) &&
                !TextUtils.isEmpty(binding.answer3TextEdt.getText()) &&
                !TextUtils.isEmpty(binding.answer4TextEdt.getText());
    }

    private boolean validateBooleanAnswers() {
        return binding.booleanAnswerTrue.isSelected() || binding.booleanAnswerFalse.isSelected();
    }


    private Answer getBooleanAnswer(boolean getCorrect) {
        if (binding.booleanAnswerTrue.isSelected())
            return getCorrect ? new Answer(String.valueOf(true), viewModel.getNewQuestionToAdd().getQuestionId()) : new Answer(String.valueOf(false), viewModel.getNewQuestionToAdd().getQuestionId());
        else if (binding.booleanAnswerFalse.isSelected())
            return getCorrect ? new Answer(String.valueOf(false), viewModel.getNewQuestionToAdd().getQuestionId()) : new Answer(String.valueOf(true), viewModel.getNewQuestionToAdd().getQuestionId());
        return null;
    }

    private void finishQuizCreation() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constants.GENERAL_SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(Constants.GENERAL_SHARED_PREFERENCES_PRO_SUBSCRIPTION_KEY, false)) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(requireActivity());
            }
            requireView().postDelayed(() -> Navigation.findNavController(requireView()).popBackStack(R.id.questionsFragment, false), 300);
        } else {
            Navigation.findNavController(requireView()).popBackStack(R.id.questionsFragment, false);
        }
    }

}
