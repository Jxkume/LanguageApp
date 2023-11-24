package com.example.testi;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;


public class SessionsActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<SessionsActivity> activityRule = new ActivityScenarioRule<>(SessionsActivity.class);

}