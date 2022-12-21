package com.group20.pi_software.ui.summary;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.motion.widget.MotionScene;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.group20.pi_software.MainActivity;
import com.group20.pi_software.R;
import com.group20.pi_software.databinding.FragmentSummaryBinding;
import com.group20.pi_software.model.DataAnalyser;
import com.group20.pi_software.model.DataBaseHelper;
import com.group20.pi_software.model.UserModel;
import com.group20.pi_software.ui.bottomSheet.BottomSheetFragment;
import com.group20.pi_software.utility.ShareHelper;
import com.timqi.sectorprogressview.ColorfulRingProgressView;


import java.util.List;

public class SummaryFragment extends Fragment {

    private SummaryViewModel summaryViewModel;
    private FragmentSummaryBinding binding;

    private ColorfulRingProgressView sleepRing;
    private ColorfulRingProgressView studyRing;
    private ColorfulRingProgressView stepsRing;
    private ColorfulRingProgressView workoutRing;

    private TextView sleepPercentage;
    private TextView studyPercentage;
    private TextView stepsPercentage;
    private TextView workoutPercentage;

    private TextView sleepSummaryText;
    private TextView studySummaryText;
    private TextView stepsSummaryText;
    private TextView workoutSummaryText;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        summaryViewModel =
                new ViewModelProvider(this).get(SummaryViewModel.class);

        binding = FragmentSummaryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final CardView sleepCard = binding.cardSleepSummary;
        final CardView studyCard = binding.cardStudySummary;
        final CardView stepsCard = binding.cardStepsSummary;
        final CardView workoutCard = binding.cardWorkoutSummary;

        sleepSummaryText = binding.textSleepSummary;
        studySummaryText = binding.textStudySummary;
        stepsSummaryText = binding.textStepsSummary;
        workoutSummaryText = binding.textWorkoutSummary;

        sleepPercentage = binding.textSleepPercentage;
        studyPercentage = binding.textStudyPercentage;
        stepsPercentage = binding.textStepsPercentage;
        workoutPercentage = binding.textWorkoutPercentage;

        sleepRing = binding.progressRingSleep;
        studyRing = binding.progressRingStudy;
        stepsRing = binding.progressRingSteps;
        workoutRing = binding.progressRingWorkout;

        final FloatingActionButton floatingActionButton = binding.floatingActionButtonAdd;
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        LinearLayout summaryLayout = binding.layoutSummary;

        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_share) {
                    ShareHelper.openShareThisApp(summaryLayout);
                }
                return false;
            }
        });

        reload();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
                bottomSheetFragment.show(getChildFragmentManager(), "navigation_bottom_sheet");
            }
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void reload(){
        DataBaseHelper helper = MainActivity.getDataBaseHelper();
        sleepSummaryText.setText(helper.getSleepToday());
        studySummaryText.setText(helper.getStudyToday());
        stepsSummaryText.setText(String.format("%,d", helper.getStepsToday()));
        workoutSummaryText.setText(helper.getExerciseToday());

        sleepRing.setPercent(100);
        studyRing.setPercent(100);
        workoutRing.setPercent(100);
        stepsRing.setPercent(100);

        List<UserModel> users = helper.getUser();
        if (!users.isEmpty()){
            if(users.get(0).getStepsGoal() != 0){
                stepsRing.setPercent(100 * helper.getStepsToday() / users.get(0).getStepsGoal());
            }
            if (users.get(0).getSleepHoursGoal() != 0){
                sleepRing.setPercent((float) (100 * DataAnalyser.parseToHours(helper.getSleepToday()) / users.get(0).getSleepHoursGoal()));
            }
            if (users.get(0).getStudyHoursGoal() != 0){

                studyRing.setPercent((float) (100 * DataAnalyser.parseToHours(helper.getStudyToday()) / users.get(0).getStudyHoursGoal()));
            }
            if (users.get(0).getExerciseGoal() != 0){
                workoutRing.setPercent((float) (100 * DataAnalyser.parseToHours(helper.getExerciseToday()) / users.get(0).getExerciseGoal()));
            }
        }

        sleepPercentage.setText(String.format("%d%%", (int) sleepRing.getPercent()));
        studyPercentage.setText(String.format("%d%%", (int) studyRing.getPercent()));
        workoutPercentage.setText(String.format("%d%%", (int) workoutRing.getPercent()));
        stepsPercentage.setText(String.format("%d%%", (int) stepsRing.getPercent()));
    }

    @Override
    public void onStop() {
        super.onStop();
        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);

        Menu menu = toolbar.getMenu();
        menu.clear();
        toolbar.setOnMenuItemClickListener(null);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}