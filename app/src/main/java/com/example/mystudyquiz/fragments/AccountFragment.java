package com.example.mystudyquiz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mystudyquiz.App;
import com.example.mystudyquiz.BuildConfig;
import com.example.mystudyquiz.utils.AppUtils;
import com.example.mystudyquiz.Constants;
import com.example.mystudyquiz.databinding.HomeFragmentBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AccountFragment extends Fragment {
    private HomeFragmentBinding binding;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        initializeSwitches();
        setUserDetails();
        showAdBanner();
    }

    private void initializeSwitches() {
        binding.proSubSwitch.setChecked(sharedPreferences.getBoolean(Constants.GENERAL_SHARED_PREFERENCES_PRO_SUBSCRIPTION_KEY, false));
        binding.proSubSwitch.setOnClickListener(view -> {
           boolean isChecked = binding.proSubSwitch.isChecked();
            if (!isChecked)
                unsubscribe();
            else subscribe();
        });
    }

    private void subscribe() {
        AppUtils.showAlertDialog(requireActivity(), "Upgrade to the pro version and unlock unlimited quiz creation, an unlimited number of questions, and no ads! only 4.99$. Subscribing is easy and you can cancel at any time. Click Ok to get started.", "Pro Subscription", new AppUtils.AlertDialogCallback() {
            @Override
            public void onPositiveButtonClicked() {
                AppUtils.showToast(requireContext(), "Pro Subscription purchased");
                sharedPreferences.edit().putBoolean(Constants.GENERAL_SHARED_PREFERENCES_PRO_SUBSCRIPTION_KEY, true).apply();
            }

            @Override
            public void onNegativeButtonClicked() {
                binding.proSubSwitch.setChecked(false);
            }
        });
    }

    private void unsubscribe() {
        AppUtils.showAlertDialog(requireActivity(), "Are you sure you would like to cancel your pro subscription ? ", "Unsubscribe", new AppUtils.AlertDialogCallback() {
            @Override
            public void onPositiveButtonClicked() {
                AppUtils.showToast(requireContext(), "You have successfully unsubscribed from the pro version");
                sharedPreferences.edit().putBoolean(Constants.GENERAL_SHARED_PREFERENCES_PRO_SUBSCRIPTION_KEY, false).apply();
            }

            @Override
            public void onNegativeButtonClicked() {
                binding.proSubSwitch.setChecked(true);
            }
        });

    }

    private void setUserDetails() {
        binding.nameTextView.setText(sharedPreferences.getString(Constants.GENERAL_SHARED_PREFERENCES_USER_NAME_KEY, ""));
    }

    private void setClickListeners() {
        binding.privacyPolicyBtn.setOnClickListener(v -> openPrivacyPolicy());
        binding.termsOfUseBtn.setOnClickListener(v -> openTermsOfUse());
        binding.aboutBtn.setOnClickListener(v -> openAbout());
    }

    private void showAdBanner() {
        if (sharedPreferences.getBoolean(Constants.GENERAL_SHARED_PREFERENCES_PRO_SUBSCRIPTION_KEY, false))
            return;

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


    private void openAbout() {
        AppUtils.openHtmlTextDialog(requireActivity(), "about.html");
    }

    private void openTermsOfUse() {
        AppUtils.openHtmlTextDialog(requireActivity(), "terms_of_use.html");
    }

    private void openPrivacyPolicy() {
        AppUtils.openHtmlTextDialog(requireActivity(), "privacy_policy.html");
    }


}
