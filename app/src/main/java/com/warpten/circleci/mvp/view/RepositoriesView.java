package com.warpten.circleci.mvp.view;

import com.warpten.circleci.model.Branch;

import java.util.List;

/**
 * Created by bhaskar on 15-04-20.
 */
public interface RepositoriesView extends View {
    void loadData(List<Branch> branches);
    void showLoading(boolean isLoading);
}
