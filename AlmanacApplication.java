package org.radiokit.almanac;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;

import org.radiokit.almanac.di.component.AppComponent;
import org.radiokit.almanac.di.component.DaggerAppComponent;
import org.radiokit.almanac.di.module.AppModule;
import org.radiokit.almanac.di.module.PresenterModule;

/**
 * Created by mateuszziomek on 04.10.16.
 */

public class AlmanacApplication extends Application {

    private AppComponent appComponent;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {

        // Initialize Dagger modules
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .presenterModule(new PresenterModule())
                .build();

        // Initialize Joda-Time
        JodaTimeAndroid.init(this);
    }
}
