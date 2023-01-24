package com.studyquiz.mystudyquiz.fragments;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.Navigation;

import com.studyquiz.mystudyquiz.R;
import com.studyquiz.mystudyquiz.adapters.QuizStatisticsAdapter;
import com.studyquiz.mystudyquiz.data.entities.QuizStatistic;
import com.studyquiz.mystudyquiz.databinding.SatisticsDialogBinding;
import com.studyquiz.mystudyquiz.viewmodel.QuizViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;
import java.util.Objects;

public class StatisticsDialog extends BottomSheetDialogFragment {

    private static final String EVENT_DISPLAY_STATISTICS_DIALOG = "display_statistics_dialog";
    private SatisticsDialogBinding binding;
    private QuizViewModel viewModel;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SatisticsDialogBinding.inflate(inflater, container, false);
        //set variables in Binding
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Obtain the FirebaseAnalytics instance.
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(requireContext());
        Bundle bundle = new Bundle();
        bundle.putString("action", "open");
        mFirebaseAnalytics.logEvent(EVENT_DISPLAY_STATISTICS_DIALOG, bundle);
        setViewModel(view);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.progressCircle.setVisibility(View.VISIBLE);
        getData();

    }

    private void getData() {
        viewModel.getAllStatistics(this::setQuizStatistics);
    }

    private void setQuizStatistics(List<QuizStatistic> quizStatisticList) {
        binding.progressCircle.setVisibility(View.GONE);

        if (quizStatisticList == null || quizStatisticList.isEmpty()) {
            binding.noResultsTextView.setVisibility(View.VISIBLE);
        } else {
            binding.quizzesStatisticsRecyclerViewLayout.setVisibility(View.VISIBLE);
            binding.quizzesStatisticsRecyclerView.setAdapter(new QuizStatisticsAdapter(quizStatisticList));
        }
    }


    private void setViewModel(View view) {
        NavBackStackEntry backStackEntry = Navigation.findNavController(requireParentFragment().requireView()).getBackStackEntry(R.id.my_quizzes_navigation);
        viewModel = new ViewModelProvider(backStackEntry).get(QuizViewModel.class);
    }
}
