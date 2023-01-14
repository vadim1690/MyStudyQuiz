package com.example.mystudyquiz.fragments;

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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;

import com.example.mystudyquiz.Constants;
import com.example.mystudyquiz.utils.AppUtils;
import com.example.mystudyquiz.BuildConfig;
import com.example.mystudyquiz.databinding.QuestionsFragmentBinding;
import com.example.mystudyquiz.adapters.QuestionAdapter;
import com.example.mystudyquiz.R;
import com.example.mystudyquiz.model.Question;
import com.example.mystudyquiz.model.QuestionList;
import com.example.mystudyquiz.model.VideoAd;
import com.example.mystudyquiz.viewmodel.QuizViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

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
        initializeAds();
    }



    private void initializeAds() {
        showAdBanner();
    }

    private void showAdBanner() {

        String UNIT_ID = BuildConfig.ADMOB_BANNER_AD_ID;

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
    }

    private void addQuestionClicked() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constants.GENERAL_SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean(Constants.GENERAL_SHARED_PREFERENCES_PRO_SUBSCRIPTION_KEY, false) && quizViewModel.getSelectedQuiz().getSize() == quizViewModel.getSelectedQuiz().getCapacity()) {
            AppUtils.showAlertDialog(requireContext(), "Reached Quiz full questions capacity\n( " +  quizViewModel.getSelectedQuiz().getCapacity() +" questions ) , would you like to pay 0.99$ for adding one more to the quiz questions capacity ? ","Reached Quiz full questions capacity", new AppUtils.AlertDialogCallback() {
                @Override
                public void onPositiveButtonClicked() {
                    AppUtils.showToast(requireContext(),"Purchased 1 more quiz questions capacity");
                    quizViewModel.addToCurrentQuizCapacity(1);
                    Navigation.findNavController(requireView()).navigate(QuestionsFragmentDirections.actionQuestionsFragmentToAddNewQuestionFragment());
                }

                @Override
                public void onNegativeButtonClicked() {
                    AppUtils.showToast(requireContext(),"Purchase canceled");
                }
            });
        } else {
            if(quizViewModel.getSelectedQuiz().getSize() == quizViewModel.getSelectedQuiz().getCapacity())
                quizViewModel.addToCurrentQuizCapacity(1);
            Navigation.findNavController(requireView()).navigate(QuestionsFragmentDirections.actionQuestionsFragmentToAddNewQuestionFragment());
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
        } else {
            binding.quizNameTv.setTextColor(Color.WHITE);
            binding.startQuizBtn.setIconTint(ColorStateList.valueOf(Color.WHITE));
            binding.startQuizBtn.setTextColor(Color.WHITE);
            binding.addQuestionBtn.getDrawable().setTint(Color.WHITE);
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

}
