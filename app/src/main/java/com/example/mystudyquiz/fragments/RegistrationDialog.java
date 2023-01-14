package com.example.mystudyquiz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.mystudyquiz.Constants;
import com.example.mystudyquiz.databinding.AnswersReportBottomDialogBinding;
import com.example.mystudyquiz.databinding.RegisterationDialogBinding;

public class RegistrationDialog extends DialogFragment {
    private RegisterationDialogBinding binding;
    private NameCallback nameCallback;

    public interface NameCallback{
        void onNameEntered(String name);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = RegisterationDialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnClickListeners();
    }

    public void setNameCallback(NameCallback nameCallback){
        this.nameCallback = nameCallback;
    }

    private void setOnClickListeners() {
        binding.startButton.setOnClickListener(v -> startButtonClicked());
    }

    private void startButtonClicked() {
        if (binding.nameEditText.getText() == null || binding.nameEditText.getText().toString().isEmpty()) {
            binding.nameEditText.setError("Must enter valid name");
            return;
        }
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constants.GENERAL_SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(Constants.GENERAL_SHARED_PREFERENCES_USER_NAME_KEY, binding.nameEditText.getText().toString()).apply();
        if (nameCallback != null)
            nameCallback.onNameEntered(binding.nameEditText.getText().toString());
        dismiss();
    }
}
