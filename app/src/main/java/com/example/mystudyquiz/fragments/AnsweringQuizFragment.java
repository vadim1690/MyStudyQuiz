package com.example.mystudyquiz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.mystudyquiz.Constants;
import com.example.mystudyquiz.utils.AppUtils;
import com.example.mystudyquiz.R;
import com.example.mystudyquiz.databinding.AnsweringQuizFragmentBinding;
import com.example.mystudyquiz.model.Answer;
import com.example.mystudyquiz.model.Question;
import com.example.mystudyquiz.viewmodel.QuizViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.button.MaterialButton;


public class AnsweringQuizFragment extends Fragment {
    private AnsweringQuizFragmentBinding binding;
    private QuizViewModel viewModel;
    private OnBackPressedCallback callback;

    private InterstitialAd mInterstitialAd;

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

    private void setClickListeners() {
        binding.exitQuizButton.setOnClickListener(v -> requireActivity().onBackPressed());
        binding.skipBtn.setOnClickListener(view -> skipClicked());
        binding.nextBtn.setOnClickListener(view -> nextClicked());
        binding.answerButton1.setOnClickListener(view -> multipleChoiceButtonClicked(binding.answerButton1));
        binding.answerButton2.setOnClickListener(view -> multipleChoiceButtonClicked(binding.answerButton2));
        binding.answerButton3.setOnClickListener(view -> multipleChoiceButtonClicked(binding.answerButton3));
        binding.answerButton4.setOnClickListener(view -> multipleChoiceButtonClicked(binding.answerButton4));
        binding.booleanAnswerButton1.setOnClickListener(view -> multipleChoiceButtonClicked(binding.booleanAnswerButton1));
        binding.booleanAnswerButton2.setOnClickListener(view -> multipleChoiceButtonClicked(binding.booleanAnswerButton2));
    }

    private void multipleChoiceButtonClicked(MaterialButton answerButton) {

        Answer selectedAnswer = viewModel.getAnswerByText(answerButton.getText().toString());
        Answer correctAnswer = viewModel.getCurrentCorrectAnswer();
        if (selectedAnswer.equals(correctAnswer)) {
            // button green
            answerButton.setBackgroundColor(Color.GREEN);
            YoYo.with(Techniques.Pulse)
                    .duration(500)
                    .playOn(answerButton);
        } else {
            // button red
            answerButton.setBackgroundColor(Color.RED);
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(answerButton);
            setCorrectButton(correctAnswer);
        }
        viewModel.setSelectedAnswer(selectedAnswer);
        disableMultipleChoiceButtons();
        disableBooleanAnswersButtons();
        changeSkipButtonToNext();
    }

    private void disableBooleanAnswersButtons() {
        binding.booleanAnswerButton1.setEnabled(false);
        binding.booleanAnswerButton2.setEnabled(false);
    }

    private void disableMultipleChoiceButtons() {
        binding.answerButton1.setEnabled(false);
        binding.answerButton2.setEnabled(false);
        binding.answerButton3.setEnabled(false);
        binding.answerButton4.setEnabled(false);
    }

    private void changeSkipButtonToNext() {
        binding.skipBtn.setVisibility(View.GONE);
        binding.nextBtn.setVisibility(View.VISIBLE);
        if (viewModel.isQuizFinished())
            binding.nextBtn.setText("Finish");
    }

    private void changeNextButtonToSkip() {
        binding.skipBtn.setVisibility(View.VISIBLE);
        binding.nextBtn.setVisibility(View.GONE);
    }

    private void setCorrectButton(Answer correctAnswer) {
        if (binding.answerButton1.getText().toString().equalsIgnoreCase(correctAnswer.getText()))
            binding.answerButton1.setBackgroundColor(Color.GREEN);
        if (binding.answerButton2.getText().toString().equalsIgnoreCase(correctAnswer.getText()))
            binding.answerButton2.setBackgroundColor(Color.GREEN);
        if (binding.answerButton3.getText().toString().equalsIgnoreCase(correctAnswer.getText()))
            binding.answerButton3.setBackgroundColor(Color.GREEN);
        if (binding.answerButton4.getText().toString().equalsIgnoreCase(correctAnswer.getText()))
            binding.answerButton4.setBackgroundColor(Color.GREEN);
        if (binding.booleanAnswerButton1.getText().toString().equalsIgnoreCase(correctAnswer.getText()))
            binding.booleanAnswerButton1.setBackgroundColor(Color.GREEN);
        if (binding.booleanAnswerButton2.getText().toString().equalsIgnoreCase(correctAnswer.getText()))
            binding.booleanAnswerButton2.setBackgroundColor(Color.GREEN);

    }

