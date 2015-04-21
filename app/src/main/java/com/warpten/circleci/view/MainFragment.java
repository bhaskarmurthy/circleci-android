package com.warpten.circleci.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.warpten.circleci.CircleCIApplication;
import com.warpten.circleci.adapter.BaseArrayAdapter;
import com.warpten.circleci.model.Project;
import com.warpten.circleci.model.Repository;
import com.warpten.circleci.mvp.presenter.ProjectsPresenter;
import com.warpten.circleci.mvp.view.ProjectsView;
import com.warpten.circleci.service.CircleCIService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.cloudstudios.circleci.R;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.functions.Action0;
import rx.functions.Action1;
import timber.log.Timber;

/**
 * Created by bhaskar on 15-04-20.
 */
public class MainFragment extends Fragment implements ProjectsView {

    @Inject
    CircleCIService service;

    @InjectView(R.id.list)
    SuperRecyclerView mList;

    private RepositoriesAdapter adapter = new RepositoriesAdapter();

    private ProjectsPresenter presenter = new ProjectsPresenter();

    private Subscription mSubscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CircleCIApplication) getActivity().getApplication()).getApplicationComponent().inject(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        presenter.bindView(this);
    }

    @Override
    public void onDetach() {
        presenter.unbindView();
        super.onDetach();
    }

    @Override
    public void onStart() {
        super.onStart();
        mSubscription = AppObservable.bindFragment(this, service.getProjects())
                .subscribe(new Action1<Repository>() {
                    @Override
                    public void call(Repository repository) {
                        Timber.d("New project", repository);
                        adapter.add(repository);
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
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setAdapter(adapter);
    }

    @Override
    public void addProject(Project project) {
        Timber.d("Added project", project);
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    static class RepositoriesAdapter extends BaseArrayAdapter<Repository, RepositoryViewHolder> {

        @Override
        public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_branch, parent, false);
            return new RepositoryViewHolder(view);
        }

        @Override
        public void onBindViewHolder(RepositoryViewHolder holder, int position) {
            final Repository repository = getItem(position);
            holder.text.setText(repository.name);
        }
    }

    static class RepositoryViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.title)
        TextView text;

        public RepositoryViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
