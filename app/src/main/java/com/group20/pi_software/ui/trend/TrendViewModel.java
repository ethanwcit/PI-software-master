package com.group20.pi_software.ui.trend;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TrendViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public TrendViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is trend fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
