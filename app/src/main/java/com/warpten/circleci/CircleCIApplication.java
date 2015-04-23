package com.warpten.circleci;

import android.app.Application;

import com.warpten.circleci.modules.DataModule;
import com.warpten.circleci.modules.DebugDataModule;
import com.warpten.circleci.view.MainActivity;
import com.warpten.circleci.view.MainFragment;

import javax.inject.Inject;
import javax.inject.Singleton;

import ca.cloudstudios.circleci.BuildConfig;
import dagger.Component;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by bhaskar on 15-04-12.
 */
public class CircleCIApplication extends Application {

    @Singleton
    @Component(modules = DataModule.class)
    public interface ApplicationComponent {
        void inject (CircleCIApplication application);
        void inject (MainFragment fragment);
    }

    /*
    @Singleton
    @Component(modules = DebugDataModule.class)
    public interface ApplicationComponent {
        void inject (CircleCIApplication application);
        void inject (MainFragment activity);
    }
    */

    @Inject EventBus mEventBus;

    private ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setupLogging();
        setupModules();
    }

    private void setupLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new Timber.DebugTree()); // TODO: fix this for release
        }
    }

    private void setupModules() {
        mApplicationComponent = DaggerCircleCIApplication_ApplicationComponent.builder()
                .dataModule(new DataModule(this))
                .build();
        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
