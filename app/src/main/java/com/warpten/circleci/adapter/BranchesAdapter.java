package com.warpten.circleci.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.warpten.circleci.model.Branch;
import com.warpten.circleci.model.Build;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import ca.cloudstudios.circleci.R;

/**
 * Created by bhaskar on 15-04-30.
 */
public class BranchesAdapter extends BaseArrayAdapter<Branch, BranchesAdapter.BranchViewHolder> {

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
