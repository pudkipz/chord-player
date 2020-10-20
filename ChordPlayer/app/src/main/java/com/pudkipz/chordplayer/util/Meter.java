package com.pudkipz.chordplayer.util;

public class Meter {
    int numerator;
    int denominator;

    public Meter(int n, int d) {
        this.numerator = n;
        this.denominator = d;
    }

    @Override
    public String toString() {
        return (numerator + "/" + denominator);
    }

    public float getValue() {
        return (float) numerator/(float) denominator;
    }

    public int getNumerator() {
        return numerator;
    }

    public void setNumerator(int numerator) {
        this.numerator = numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    public void setDenominator(int denominator) {
        this.denominator = denominator;
    }

    public void setMeter(int n, int d) {
        this.numerator = n;
        this.denominator = d;
    }

    public String getName() {
        return (numerator + "/" + denominator);
    }
}