    /**
     * sets on back pressed callback to handle pressing back option during a quiz.
     */
    private void setOnBackPressedCallback() {
        callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                AppUtils.showAlertDialog(requireContext(), "Are you sure you want to exit the quiz ?\nall current quiz data will not be saved.", new AppUtils.AlertDialogCallback() {
                    @Override
                    public void onPositiveButtonClicked() {
                        callback.remove();
                        viewModel.clearViewModel();
                        Navigation.findNavController(requireView()).popBackStack(R.id.my_quizzes, false);
                    }

                    @Override
                    public void onNegativeButtonClicked() {
                        // Do nothing...
                    }
                });

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
        binding.progress.setMax(viewModel.getSelectedQuiz().getSize());
        bindQuestionToView(viewModel.getNextQuestionForQuiz());
    }

    private void skipClicked() {
        viewModel.setSelectedAnswer(null);
        nextClicked();
    }


    private void finishQuiz() {
        viewModel.finishQuiz();
        callback.remove();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constants.GENERAL_SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(Constants.GENERAL_SHARED_PREFERENCES_PRO_SUBSCRIPTION_KEY, false)) {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(requireActivity());
            }
            requireView().postDelayed(() -> Navigation.findNavController(requireView()).navigate(AnsweringQuizFragmentDirections.actionAnsweringQuizFragmentToFinishedQuizFragment()), 300);
        } else {
            Navigation.findNavController(requireView()).navigate(AnsweringQuizFragmentDirections.actionAnsweringQuizFragmentToFinishedQuizFragment());
        }
    }

    /**
     * binds the current question to the view
     *
     * @param question the current question to bind.
     */
    private void bindQuestionToView(Question question) {
        binding.progress.setProgress((viewModel.getCurrentQuestionIndex() + 1));
        binding.progressText.setText((viewModel.getCurrentQuestionIndex() + 1) + "/" + viewModel.getSelectedQuiz().getSize());
        viewModel.sortCurrentQuestionAnswersRandomly();
        restoreButtons();
        viewModel.setSelectedAnswer(null);
        binding.setQuestion(question);
        YoYo.with(Techniques.BounceIn)
                .duration(1000)
                .playOn(binding.questionView);

        switch (question.getQuestionType()) {
            case MULTIPLE_CHOICE:
                binding.multipleChoiceAnswersLayout.setVisibility(View.VISIBLE);
                binding.booleanAnswersLayout.setVisibility(View.GONE);
                binding.answerButton1.setText(question.getAnswerList().get(0).getText());
                binding.answerButton2.setText(question.getAnswerList().get(1).getText());
                binding.answerButton3.setText(question.getAnswerList().get(2).getText());
                binding.answerButton4.setText(question.getAnswerList().get(3).getText());
                YoYo.with(Techniques.ZoomIn)
                        .duration(500)
                        .playOn(binding.answerButton1);
                YoYo.with(Techniques.ZoomIn)
                        .duration(500)
                        .playOn(binding.answerButton2);
                YoYo.with(Techniques.ZoomIn)
                        .duration(500)
                        .playOn(binding.answerButton3);
                YoYo.with(Techniques.ZoomIn)
                        .duration(500)
                        .playOn(binding.answerButton4);
                break;
            case BOOLEAN:
                binding.multipleChoiceAnswersLayout.setVisibility(View.GONE);
                binding.booleanAnswersLayout.setVisibility(View.VISIBLE);
                binding.booleanAnswerButton1.setText(question.getAnswerList().get(0).getText());
                binding.booleanAnswerButton2.setText(question.getAnswerList().get(1).getText());


                YoYo.with(Techniques.ZoomIn)
                        .duration(500)
                        .playOn(binding.booleanAnswerButton1);
                YoYo.with(Techniques.ZoomIn)
                        .duration(500)
                        .playOn(binding.booleanAnswerButton2);

                break;
        }

    }

    private void restoreButtons() {
        binding.answerButton1.setBackgroundColor(Color.parseColor("#D6D6D6"));
        binding.answerButton2.setBackgroundColor(Color.parseColor("#D6D6D6"));
        binding.answerButton3.setBackgroundColor(Color.parseColor("#D6D6D6"));
        binding.answerButton4.setBackgroundColor(Color.parseColor("#D6D6D6"));
        binding.booleanAnswerButton1.setBackgroundColor(Color.parseColor("#D6D6D6"));
        binding.booleanAnswerButton2.setBackgroundColor(Color.parseColor("#D6D6D6"));
        binding.answerButton1.setEnabled(true);
        binding.answerButton2.setEnabled(true);
        binding.answerButton3.setEnabled(true);
        binding.answerButton4.setEnabled(true);
        binding.booleanAnswerButton1.setEnabled(true);
        binding.booleanAnswerButton2.setEnabled(true);
        changeNextButtonToSkip();
    }

    private void nextClicked() {
        viewModel.computeSelectedAnswer();
        continueTheQuiz();
    }

    private void continueTheQuiz() {
        if (viewModel.isQuizFinished())
            finishQuiz();
        else {
            bindQuestionToView(viewModel.getNextQuestionForQuiz());
        }

    }
}
