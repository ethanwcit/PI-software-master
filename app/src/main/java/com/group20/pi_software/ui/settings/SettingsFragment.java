package com.group20.pi_software.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group20.pi_software.MainActivity;
import com.group20.pi_software.R;
import com.group20.pi_software.databinding.FragmentSettingsBinding;
import com.group20.pi_software.ui.feed.FeedFragment;
import com.group20.pi_software.ui.settings.SettingsViewModel;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;
    private View root;

    private int counter = 0;
    private Toast toast = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        root = binding.getRoot();

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

        SwitchCompat darkModeSwitch = binding.switchDarkMode;
        SwitchCompat feedNotificationsSwitch = binding.switchFeedNotifications;
        SwitchCompat trendsNotificationsSwitch = binding.switchTrendsNotifications;
        SwitchCompat loggingNotificationsSwitch = binding.switchLoggingNotifications;
        SwitchCompat trackSwitch = binding.switchTrack;
        Button resetButton = binding.buttonReset;
        ConstraintLayout sampleLayout = binding.layoutLoadSample;
        Button loadButton = binding.buttonSample;
        TextView aboutAppText = binding.textAboutApp;

        SharedPreferences prefs = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        darkModeSwitch.setChecked(prefs.getBoolean("dark", false));
        feedNotificationsSwitch.setChecked(prefs.getBoolean("feedNotification", true));
        trendsNotificationsSwitch.setChecked(prefs.getBoolean("trendsNotification", true));
        loggingNotificationsSwitch.setChecked(prefs.getBoolean("loggingNotification", true));
        trackSwitch.setChecked(prefs.getBoolean("track", true));

        feedNotificationsSwitch.setEnabled(false);
        trendsNotificationsSwitch.setEnabled(false);
        loggingNotificationsSwitch.setEnabled(false);

        darkModeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean("dark", isChecked).apply();
                NavHostFragment navHostFragment = (NavHostFragment) ((MainActivity)getActivity()).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main);
                ((MainActivity)getActivity()).setNightModeEnabled(isChecked);
            }
        });
        feedNotificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean("feedNotification", isChecked).apply();
            }
        });
        trendsNotificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean("trendsNotification", isChecked).apply();
            }
        });
        loggingNotificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean("loggingNotification", isChecked).apply();
            }
        });
        trackSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean("track", isChecked).apply();
            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Reset this app")
                        .setMessage("You cannot restore data after this process.\nDo you want to clean all data on this app?")
                        .setIcon(R.drawable.ic_round_error_24)
                        .setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        getActivity().getSharedPreferences("Logs", Context.MODE_PRIVATE).edit().clear().apply();
                                        prefs.edit().clear().apply();
                                        MainActivity.getDataBaseHelper().clearAll();
                                        FeedFragment.clearArr();

                                        MainActivity activity = (MainActivity) getActivity();
                                        activity.finish();
                                        activity.startActivity(activity.getIntent());
                                        activity.overridePendingTransition(0,0);

                                    }
                                }
                        )
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Load samples")
                        .setMessage("All data stored currently will be lost.\nYou cannot restore data after this process.\nDo you want to load sample data?")
                        .setIcon(R.drawable.ic_round_error_24)
                        .setPositiveButton(
                                "Yes",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        MainActivity activity = (MainActivity) getActivity();
                                        MainActivity.getDataBaseHelper().loadSamples(activity);
                                        FeedFragment.clearArr();

                                        activity.finish();
                                        activity.startActivity(activity.getIntent());
                                        activity.overridePendingTransition(0,0);

                                    }
                                }
                        )
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        aboutAppText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter ++;
                if (counter == 10){
                    if (toast != null){
                        toast.cancel();
                    }
                    toast = Toast.makeText(getContext(), "You are in developer mode", Toast.LENGTH_SHORT);
                    toast.show();
                    sampleLayout.setVisibility(View.VISIBLE);
                }else if (counter >= 5){
                    if (toast != null){
                        toast.cancel();
                    }
                    toast = Toast.makeText(getContext(), String.format("%d more taps for developer mode", 10 - counter), Toast.LENGTH_SHORT);
                    toast.show();
                }
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

    @Override
    public void onPause() {
        super.onPause();
    }
}
