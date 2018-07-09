package ru.timrlm.testproject.di.component;

import dagger.Subcomponent;
import ru.timrlm.testproject.ui.main.MainActivity;
import ru.timrlm.testproject.di.PerActivity;
import ru.timrlm.testproject.di.module.ActivityModule;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);
}
