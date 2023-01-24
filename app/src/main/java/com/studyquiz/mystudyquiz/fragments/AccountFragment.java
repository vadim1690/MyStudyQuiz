package com.studyquiz.mystudyquiz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.studyquiz.InApp_ID;
import com.studyquiz.mystudyquiz.BuildConfig;
import com.studyquiz.mystudyquiz.MyInApp;
import com.studyquiz.mystudyquiz.utils.AppUtils;
import com.studyquiz.mystudyquiz.Constants;
import com.studyquiz.mystudyquiz.databinding.HomeFragmentBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AccountFragment extends Fragment {
    private HomeFragmentBinding binding;
    private SharedPreferences sharedPreferences;
    private AdView adView;

    private final MyInApp.CallBack_MyInApp callback = new MyInApp.CallBack_MyInApp() {
        @Override
        public void successfullyPurchased(boolean isPurchasedNow, String purchaseKey) {
            AppUtils.showToast(requireContext(), "Pro Subscription purchased");
        }

        @Override
        public void purchaseFailed(String purchaseKey, int code, String message) {
            AppUtils.showToast(requireContext(), "Pro Subscription not purchased\n" + message);
        }

        @Override
        public void details(boolean isInAppExist, String title, String description, String price, long priceMic) {
            if(title == null || title.isEmpty())
                title = "Pro subscription features";

            if(description == null || description.isEmpty())
                description = "- Unlimited Quizzes capacity\n- Unlimited Questions capacity\n- No ads";
            AppUtils.showAlertDialog(requireActivity(), title, description);
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyInApp.getInstance().addCallBack(InApp_ID.PRO_SUBSCRIPTION, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = HomeFragmentBinding.inflate(inflater, container, false);
        //set variables in Binding
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = requireActivity().getSharedPreferences(Constants.GENERAL_SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        setClickListeners();
        setUserDetails();
        showAdBanner();

    }


    private void subscribe() {
        MyInApp.getInstance().consume((AppCompatActivity) requireActivity(), InApp_ID.PRO_SUBSCRIPTION);
    }

    private void setUserDetails() {
        binding.nameTextView.setText(sharedPreferences.getString(Constants.GENERAL_SHARED_PREFERENCES_USER_NAME_KEY, ""));
    }

    private void setClickListeners() {
        binding.privacyPolicyBtn.setOnClickListener(v -> openPrivacyPolicy());
        binding.termsOfUseBtn.setOnClickListener(v -> openTermsOfUse());
        binding.aboutBtn.setOnClickListener(v -> openAbout());
        binding.helpBtn.setOnClickListener(view -> helpClicked());
        binding.proSubButton.setOnClickListener(view -> subscribe());
    }

    private void helpClicked() {
        MyInApp.getInstance().getInAppDetails(InApp_ID.PRO_SUBSCRIPTION);

    }

    private void showAdBanner() {
        if (MyInApp.getInstance().getPurchaseValueFromPref(InApp_ID.PRO_SUBSCRIPTION))
            return;

        String UNIT_ID = BuildConfig.ADMOB_BANNER_AD_ID_ACCOUNT;

        adView = new AdView(requireContext());
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


    private void openAbout() {
        AppUtils.openHtmlTextDialog(requireActivity(), "about.html");
    }

    private void openTermsOfUse() {
        AppUtils.openHtmlTextDialog(requireActivity(), "terms_of_use.html");
    }

    private void openPrivacyPolicy() {
        AppUtils.openHtmlTextDialog(requireActivity(), "privacy_policy.html");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyInApp.getInstance().removeCallBack(InApp_ID.PRO_SUBSCRIPTION,callback);
    }
}
