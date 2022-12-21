package com.group20.pi_software;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.group20.pi_software.databinding.ActivityMainBinding;
import com.group20.pi_software.model.DataBaseHelper;
import com.group20.pi_software.model.StepsModel;
import com.group20.pi_software.model.UserModel;
import com.group20.pi_software.ui.feed.FeedFragment;
import com.group20.pi_software.utility.DataTracker;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private static DataBaseHelper dataBaseHelper;
    private ActivityMainBinding binding;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.



        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_feed, R.id.navigation_summary, R.id.navigation_trend, R.id.navigation_profile)
                .build();

        NavHostFragment navHostFragment =  (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
        NavController navController = navHostFragment.getNavController();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        
        NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        configSettings();
        setNightModeEnabled(getSetting("dark"));

        getSupportActionBar().setTitle(R.string.title_feed);
        dataBaseHelper = new DataBaseHelper(this);

        if(MainActivity.getDataBaseHelper().isUserEmpty()){
            UserModel userModel = new UserModel("Name", "Email Address", "Date Joined",
                    "Date of Birth", "ProfilePic", 0.00, 0.00,
                    10000, 2, 6, 8);
            MainActivity.getDataBaseHelper().addUser(userModel);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            getSharedPreferences("Logs", MODE_PRIVATE).edit().putString("last_goals_modified", formatter.format(Calendar.getInstance().getTime())).apply();
        }

        sync();
    }

    public ActivityMainBinding getBinding(){
        return binding;
    }

    public static DataBaseHelper getDataBaseHelper(){return dataBaseHelper;}

    public void setNightModeEnabled(boolean enabled){
        AppCompatDelegate.setDefaultNightMode((enabled)? AppCompatDelegate.MODE_NIGHT_YES: AppCompatDelegate.MODE_NIGHT_NO);
        getDelegate().applyDayNight();
    }

    private void configSettings(){
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);

        if (!prefs.contains("dark")){
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("dark", false);
            editor.putBoolean("feedNotification", true);
            editor.putBoolean("trendsNotification", true);
            editor.putBoolean("loggingNotification", true);
            editor.putBoolean("track", true);
            editor.apply();
        }
    }

    private boolean getSetting(String tag){
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean setting = prefs.getBoolean(tag, false);
        return setting;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void sync(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (getSharedPreferences("Settings", MODE_PRIVATE).getBoolean("track", false)){
                    long current = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond();
                    long last = getSharedPreferences("Logs", MODE_PRIVATE).getLong("last_sync", (long)0);
                    if (last == 0){
                        last = LocalDateTime.now().minusDays(1).atZone(ZoneId.systemDefault()).toEpochSecond();
                    }
                    List<StepsModel> models = new ArrayList<>();
                    try {
                        models = DataTracker.syncSteps(MainActivity.this, last, current);
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (StepsModel model : models) {
                        boolean result = dataBaseHelper.addStepsEntry(model);

                        FeedFragment.addtoarr(" Steps automatically!", String.valueOf(model.getSteps()), model.getTime()+ " " + model.getDate());
                    }
                    if (!models.isEmpty()){
                        getSharedPreferences("Logs", MODE_PRIVATE).edit().putLong("last_sync", current).apply();
                        FragmentContainerView view = binding.navHostFragmentActivityMain;
                        view.invalidate();
                    }
                }
            }
        }).start();
    }
}