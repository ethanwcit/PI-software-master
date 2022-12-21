package com.group20.pi_software.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.imageview.ShapeableImageView;
import com.group20.pi_software.MainActivity;
import com.group20.pi_software.R;
import com.group20.pi_software.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);


        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ShapeableImageView avatarImage = binding.imageViewAvatar;
        TextView firstnameText = binding.textFirstName;
        TextView surnameText = binding.textSurname;

        CardView profileCard = binding.cardProfile;
        CardView goalsCard = binding.cardGoals;
        CardView settingsCard = binding.cardSettings;

        if (MainActivity.getDataBaseHelper().getUserName().split(" ").length >=2){
            firstnameText.setText( MainActivity.getDataBaseHelper().getUserName().split(" ")[0]);
            surnameText.setText( MainActivity.getDataBaseHelper().getUserName().split(" ")[1]);
        }else {
            firstnameText.setText("Hello");
            surnameText.setText("Welcome");
        }


        profileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.navigation_profile_details);
            }
        });
        goalsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.navigation_goals);
            }
        });
        settingsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.navigation_settings);
            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}