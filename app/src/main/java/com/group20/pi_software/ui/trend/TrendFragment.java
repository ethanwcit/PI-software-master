package com.group20.pi_software.ui.trend;

import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.group20.pi_software.MainActivity;
import com.group20.pi_software.R;
import com.group20.pi_software.databinding.FragmentTrendBinding;
import com.group20.pi_software.model.DataAnalyser;
import com.group20.pi_software.model.UserModel;
import com.group20.pi_software.utility.ShareHelper;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator3;

public class TrendFragment extends Fragment {

    private final int SLEEP = 1;
    private final int STUDY = 2;
    private final int STEPS = 3;
    private final int EXERCISE = 4;
    private final int[] dataTypes = {SLEEP, STUDY, STEPS, EXERCISE};

    private final int DAY = 11;
    private final int WEEK = 12;
    private final int MONTH = 13;
    private final int HALF_YEAR = 14;
    private final int YEAR = 15;

    private final int[] durations = {DAY, WEEK, MONTH, HALF_YEAR, YEAR};

    private TrendViewModel trendViewModel;
    private FragmentTrendBinding binding;
    private Calendar pivot;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        trendViewModel =
                new ViewModelProvider(this).get(TrendViewModel.class);

        binding = FragmentTrendBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        pivot = Calendar.getInstance();

        try {
            configTrendCard(
                    SLEEP,
                    binding.toggleSleepDuration,
                    binding.toggleSleepGraphType,
                    binding.barChartSleep,
                    binding.candleChartSleep,
                    binding.textSleepAverage,
                    binding.textSleepRange,
                    binding.buttonSleepRangeBefore,
                    binding.buttonSleepRangeAfter);
        } catch (ParseException e) {
            Log.d("onCreateView", "configTrendCard");
            e.printStackTrace();
        }

