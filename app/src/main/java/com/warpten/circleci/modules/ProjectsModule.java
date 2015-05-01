package com.warpten.circleci.modules;

import com.warpten.circleci.model.Projects;
import com.warpten.circleci.mvp.presenter.ProjectsPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by bhaskar on 15-04-30.
 */
@Module
public class ProjectsModule {

    private Projects model;

    public ProjectsModule(Projects model) {
        this.model = model;
    }

    @Provides
    public ProjectsPresenter providePresenter() {
        return new ProjectsPresenter(model);
    }
}
