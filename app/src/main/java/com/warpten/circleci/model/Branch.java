package com.warpten.circleci.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bhaskar on 15-04-17.
 */
public class Branch {
    public String name;

    @SerializedName("recent_builds")
    public List<Build> recentBuilds;

    @SerializedName("pusher_logins")
    public List<String> users;
}
