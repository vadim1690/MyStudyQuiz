package com.studyquiz.mystudyquiz.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import androidx.recyclerview.widget.GridLayoutManager;

import com.studyquiz.InApp_ID;
import com.studyquiz.mystudyquiz.Constants;
import com.studyquiz.mystudyquiz.MyInApp;
import com.studyquiz.mystudyquiz.R;
import com.studyquiz.mystudyquiz.adapters.QuizAdapter;
import com.studyquiz.mystudyquiz.databinding.QuizzesFragmentBinding;
import com.studyquiz.mystudyquiz.model.Quiz;
import com.studyquiz.mystudyquiz.utils.AppUtils;
import com.studyquiz.mystudyquiz.viewmodel.QuizViewModel;

public class QuizzesFragment extends Fragment {
    private QuizzesFragmentBinding binding;
    private QuizViewModel viewModel;
    private SharedPreferences sharedPreferences;

    private final MyInApp.CallBack_MyInApp inAppCallback = new MyInApp.CallBack_MyInApp() {
        @Override
        public void successfullyPurchased(boolean isPurchasedNow, String purchaseKey) {
            AppUtils.showToast(requireContext(), "Purchased 1 more quizzes capacity");
            int currentCapacity = sharedPreferences.getInt(Constants.GENERAL_SHARED_PREFERENCES_MAX_QUIZZES_KEY, Constants.QUIZZES_FREE_MAX_CAPACITY);
            if (binding.getQuizAdapter() != null && binding.getQuizAdapter().getItemCount() == currentCapacity) {
                sharedPreferences.edit().putInt(Constants.GENERAL_SHARED_PREFERENCES_MAX_QUIZZES_KEY, currentCapacity + 1).apply();
                Navigation.findNavController(requireView()).navigate(QuizzesFragmentDirections.actionMyQuizzesToAddNewQuizFragment());
            } else if (binding.getQuizAdapter() != null && binding.getQuizAdapter().getItemCount() > currentCapacity) {
                AppUtils.showToast(requireContext(), "Purchased 1 more quizzes capacity, Quizzes capacity is still less than current Quizzes number.");
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
        MyInApp.getInstance().addCallBack(InApp_ID.MORE_QUIZZES, inAppCallback);
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
        sharedPreferences = requireActivity().getSharedPreferences(Constants.GENERAL_SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE);
        initializeWelcomeDialog();

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

        if (MyInApp.getInstance().getPurchaseValueFromPref(InApp_ID.PRO_SUBSCRIPTION)) {
            Navigation.findNavController(requireView()).navigate(QuizzesFragmentDirections.actionMyQuizzesToAddNewQuizFragment());
            return;
        }

        if (binding.getQuizAdapter() != null &&
                sharedPreferences.getInt(Constants.GENERAL_SHARED_PREFERENCES_MAX_QUIZZES_KEY, Constants.QUIZZES_FREE_MAX_CAPACITY) <= binding.getQuizAdapter().getItemCount()) {
            handleFullQuizzesCapacity();

        } else {
            Navigation.findNavController(requireView()).navigate(QuizzesFragmentDirections.actionMyQuizzesToAddNewQuizFragment());
        }

    }

    private void handleFullQuizzesCapacity() {
        if (sharedPreferences.getInt(Constants.GENERAL_SHARED_PREFERENCES_MAX_QUIZZES_KEY, Constants.QUIZZES_FREE_MAX_CAPACITY) < binding.getQuizAdapter().getItemCount())
            AppUtils.showAlertDialog(requireActivity(), "Number of current quizzes ( " + binding.getQuizAdapter().getItemCount() + " )\n" +
                    "is bigger than the Number of quizzes maximum capacity ( " + sharedPreferences.getInt(Constants.GENERAL_SHARED_PREFERENCES_MAX_QUIZZES_KEY, Constants.QUIZZES_FREE_MAX_CAPACITY) + " )\n" +
                    "you can buy Quizzes capacity to match the current number of Quizzes or delete quizzes to match the current number of Quizzes capacity ", "Attention", new AppUtils.AlertDialogCallback() {
                @Override
                public void onPositiveButtonClicked() {
                    MyInApp.getInstance().consume((AppCompatActivity) requireActivity(), InApp_ID.MORE_QUIZZES);
                }

                @Override
                public void onNegativeButtonClicked() {

                }
            });
        else
            MyInApp.getInstance().consume((AppCompatActivity) requireActivity(), InApp_ID.MORE_QUIZZES);

    }

    private void setViewModel(View view) {
        NavBackStackEntry backStackEntry = Navigation.findNavController(view).getBackStackEntry(R.id.my_quizzes_navigation);
        viewModel = new ViewModelProvider(backStackEntry).get(QuizViewModel.class);

        viewModel.getQuizzes().observe(getViewLifecycleOwner(), quizzes -> {
            binding.chooseQuizTextView.setVisibility(quizzes == null || quizzes.isEmpty() ? View.GONE : View.VISIBLE);
            binding.quizzesSizeTextView.setVisibility(quizzes == null || quizzes.isEmpty() ? View.GONE : View.VISIBLE);
            if (quizzes != null && !quizzes.isEmpty())
                binding.quizzesSizeTextView.setText("Total " + quizzes.size() + " Quizzes");
            binding.quizzesRecyclerView.setVisibility(quizzes == null || quizzes.isEmpty() ? View.GONE : View.VISIBLE);
            binding.noQuizzesTextView.setVisibility(quizzes == null || quizzes.isEmpty() ? View.VISIBLE : View.GONE);
            binding.setQuizAdapter(new QuizAdapter(quizzes, requireContext(), QuizzesFragment.this::quizClicked, this::quizDeleted));
        });
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        MyInApp.getInstance().removeCallBack(InApp_ID.MORE_QUIZZES, inAppCallback);
    }

}
