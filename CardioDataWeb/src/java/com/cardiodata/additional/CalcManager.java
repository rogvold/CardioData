package com.cardiodata.additional;

import com.cardiomood.math.spectrum.FFT;

/**
 *
 * @author Shaykhlislamov Sabir (email: sha-sabir@yandex.ru)
 */
public class CalcManager {

    public static double[][] getSpectrum(double[] intervals) {
        double[] t = new double[intervals.length];
        t[0] = 0;
        for (int i = 1; i < intervals.length; i++){
            t[i]= t[i-1]+intervals[i-1];
        }
        
        FFT fft = new FFT(t, intervals, 1);
        double[] d = fft.getPower();
        
        double[][] res = new double[d.length][2];
        
        for (int i = 0; i < d.length; i++) {
            res[i][0] = fft.toFrequency(i);
            res[i][1] = d[i];
        }
        
        return res;
    }
}
