package com.cardiodata.utils;

import com.cardiodata.core.jpa.CardioDataItem;
import com.cardiodata.json.entity.JsonRRInterval;
import com.cardiomood.math.HeartRateUtils;
import com.cardiomood.math.filter.ArtifactFilter;
import com.cardiomood.math.filter.PisarukArtifactFilter;
import com.cardiomood.math.filter.SimpleInterpolationArtifactFilter;
import com.cardiomood.math.spectrum.FFT;
import com.cardiomood.math.spectrum.SpectralAnalysis;
import com.cardiomood.math.window.DataWindow;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class CalcManager {

    private static final double STRESS_WINDOW_SIZE = 120 * 1000;
    private static final double STRESS_STEP_SIZE = 5 * 1000;
    private static final double SIGMA_WINDOW_SIZE = 20;
    private static final double SIGMA_STEP_SIZE = 5;
    private static final ArtifactFilter filter = new PisarukArtifactFilter();
    private static final int STRESS_WINDOW_SIZE_INT = 120 * 1000;
    private static final int STRESS_STEP_SIZE_INT = 5 * 1000;
    private static final int SIGMA_WINDOW_SIZE_INT = 20;
    private static final int SIGMA_STEP_SIZE_INT = 5;

    public static double[] pisarukFilter(double[] arr){
        return filter.doFilter(arr);
    }
    
    public static double[][] getSpectrum(double[] intervals) {

        double[] t = new double[intervals.length];
        t[0] = 0;
        for (int i = 1; i < intervals.length; i++) {
            t[i] = t[i - 1] + intervals[i - 1];
        }

        FFT fft = new FFT(t, intervals);
        double[] d = fft.getPower();

        double[][] res = new double[d.length][2];

        for (int i = 0; i < d.length; i++) {
            res[i][0] = fft.toFrequency(i);
            res[i][1] = d[i];
        }

        return res;
    }

    public static double[][] getTensionArray(double[] intervals) {
        intervals = filter.doFilter(intervals);
        DataWindow window = new DataWindow.Timed(STRESS_WINDOW_SIZE, STRESS_STEP_SIZE);
        double[][] res = HeartRateUtils.getSI(intervals, window);
        return res;
    }

    public static double[][] getSDNN(double[] intervals) {
        intervals = filter.doFilter(intervals);
        DataWindow window = new DataWindow.IntervalsCount(SIGMA_WINDOW_SIZE, SIGMA_STEP_SIZE);
        double[][] res = HeartRateUtils.getSDNN(intervals, window);
        return res;
    }

    public static double[][] getTensionArray(double[] intervals, double[] time, boolean useFilter) {
        if (useFilter == true){
            intervals = filter.doFilter(intervals);
        }
        DataWindow window = new DataWindow.Timed(STRESS_WINDOW_SIZE, STRESS_STEP_SIZE);
        
        System.out.println(" ");
        
        double[][] res = HeartRateUtils.getSI(intervals, time, STRESS_WINDOW_SIZE_INT, STRESS_STEP_SIZE_INT);
        return res;
    }

    public static double[][] getSDNN(double[] intervals, double[] time, boolean useFilter) {
        if (useFilter == true){
            intervals = filter.doFilter(intervals);
        }
        DataWindow window = new DataWindow.IntervalsCount(SIGMA_WINDOW_SIZE, SIGMA_STEP_SIZE);
        double[][] res = HeartRateUtils.getSDNN(intervals, time, SIGMA_WINDOW_SIZE_INT, SIGMA_STEP_SIZE_INT);
        return res;
    }
    
    public static double[] getArrayFromRRCardioDataItemList(List<CardioDataItem> items){
        if (items == null || items.size() == 0){
            return new double[0];
        }
        double[] arr = new double[items.size()];
        Gson gson = new Gson();
        for (int i = 0; i < items.size(); i++){
            String data = items.get(i).getDataItem();
            Integer r = gson.fromJson(data, JsonRRInterval.class).getR();
            arr[i] = 1.0 * r;
        }
        return arr;
    }
    
    public static double[][] get2DArrayFromRRCardioDataItemList(List<CardioDataItem> items){
        if (items == null || items.size() == 0){
            return new double[0][];
        }
        double[][] arr = new double[2][items.size()];
        Gson gson = new Gson();
        for (int i = 0; i < items.size(); i++){
            String data = items.get(i).getDataItem();
            Integer r = gson.fromJson(data, JsonRRInterval.class).getR();
            arr[0][i] = items.get(i).getCreationTimestamp();
            arr[1][i] = 1.0 * r;
        }
        return arr;
    }
    
    public static double[][] filter2DRRArray(double[][] arr){
        arr[1] = filter.doFilter(arr[1]);
        arr[0] = Arrays.copyOf(arr[0], arr[0].length);
        return arr;
    }
    
    
    
    public static Double getLastSDNN(double[] arr){
        arr = filter.doFilter(arr);
        if (arr.length < SIGMA_WINDOW_SIZE_INT){
            return null;
        }
        double v = HeartRateUtils.getSDNN(arr, arr.length - SIGMA_WINDOW_SIZE_INT, SIGMA_WINDOW_SIZE_INT);
        return v;
    }
    
    public static Double getLastFilteredBPM(double[] arr){
        if (arr.length < 5){
            return null;
        }
        double[] arr2 = new double[5];
        for(int i = arr.length - 5, k = 0; i < arr.length; i++, k++){
            arr2[k] = arr[i];
        }
        
        arr2 = filter.doFilter(arr2);
        double v = Math.floor(600000.0/arr2[arr2.length - 1]) / 10.0;
        return v;
    }
    
}