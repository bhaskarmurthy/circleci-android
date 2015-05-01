package com.warpten.circleci.mvp.presenter;

import com.warpten.circleci.model.Branch;
import com.warpten.circleci.model.Project;
import com.warpten.circleci.model.Projects;
import com.warpten.circleci.mvp.view.RepositoriesView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by bhaskar on 15-04-20.
 */
public class ProjectsPresenter implements Presenter<RepositoriesView> {

    private RepositoriesView view;

    private boolean showMine;

    private Subscription subscription;

    private Projects projects;

    public ProjectsPresenter(Projects model) {
        super();
        this.projects = model;
    }

    @Override
    public void bindView(RepositoriesView view) {
        this.view = view;
        this.subscription = projects.getObservable()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Project>>() {
                    @Override
                    public void call(List<Project> repositories) {
                        loadData(repositories);
                        hideLoading();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e(throwable, "Error getting projects");
                        hideLoading();
                    }
                });

        this.projects.refresh();
    }

    @Override
    public void unbindView() {
        this.view = null;
        if (this.subscription != null) {
            this.subscription.unsubscribe();
        }
    }

    public void fetchData() {
        showLoading();
        projects.refresh();
    }

    private void loadData(List<Project> repositories) {
        List<Branch> branches = new ArrayList<>();
        for (Project repository : repositories) {
            if (repository.branches != null) {
                for (String name : repository.branches.keySet()) {
                    Branch branch = repository.branches.get(name);
                    branch.name = name;

                    if (!showMine || showMine && branch.users.contains("bhaskarmurthy")) {
                        branches.add(branch);
                    }
                }
            }
        }

        view.loadData(branches);
    }

    public void setShowMine(boolean showMine) {
        this.showMine = showMine;
    }

    public void showLoading() {
        view.showLoading(true);
    }

    public void hideLoading() {
        view.showLoading(false);
    }
}
