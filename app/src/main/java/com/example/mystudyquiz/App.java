package com.example.mystudyquiz;

import android.app.Application;

import com.example.mystudyquiz.data.AppDatabase;
import com.google.android.gms.ads.MobileAds;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this);
        //AppDatabase.databaseExecutor.execute(()->AppDatabase.getInstance(App.this).clearAllTables());

        MyInApp.Item[] items = new MyInApp.Item[] {
                new MyInApp.Item(MyInApp.TYPE.Subscription, "InApp_ID.PRO_ID"),
                new MyInApp.Item(MyInApp.TYPE.OneTimeInApp, "InApp_ID.NO_ADS_ID"),
                new MyInApp.Item(MyInApp.TYPE.RepurchaseInApp, "InApp_ID.STARTER_ID"),
                new MyInApp.Item(MyInApp.TYPE.RepurchaseInApp, "InApp_ID.COINS_2_ID"),
                new MyInApp.Item(MyInApp.TYPE.RepurchaseInApp, "InApp_ID.COINS_1_ID"),
        };
        MyInApp.initHelper(this, "LICENSE_KEY", items);
    }
}
