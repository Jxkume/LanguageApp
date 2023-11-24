package com.example.testi;

import android.view.View;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.ViewAssertion;

import org.hamcrest.Matcher;

public class EspressoTestMatchers implements ViewAssertion {
    public static Matcher<View> withDrawable(final int resourceId) {
        return new DrawableMatcher(resourceId);
    }

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {

    }

    //noDrawable method is also available
}
