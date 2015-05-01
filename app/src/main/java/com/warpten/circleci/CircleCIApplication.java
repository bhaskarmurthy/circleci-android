package com.warpten.circleci;

import android.app.Application;

import com.warpten.circleci.model.Projects;
import com.warpten.circleci.modules.DataModule;
import com.warpten.circleci.modules.ProjectsModule;
import com.warpten.circleci.projects.MainFragment;

import javax.inject.Inject;
import javax.inject.Singleton;

import ca.cloudstudios.circleci.BuildConfig;
import ca.cloudstudios.circleci.R;
import dagger.Component;
import de.greenrobot.event.EventBus;
import timber.log.Timber;

/**
 * Created by bhaskar on 15-04-12.
 */
public class CircleCIApplication extends Application {

    /*
    @Singleton
    @Component(modules = DebugDataModule.class)
    public interface com.warpten.circleci.modules.ApplicationComponent {
        void inject (CircleCIApplication application);
        void inject (MainFragment activity);
    }
    */

    @Singleton
    @Component(modules = { DataModule.class, ProjectsModule.class })
    public interface ApplicationComponent {
        void inject (CircleCIApplication application);
        void inject (MainFragment fragment);
    }

    @Inject EventBus mEventBus;

    @Inject Projects projectsModel;

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
                .dataModule(new DataModule(getString(R.string.api_url), getString(R.string.api_token)))
                .projectsModule(new ProjectsModule(projectsModel))
                .build();
        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }
}
