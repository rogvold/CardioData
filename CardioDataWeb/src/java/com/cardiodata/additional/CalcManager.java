package com.cardiodata.additional;

import com.cardiodata.core.jpa.CardioDataItem;
import com.cardiodata.json.entity.JsonRRInterval;
import com.cardiomood.math.HeartRateUtils;
import com.cardiomood.math.filter.ArtifactFilter;
import com.cardiomood.math.filter.SimpleInterpolationArtifactFilter;
import com.cardiomood.math.spectrum.FFT;
import com.cardiomood.math.spectrum.SpectralAnalysis;
import com.cardiomood.math.window.DataWindow;
import com.google.gson.Gson;
import java.util.ArrayList;
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
    private static final ArtifactFilter filter = new SimpleInterpolationArtifactFilter();
    private static final int STRESS_WINDOW_SIZE_INT = 120 * 1000;
    private static final int STRESS_STEP_SIZE_INT = 5 * 1000;
    private static final int SIGMA_WINDOW_SIZE_INT = 20;
    private static final int SIGMA_STEP_SIZE_INT = 5;

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

    public static double[][] getTensionArray(double[] intervals, double[] time) {
        intervals = filter.doFilter(intervals);
        DataWindow window = new DataWindow.Timed(STRESS_WINDOW_SIZE, STRESS_STEP_SIZE);
        double[][] res = HeartRateUtils.getSI(intervals, time, STRESS_WINDOW_SIZE_INT, STRESS_STEP_SIZE_INT);
        return res;
    }

    public static double[][] getSDNN(double[] intervals, double[] time) {
        intervals = filter.doFilter(intervals);
        DataWindow window = new DataWindow.IntervalsCount(SIGMA_WINDOW_SIZE, SIGMA_STEP_SIZE);
        double[][] res = HeartRateUtils.getSDNN(intervals, time, SIGMA_WINDOW_SIZE_INT, SIGMA_STEP_SIZE_INT);
        return res;
    }
    
    public static Double getLastSDNN(List<CardioDataItem> items){
        if (items == null){
            return null;
        }
        double[] arr = new double[items.size()];
        arr = filter.doFilter(arr);
        Gson gson = new Gson();
        for (int i = 0; i < items.size(); i++){
            String data = items.get(i).getDataItem();
            Integer r = gson.fromJson(data, JsonRRInterval.class).getR();
            arr[i] = 1.0 * r;
        }
        double v = HeartRateUtils.getSDNN(arr, arr.length - SIGMA_WINDOW_SIZE_INT, arr.length);
        return v;
    }
    
}
