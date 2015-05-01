package com.warpten.circleci.modules;

import com.warpten.circleci.model.Projects;
import com.warpten.circleci.service.CircleCIService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import timber.log.Timber;

/**
 * Created by bhaskar on 15-04-12.
 */
@Module
public class DataModule {
    private final CircleCIService service;

    public DataModule(final String apiUrl, final String apiToken) {
        this.service = new RestAdapter.Builder()
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String message) {
                        Timber.d(message);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
                .setEndpoint(apiUrl)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept", "application/json");
                        request.addQueryParam("circle-token", apiToken);
                    }
                })
                .build()
                .create(CircleCIService.class);
    }

    @Provides @Singleton
    public EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides
    public Projects provideProjectsModel() {
        return new Projects(service);
    }
}
