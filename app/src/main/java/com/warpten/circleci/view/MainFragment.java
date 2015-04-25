package com.warpten.circleci.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.warpten.circleci.CircleCIApplication;
import com.warpten.circleci.adapter.BaseArrayAdapter;
import com.warpten.circleci.model.Branch;
import com.warpten.circleci.model.Build;
import com.warpten.circleci.mvp.presenter.RepositoriesPresenter;
import com.warpten.circleci.mvp.view.RepositoriesView;
import com.warpten.circleci.service.CircleCIService;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import ca.cloudstudios.circleci.R;
import icepick.Icicle;

/**
 * Created by bhaskar on 15-04-20.
 */
public class MainFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, RepositoriesView, CompoundButton.OnCheckedChangeListener {

    private static final String BRANCH_TOGGLE_STATE = "branch_toggle_state";

    @Inject
    CircleCIService service;

    @InjectView(R.id.swiperefresh)
    SwipeRefreshLayout mRefresh;

    @InjectView(R.id.list)
    RecyclerView mList;

    private SwitchCompat mBranchToggle;

    private RepositoriesPresenter presenter;

    private BranchesAdapter adapter = new BranchesAdapter();

    private MainActivityCallback callback;

    @Icicle
    boolean mFetchMyBranches;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CircleCIApplication) getActivity().getApplication()).getApplicationComponent().inject(this);
        setHasOptionsMenu(true);

        presenter = new RepositoriesPresenter(service);
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
        if (activity instanceof MainActivityCallback) {
            callback = (MainActivityCallback) activity;
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

            if (callback != null) {
                callback.setDarkActionbar(isChecked);
            }
        }
    }

    public interface MainActivityCallback {
        void setDarkActionbar(boolean isDark);
    }

    static class BranchesAdapter extends BaseArrayAdapter<Branch, BranchViewHolder> {

        @Override
        public BranchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_branch, parent, false);
            return new BranchViewHolder(view);
        }

        @Override
        public void onBindViewHolder(BranchViewHolder holder, int position) {
            final Branch branch = getItem(position);
            holder.title.setText(branch.name);

            if (branch.recentBuilds != null && branch.recentBuilds.size() > 0) {
                Build build = branch.recentBuilds.get(0);
                holder.subtitle.setText("#" + build.buildNum + " @ " + build.addedAt.toString());
            }
        }
    }

    static class BranchViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.title)
        TextView title;

        @InjectView(R.id.subtitle)
        TextView subtitle;

        @InjectView(R.id.status)
        ImageView status;


        @InjectView(R.id.status_1)
        ImageView status1;

        @InjectView(R.id.status_2)
        ImageView status2;

        @InjectView(R.id.status_3)
        ImageView status3;

        @InjectView(R.id.status_4)
        ImageView status4;

        @InjectView(R.id.status_5)
        ImageView status5;

        @InjectViews({R.id.status_1, R.id.status_2, R.id.status_3, R.id.status_4, R.id.status_5})
        List<ImageView> statuses;

        public BranchViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
