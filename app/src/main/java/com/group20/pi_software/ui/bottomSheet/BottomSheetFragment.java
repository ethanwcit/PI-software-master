package com.group20.pi_software.ui.bottomSheet;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.group20.pi_software.MainActivity;
import com.group20.pi_software.R;
import com.group20.pi_software.databinding.FragmentBottomSheetBinding;
import com.group20.pi_software.model.ExerciseModel;
import com.group20.pi_software.model.SleepModel;
import com.group20.pi_software.model.StepsModel;
import com.group20.pi_software.model.StudyModel;
import com.group20.pi_software.ui.feed.FeedFragment;
import com.group20.pi_software.ui.summary.SummaryFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private BottomSheetViewModel bottomSheetViewModel;
    private FragmentBottomSheetBinding binding;
    DateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy ");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        bottomSheetViewModel =
                new ViewModelProvider(this).get(BottomSheetViewModel.class);

        binding = FragmentBottomSheetBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ImageButton closeButton = binding.buttonClose;
        ImageButton doneButton = binding.buttonDone;

        Spinner dataTypeSpinner = binding.spinner;

        TextView dateStartText = binding.textDateStart;
        EditText dateEditText = binding.editTextDate;
        ConstraintLayout datepickerLayout = binding.layoutDatepicker;
        ImageButton selectDateButton = binding.buttonSelectDate;
        DatePicker datePicker = binding.datePicker;

        TextView timeStartText = binding.textTimeStart;
        EditText timeEditText = binding.editTextTime;
        ConstraintLayout timepickerLayout = binding.layoutTimepicker;
        ImageButton selectTimeButton = binding.bottonSelectTime;
        TimePicker timePicker = binding.timePicker;;

        TextView timeEndText = binding.textTimeEnd;
        EditText timeEndEditText = binding.editTextTimeEnd;
        ConstraintLayout timepickerEndLayout = binding.layoutTimepickerEnd;
        ImageButton selectTimeEndButton = binding.buttonSelectTimeEnd;
        TimePicker timepickerEnd = binding.timepickerEnd;

        ConstraintLayout stepsLayout = binding.layoutSteps;
        ConstraintLayout endLayout = binding.layoutEnd;

        EditText stepsEditText = binding.editTextSteps;

        String[] dataTypes = {"Steps", "Sleep Hours", "Study Hours", "Exercise"};
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.custom_spinner_item, dataTypes);
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown_item);
        dataTypeSpinner.setAdapter(adapter);
        dataTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0){
                    endLayout.setVisibility(View.GONE);
                    stepsLayout.setVisibility(View.VISIBLE);
                    timeStartText.setText("Time");
                }else{
                    stepsLayout.setVisibility(View.GONE);
                    endLayout.setVisibility(View.VISIBLE);
                    timeStartText.setText("Start Time");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                boolean successful = false;
                switch (dataTypeSpinner.getSelectedItemPosition()){
                    case 0:
                        if (!stepsEditText.getText().toString().equals("")){
                            StepsModel stepsModel = new StepsModel(-1, dateEditText.getText().toString(), timeEditText.getText().toString(), Integer.parseInt(String.valueOf(stepsEditText.getText())));
                            successful = MainActivity.getDataBaseHelper().addStepsEntry(stepsModel);
                            FeedFragment.addtoarr(" Steps!", stepsEditText.getText().toString(), dateFormat.format(new Date()));
                        }
                        break;
                    case 1:
                        SleepModel sleepModel = new SleepModel(-1, dateEditText.getText().toString(), timeEditText.getText().toString(), timeEndEditText.getText().toString());
                        successful = MainActivity.getDataBaseHelper().addSleepEntry(sleepModel);
                        String Data = sleepModel.getSleepTime() +"";
                        FeedFragment.addtoarr(" Sleep-Hours!",Data, dateFormat.format(new Date()));
                        break;
                    case 2:
                        StudyModel studyModel = new StudyModel(-1,  dateEditText.getText().toString(), timeEditText.getText().toString(), timeEndEditText.getText().toString());
                        successful = MainActivity.getDataBaseHelper().addStudySession(studyModel);
                        String Data2 = studyModel.getStudyTime() +"";
                        FeedFragment.addtoarr(" Study-Hours!",Data2, dateFormat.format(new Date()));
                        break;
                    case 3:
                        ExerciseModel exerciseModel = new ExerciseModel(-1, dateEditText.getText().toString(), timeEditText.getText().toString(), timeEndEditText.getText().toString());
                        successful = MainActivity.getDataBaseHelper().addExerciseEntry(exerciseModel);
                        String Data3 = exerciseModel.getExerciseTime() +"";
                        FeedFragment.addtoarr(" Exercise-Hours!",Data3, dateFormat.format(new Date()));
                        break;
                    default:
                        break;
                }

                dismiss();
                if(getParentFragment() != null){
                    ((SummaryFragment) getParentFragment()).reload();
                }
            }
        });

        setupDatePicker(datepickerLayout, dateEditText, datePicker, selectDateButton, dateStartText);

        setupTimePicker(timepickerLayout, timeEditText, timePicker, selectTimeButton, timeStartText);
        setupTimePicker(timepickerEndLayout, timeEndEditText, timepickerEnd, selectTimeEndButton, timeEndText);

        return root;
    }

    private void setupDatePicker(ConstraintLayout layout, EditText text, DatePicker picker, ImageButton button, TextView title){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        text.setText(dateFormat.format(Calendar.getInstance().getTime()));
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.VISIBLE);
                text.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text.setText(String.format("%02d/%02d/%d", picker.getDayOfMonth(), picker.getMonth(), picker.getYear()));
                layout.setVisibility(View.GONE);
                text.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupTimePicker(ConstraintLayout layout, EditText text, TimePicker picker, ImageButton button, TextView title){
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        text.setText(timeFormat.format(Calendar.getInstance().getTime()));
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.VISIBLE);
                text.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                text.setText(String.format("%02d:%02d", picker.getHour(), picker.getMinute()));
                layout.setVisibility(View.GONE);
                text.setVisibility(View.VISIBLE);
                title.setVisibility(View.VISIBLE);
            }
        });
    }


}
