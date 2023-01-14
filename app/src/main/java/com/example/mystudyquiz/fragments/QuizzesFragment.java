package com.example.mystudyquiz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mystudyquiz.BuildConfig;
import com.example.mystudyquiz.Constants;
import com.example.mystudyquiz.MyInApp;
import com.example.mystudyquiz.R;
import com.example.mystudyquiz.adapters.QuizAdapter;
import com.example.mystudyquiz.databinding.QuizzesFragmentBinding;
import com.example.mystudyquiz.model.Quiz;
import com.example.mystudyquiz.utils.AppUtils;
import com.example.mystudyquiz.viewmodel.QuizViewModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import java.util.Objects;

public class QuizzesFragment extends Fragment {
    private QuizzesFragmentBinding binding;
    private QuizViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = QuizzesFragmentBinding.inflate(inflater, container, false);
        //set variables in Binding
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setViewModel(view);
        setViews();
        initializeWelcomeDialog();
        viewModel.getQuizzes().observe(getViewLifecycleOwner(), quizzes -> {
            binding.chooseQuizTextView.setVisibility(quizzes == null ||quizzes.isEmpty() ? View.GONE : View.VISIBLE);
            binding.quizzesRecyclerView.setVisibility(quizzes == null ||quizzes.isEmpty() ? View.GONE : View.VISIBLE);
            binding.noQuizzesTextView.setVisibility(quizzes == null ||quizzes.isEmpty() ? View.VISIBLE : View.GONE);
            binding.setQuizAdapter(new QuizAdapter(quizzes, requireContext(), QuizzesFragment.this::quizClicked, this::quizDeleted));
        });
    }

    private void setViews() {
        binding.addQuizBtn.setOnClickListener(v -> addNewQuizClicked());
        binding.statisticsBtn.setOnClickListener(v -> openStatisticsDialog());
        binding.quizzesRecyclerView.setLayoutManager(new GridLayoutManager(requireActivity(), 2));
    }

    private void openStatisticsDialog() {
        Navigation.findNavController(requireView()).navigate(QuizzesFragmentDirections.actionMyQuizzesToStatisticsDialog());
    }

    private void initializeWelcomeDialog() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constants.GENERAL_SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(Constants.GENERAL_SHARED_PREFERENCES_USER_NAME_KEY, null) == null) {
            RegistrationDialog registrationDialog = new RegistrationDialog();
            registrationDialog.setCancelable(false);
            registrationDialog.setNameCallback(name -> {
                binding.nameTextView.setText(name);
            });
            registrationDialog.show(getParentFragmentManager(), registrationDialog.getTag());
        } else {
            binding.nameTextView.setText(sharedPreferences.getString(Constants.GENERAL_SHARED_PREFERENCES_USER_NAME_KEY, ""));

        }
    }

    private void addNewQuizClicked() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(Constants.GENERAL_SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        if(!sharedPreferences.getBoolean(Constants.GENERAL_SHARED_PREFERENCES_PRO_SUBSCRIPTION_KEY,false) && binding.getQuizAdapter() != null && sharedPreferences.getInt(Constants.GENERAL_SHARED_PREFERENCES_MAX_QUIZZES_KEY, Constants.QUIZZES_FREE_MAX_CAPACITY) == binding.getQuizAdapter().getItemCount()){
            AppUtils.showAlertDialog(requireContext(), "Reached full quizzes capacity \n( " +  sharedPreferences.getInt(Constants.GENERAL_SHARED_PREFERENCES_MAX_QUIZZES_KEY,Constants.QUIZZES_FREE_MAX_CAPACITY)+" Quizzes ) , would you like to pay 0.99$ for adding one more quiz ? ","Reached full Quizzes capacity", new AppUtils.AlertDialogCallback() {
                @Override
                public void onPositiveButtonClicked() {
                    AppUtils.showToast(requireContext(),"Purchased 1 more quizzes capacity");
                    int currentCapacity = sharedPreferences.getInt(Constants.GENERAL_SHARED_PREFERENCES_MAX_QUIZZES_KEY,Constants.QUIZZES_FREE_MAX_CAPACITY);
                    sharedPreferences.edit().putInt(Constants.GENERAL_SHARED_PREFERENCES_MAX_QUIZZES_KEY,currentCapacity + 1).apply();
                    Navigation.findNavController(requireView()).navigate(QuizzesFragmentDirections.actionMyQuizzesToAddNewQuizFragment());
                }

                @Override
                public void onNegativeButtonClicked() {
                    AppUtils.showToast(requireContext(),"Purchase canceled");
                }
            });
        }else{
            if(binding.getQuizAdapter() != null && sharedPreferences.getInt(Constants.GENERAL_SHARED_PREFERENCES_MAX_QUIZZES_KEY, Constants.QUIZZES_FREE_MAX_CAPACITY) == binding.getQuizAdapter().getItemCount())
                sharedPreferences.edit().putInt(Constants.GENERAL_SHARED_PREFERENCES_MAX_QUIZZES_KEY,sharedPreferences.getInt(Constants.GENERAL_SHARED_PREFERENCES_MAX_QUIZZES_KEY,Constants.QUIZZES_FREE_MAX_CAPACITY) + 1).apply();
            Navigation.findNavController(requireView()).navigate(QuizzesFragmentDirections.actionMyQuizzesToAddNewQuizFragment());
        }

    }

    private void setViewModel(View view) {
        NavBackStackEntry backStackEntry = Navigation.findNavController(view).getBackStackEntry(R.id.my_quizzes_navigation);
        viewModel = new ViewModelProvider(backStackEntry).get(QuizViewModel.class);
    }

    private void quizDeleted(Quiz quiz) {
        viewModel.deleteQuiz(quiz);
    }

    /**
     * sets the quiz as the current one and pass it to the next window.
     *
     * @param quiz the selected quiz.
     */
    private void quizClicked(Quiz quiz) {
        viewModel.setSelectedQuiz(quiz);
        Navigation.findNavController(requireView()).navigate(QuizzesFragmentDirections.actionMyQuizzesToQuestionsFragment());
    }

}
