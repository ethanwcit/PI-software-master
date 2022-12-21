package com.group20.pi_software.utility;



import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.HistoryClient;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.group20.pi_software.MainActivity;
import com.group20.pi_software.model.StepsModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class DataTracker {
    private final static int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE = 1;

    public static GoogleSignInAccount checkPermission(MainActivity activity){
        FitnessOptions fitnessOptions =  FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .build();
        GoogleSignInAccount account = GoogleSignIn.getAccountForExtension(activity, fitnessOptions);
        if (!GoogleSignIn.hasPermissions(account, fitnessOptions)){
            GoogleSignIn.requestPermissions(
                    activity,
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    account,
                    fitnessOptions
            );
        }
        return account;
    }

    public static List<StepsModel> syncSteps(MainActivity activity, long start, long end) throws ExecutionException, InterruptedException {
        GoogleSignInAccount account = checkPermission(activity);
        List<StepsModel> models = new ArrayList<>();


        DataReadRequest request = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .setTimeRange(start, end, TimeUnit.SECONDS)
                .bucketByTime(5, TimeUnit.MINUTES)
                .build();

        HistoryClient client = Fitness.getHistoryClient(activity, account);
        Task<DataReadResponse> task = client.readData(request);
        Tasks.await(task);

        DataReadResponse response = Objects.requireNonNull(task.getResult());

        for (Bucket bucket : response.getBuckets()) {
            String date = new SimpleDateFormat("dd/MM/yyyy").format(new Date(bucket.getStartTime(TimeUnit.MILLISECONDS)));
            String startTime = new SimpleDateFormat("HH:mm").format(new Date(bucket.getStartTime(TimeUnit.MILLISECONDS)));
            List<DataPoint> dataPoints = bucket.getDataSet(DataType.AGGREGATE_STEP_COUNT_DELTA).getDataPoints();
            if (!dataPoints.isEmpty()){
                int steps = Integer.parseInt(dataPoints.get(0).getValue(Field.FIELD_STEPS).toString());
                if (steps != 0){
                    models.add(new StepsModel(-1, date, startTime, steps));
                }
            }
        }

        return models;
    }
}
