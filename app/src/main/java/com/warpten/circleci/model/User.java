package com.warpten.circleci.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bhaskar on 15-04-18.
 */
public class User {
    @SerializedName("is_user")
    public boolean isUser;

    public String login;

    public String email;
}
