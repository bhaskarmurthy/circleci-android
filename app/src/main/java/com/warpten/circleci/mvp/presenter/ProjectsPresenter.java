package com.warpten.circleci.mvp.presenter;

import com.warpten.circleci.model.Project;
import com.warpten.circleci.mvp.view.ProjectsView;

/**
 * Created by bhaskar on 15-04-20.
 */
public class ProjectsPresenter implements Presenter<ProjectsView> {

    private ProjectsView view;

    @Override
    public void bindView(ProjectsView view) {
        this.view = view;
    }

    @Override
    public void unbindView() {
        this.view = null;
    }

    public void addProject(Project project) {
        if (view != null) {
            view.addProject(project);
        }
    }
}
