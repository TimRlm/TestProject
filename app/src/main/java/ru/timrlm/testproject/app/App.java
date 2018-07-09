package ru.timrlm.testproject.app;

import android.app.Application;
import android.content.Context;

import ru.timrlm.testproject.di.component.ApplicationComponent;
import ru.timrlm.testproject.di.component.DaggerApplicationComponent;
import ru.timrlm.testproject.di.module.ApplicationModule;


public class App extends Application {

    ApplicationComponent mApplicationComponent;

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }
}
