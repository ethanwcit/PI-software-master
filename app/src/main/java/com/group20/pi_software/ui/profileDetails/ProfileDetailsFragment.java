package com.group20.pi_software.ui.profileDetails;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.group20.pi_software.MainActivity;
import com.group20.pi_software.R;
import com.group20.pi_software.databinding.FragmentProfileDetailsBinding;

import java.util.ArrayList;

public class ProfileDetailsFragment extends Fragment {

    private ProfileDetailsViewModel profileDetailsViewModel;
    private FragmentProfileDetailsBinding binding;
    private View root;
    private DatePicker datePicker;

    private ArrayList<Component> components;
    private static final String[] heightUnits = new String[] {"cm", "feet"};
    public static final String[] weightUnits = new String[] {"lbs", "kg"};
    private boolean pickerVisible = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileDetailsViewModel =
                new ViewModelProvider(this).get(ProfileDetailsViewModel.class);

        binding = FragmentProfileDetailsBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        components = new ArrayList<Component>() {{
            add(new Component(binding.pdNameInput, DataType.NAME));
            add(new Component(binding.pdEmailInput, DataType.EMAIL));
            add(new Component(binding.pdHeightInput, DataType.HEIGHT));
            add(new Component(binding.pdWeightInput, DataType.WEIGHT));
            add(new Component(binding.pdDoBInput, DataType.DATE_OF_BIRTH));
        }};
        datePicker = binding.pdDatePicker;

        for (Component component : components) {
            displayStoredValues(component);
            setEditTextListeners(component);
        }

        EditText DoB = binding.pdDoBInput;
        ConstraintLayout DoBLayout = binding.pdLayoutDatePicker;
        DoB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleDatePickerVisibility(DoB, DoBLayout);
            }
        });

        ImageButton dateConfirm = binding.pdBottonSelectDate;
        dateConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                components.get(4).getEditText().setText(datePicker.getYear()+"/"+(datePicker.getMonth()+1)+"/"+datePicker.getDayOfMonth());
                saveData(components.get(4));
                toggleDatePickerVisibility(DoB, DoBLayout);
            }
        });

        final Spinner heightUnitIn = binding.pdHeightUnit;
        final Spinner weightUnitIn = binding.pdWeightUnit;

        ArrayAdapter<String> heightAdapter = new ArrayAdapter<>(getContext(), R.layout.pd_custom_spinner_item, heightUnits);
        ArrayAdapter<String> weightAdapter = new ArrayAdapter<>(getContext(), R.layout.pd_custom_spinner_item, weightUnits);
        heightAdapter.setDropDownViewResource(R.layout.pd_custom_spinner_dropdown_item);
        weightAdapter.setDropDownViewResource(R.layout.pd_custom_spinner_dropdown_item);
        heightUnitIn.setAdapter(heightAdapter);
        weightUnitIn.setAdapter(weightAdapter);

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

    private void setEditTextListeners(Component component) {
        component.getEditText().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focused) {
                if (!focused) {
                    saveData(component);
                }
            }
        });

        component.getEditText().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    saveData(component);
                    return true;
                }
                return false;
            }
        });
    }

    private void toggleDatePickerVisibility(EditText DoBText, ConstraintLayout DoBLayout) {
        pickerVisible = !pickerVisible;
        if (!pickerVisible) {
            DoBText.setVisibility(View.VISIBLE);
            DoBLayout.setVisibility(View.GONE);
            return;
        }

        DoBText.setVisibility(View.GONE);
        DoBLayout.setVisibility(View.VISIBLE);
    }

    private void displayStoredValues(Component component) {
        switch (component.getDataType()) {
            case NAME:
                MainActivity.getDataBaseHelper().getUserName();
                System.out.println(MainActivity.getDataBaseHelper().getUserName());
                component.getEditText().setText(MainActivity.getDataBaseHelper().getUserName());
                break;
            case EMAIL:
                MainActivity.getDataBaseHelper().getUserEmail();
                System.out.println(MainActivity.getDataBaseHelper().getUserEmail());
                component.getEditText().setText(MainActivity.getDataBaseHelper().getUserEmail());
                break;
            case DATE_OF_BIRTH:
                MainActivity.getDataBaseHelper().getUserDateOfBirth();
                System.out.println(MainActivity.getDataBaseHelper().getUserDateOfBirth());
                component.getEditText().setText(MainActivity.getDataBaseHelper().getUserDateOfBirth());
                break;
            case HEIGHT:
                MainActivity.getDataBaseHelper().getUserHeight();
                System.out.println(MainActivity.getDataBaseHelper().getUserHeight());
                component.getEditText().setText(String.valueOf(MainActivity.getDataBaseHelper().getUserHeight()));
                break;
            case WEIGHT:
                MainActivity.getDataBaseHelper().getUserWeight();
                System.out.println(MainActivity.getDataBaseHelper().getUserWeight());
                component.getEditText().setText(String.valueOf(MainActivity.getDataBaseHelper().getUserWeight()));
                break;
        }
    }

    private static class Component {
        private final EditText editText;
        private final DataType dataType;
        public Component(EditText editText, DataType dataType) {
            this.editText = editText;
            this.dataType = dataType;
        }
        public EditText getEditText() { return this.editText; }
        public DataType getDataType() { return this.dataType; }
    }

    private enum DataType {
        NAME,
        EMAIL,
        DATE_OF_BIRTH,
        HEIGHT,
        WEIGHT
    }

    private void saveData(Component component) {

            switch (component.getDataType()) {
                case NAME:
                    MainActivity.getDataBaseHelper().updateUserName(component.getEditText().getText().toString());
                    break;
                case EMAIL:
                    MainActivity.getDataBaseHelper().updateUserEmail(component.getEditText().getText().toString());
                    break;
                case DATE_OF_BIRTH:
                    MainActivity.getDataBaseHelper().updateUserDateOfBirth(component.getEditText().getText().toString());
                    break;
                case HEIGHT:
                    MainActivity.getDataBaseHelper().updateUserHeight(Double.parseDouble(component.getEditText().getText().toString()));
                    break;
                case WEIGHT:
                    MainActivity.getDataBaseHelper().updateUserWeight(Double.parseDouble(component.getEditText().getText().toString()));
                    break;
            }
    }
}
