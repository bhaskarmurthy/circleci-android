package com.warpten.circleci.view;

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
import android.widget.TextView;
import android.widget.Toast;

import com.warpten.circleci.CircleCIApplication;
import com.warpten.circleci.adapter.BaseArrayAdapter;
import com.warpten.circleci.model.Branch;
import com.warpten.circleci.model.Build;
import com.warpten.circleci.model.Repository;
import com.warpten.circleci.mvp.view.RepositoriesView;
import com.warpten.circleci.service.CircleCIService;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.cloudstudios.circleci.R;
import icepick.Icicle;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.functions.Action0;
import rx.functions.Action1;
import timber.log.Timber;

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

    private BranchesAdapter adapter = new BranchesAdapter();

    private Subscription mSubscription;

    @Icicle
    boolean mFetchMyBranches;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CircleCIApplication) getActivity().getApplication()).getApplicationComponent().inject(this);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_repositories, menu);
        mBranchToggle = (SwitchCompat) menu.findItem(R.id.action_toggle_branch).getActionView();
        mBranchToggle.setChecked(mFetchMyBranches);
        mBranchToggle.setOnCheckedChangeListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mSubscription = AppObservable.bindFragment(this, service.getRepositories())
                .subscribe(new Action1<List<Repository>>() {
                    @Override
                    public void call(List<Repository> repositories) {
                        adapter.clear();
                        for (Repository repository : repositories) {
                            for (String branchName : repository.branches.keySet()) {
                                Branch branch = repository.branches.get(branchName);
                                branch.name = branchName;
                                adapter.add(branch);
                            }
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e(throwable, "Error getting projects");
                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        Timber.d("Completed projects");
                    }
                });
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mSubscription !=null) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mList.setAdapter(adapter);
        mRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {

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
            Toast.makeText(getContext(), String.valueOf(isChecked), Toast.LENGTH_SHORT).show();
        }
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

        public BranchViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
