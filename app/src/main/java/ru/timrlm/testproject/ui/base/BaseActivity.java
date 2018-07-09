package ru.timrlm.testproject.ui.base;

import android.os.Bundle;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AppCompatActivity;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.concurrent.atomic.AtomicLong;


import butterknife.ButterKnife;
import ru.timrlm.testproject.R;
import ru.timrlm.testproject.app.App;
import ru.timrlm.testproject.di.component.ActivityComponent;
import ru.timrlm.testproject.di.component.ConfigPersistentComponent;
import ru.timrlm.testproject.di.component.DaggerConfigPersistentComponent;
import ru.timrlm.testproject.di.module.ActivityModule;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final LongSparseArray<ConfigPersistentComponent> sComponentsMap = new LongSparseArray<>();
    private ActivityComponent mActivityComponent;
    private long mActivityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setlayout());
        ButterKnife.bind(this);

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState != null ? savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent = sComponentsMap.get(mActivityId, null);
        if (configPersistentComponent == null) {
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(App.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        }
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));
    }

    public ActivityComponent activityComponent() { return mActivityComponent; }
    public abstract int setlayout();

    protected void showError(String mes){
        new MaterialDialog.Builder(this)
                .title(R.string.error)
                .content(mes)
                .positiveText("OK")
                .show();
    }

    private MaterialDialog progress;
    protected void showProgress(){
        progress = new MaterialDialog.Builder(this)
                .progress(true,0)
                .cancelable(false)
                .show();
    }

    protected void hideProgress(){progress.dismiss();}

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) sComponentsMap.remove(mActivityId);
        super.onDestroy();
    }
}
