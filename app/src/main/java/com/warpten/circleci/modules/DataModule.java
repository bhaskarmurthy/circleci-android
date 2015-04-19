package com.warpten.circleci.modules;

import javax.inject.Singleton;

import com.warpten.circleci.CircleCIApplication;
import com.warpten.circleci.service.CircleCIService;
import ca.cloudstudios.circleci.R;
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
    private final CircleCIApplication mApplication;

    public DataModule(CircleCIApplication application) {
        mApplication = application;
    }

    @Provides @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides @Singleton
    CircleCIService provideCircleCiService() {
        return new RestAdapter.Builder()
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String message) {
                        Timber.d(message);
                    }
                })
                .setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Accept", "application/json");
                        request.addQueryParam("circle-token", mApplication.getString(R.string.api_token));
                    }
                })
                .setEndpoint(mApplication.getString(R.string.api_url))
                .build()
                .create(CircleCIService.class);
    }
}
