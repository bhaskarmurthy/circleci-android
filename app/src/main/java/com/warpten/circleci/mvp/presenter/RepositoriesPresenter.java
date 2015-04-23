package com.warpten.circleci.mvp.presenter;

import com.warpten.circleci.model.Branch;
import com.warpten.circleci.model.Repository;
import com.warpten.circleci.mvp.view.RepositoriesView;
import com.warpten.circleci.service.CircleCIService;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by bhaskar on 15-04-20.
 */
public class RepositoriesPresenter implements Presenter<RepositoriesView> {

    final CircleCIService service;

    private RepositoriesView view;

    private boolean showMine;

    private Subscription subscription;

    public RepositoriesPresenter(CircleCIService service) {
        super();
        this.service = service;
    }

    @Override
    public void bindView(RepositoriesView view) {
        this.view = view;
        fetchData();
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
        subscription = service.getRepositories()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Repository>>() {
                    @Override
                    public void call(List<Repository> repositories) {
                        loadData(repositories);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e(throwable, "Error getting projects");
                        hideLoading();
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Timber.d("Completed projects");
                        hideLoading();
                    }
                });
    }

    private void loadData(List<Repository> repositories) {
        List<Branch> branches = new ArrayList<>();
        for (Repository repository : repositories) {
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
