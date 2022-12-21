package com.group20.pi_software.ui.goals;

import static android.content.Context.MODE_PRIVATE;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group20.pi_software.MainActivity;
import com.group20.pi_software.R;
import com.group20.pi_software.databinding.FragmentGoalsBinding;
import com.group20.pi_software.model.DataAnalyser;
import com.group20.pi_software.model.DataBaseHelper;
import com.group20.pi_software.model.StepsModel;
import com.group20.pi_software.model.UserModel;
import com.timqi.sectorprogressview.ColorfulRingProgressView;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.List;

public class GoalsFragment extends Fragment {

    private GoalsViewModel goalsViewModel;
    private FragmentGoalsBinding binding;
    private View root;

    private EditText stepsET, sleepET, activityET, studyET;
    private TextView progressPercentage;
    private ColorfulRingProgressView progressBar;
    private ConstraintLayout goalIndicators[];

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        goalsViewModel =
                new ViewModelProvider(this).get(GoalsViewModel.class);

        binding = FragmentGoalsBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        stepsET = binding.goalCardInput;
        sleepET = binding.goalCardInput2;
        activityET = binding.goalCardInput3;
        studyET = binding.goalCardInput4;
        progressBar = binding.goalsProgressBar;
        progressPercentage = binding.goalsProgressPercentage;

        //Task completed indicators
        goalIndicators = new ConstraintLayout[4];
        goalIndicators[0] = binding.goalComplete1;
        goalIndicators[1] = binding.goalComplete2;
        goalIndicators[2] = binding.goalComplete3;
        goalIndicators[3] = binding.goalComplete4;

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        loadGoals();
        updateGoalsProgress();

        stepsET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused) return;

                MainActivity.getDataBaseHelper().updateUserStepsGoal(Integer.parseInt(stepsET.getText().toString()));
                updateGoalsProgress();
                getActivity().getSharedPreferences("Logs", MODE_PRIVATE).edit().putString("last_goals_modified", formatter.format(Calendar.getInstance().getTime())).apply();
            }
        });

        sleepET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused) return;

                MainActivity.getDataBaseHelper().updateUserSleepGoal((int)Double.parseDouble(sleepET.getText().toString()));
                updateGoalsProgress();
                getActivity().getSharedPreferences("Logs", MODE_PRIVATE).edit().putString("last_goals_modified", formatter.format(Calendar.getInstance().getTime())).apply();
            }
        });

        activityET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused) return;

                MainActivity.getDataBaseHelper().updateUserExerciseGoal(Integer.parseInt(activityET.getText().toString()));
                updateGoalsProgress();
                getActivity().getSharedPreferences("Logs", MODE_PRIVATE).edit().putString("last_goals_modified", formatter.format(Calendar.getInstance().getTime())).apply();
            }
        });

        studyET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (focused) return;

                MainActivity.getDataBaseHelper().updateUserStudyGoal((int)Double.parseDouble(studyET.getText().toString()));
                updateGoalsProgress();
                getActivity().getSharedPreferences("Logs", MODE_PRIVATE).edit().putString("last_goals_modified", formatter.format(Calendar.getInstance().getTime())).apply();
            }
        });

        setBottomNavigationViewEnabled(false);

        Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_round_arrow_back_48);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(root).popBackStack();
                toolbar.setNavigationIcon(null);
                ((NavHostFragment) ((MainActivity) getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main)).getNavController().navigate(R.id.navigation_profile);
                setBottomNavigationViewEnabled(true);
            }
        });

        return root;
    }

    private void setBottomNavigationViewEnabled(boolean enabled){
        Menu menu = ((BottomNavigationView) getActivity().findViewById(R.id.nav_view)).getMenu();
        for (int i = 0; i < menu.size(); i++){
            menu.getItem(i).setEnabled(enabled);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateGoalsProgress() {
        int goalsCompleted = 0;

        DataBaseHelper helper = MainActivity.getDataBaseHelper();
        List<UserModel> users = helper.getUser();
        if (!users.isEmpty()){
            UserModel userModel = users.get(0);
            if(userModel.getStepsGoal() != 0){
                if (helper.getStepsToday() > userModel.getStepsGoal()) {
                    goalIndicators[0].setVisibility(View.VISIBLE);
                    goalsCompleted++;
                }
            }
            if (userModel.getSleepHoursGoal() != 0){
                if (DataAnalyser.parseToHours(helper.getSleepToday()) > userModel.getSleepHoursGoal()) {
                    goalIndicators[1].setVisibility(View.VISIBLE);
                    goalsCompleted++;
                }
            }
            if (userModel.getStudyHoursGoal() != 0){
                if (DataAnalyser.parseToHours(helper.getStudyToday()) > userModel.getStudyHoursGoal()) {
                    goalIndicators[3].setVisibility(View.VISIBLE);
                    goalsCompleted++;
                }
            }
            if (userModel.getExerciseGoal() != 0){
                if (DataAnalyser.parseToHours(helper.getExerciseToday()) > userModel.getExerciseGoal()) {
                    goalIndicators[2].setVisibility(View.VISIBLE);
                    goalsCompleted++;
                }
            }
            Float percentage = (float) (goalsCompleted/8.0)*100;
            progressBar.setPercent(percentage);
            int dispalyedPercentage = (int)(goalsCompleted*100.0f)/4;
            progressPercentage.setText(String.valueOf(dispalyedPercentage)+"%");
        }
    }

    public void loadGoals() {
        DataBaseHelper helper = MainActivity.getDataBaseHelper();
        List<UserModel> users = helper.getUser();
        if (!users.isEmpty()){
            UserModel userModel = users.get(0);
            stepsET.setText(String.valueOf(userModel.getStepsGoal()));
            sleepET.setText(Double.toString(userModel.getSleepHoursGoal()));
            activityET.setText(String.valueOf(userModel.getExerciseGoal()));
            studyET.setText(Double.toString(userModel.getStudyHoursGoal()));
        }
    }
}
