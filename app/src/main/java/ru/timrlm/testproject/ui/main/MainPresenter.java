package ru.timrlm.testproject.ui.main;

import javax.inject.Inject;

import ru.timrlm.testproject.data.DataManager;
import ru.timrlm.testproject.di.ConfigPersistent;
import ru.timrlm.testproject.ui.base.BasePresenter;

@ConfigPersistent
class MainPresenter extends BasePresenter<MainMvpView> {
    private DataManager mDataManager;

    @Inject
    public MainPresenter(DataManager mDataManager) { this.mDataManager = mDataManager; }
}
