package ru.timrlm.testproject.di.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import ru.timrlm.testproject.data.DataManager;
import ru.timrlm.testproject.data.local.PreferencesHelper;
import ru.timrlm.testproject.di.ApplicationContext;
import ru.timrlm.testproject.di.module.ApplicationModule;
import ru.timrlm.testproject.util.RxEventBus;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();
    Application application();
    PreferencesHelper preferencesHelper();
    DataManager dataManager();
    RxEventBus eventBus();

}
