package ru.timrlm.testproject.data;

import javax.inject.Inject;
import javax.inject.Singleton;

import ru.timrlm.testproject.data.local.PreferencesHelper;
import ru.timrlm.testproject.util.RxEventBus;

@Singleton
public class DataManager {

    private final PreferencesHelper mPreferencesHelper;
    private final RxEventBus mRxEventBus;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper, RxEventBus rxEventBus) {
        mPreferencesHelper = preferencesHelper;
        mRxEventBus = rxEventBus;
    }
}