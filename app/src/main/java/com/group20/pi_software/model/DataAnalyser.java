package com.group20.pi_software.model;


import android.os.Build;
import android.util.Log;
import android.webkit.WebResourceError;

import androidx.annotation.RequiresApi;

import com.group20.pi_software.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataAnalyser {
    public final static int DAY = 1;
    public final static int WEEK = 2;
    public final static int MONTH = 3;
    public final static int HALF_YEAR = 4;
    public final static int YEAR = 5;

    public final static int SLEEP = 11;
    public final static int STUDY = 12;
    public final static int STEPS = 13;
    public final static int EXERCISE = 14;
    public final static int[] DATATYPES = {SLEEP, STUDY, STEPS, EXERCISE};

    public static Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<StepsModel>>>>> splitStepsByDate(List<StepsModel> models) throws ParseException {
        Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<StepsModel>>>>> map = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String key = "";
        Calendar calendar = Calendar.getInstance();
        int year;
        int month;
        int day;
        int hour;

        for (StepsModel model: models){
            calendar.setTime(dateFormat.parse(model.getDate()));
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = Integer.parseInt(model.getTime().split(":")[0]);
            if (!map.containsKey(year)){
                map.put(year, new HashMap<>());
            }
            if (!map.get(year).containsKey(month)) {
                map.get(year).put(month, new HashMap<>());
            }
            if (!map.get(year).get(month).containsKey(day)){
                map.get(year).get(month).put(day, new HashMap<>());
            }
            if (!map.get(year).get(month).get(day).containsKey(hour)){
                map.get(year).get(month).get(day).put(hour, new ArrayList<>());
            }
            map.get(year).get(month).get(day).get(hour).add(model);
        }

        return map;
    }

    public static Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<ExerciseModel>>>>> splitExerciseByDate(List<ExerciseModel> models) throws ParseException {
        Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<ExerciseModel>>>>> map = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String key = "";
        Calendar calendar = Calendar.getInstance();
        int year;
        int month;
        int day;
        int hour;

        for (ExerciseModel model: models){
            calendar.setTime(dateFormat.parse(model.getDate()));
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = Integer.parseInt(model.getStartTime().split(":")[0]);
            if (!map.containsKey(year)){
                map.put(year, new HashMap<>());
            }
            if (!map.get(year).containsKey(month)) {
                map.get(year).put(month, new HashMap<>());
            }
            if (!map.get(year).get(month).containsKey(day)){
                map.get(year).get(month).put(day, new HashMap<>());
            }
            if (!map.get(year).get(month).get(day).containsKey(hour)){
                map.get(year).get(month).get(day).put(hour, new ArrayList<>());
            }
            map.get(year).get(month).get(day).get(hour).add(model);
        }

        return map;
    }

    public static Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<SleepModel>>>>> splitSleepByDate(List<SleepModel> models) throws ParseException {
        Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<SleepModel>>>>> map = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String key = "";
        Calendar calendar = Calendar.getInstance();
        int year;
        int month;
        int day;
        int hour;

        for (SleepModel model: models){
            calendar.setTime(dateFormat.parse(model.getDate()));
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = Integer.parseInt(model.getStartTime().split(":")[0]);
            if (!map.containsKey(year)){
                map.put(year, new HashMap<>());
            }
            if (!map.get(year).containsKey(month)) {
                map.get(year).put(month, new HashMap<>());
            }
            if (!map.get(year).get(month).containsKey(day)){
                map.get(year).get(month).put(day, new HashMap<>());
            }
            if (!map.get(year).get(month).get(day).containsKey(hour)){
                map.get(year).get(month).get(day).put(hour, new ArrayList<>());
            }
            map.get(year).get(month).get(day).get(hour).add(model);
        }

        return map;
    }

    public static Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<StudyModel>>>>> splitStudyByDate(List<StudyModel> models) throws ParseException {
        Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<StudyModel>>>>> map = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Calendar calendar = Calendar.getInstance();
        int year;
        int month;
        int day;
        int hour;

        for (StudyModel model: models){
            calendar.setTime(dateFormat.parse(model.getDate()));
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH) + 1;
            day = calendar.get(Calendar.DAY_OF_MONTH);
            hour = Integer.parseInt(model.getStartTime().split(":")[0]);
            if (!map.containsKey(year)){
                map.put(year, new HashMap<>());
            }
            if (!map.get(year).containsKey(month)) {
                map.get(year).put(month, new HashMap<>());
            }
            if (!map.get(year).get(month).containsKey(day)){
                map.get(year).get(month).put(day, new HashMap<>());
            }
            if (!map.get(year).get(month).get(day).containsKey(hour)){
                map.get(year).get(month).get(day).put(hour, new ArrayList<>());
            }
            map.get(year).get(month).get(day).get(hour).add(model);
        }

        return map;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static float average(Map<Integer, Float> values, Calendar pivot, int range){
        float average = 0;

        for (Float value: values.values()){
            average += value;
        }

        switch (range){
            case DAY:
                average /= 1;
                break;
            case WEEK:
                average /= 7;
                break;
            case MONTH:
                average /= pivot.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;
            case HALF_YEAR:
                average /= pivot.getActualMaximum(Calendar.YEAR);
                average *= 2;
                break;
            case YEAR:
                average /= pivot.getActualMaximum(Calendar.YEAR);
                break;
        }

        return average;
    }

    public static int sumSteps(List<StepsModel> models){
        int sum = 0;
        for (StepsModel model: models){
            sum += model.getSteps();
        }
        return sum;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static float sumSleep(List<SleepModel> models){
        float sum = 0f;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
        Temporal start;
        Temporal end;
        Calendar temp = Calendar.getInstance();
        for (SleepModel model: models){
            try {
                temp.setTime(formatter.parse(model.getDate()+"-"+model.getStartTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            start = ((Calendar) temp.clone()).toInstant();
            try {
                temp.setTime(formatter.parse(model.getDate()+"-"+model.getEndTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            end = ((Calendar) temp.clone()).toInstant();
            if (Duration.between(start, end).toHours() < 0){
                sum += 24 + Duration.between(start, end).toHours();
            }else{
                sum += Duration.between(start, end).toHours();
            }
        }
        return sum;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static float sumStudy(List<StudyModel> models){
        float sum = 0f;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
        Temporal start;
        Temporal end;
        Calendar temp = Calendar.getInstance();
        for (StudyModel model: models){
            try {
                temp.setTime(formatter.parse(model.getDate()+"-"+model.getStartTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            start = ((Calendar) temp.clone()).toInstant();
            try {
                temp.setTime(formatter.parse(model.getDate()+"-"+model.getEndTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            end = ((Calendar) temp.clone()).toInstant();
            if (Duration.between(start, end).toHours() < 0){
                sum += 24 + Duration.between(start, end).toHours();
            }else{
                sum += Duration.between(start, end).toHours();
            }
        }
        return sum;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static float sumExercise(List<ExerciseModel> models){
        float sum = 0f;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy-HH:mm");
        Temporal start;
        Temporal end;
        Calendar temp = Calendar.getInstance();
        for (ExerciseModel model: models){
            try {
                temp.setTime(formatter.parse(model.getDate()+"-"+model.getStartTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            start = ((Calendar) temp.clone()).toInstant();
            try {
                temp.setTime(formatter.parse(model.getDate()+"-"+model.getEndTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            end = ((Calendar) temp.clone()).toInstant();
            if (Duration.between(start, end).toHours() < 0){
                sum += 24 + Duration.between(start, end).toHours();
            }else{
                sum += Duration.between(start, end).toHours();
            }
        }
        return sum;
    }

    public static float parseToHours(String duration){
        float hours = 0f;

        if (duration.matches("[0-9]+h[0-9]+min")){
            duration = duration.replace("min", "");
            hours = Integer.parseInt(duration.split("h")[0]) + Integer.parseInt(duration.split("h")[1]) / 60;
        }else if (duration.matches("[0-9]+:[0-9]+")){
            hours = Integer.parseInt(duration.split(":")[0]) + Integer.parseInt(duration.split(":")[1]) / 60;
        }

        return hours;
    }

    public static String getRangeLabel(Calendar pivot, int range){
        String start;
        String end;
        String label;
        Calendar temp = Calendar.getInstance();

        int year;
        int[] month;
        int[] day;

        year = pivot.get(Calendar.YEAR);
        temp.set(Calendar.YEAR, year);
        if (range == YEAR){
            month = new int[]{1, 12};
        }else{
            month = new int[]{pivot.get(Calendar.MONTH) + 1, pivot.get(Calendar.MONTH) + 1};
        }
        if (range == HALF_YEAR){
            if (pivot.get(Calendar.MONTH) < 7){
                month = new int[]{1, 6};
            }else{
                month = new int[]{7, 12};
            }
        }
        temp.set(Calendar.MONTH, month[1] -1);
        if (range == MONTH || range == YEAR || range == HALF_YEAR){
            day = new int[]{1, temp.getActualMaximum(Calendar.DAY_OF_MONTH)};
        }else{
            day = new int[]{pivot.get(Calendar.DAY_OF_MONTH), pivot.get(Calendar.DAY_OF_MONTH)};
        }
        if (range == WEEK){
            day = new int[]{pivot.get(Calendar.DAY_OF_MONTH) - pivot.get(Calendar.DAY_OF_WEEK) + 1, pivot.get(Calendar.DAY_OF_MONTH) + 7 - pivot.get(Calendar.DAY_OF_WEEK)};
        }

        start = String.format("%02d/%02d/%d", day[0], month[0], year);
        end = String.format("%02d/%02d/%d", day[1], month[1], year);

        if (start.equals(end)){
            label = start;
        }else {
            label = start + "~" + end;
        }

        return label;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static Map<Integer, Float> getBarGraphValues(int datatype, Calendar pivot, int range) throws ParseException {
        Map<Integer, Float> values = new HashMap<>();
        Calendar temp = Calendar.getInstance();

        int year;
        int[] month;
        int[] day;
        int[] hour;

        year = pivot.get(Calendar.YEAR);
        temp.set(Calendar.YEAR, year);
        if (range == YEAR){
            month = new int[]{1, 12};
        }else{
            month = new int[]{pivot.get(Calendar.MONTH) + 1, pivot.get(Calendar.MONTH) + 1};
        }
        if (range == HALF_YEAR){
            if (pivot.get(Calendar.MONTH) < 7){
                month = new int[]{1, 6};
            }else{
                month = new int[]{7, 12};
            }
        }
        temp.set(Calendar.MONTH, month[1] -1);
        if (range == MONTH || range == YEAR || range == HALF_YEAR){
            day = new int[]{1, temp.getActualMaximum(Calendar.DAY_OF_MONTH)};
        }else{
            day = new int[]{pivot.get(Calendar.DAY_OF_MONTH), pivot.get(Calendar.DAY_OF_MONTH)};
        }
        if (range == WEEK){
            day = new int[]{pivot.get(Calendar.DAY_OF_MONTH) - pivot.get(Calendar.DAY_OF_WEEK) + 1, pivot.get(Calendar.DAY_OF_MONTH) + 7 - pivot.get(Calendar.DAY_OF_WEEK)};
        }
        hour = new int[]{0, 23};

        float sum = 0;

        switch (datatype){
            case SLEEP:
                Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<SleepModel>>>>> map1 = splitSleepByDate(MainActivity.getDataBaseHelper().getAllSleep());
                for (int m = month[0]; m < month[1] + 1; m++){
                    for (int d = day[0]; d < day[1] + 1; d++){
                        for (int h = hour[0]; h < hour[1] + 1; h++){
                            if (map1.get(year) != null && map1.get(year).get(m) != null && map1.get(year).get(m).get(d) != null && map1.get(year).get(m).get(d).get(h) != null){
                                sum += sumSleep(map1.get(year).get(m).get(d).get(h));
                            }
                            if (range == DAY){
                                values.put(h, sum);
                                sum = 0;
                            }
                        }
                        if (range == MONTH){
                            values.put(d, sum);
                            sum = 0;
                        }else if (range == WEEK){
                            values.put(1 + d - day[0], sum);
                            sum = 0;
                        }
                    }
                    if (range == YEAR || range == HALF_YEAR){
                        values.put(m, sum);
                        sum = 0;
                    }
                }
                break;
            case STUDY:
                Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<StudyModel>>>>> map2 = splitStudyByDate(MainActivity.getDataBaseHelper().getAllStudy());
                for (int m = month[0]; m < month[1] + 1; m++){
                    for (int d = day[0]; d < day[1] + 1; d++){
                        for (int h = hour[0]; h < hour[1] + 1; h++){
                            if (map2.get(year) != null && map2.get(year).get(m) != null && map2.get(year).get(m).get(d) != null && map2.get(year).get(m).get(d).get(h) != null){
                                sum += sumStudy(map2.get(year).get(m).get(d).get(h));
                            }
                            if (range == DAY){
                                values.put(h, sum);
                                sum = 0;
                            }
                        }
                        if (range == MONTH){
                            values.put(d, sum);
                            sum = 0;
                        }else if (range == WEEK){
                            values.put(1 + d - day[0], sum);
                            sum = 0;
                        }
                    }
                    if (range == YEAR || range == HALF_YEAR){
                        values.put(m, sum);
                        sum = 0;
                    }
                }
                break;
            case STEPS:
                Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<StepsModel>>>>> map3 = splitStepsByDate(MainActivity.getDataBaseHelper().getAllSteps());
                for (int m = month[0]; m < month[1] + 1; m++){
                    for (int d = day[0]; d < day[1] + 1; d++){
                        for (int h = hour[0]; h < hour[1] + 1; h++){
                            if (map3.get(year) != null && map3.get(year).get(m) != null && map3.get(year).get(m).get(d) != null && map3.get(year).get(m).get(d).get(h) != null){
                                sum += sumSteps(map3.get(year).get(m).get(d).get(h));
                            }
                            if (range == DAY){
                                values.put(h, sum);
                                sum = 0;
                            }
                        }
                        if (range == MONTH){
                            values.put(d, sum);
                            sum = 0;
                        }else if (range == WEEK){
                            values.put( 1 + d - day[0], sum);
                            sum = 0;
                        }
                    }
                    if (range == YEAR || range == HALF_YEAR){
                        values.put(m, sum);
                        sum = 0;
                    }
                }
                break;
            case EXERCISE:
                Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<ExerciseModel>>>>> map4 = splitExerciseByDate(MainActivity.getDataBaseHelper().getAllExercise());
                for (int m = month[0]; m < month[1] + 1; m++){
                    for (int d = day[0]; d < day[1] + 1; d++){
                        for (int h = hour[0]; h < hour[1] + 1; h++){
                            if (map4.get(year) != null && map4.get(year).get(m) != null && map4.get(year).get(m).get(d) != null && map4.get(year).get(m).get(d).get(h) != null){
                                sum += sumExercise(map4.get(year).get(m).get(d).get(h));
                            }
                            if (range == DAY){
                                values.put(h, sum);
                                sum = 0;
                            }
                        }
                        if (range == MONTH){
                            values.put(d, sum);
                            sum = 0;
                        }else if (range == WEEK){
                            values.put(1 + d - day[0], sum);
                            sum = 0;
                        }
                    }
                    if (range == YEAR || range == HALF_YEAR){
                        values.put(m, sum);
                        sum = 0;
                    }
                }
                break;
            default:
                break;
        }
        
        return values;
    }

    public static Map<Integer, Float[]> getCandleGraphValues(int datatype, Calendar pivot, int range) throws ParseException {
        Map<Integer, Float[]> values = new HashMap<>();
        Calendar temp = Calendar.getInstance();

        int year;
        int[] month;
        int[] day;
        int[] hour;

        year = pivot.get(Calendar.YEAR);
        temp.set(Calendar.YEAR, year);
        if (range == YEAR){
            month = new int[]{1, 12};
        }else{
            month = new int[]{pivot.get(Calendar.MONTH) + 1, pivot.get(Calendar.MONTH) + 1};
        }
        if (range == HALF_YEAR){
            if (pivot.get(Calendar.MONTH) < 7){
                month = new int[]{1, 6};
            }else{
                month = new int[]{7, 12};
            }
        }
        temp.set(Calendar.MONTH, month[1] -1);
        if (range == MONTH || range == YEAR || range == HALF_YEAR){
            day = new int[]{1, temp.getActualMaximum(Calendar.DAY_OF_MONTH)};
        }else{
            day = new int[]{pivot.get(Calendar.DAY_OF_MONTH), pivot.get(Calendar.DAY_OF_MONTH)};
        }
        if (range == WEEK){
            day = new int[]{pivot.get(Calendar.DAY_OF_MONTH) - pivot.get(Calendar.DAY_OF_WEEK) + 1, pivot.get(Calendar.DAY_OF_MONTH) + 7 - pivot.get(Calendar.DAY_OF_WEEK)};
        }
        hour = new int[]{0, 23};

        Float[] times = {0f,0f};
        ArrayList<Float[]> tempTimes = new ArrayList<>();

        switch (datatype){
            case SLEEP:
                Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<SleepModel>>>>> map1 = splitSleepByDate(MainActivity.getDataBaseHelper().getAllSleep());
                for (int m = month[0]; m < month[1] + 1; m++){
                    for (int d = day[0]; d < day[1] + 1; d++){
                        times[0] = 0f;
                        times[1] = 0f;
                        if (map1.get(year) != null && map1.get(year).get(m) != null && map1.get(year).get(m).get(d) != null){
                            for (int h: map1.get(year).get(m).get(d).keySet()){
                                for (SleepModel model: map1.get(year).get(m).get(d).get(h)) {
                                    times[0] = parseToHours(model.getStartTime());
                                    times[1] = parseToHours(model.getEndTime());
                                    for (int x = 0; x < 2; x++){
                                        if (times[x] > 12){
                                            times[x] = 36 - times[x];
                                        }else{
                                            times[x] = 12 - times[x];
                                        }
                                    }
                                    if (range == WEEK) {
                                        values.put(1 + d - day[0], times.clone());
                                    } else if (range == MONTH) {
                                        values.put(d, times.clone());
                                    }else{
                                        tempTimes.add(times.clone());
                                    }
                                }
                            }
                        }

                        if (times[0] == 0f && times[1] == 0f){
                            if (range == WEEK){
                                values.put(1 + d - day[0], times.clone());
                            }else if (range == MONTH){
                                values.put(d, times.clone());
                            }else{
                                tempTimes.add(times.clone());
                            }
                        }
                    }
                    if (range == HALF_YEAR || range == YEAR){
                        Float start = 0f;
                        Float end = 0f;
                        int count = 0;
                        for (Float[] time: tempTimes){
                            if (time[0] != 0f && time[1] != 0){
                                count++;
                            }
                            start += time[0];
                            end += time[1];
                        }
                        values.put(m, new Float[]{start / count, end / count});
                        tempTimes.clear();
                    }

                }
                break;
            case STUDY:
                Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<StudyModel>>>>> map2 = splitStudyByDate(MainActivity.getDataBaseHelper().getAllStudy());
                for (int m = month[0]; m < month[1] + 1; m++){
                    for (int d = day[0]; d < day[1] + 1; d++){
                        times[0] = 0f;
                        times[1] = 0f;
                        if (map2.get(year) != null && map2.get(year).get(m) != null && map2.get(year).get(m).get(d) != null){
                            for (int h: map2.get(year).get(m).get(d).keySet()){
                                for (StudyModel model: map2.get(year).get(m).get(d).get(h)) {
                                    times[0] = 24 - parseToHours(model.getStartTime());
                                    times[1] = 24 - parseToHours(model.getEndTime());
                                    if (range == WEEK) {
                                        values.put(1 + d - day[0], times.clone());
                                    } else if (range == MONTH) {
                                        values.put(d, times.clone());
                                    }else{
                                        tempTimes.add(times.clone());
                                    }
                                }
                            }
                        }

                        if (times[0] == 0f && times[1] == 0f){
                            if (range == WEEK){
                                values.put(1 + d - day[0], times.clone());
                            }else if (range == MONTH){
                                values.put(d, times.clone());
                            }else{
                                tempTimes.add(times.clone());
                            }
                        }
                    }
                    if (range == HALF_YEAR || range == YEAR){
                        Float start = 0f;
                        Float end = 0f;
                        int count = 0;
                        for (Float[] time: tempTimes){
                            if (time[0] != 0f && time[1] != 0){
                                count++;
                            }
                            start += time[0];
                            end += time[1];
                        }
                        values.put(m, new Float[]{start / count, end / count});
                        tempTimes.clear();
                    }

                }
                break;
            case EXERCISE:
                Map<Integer, Map<Integer, Map<Integer, Map<Integer, List<ExerciseModel>>>>> map3 = splitExerciseByDate(MainActivity.getDataBaseHelper().getAllExercise());
                for (int m = month[0]; m < month[1] + 1; m++){
                    for (int d = day[0]; d < day[1] + 1; d++){
                        times[0] = 0f;
                        times[1] = 0f;
                        if (map3.get(year) != null && map3.get(year).get(m) != null && map3.get(year).get(m).get(d) != null){
                            for (int h: map3.get(year).get(m).get(d).keySet()){
                                for (ExerciseModel model: map3.get(year).get(m).get(d).get(h)) {
                                    times[0] = 24 - parseToHours(model.getStartTime());
                                    times[1] = 24 - parseToHours(model.getEndTime());
                                    if (range == WEEK) {
                                        values.put(1 + d - day[0], times.clone());
                                    } else if (range == MONTH) {
                                        values.put(d, times.clone());
                                    }else{
                                        tempTimes.add(times.clone());
                                    }
                                }
                            }
                        }

                        if (times[0] == 0f && times[1] == 0f){
                            if (range == WEEK){
                                values.put(1 + d - day[0], times.clone());
                            }else if (range == MONTH){
                                values.put(d, times.clone());
                            }else{
                                tempTimes.add(times.clone());
                            }
                        }
                    }
                    if (range == HALF_YEAR || range == YEAR){
                        Float start = 0f;
                        Float end = 0f;
                        int count = 0;
                        for (Float[] time: tempTimes){
                            if (time[0] != 0f && time[1] != 0){
                                count++;
                            }
                            start += time[0];
                            end += time[1];
                        }
                        values.put(m, new Float[]{start / count, end / count});
                        tempTimes.clear();
                    }
                }
                break;
            default:
                break;
        }

        return values;
    }

    public static Float[] getCorrelation(ArrayList<Float[]> values){
        Float[] result = {0f, 0f, 0f}; //{r, a, b}
        float meanX = 0f;
        float meanY = 0f;
        float Sxx = 0f;
        float Syy = 0f;
        float Sxy = 0f;

        for (Float[] xy: values){
            meanX += xy[0];
            meanY += xy[1];
        }
        meanX /= values.size();
        meanY /= values.size();

        for (Float[] xy: values){
            Sxx += Math.pow((xy[0] - meanX), 2);
            Syy += Math.pow((xy[1] - meanY), 2);
            Sxy += (xy[0] - meanX) * (xy[1] - meanY);
        }
        result[0] = (float)(Sxy / Math.sqrt(Sxx * Syy));
        result[2] = Sxy / Sxx;
        result[1] = meanY - result[2] * meanX;

        return result.clone();
    }
}
