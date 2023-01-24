package com.studyquiz.mystudyquiz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.studyquiz.InApp_ID;
import com.studyquiz.mystudyquiz.BuildConfig;
import com.studyquiz.mystudyquiz.Constants;
import com.studyquiz.mystudyquiz.MyInApp;
import com.studyquiz.mystudyquiz.utils.AppUtils;
import com.studyquiz.mystudyquiz.R;
import com.studyquiz.mystudyquiz.databinding.AddNewQuizFragmentBinding;
import com.studyquiz.mystudyquiz.model.Quiz;
import com.studyquiz.mystudyquiz.viewmodel.QuizViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.Objects;



public class AddNewQuizFragment extends Fragment {
    private AddNewQuizFragmentBinding binding;
    private QuizViewModel viewModel;
    private Integer selectedBackgroundColor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = AddNewQuizFragmentBinding.inflate(inflater, container, false);
        //set variables in Binding
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewModel(view);
        setOnClickListeners();
        showAdBanner();
    }

    private void showAdBanner() {
        if (MyInApp.getInstance().getPurchaseValueFromPref(InApp_ID.PRO_SUBSCRIPTION))
            return;

        String UNIT_ID = BuildConfig.ADMOB_BANNER_AD_ID_ADD_NEW_QUIZ;

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

    private void setViewModel(View view) {
        NavBackStackEntry backStackEntry = Navigation.findNavController(view).getBackStackEntry(R.id.my_quizzes_navigation);
        viewModel = new ViewModelProvider(backStackEntry).get(QuizViewModel.class);

    }

    private void setOnClickListeners() {
        binding.pickColorButton.setOnClickListener(view -> pickColor());
        binding.createQuizBtn.setOnClickListener(v -> createQuizClicked());
    }

    private void pickColor() {
        AppUtils.colorPickerBottomSheet(color -> {
            selectedBackgroundColor = color;
            binding.pickColorButton.setBackgroundColor(color);
        }, getParentFragmentManager());
    }

    private void createQuizClicked() {
        if (!validateInput())
            return;
        viewModel.addNewQuiz(new Quiz(Objects.requireNonNull(binding.quizNameEdt.getText()).toString(), selectedBackgroundColor));
        Navigation.findNavController(requireView()).popBackStack();
    }

    private boolean validateInput() {
        if (TextUtils.isEmpty(binding.quizNameEdt.getText())) {
            AppUtils.showAlertDialog(requireContext(), "Please enter quiz name!");
            return false;
        }

        if (selectedBackgroundColor == null)
            selectedBackgroundColor = Color.WHITE;
        return true;

    }

}
