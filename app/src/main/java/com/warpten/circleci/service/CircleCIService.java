package com.warpten.circleci.service;

import com.warpten.circleci.model.Repository;
import com.warpten.circleci.model.Me;

import java.util.List;

import retrofit.http.GET;
import rx.Observable;

/**
 * Created by bhaskar on 15-04-12.
 */
public interface CircleCIService {
    @GET("/me")
    Observable<Me> getMe();

    @GET("/projects")
    Observable<List<Repository>> getProjects();
}
