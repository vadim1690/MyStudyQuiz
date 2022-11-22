package com.example.mystudyquiz.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mystudyquiz.AppUtils;
import com.example.mystudyquiz.R;
import com.example.mystudyquiz.databinding.AddNewQuizFragmentBinding;
import com.example.mystudyquiz.databinding.HomeFragmentBinding;

public class HomeFragment extends Fragment {
    private HomeFragmentBinding binding;
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
        binding.privacyPolicyBtn.setOnClickListener(v->openPrivacyPolicy());
        binding.termsOfUseBtn.setOnClickListener(v->openTermsOfUse());
        binding.aboutBtn.setOnClickListener(v->openAbout());
    }

    private void openAbout() {
        AppUtils.openHtmlTextDialog(requireActivity(),"about.html");
    }

    private void openTermsOfUse() {
        AppUtils.openHtmlTextDialog(requireActivity(),"terms_of_use.html");
    }

    private void openPrivacyPolicy() {
        AppUtils.openHtmlTextDialog(requireActivity(),"privacy_policy.html");
    }


}
