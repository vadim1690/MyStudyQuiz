package com.example.mystudyquiz.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;

import com.example.mystudyquiz.AppUtils;
import com.example.mystudyquiz.R;
import com.example.mystudyquiz.databinding.AddNewQuizFragmentBinding;
import com.example.mystudyquiz.model.Quiz;
import com.example.mystudyquiz.viewmodel.QuizViewModel;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;

import org.jetbrains.annotations.NotNull;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;


public class AddNewQuizFragment extends Fragment {
    private AddNewQuizFragmentBinding binding;
    private QuizViewModel viewModel;
    private ActivityResultLauncher<Intent> launcher;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setResultLaunchers();
    }

    private void setResultLaunchers() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
            if(result.getResultCode()==RESULT_OK){
                if (result.getData() != null) {
                    setPhoto(result.getData().getData());
                }
            }else if(result.getResultCode()== ImagePicker.RESULT_ERROR){
                AppUtils.showAlertDialog(requireContext(),"FAILED!!!");
            }
        });
    }

    private void setPhoto(Uri uri) {
        binding.addImageBtn.setImageURI(uri);
        binding.addImageBtn.setEnabled(false);
        binding.addImageText.setVisibility(View.GONE);
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

    }

    private void setViewModel(View view) {
        NavBackStackEntry backStackEntry = Navigation.findNavController(view).getBackStackEntry(R.id.my_quizzes_navigation);
        viewModel = new ViewModelProvider(backStackEntry).get(QuizViewModel.class);

    }

    private void setOnClickListeners() {
        binding.addImageBtn.setOnClickListener(v -> openImagePicker());
        binding.createQuizBtn.setOnClickListener(v -> createQuizClicked());
    }

    private void createQuizClicked() {
        viewModel.addNewQuiz(new Quiz(binding.quizNameEdt.getText().toString()));
        Navigation.findNavController(requireView()).popBackStack();
    }

    private void openImagePicker() {

        ImagePicker.Companion.with(requireActivity())
                .crop()
                .cropFreeStyle()
                .maxResultSize(512,512,true)
                .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                .createIntentFromDialog(new Function1(){
                    public Object invoke(Object var1){
                        this.invoke((Intent)var1);
                        return Unit.INSTANCE;
                    }

                    public final void invoke(@NotNull Intent it){
                        Intrinsics.checkNotNullParameter(it,"it");
                        launcher.launch(it);
                    }
                });
    }
}
