package com.jbl.t24.rest.api.enums.utils;

public class DoubleEqualityCheck {

    public static boolean isEqual(double a, double b){
        double epsilon = 0.0001;
        return Math.abs(a-b) < epsilon;
    }

}
