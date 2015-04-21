package com.warpten.circleci.mvp.view;

import com.warpten.circleci.model.Project;

/**
 * Created by bhaskar on 15-04-20.
 */
public interface ProjectsView extends View {
    void addProject(Project project);
}
