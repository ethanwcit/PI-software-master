package com.group20.pi_software.ui.feed;

import static android.content.Context.MODE_PRIVATE;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.group20.pi_software.MainActivity;
import com.group20.pi_software.R;
import com.group20.pi_software.model.DataAnalyser;
import com.group20.pi_software.model.DataBaseHelper;
import com.group20.pi_software.model.UserModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FeedFragment extends Fragment {
    static ArrayList<Feed_Class> feed_class = new ArrayList<Feed_Class>();
    private RecyclerView recyclerView;
    private String StepsToday;
    private String StudyToday;
    private String SleepToday;
    private String ExerciseToday;
    private int StepsGoal;
    private double StudyGoal;
    private double SleepGoal;
    private double ExerciseGoal;
    int StepPer = 0;
    double SleepPer = 0;
    double StudyPer = 0;
    double ExercisePer = 0;
    DateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy ");

    public static void addtoarr(String type, String data, String date){
        feed_class.add(0, new Feed_Class("You just logged " + data + type, date));
    }

    public static void clearArr(){
        feed_class.clear();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reload();
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        recyclerView = view.findViewById(R.id.feed);
        if(!feed_class.isEmpty()) {
            TextView tv1 = view.findViewById(R.id.feed_text);
            tv1.setText("");
        }
        recyclerView.setHasFixedSize(true);
        FeedAdapter adapter = new FeedAdapter(feed_class);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);

        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void reload() {
        DataBaseHelper helper = MainActivity.getDataBaseHelper();
        SleepToday = helper.getSleepToday();
        StudyToday = helper.getStudyToday();
        StepsToday = String.format("%,d", helper.getStepsToday());
        ExerciseToday = helper.getExerciseToday();
        List<UserModel> users = helper.getUser();
        Feed_Class feed;
        if (SleepToday == "0h00min"){
            feed = new Feed_Class("You have not logged sleep today, yet!", dateFormat.format(new Date()));
            boolean isin = false;
            for(Feed_Class item : feed_class){
                if(feed.getMainMessage().equals(item.getMainMessage())){
                    isin = true;
                }
            }
            if(!isin) {
                feed_class.add(0, feed);
            }
        }
        if (StudyToday == "0h00min"){
            feed = new Feed_Class("You have not logged study today, yet!\nLet's study!", dateFormat.format(new Date()));
            boolean isin = false;
            for(Feed_Class item : feed_class){
                if(feed.getMainMessage().equals(item.getMainMessage())){
                    isin = true;
                }
            }
            if(!isin) {
                feed_class.add(0, feed);
            }
        }
        if (StepsToday == "0"){
            feed = new Feed_Class("You have not logged step today, yet!\nLet's walk!", dateFormat.format(new Date()));
            boolean isin = false;
            for(Feed_Class item : feed_class){
                if(feed.getMainMessage().equals(item.getMainMessage())){
                    isin = true;
                }
            }
            if(!isin) {
                feed_class.add(0, feed);
            }
        }
        if (ExerciseToday == "0h00min"){
            feed = new Feed_Class("You have not logged exercise today, yet!\nLet's do some!", dateFormat.format(new Date()));
            boolean isin = false;
            for(Feed_Class item : feed_class){
                if(feed.getMainMessage().equals(item.getMainMessage())){
                    isin = true;
                }
            }
            if(!isin) {
                feed_class.add(0, feed);
            }
        }
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Calendar lastEdit = Calendar.getInstance();
        try {
            lastEdit.setTime(formatter.parse(getActivity().getSharedPreferences("Logs", MODE_PRIVATE).getString("last_goals_modified", "01/01/2000")));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (Duration.between(lastEdit.toInstant(), Calendar.getInstance().toInstant()).toDays() > 30){
            feed = new Feed_Class("You have not changed goals these days.\nMaybe, it is good time to review?", dateFormat.format(new Date()));
            boolean isin = false;
            for(Feed_Class item : feed_class){
                if(feed.getMainMessage().equals(item.getMainMessage())){
                    isin = true;
                }
            }
            if(!isin) {
                feed_class.add(0, feed);
            }
        }

        if (!users.isEmpty()) {
            SleepGoal = users.get(0).getSleepHoursGoal();
            StudyGoal = users.get(0).getStudyHoursGoal();
            StepsGoal = users.get(0).getStepsGoal();
            ExerciseGoal = users.get(0).getExerciseGoal();
            Feed_Class feed1 = null;
            if (StepsGoal != 0) {
                int StepPer = 100 * helper.getStepsToday() / users.get(0).getStepsGoal();
                if (StepPer == 25) {
                    feed1 = new Feed_Class("You have completed 25% of your Step Goal Today, at " + StepsToday + " Steps Well Done!", dateFormat.format(new Date()));
                } else if (StepPer == 50) {
                    feed1 = new Feed_Class("You have completed 50% of your Step Goal Today, at " + StepsToday + " Steps Well Done!", dateFormat.format(new Date()));
                } else if (StepPer == 75) {
                    feed1 = new Feed_Class("You have completed 75% of your Step Goal Today, at " + StepsToday + " Steps Well Done!", dateFormat.format(new Date()));
                } else if (StepPer >= 100 && StepPer != 200) {
                    feed1 = new Feed_Class("You have completed 100% of your Step Goal Today, at " + StepsToday + " Steps Well Done!", dateFormat.format(new Date()));
                }
                else if (StepPer == 200) {
                    feed1 = new Feed_Class("You have completed doubled your Step Goal Today, at " + StepsToday + " Steps amazing job!", dateFormat.format(new Date()));
                }
                if (feed1 != null) {
                    boolean isin = false;
                    for(Feed_Class item : feed_class){
                        String test = item.getMainMessage();
                        if(feed1.getMainMessage().equals(item.getMainMessage())){
                            isin = true;
                        }
                    }
                    if(!isin) {
                        feed_class.add(0, feed1);
                    }
                }
            }
            feed1 = null;
            if (SleepGoal != 0) {
                float test2 = DataAnalyser.parseToHours(helper.getSleepToday());
                double SleepPer = (100 * DataAnalyser.parseToHours(helper.getSleepToday()) / users.get(0).getSleepHoursGoal());
                if (SleepPer == 25) {
                    feed1 = new Feed_Class("You have completed 25% of your Sleep Goal Today, at " + SleepToday + " Well Done!", dateFormat.format(new Date()));
                } else if (SleepPer == 50) {
                    feed1 = new Feed_Class("You have completed 50% of your Sleep Goal Today, at " + SleepToday + " Well Done!", dateFormat.format(new Date()));
                } else if (SleepPer == 75) {
                    feed1 = new Feed_Class("You have completed 75% of your Sleep Goal Today, at " + SleepToday + " Well Done!", dateFormat.format(new Date()));
                } else if (SleepPer >= 100 && SleepPer != 200) {
                    feed1 = new Feed_Class("You have completed 100% of your Sleep Goal Today, at " + SleepToday + " Well Done!", dateFormat.format(new Date()));
                }
                else if (SleepPer == 200) {
                    feed1 = new Feed_Class("You have completed doubled your Sleep Goal Today, at " + SleepToday + " amazing job!", dateFormat.format(new Date()));
                }

                if (feed1 != null) {
                    boolean isin = false;
                    for(Feed_Class item : feed_class){
                        String test = item.getMainMessage();
                        if(feed1.getMainMessage().equals(item.getMainMessage())){
                            isin = true;
                        }
                    }
                    if(!isin) {
                        feed_class.add(0, feed1);
                    }
                }
            }
            feed1 = null;
            if (StudyGoal != 0) {
                float test2 = DataAnalyser.parseToHours(helper.getStudyToday());
                double StudyPer = (100 * DataAnalyser.parseToHours(helper.getStudyToday()) / users.get(0).getStudyHoursGoal());
                if (StudyPer == 25) {
                    feed1 = new Feed_Class("You have completed 25% of your Study Goal Today, at " + StudyToday + " Well Done!", dateFormat.format(new Date()));
                } else if (StudyPer == 50) {
                    feed1 = new Feed_Class("You have completed 50% of your Study Goal Today, at " + StudyToday + " Well Done!", dateFormat.format(new Date()));
                } else if (StudyPer == 75) {
                    feed1 = new Feed_Class("You have completed 75% of your Study Goal Today, at " + StudyToday + " Well Done!", dateFormat.format(new Date()));
                } else if (StudyPer >= 100 && StudyPer != 200) {
                    feed1 = new Feed_Class("You have completed 100% of your Study Goal Today, at " + StudyToday + " Well Done!", dateFormat.format(new Date()));
                }
                else if (StudyPer == 200) {
                    feed1 = new Feed_Class("You have completed doubled your Study Goal Today, at " + StudyToday + " amazing job!", dateFormat.format(new Date()));
                }
                if (feed1 != null) {
                    boolean isin = false;
                    for(Feed_Class item : feed_class){
                        String test = item.getMainMessage();
                        if(feed1.getMainMessage().equals(item.getMainMessage())){
                            isin = true;
                        }
                    }
                    if(!isin) {
                        feed_class.add(0, feed1);
                    }
                }
            }
            feed1 = null;
            if (ExerciseGoal != 0) {
                float test2 = DataAnalyser.parseToHours(helper.getStudyToday());
                double ExercisePer = (100 * DataAnalyser.parseToHours(helper.getExerciseToday()) / users.get(0).getExerciseGoal());
                if (ExercisePer == 25) {
                    feed1 = new Feed_Class("You have completed 25% of your Exercise Goal Today, at " + ExerciseToday + " Well Done!", dateFormat.format(new Date()));
                } else if (ExercisePer == 50) {
                    feed1 = new Feed_Class("You have completed 50% of your Exercise Goal Today, at " + ExerciseToday + " Well Done!", dateFormat.format(new Date()));
                } else if (ExercisePer == 75) {
                    feed1 = new Feed_Class("You have completed 75% of your Exercise Goal Today, at " + ExerciseToday + " Well Done!", dateFormat.format(new Date()));
                } else if (ExercisePer >= 100 && ExercisePer != 200) {
                    feed1 = new Feed_Class("You have completed 100% of your Exercise Goal Today, at " + ExerciseToday + " Well Done!", dateFormat.format(new Date()));
                }
                else if (ExercisePer == 200) {
                    feed1 = new Feed_Class("You have completed doubled your Exercise Goal Today, at " + ExerciseToday + " amazing job!", dateFormat.format(new Date()));
                }
                if (feed1 != null) {
                    boolean isin = false;
                    for(Feed_Class item : feed_class){
                        String test = item.getMainMessage();
                        if(feed1.getMainMessage().equals(item.getMainMessage())){
                            isin = true;
                        }
                    }
                    if(!isin) {
                        feed_class.add(0, feed1);
                    }
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        reload();
    }
}