        try {
            configTrendCard(
                    STUDY,
                    binding.toggleStudyDuration,
                    binding.toggleStudyGraphType,
                    binding.barChartStudy,
                    binding.candleChartStudy,
                    binding.textStudyAverage,
                    binding.textStudyRange,
                    binding.buttonStudyRangeBefore,
                    binding.buttonStudyRangeAfter);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            configTrendCard(
                    EXERCISE,
                    binding.toggleActivityDuration,
                    binding.toggleActivityGraphType,
                    binding.barChartActivity,
                    binding.candleChartActivity,
                    binding.textActivityAverage,
                    binding.textActivityRange,
                    binding.buttonActivityRangeBefore,
                    binding.buttonActivityRangeAfter);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            configTrendCard(
                    STEPS,
                    binding.toggleStepsDuration,
                    null,
                    binding.barChartSteps,
                    null,
                    binding.textStepsAverage,
                    binding.textStepsRange,
                    binding.buttonStepsRangeBefore,
                    binding.buttonStepsRangeAfter);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        ImageButton shareCorrelation = binding.buttonShareCorrelation;
        ImageButton shareSleep = binding.buttonShareSleep;
        ImageButton shareStudy = binding.buttonShareStudy;
        ImageButton shareExercise = binding.buttonShareExercise;
        ImageButton shareSteps = binding.buttonShareSteps;

        shareCorrelation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareHelper.openShareThisApp(binding.cardCorrelationChart);
            }
        });
        shareSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareHelper.openShareThisApp(binding.cardSleepChart);
            }
        });
        shareStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareHelper.openShareThisApp(binding.cardStudyChart);
            }
        });
        shareExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareHelper.openShareThisApp(binding.cardExerciseChart);
            }
        });
        shareSteps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareHelper.openShareThisApp(binding.cardStepsChart);
            }
        });

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Map<String, ArrayList<Float[]>> map = checkCorrelation();
        int size;
        if (map.size() == 0){
            size = 1;
        }else{
            size = map.size();
        }
        ViewPager2 viewPager = binding.viewPagerCorrelation;
        GraphFragmentStatePagerAdapter adapter = new GraphFragmentStatePagerAdapter(getActivity(), size, map);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        CircleIndicator3 indicator = binding.indicatorCorrelationGraph;
        indicator.setViewPager(viewPager);
        adapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void configTrendCard(int dataType,
                                 @NonNull MultiStateToggleButton duration,
                                 MultiStateToggleButton graphType,
                                 @NonNull CombinedChart bar,
                                 CandleStickChart candle,
                                 TextView average,
                                 TextView range,
                                 ImageButton before,
                                 ImageButton after) throws ParseException {
        duration.setValue(0);

        float[] goals = {-1,-1,-1,-1};
        List<UserModel> users = MainActivity.getDataBaseHelper().getUser();
        if (!users.isEmpty()){
            goals[0] = (float) users.get(0).getSleepHoursGoal();
            goals[1] = (float) users.get(0).getStudyHoursGoal();
            goals[2] = (float) users.get(0).getStepsGoal();
            goals[3] = (float) users.get(0).getExerciseGoal();
        }

        int d;
        if (dataType == SLEEP){
            d = durations[duration.getValue() + 1];
        }else if (graphType != null && graphType.getValue() == 1 && dataType != STEPS){
            d = durations[duration.getValue() + 1];
        }else{
            d = durations[duration.getValue()];
        }

        refreshBar(bar, dataType, d, goals, average, pivot);

        duration.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                int d;

                if (dataType == SLEEP){
                    d = durations[value + 1];
                }else if (graphType != null && graphType.getValue() == 1 && dataType != STEPS){
                    d = durations[value + 1];
                }else{
                    d = durations[value];
                }
                refreshBar(bar, dataType, d, goals, average, pivot);
                if (graphType != null && graphType.getValue() == 1){
                    try {
                        refreshCandle(candle, dataType, d, average, pivot);
                    } catch (ParseException e) {
                        Log.d("configTrendCard", "refreshCandle");
                        e.printStackTrace();
                    }
                }
                range.setText(DataAnalyser.getRangeLabel(pivot, d - 10));
            }
        });

        if (graphType != null){
            graphType.setValue(0);
            graphType.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
                @Override
                public void onValueChanged(int value) {
                    if (value == 1){
                        bar.setVisibility(View.GONE);
                        candle.setVisibility(View.VISIBLE);

                        if (dataType == STUDY || dataType == EXERCISE){
                            duration.setElements(getResources().getStringArray(R.array.data_durations));
                            duration.setValue(0);

                        }
                        int d;
                        if (dataType == SLEEP){
                            d = durations[duration.getValue() + 1];
                        }else if (dataType != STEPS){
                            d = durations[duration.getValue() + 1];
                        }else{
                            d = durations[duration.getValue()];
                        }
                        try {
                            refreshCandle(candle, dataType, d, average, pivot);
                        } catch (ParseException e) {
                            Log.d("configTrendCard", "refreshCandle");
                            e.printStackTrace();
                        }
                    }else{
                        candle.setVisibility(View.GONE);
                        bar.setVisibility(View.VISIBLE);
                        if (dataType == STUDY || dataType == EXERCISE){
                            duration.setElements(getResources().getStringArray(R.array.data_finerDurations));
                            duration.setValue(0);
                        }
                    }


                }
            });
        }

        range.setText(DataAnalyser.getRangeLabel(pivot, d - 10));
        
        before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int d;
                if (dataType == SLEEP){
                    d = durations[duration.getValue() + 1];
                }else if (graphType != null && graphType.getValue() == 1 && dataType != STEPS){
                    d = durations[duration.getValue() + 1];
                }else{
                    d = durations[duration.getValue()];
                }
                switch (d){
                    case DAY:
                        pivot.add(Calendar.DAY_OF_MONTH, -1);
                        break;
                    case WEEK:
                        pivot.add(Calendar.WEEK_OF_MONTH, -1);
                        break;
                    case MONTH:
                        pivot.add(Calendar.MONTH, -1);
                        break;
                    case HALF_YEAR:
                        pivot.add(Calendar.MONTH, -6);
                        break;
                    case YEAR:
                        pivot.add(Calendar.YEAR, -1);
                        break;
                    default:
                        break;
                }
                refreshBar(bar, dataType, d, goals, average, pivot);
                if (graphType != null && graphType.getValue() == 1){
                    try {
                        refreshCandle(candle, dataType, d, average, pivot);
                    } catch (ParseException e) {
                        Log.d("configTrendCard", "refreshCandle");
                        e.printStackTrace();
                    }
                }
                range.setText(DataAnalyser.getRangeLabel(pivot, d - 10));
            }
        });
        after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int d;
                if (dataType == SLEEP){
                    d = durations[duration.getValue() + 1];
                }else if (graphType != null && graphType.getValue() == 1 && dataType != STEPS){
                    d = durations[duration.getValue() + 1];
                }else{
                    d = durations[duration.getValue()];
                }
                switch (d){
                    case DAY:
                        pivot.add(Calendar.DAY_OF_MONTH, 1);
                        break;
                    case WEEK:
                        pivot.add(Calendar.WEEK_OF_MONTH, 1);
                        break;
                    case MONTH:
                        pivot.add(Calendar.MONTH, 1);
                        break;
                    case HALF_YEAR:
                        pivot.add(Calendar.MONTH, 6);
                        break;
                    case YEAR:
                        pivot.add(Calendar.YEAR, 1);
                        break;
                    default:
                        break;
                }
                refreshBar(bar, dataType, d, goals, average, pivot);
                if (graphType != null && graphType.getValue() == 1){
                    try {
                        refreshCandle(candle, dataType, d, average, pivot);
                    } catch (ParseException e) {
                        Log.d("configTrendCard", "refreshCandle");
                        e.printStackTrace();
                    }
                }
                range.setText(DataAnalyser.getRangeLabel(pivot, d - 10));
            }
        });

    }

    private CombinedData getBarData(Map<Integer, Float> values, float goal){

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        for(Map.Entry<Integer, Float> value: values.entrySet()){
            if (value.getKey() != 0){
                entries.add(new BarEntry(value.getKey(), value.getValue()));
            }
        }
        entries.add(new BarEntry(Collections.max(values.keySet()) + 1, 0f));

        BarDataSet barDataSet = new BarDataSet(entries, "Record");
        barDataSet.setColor(getResources().getColor(R.color.red_orange));
        barDataSet.setDrawValues(false);

        CombinedData combinedData = new CombinedData();
        combinedData.setData(new BarData(barDataSet));

        if (goal > 0){
            ArrayList<Entry> lineEntries = new ArrayList<>();
            lineEntries.add(new Entry(1, goal));
            lineEntries.add(new Entry(values.size(), goal));
            LineDataSet lineDataSet = new LineDataSet(lineEntries, "Goal");
            lineDataSet.setColor(getResources().getColor(R.color.grey_purple));
            lineDataSet.setDrawValues(false);
            lineDataSet.setDrawCircles(false);
            lineDataSet.setLineWidth(5f);
            combinedData.setData(new LineData(lineDataSet));
        }

        return  combinedData;
    }

    private CandleData getCandleData(Map<Integer, Float[]> values){

        ArrayList<CandleEntry> entries = new ArrayList<CandleEntry>();
        for(Map.Entry<Integer, Float[]> entry: values.entrySet()){
            entries.add(new CandleEntry(entry.getKey(), 22, 2, entry.getValue()[1], entry.getValue()[0]));
        }

        CandleDataSet dataSet = new CandleDataSet(entries, "Sleep");
        dataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        dataSet.setIncreasingColor(getResources().getColor(R.color.red_orange));
        dataSet.setDecreasingColor(getResources().getColor(R.color.grey_purple));
        dataSet.setDrawValues(false);

        CandleData candleData = new CandleData();
        candleData.addDataSet(dataSet);

        return candleData;
    }

    private void formatGraph(CombinedChart chart, String[] xValueLabels, int textColor){
        chart.getLegend().setTextColor(textColor);
        chart.getDescription().setEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setTextSize(getResources().getDimension(R.dimen.small_font_size)/2);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setTextColor(textColor);
        chart.getAxisRight().setAxisMinimum(0f);
        chart.getAxisRight().setLabelCount(5);

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setTextSize(getResources().getDimension(R.dimen.small_font_size)/2);
        chart.getXAxis().setTextColor(textColor);

        if (xValueLabels != null){
            ArrayList<String> labels = new ArrayList<>();
            labels.addAll(Arrays.asList(xValueLabels));
            labels.add(0, "");
            labels.add("");
            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        }else{
            chart.getXAxis().setValueFormatter(null);
        }
    }

    private void formatGraph(CandleStickChart chart, String[] xValueLabels, int textColor, int dataType){
        chart.getLegend().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.setDragEnabled(false);
        chart.setScaleEnabled(false);
        chart.setPinchZoom(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.getAxisLeft().setEnabled(false);
        chart.getAxisRight().setDrawGridLines(false);
        chart.getAxisRight().setTextSize(getResources().getDimension(R.dimen.small_font_size)/2);
        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setTextColor(textColor);
        chart.getAxisRight().setAxisMinimum(0f);
        chart.getAxisRight().setAxisMaximum(24f);
        ArrayList<String> yLabels = new ArrayList<>();
        if (dataType == SLEEP){
            for (int i = 0; i <= 24; i++){
                if (i > 12){
                    yLabels.add(String.valueOf(36 - i));
                }else{
                    yLabels.add(String.valueOf(12 - i));
                }
            }
        }else{
            for (int i = 24; i >= 0; i --){
                yLabels.add(String.valueOf(i));
            }
        }
        chart.getAxisRight().setValueFormatter(new IndexAxisValueFormatter(yLabels));

        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawGridLines(false);
        chart.getXAxis().setDrawAxisLine(false);
        chart.getXAxis().setTextSize(getResources().getDimension(R.dimen.small_font_size)/2);
        chart.getXAxis().setTextColor(textColor);

        if (xValueLabels != null){
            ArrayList<String> labels = new ArrayList<>();
            labels.addAll(Arrays.asList(xValueLabels));
            labels.add(0, "");
            labels.add("");
            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        }else{
            chart.getXAxis().setValueFormatter(null);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refreshBar(CombinedChart bar, int dataType, int d, float[] goals, TextView average, Calendar pivot){
        if (bar.getData() != null){
            bar.getData().clearValues();
        }
        bar.clear();

        try {
            if (d == DAY){
                bar.setData(getBarData(DataAnalyser.getBarGraphValues(DataAnalyser.DATATYPES[dataType -1], pivot, d-10), 0));
            }else{
                bar.setData(getBarData(DataAnalyser.getBarGraphValues(DataAnalyser.DATATYPES[dataType -1], pivot, d-10), goals[dataType -1]));
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] arr = null;
        if (d == WEEK){
            arr = getResources().getStringArray(R.array.week_headers);
        }
        formatGraph(bar, arr, average.getCurrentTextColor());

        try {
            average.setText(String.format("%,.2f", DataAnalyser.average(DataAnalyser.getBarGraphValues(DataAnalyser.DATATYPES[dataType -1], pivot, d-10), pivot, d-10)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        bar.notifyDataSetChanged();
        bar.invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void refreshCandle(CandleStickChart candle, int dataType, int d, TextView average, Calendar pivot) throws ParseException {
        if (candle.getData() != null){
            candle.getData().clearValues();
        }
        candle.clear();

        try {
            candle.setData(getCandleData(DataAnalyser.getCandleGraphValues(DataAnalyser.DATATYPES[dataType -1], pivot, d-10)));
        } catch (ParseException e) {
            Log.d("refreshCandle", "setData");
            e.printStackTrace();
        }
        String[] arr = null;
        if (d == WEEK){
            arr = getResources().getStringArray(R.array.week_headers);
        }

        formatGraph(candle, arr, average.getCurrentTextColor(), dataType);

        try {
            average.setText(String.format("%,.2f", DataAnalyser.average(DataAnalyser.getBarGraphValues(DataAnalyser.DATATYPES[dataType -1], pivot, d-10), pivot, d-10)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        candle.notifyDataSetChanged();
        candle.invalidate();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Map<String, ArrayList<Float[]>> checkCorrelation(){
        Map<String, ArrayList<Float[]>> map = new HashMap<>();
        Map<Integer, Float> data1 = new HashMap<>();
        Map<Integer, Float> data2 = new HashMap<>();
        ArrayList<Float[]> tempArr = new ArrayList<>();
        Float[] temp;
        String[] labels = getResources().getStringArray(R.array.data_type_names);
        
        for (int type2: dataTypes){
            for (int type1 = type2 + 1; type1 < 5; type1++){
                try {
                    data1 = DataAnalyser.getBarGraphValues(DataAnalyser.DATATYPES[type2 -1], Calendar.getInstance(), MONTH-10);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    data2 = DataAnalyser.getBarGraphValues(DataAnalyser.DATATYPES[type1 -1], Calendar.getInstance(), MONTH-10);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < Math.min(data1.size(), data2.size()); i++){
                    tempArr.add(new Float[]{data2.get(i + 1), data1.get(i + 1)});
                }
                temp = DataAnalyser.getCorrelation(tempArr);
                if (Math.abs(temp[0]) > 0.5){
                    tempArr.add(0, temp.clone());
                    map.put(String.format("%s - %s", labels[type2 -1], labels[type1 -1]), (ArrayList<Float[]>) tempArr.clone());
                }
                tempArr.clear();
            }
        }

        return map;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onResume() {
        super.onResume();
        try {
            configTrendCard(
                    SLEEP,
                    binding.toggleSleepDuration,
                    binding.toggleSleepGraphType,
                    binding.barChartSleep,
                    binding.candleChartSleep,
                    binding.textSleepAverage,
                    binding.textSleepRange,
                    binding.buttonSleepRangeBefore,
                    binding.buttonSleepRangeAfter);
        } catch (ParseException e) {
            Log.d("onResume", "configTrendCard");
            e.printStackTrace();
        }

        try {
            configTrendCard(
                    STUDY,
                    binding.toggleStudyDuration,
                    binding.toggleStudyGraphType,
                    binding.barChartStudy,
                    binding.candleChartStudy,
                    binding.textStudyAverage,
                    binding.textStudyRange,
                    binding.buttonStudyRangeBefore,
                    binding.buttonStudyRangeAfter);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            configTrendCard(
                    EXERCISE,
                    binding.toggleActivityDuration,
                    null,
                    binding.barChartActivity,
                    null,
                    binding.textActivityAverage,
                    binding.textActivityRange,
                    binding.buttonActivityRangeBefore,
                    binding.buttonActivityRangeAfter);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            configTrendCard(
                    STEPS,
                    binding.toggleStepsDuration,
                    null,
                    binding.barChartSteps,
                    null,
                    binding.textStepsAverage,
                    binding.textStepsRange,
                    binding.buttonStepsRangeBefore,
                    binding.buttonStepsRangeAfter);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}