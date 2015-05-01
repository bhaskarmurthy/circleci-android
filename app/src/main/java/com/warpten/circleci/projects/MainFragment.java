package com.warpten.circleci.projects;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;

import com.warpten.circleci.CircleCIApplication;
import com.warpten.circleci.adapter.BranchesAdapter;
import com.warpten.circleci.model.Branch;
import com.warpten.circleci.model.Projects;
import com.warpten.circleci.mvp.presenter.ProjectsPresenter;
import com.warpten.circleci.mvp.view.RepositoriesView;
import com.warpten.circleci.view.BaseFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.cloudstudios.circleci.R;
import icepick.Icicle;

/**
 * Created by bhaskar on 15-04-20.
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RepositoriesView, CompoundButton.OnCheckedChangeListener {

    @InjectView(R.id.swiperefresh)
    SwipeRefreshLayout mRefresh;

    @InjectView(R.id.list)
    RecyclerView mList;

    private SwitchCompat mBranchToggle;

    @Inject
    Projects model;

    private ProjectsPresenter presenter;

    private BranchesAdapter adapter = new BranchesAdapter();

    private Toolbar toolbar;

    @Icicle
    boolean mFetchMyBranches;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        ((CircleCIApplication) getActivity().getApplication()).getApplicationComponent().inject(this);

        presenter = new ProjectsPresenter(model);
        presenter.setShowMine(mFetchMyBranches);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_repositories, menu);
        mBranchToggle = (SwitchCompat) menu.findItem(R.id.action_toggle_branch).getActionView();
        mBranchToggle.setChecked(mFetchMyBranches);
        mBranchToggle.setOnCheckedChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            toolbar = ((MainActivity) activity).getToolbar();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.setAdapter(adapter);
        mRefresh.setOnRefreshListener(this);

        presenter.bindView(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unbindView();
    }

    @Override
    public void onRefresh() {
        presenter.fetchData();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void loadData(List<Branch> branches) {
        adapter.clear();
        adapter.addAll(branches);
    }

    @Override
    public void showLoading(boolean isLoading) {
        mRefresh.setRefreshing(isLoading);
    }

    @Override
    public void onCheckedChanged(CompoundButton view, boolean isChecked) {
        if (R.id.action_toggle_branch == view.getId()) {
            mFetchMyBranches = isChecked;
            presenter.setShowMine(isChecked);
            presenter.fetchData();

            setDarkActionbar(isChecked);
        }
    }

    private void setDarkActionbar(boolean isDark) {
        if (isDark) {
            toolbar.setBackgroundResource(R.color.primary_dark);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getActivity().getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
            }
        } else {
            toolbar.setBackgroundResource(R.color.primary);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getActivity().getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(getResources().getColor(R.color.primary));
            }
        }
    }

}
