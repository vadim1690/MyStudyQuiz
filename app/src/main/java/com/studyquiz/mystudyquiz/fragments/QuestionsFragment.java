package com.studyquiz.mystudyquiz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;

import com.studyquiz.InApp_ID;
import com.studyquiz.mystudyquiz.App;
import com.studyquiz.mystudyquiz.Constants;
import com.studyquiz.mystudyquiz.MyInApp;
import com.studyquiz.mystudyquiz.utils.AppUtils;
import com.studyquiz.mystudyquiz.BuildConfig;
import com.studyquiz.mystudyquiz.databinding.QuestionsFragmentBinding;
import com.studyquiz.mystudyquiz.adapters.QuestionAdapter;
import com.studyquiz.mystudyquiz.R;
import com.studyquiz.mystudyquiz.model.Question;
import com.studyquiz.mystudyquiz.model.QuestionList;
import com.studyquiz.mystudyquiz.viewmodel.QuizViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class QuestionsFragment extends Fragment {
    private QuestionsFragmentBinding binding;
    private QuizViewModel quizViewModel;

    private final MyInApp.CallBack_MyInApp inAppCallback = new MyInApp.CallBack_MyInApp() {
        @Override
        public void successfullyPurchased(boolean isPurchasedNow, String purchaseKey) {
            AppUtils.showToast(requireContext(), "Purchased 1 more quiz questions capacity");
            if (quizViewModel.getSelectedQuiz().getSize() == quizViewModel.getSelectedQuiz().getCapacity()) {
                quizViewModel.addToCurrentQuizCapacity(1);
                Navigation.findNavController(requireView()).navigate(QuestionsFragmentDirections.actionQuestionsFragmentToAddNewQuestionFragment());
            } else if (quizViewModel.getSelectedQuiz().getSize() > quizViewModel.getSelectedQuiz().getCapacity()) {
                AppUtils.showToast(requireContext(), "Purchased 1 more quiz questions capacity, the questions capacity is still less than current questions number.");
            }

        }

        @Override
        public void purchaseFailed(String purchaseKey, int code, String message) {

        }

        @Override
        public void details(boolean isInAppExist, String title, String description, String price, long priceMic) {

        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyInApp.getInstance().addCallBack(InApp_ID.MORE_QUESTIONS, inAppCallback);
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
        initializeAds();
    }


    private void initializeAds() {
        if (!MyInApp.getInstance().getPurchaseValueFromPref(InApp_ID.PRO_SUBSCRIPTION))
            showAdBanner();
    }

    private void showAdBanner() {

        String UNIT_ID = BuildConfig.ADMOB_BANNER_AD_ID_QUESTIONS;

        AdView adView = new AdView(requireContext());
        adView.setAdUnitId(UNIT_ID);
        binding.bannerLayout.addView(adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);
    }

    private AdSize getAdSize() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = requireActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(requireContext(), adWidth);
    }

    private void setOnClickListeners() {
        binding.startQuizBtn.setOnClickListener(v -> startQuizClicked());
        binding.addQuestionBtn.setOnClickListener(v -> addQuestionClicked());
        binding.helpBtn.setOnClickListener(v -> help());
    }

    private void help() {
        AppUtils.showAlertDialog(requireActivity(), "Information", "Current Quiz questions capacity is " + quizViewModel.getSelectedQuiz().getCapacity() );
    }

    private void addQuestionClicked() {

        if (MyInApp.getInstance().getPurchaseValueFromPref(InApp_ID.PRO_SUBSCRIPTION)) {
            Navigation.findNavController(requireView()).navigate(QuestionsFragmentDirections.actionQuestionsFragmentToAddNewQuestionFragment());
            return;
        }
        if (quizViewModel.getSelectedQuiz().getSize() >= quizViewModel.getSelectedQuiz().getCapacity()) {
            handleFullQuestionsCapacity();

        } else {
            Navigation.findNavController(requireView()).navigate(QuestionsFragmentDirections.actionQuestionsFragmentToAddNewQuestionFragment());
        }


    }

    private void handleFullQuestionsCapacity() {
        if (quizViewModel.getSelectedQuiz().getCapacity() < quizViewModel.getSelectedQuiz().getSize()) {
            AppUtils.showAlertDialog(requireActivity(), "Number of current questions ( " + quizViewModel.getSelectedQuiz().getSize() + " )\n" +
                    "is bigger than the Number of this quiz maximum questions capacity ( " + quizViewModel.getSelectedQuiz().getCapacity() + " )\n" +
                    "you can buy Quiz questions capacity to match the current number of questions or delete questions to match the current number of quiz questions capacity ", "Attention", new AppUtils.AlertDialogCallback() {
                @Override
                public void onPositiveButtonClicked() {
                    MyInApp.getInstance().consume((AppCompatActivity) requireActivity(), InApp_ID.MORE_QUESTIONS);
                }

                @Override
                public void onNegativeButtonClicked() {

                }
            });
        } else {
            MyInApp.getInstance().consume((AppCompatActivity) requireActivity(), InApp_ID.MORE_QUESTIONS);
        }
    }

    private void setQuestions() {
        quizViewModel.getQuestionsForQuiz(quizViewModel.getSelectedQuiz().getQuizId()).observe(getViewLifecycleOwner(), questions -> {
            binding.setQuestionsAdapter(new QuestionAdapter(new QuestionList(questions), quizViewModel.getSelectedQuiz(), requireContext(), this::questionDeleted));
            binding.setQuiz(quizViewModel.getSelectedQuiz());
        });
        binding.headerLayout.setBackgroundColor(quizViewModel.getSelectedQuiz().getBackgroundColor());
        binding.addQuestionBtn.setBackgroundTintList(ColorStateList.valueOf(quizViewModel.getSelectedQuiz().getBackgroundColor()));
        binding.startQuizBtn.setBackgroundTintList(ColorStateList.valueOf(quizViewModel.getSelectedQuiz().getBackgroundColor()));
        if (quizViewModel.getSelectedQuiz().getBackgroundColor() == Color.WHITE ||
                quizViewModel.getSelectedQuiz().getBackgroundColor() == Color.CYAN ||
                quizViewModel.getSelectedQuiz().getBackgroundColor() == Color.LTGRAY ||
                quizViewModel.getSelectedQuiz().getBackgroundColor() == Color.YELLOW ||
                quizViewModel.getSelectedQuiz().getBackgroundColor() == Color.GREEN) {
            binding.quizNameTv.setTextColor(Color.BLACK);
            binding.startQuizBtn.setIconTint(ColorStateList.valueOf(Color.BLACK));
            binding.startQuizBtn.setTextColor(Color.BLACK);
            binding.addQuestionBtn.getDrawable().setTint(Color.BLACK);
            binding.helpBtn.getDrawable().setTint(Color.BLACK);
        } else {
            binding.quizNameTv.setTextColor(Color.WHITE);
            binding.startQuizBtn.setIconTint(ColorStateList.valueOf(Color.WHITE));
            binding.startQuizBtn.setTextColor(Color.WHITE);
            binding.addQuestionBtn.getDrawable().setTint(Color.WHITE);
            binding.helpBtn.getDrawable().setTint(Color.WHITE);
        }

    }

    private void questionDeleted(Question question) {
        quizViewModel.deleteQuestionFromCurrentQuiz(question);
    }

    private void setViewModel(View view) {
        NavBackStackEntry backStackEntry = Navigation.findNavController(view).getBackStackEntry(R.id.my_quizzes_navigation);
        quizViewModel = new ViewModelProvider(backStackEntry).get(QuizViewModel.class);
    }

    private void startQuizClicked() {
        if (quizViewModel.getSelectedQuiz().getSize() == 0) {
            AppUtils.showAlertDialog(requireContext(), "The are currently no questions for this quiz, try to add some questions using the '+' button.");
            return;
        }
        quizViewModel.startCurrentQuiz(() -> Navigation.findNavController(requireView()).navigate(QuestionsFragmentDirections.actionQuestionsFragmentToAnsweringQuizFragment()));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyInApp.getInstance().removeCallBack(InApp_ID.MORE_QUESTIONS, inAppCallback);
    }

}
