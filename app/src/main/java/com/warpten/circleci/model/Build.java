package com.warpten.circleci.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bhaskar on 15-04-17.
 */
public class Build {
    @SerializedName("build_num")
    public int buildNum;

    @SerializedName("vcs_revision")
    public String vcsRevision;

    public String status;
    public String outcome;

    @SerializedName("added_at")
    public String addedAt;

    @SerializedName("pushed_at")
    public String pushedAt;

    public String subject;

    public User user;
}
