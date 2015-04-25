package com.warpten.circleci.service;

import com.warpten.circleci.model.Branch;
import com.warpten.circleci.model.Build;
import com.warpten.circleci.model.Me;
import com.warpten.circleci.model.Repository;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by bhaskar on 15-04-18.
 */
public class CircleCIServiceTest {

    private CircleCIService mService;

    public CircleCIServiceTest() {
        mService = new RestAdapter.Builder()
                .setEndpoint("https://circleci.com/api/v1")
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addQueryParam("circle-token", "8e3217869e94ac13d86bc38dfdb0b84858cb2f43");
                    }
                })
                .build()
                .create(CircleCIService.class);
    }

    @Test
    public void testGetMe() throws Exception {
        Me me = mService.getMe()
                .toBlocking()
                .single();

        assertNotNull(me);
        assertNotNull(me.name);
        assertNotNull(me.email);
    }

    @Test
    public void testGetProjects() throws Exception {
        List<Repository> repositories = mService.getRepositories()
                .toBlocking()
                .single();

        assertNotNull(repositories);

        for (Repository repository : repositories) {
            assertNotNull(repository.name);

            Map<String, Branch> branches = repository.branches;
            assertNotNull(branches);
            assertTrue(branches.size() > 0);

            for (String name : branches.keySet()) {
                Branch branch = branches.get(name);
                assertNotNull(branch.recentBuilds);
                assertTrue(branch.recentBuilds.size() > 0);

                for (Build build : branch.recentBuilds) {
                    assertNotNull(build.buildNum);
                    assertNotNull(build.status);
                    assertNotNull(build.outcome);
                    assertNotNull(build.vcsRevision);
                    assertNotNull(build.pushedAt);
                    assertNotNull(build.addedAt);
                }
            }
        }
    }
}