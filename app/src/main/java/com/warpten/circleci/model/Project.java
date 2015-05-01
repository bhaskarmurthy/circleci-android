package com.warpten.circleci.model;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by bhaskar on 15-04-17.
 */
public class Project {
    @SerializedName("reponame")
    public String name;

    public String username;

    public Map<String, Branch> branches;
}
