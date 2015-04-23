package com.warpten.circleci.mvp.presenter;

import com.warpten.circleci.model.Branch;
import com.warpten.circleci.model.Repository;
import com.warpten.circleci.mvp.view.RepositoriesView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bhaskar on 15-04-20.
 */
public class RepositoriesPresenter implements Presenter<RepositoriesView> {

    private RepositoriesView view;
    private boolean showMine;

    @Override
    public void bindView(RepositoriesView view) {
        this.view = view;
    }

    @Override
    public void unbindView() {
        this.view = null;
    }

    public void loadData(List<Repository> repositories) {
        List<Branch> branches = new ArrayList<>();
        for (Repository repository : repositories) {
            if (repository.branches != null) {
                for (String name : repository.branches.keySet()) {
                    Branch branch = repository.branches.get(name);
                    branch.name = name;
                    branches.add(branch);
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
