package com.group20.pi_software.ui.trend;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.group20.pi_software.ui.correlationGraph.CorrelationGraphFragment;

import java.util.ArrayList;
import java.util.Map;

public class GraphFragmentStatePagerAdapter extends FragmentStateAdapter {
    private int size;
    private Object[] labels;
    private Map<String, ArrayList<Float[]>> data;


    public GraphFragmentStatePagerAdapter(@NonNull FragmentActivity fragmentActivity, int size, Map<String, ArrayList<Float[]>> map) {
        super(fragmentActivity);
        this.size = size;
        this.data = map;
        if (data.isEmpty()){
            labels = new String[]{};
        }else{
            labels = map.keySet().toArray();
        }
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (data.isEmpty()){
            return CorrelationGraphFragment.newInstance(null, null);
        }else{
            return CorrelationGraphFragment.newInstance((String) labels[position], data.get(labels[position]));
        }

    }

    @Override
    public int getItemCount() {
        return size;
    }
}
