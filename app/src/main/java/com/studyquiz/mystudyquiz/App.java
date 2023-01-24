package com.studyquiz.mystudyquiz;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;
import com.studyquiz.InApp_ID;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this);
        //AppDatabase.databaseExecutor.execute(()->AppDatabase.getInstance(App.this).clearAllTables());
        String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmF3REC6nAtoFcMYlGVT3tOTaZ+i0Yiius7NUHgFaGQUnXsEK6w4ym38hSNe32qorEjkZwTTzM97MzyWu90WZ4upDghNqCMZXgVGrVTZ73AqpkJON9FqCkzQGObs2SnZrO8FLo2Mlh8Hd9Q9DEmep/5p21ob2w48xUkzQm839XR5pBvboVbfXVwdB1K7e011y9vMZUeUxBTSsgLAlesPo+loCHqF5Ujz1Tad+/8dKjatRyPEzad7GoFOXzldJf8K0OyRPv4Jk+FWS06aVF5e1bRShk25VEeLQIsS8UhtOtxQIIsvL+fxHfMcUYUUJv4nLSSuuVNrk4YGyit27nognrwIDAQAB";
        MyInApp.Item[] items = new MyInApp.Item[] {
                new MyInApp.Item(MyInApp.TYPE.Subscription, InApp_ID.PRO_SUBSCRIPTION),
                new MyInApp.Item(MyInApp.TYPE.RepurchaseInApp, InApp_ID.MORE_QUIZZES),
                new MyInApp.Item(MyInApp.TYPE.RepurchaseInApp, InApp_ID.MORE_QUESTIONS),
        };
        MyInApp.initHelper(this, LICENSE_KEY, items);
    }
}
