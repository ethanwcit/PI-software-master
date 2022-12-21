package com.group20.pi_software.ui.correlationGraph;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.group20.pi_software.R;
import com.group20.pi_software.databinding.FragmentCorrelationGraphBinding;

import java.util.ArrayList;
import java.util.List;

public class CorrelationGraphFragment extends Fragment {
    private CorrelationGraphViewModel correlationGraphViewModel;
    private FragmentCorrelationGraphBinding binding;
    private String label;
    private Float[] regression;
    private List<Float[]> dataPoints;

    public static CorrelationGraphFragment newInstance(String label, ArrayList<Float[]> data){
        Bundle args = new Bundle();
        args.putString("label", label);
        if (label != null){
            args.putSerializable("data", data);
        }
        CorrelationGraphFragment graphFragment = new CorrelationGraphFragment();
        graphFragment.setArguments(args);
        return graphFragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        label = getArguments().getString("label");
        if (label != null){
            regression = ((ArrayList<Float[]>) getArguments().getSerializable("data")).get(0);
            dataPoints = ((ArrayList<Float[]>) getArguments().getSerializable("data")).subList(1, ((ArrayList<Float[]>) getArguments().getSerializable("data")).size());
        }

        correlationGraphViewModel =
                new ViewModelProvider(this).get(CorrelationGraphViewModel.class);

        binding = FragmentCorrelationGraphBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        TextView titleText = binding.textCorrelationTitle;
        TextView descriptionText = binding.textCorrelationDescription;
        LineChart chart = binding.scatterCorrelation;

        if (label != null){
            titleText.setText(label);
            String[] dataTypes = label.split(" - ");
            if (regression[0] > 0){
                descriptionText.setText(String.format("When %s increase, %s also increase", dataTypes[1].toLowerCase(), dataTypes[0].toLowerCase()));
            }else{
                descriptionText.setText(String.format("When %s increase, %s decrease", dataTypes[1].toLowerCase(), dataTypes[0].toLowerCase()));
            }
            chart.setData(getScatterData());
            formatGraph(chart, titleText.getCurrentTextColor());
        }else{
            titleText.setText("NO DATA");
            descriptionText.setText("There is no significant correlation between data. This may due to the lack of data point. Please check later.");
        }

        return root;
    }

    private LineData getScatterData(){
        ArrayList<Entry> entries = new ArrayList<>();
        float xMax = -100000;
        float xMin = 1000000;

        for(Float[] xy: dataPoints){
            entries.add(new BarEntry(xy[0], xy[1]));
            if (xMax < xy[0]){
                xMax = xy[0];
            }
            if (xMin > xy[0]){
                xMin = xy[0];
            }
        }

        LineDataSet scatterDataSet = new LineDataSet(entries, "Sleep vs Study");
        scatterDataSet.setDrawValues(false);
        scatterDataSet.setCircleColor(getResources().getColor(R.color.red_orange));
        scatterDataSet.setColor(Color.parseColor("#00000000"));


        ArrayList<Entry> lineEntries = new ArrayList<>();
        lineEntries.add(new Entry(xMin, regression[1] + xMin * regression[2]));
        lineEntries.add(new Entry(xMax, regression[1] + xMax * regression[2]));
        LineDataSet lineDataSet = new LineDataSet(lineEntries, "Correlation");
        lineDataSet.setColor(getResources().getColor(R.color.grey_purple));
        lineDataSet.setDrawValues(false);
        lineDataSet.setDrawCircles(false);
        lineDataSet.setLineWidth(5f);

        LineData lineData = new LineData();
        lineData.addDataSet(scatterDataSet);
        lineData.addDataSet(lineDataSet);

        return  lineData;
    }

    private void formatGraph(LineChart chart, int textColor){
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setDrawGridLines(false);
        chart.getAxisLeft().setTextSize(getResources().getDimension(R.dimen.small_font_size)/2);
        chart.getAxisLeft().setTextColor(textColor);
        chart.getAxisLeft().setLabelCount(5);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setTextSize(getResources().getDimension(R.dimen.small_font_size)/2);
        chart.getXAxis().setTextColor(textColor);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
