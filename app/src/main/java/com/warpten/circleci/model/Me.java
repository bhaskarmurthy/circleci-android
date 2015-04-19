package com.warpten.circleci.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bhaskar on 15-04-12.
 */
public class Me {
    public int githubId;

    public String name;

    @SerializedName("selected_email")
    public String email;

    public String avatarUrl;
}
