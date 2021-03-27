package com.example.recorddemo;

import android.app.Application;

public class App extends Application {
    private static App instance;
    public static App instance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
