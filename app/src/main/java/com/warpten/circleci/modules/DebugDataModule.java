package com.warpten.circleci.modules;

import com.warpten.circleci.CircleCIApplication;
import com.warpten.circleci.model.Me;
import com.warpten.circleci.model.Project;
import com.warpten.circleci.service.CircleCIService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscriber;

/**
 * Created by bhaskar on 15-04-20.
 */
@Module
public class DebugDataModule {

    private final CircleCIApplication mApplication;

    public DebugDataModule(CircleCIApplication application) {
        mApplication = application;
    }

    @Provides @Singleton
    EventBus provideEventBus() {
        return EventBus.getDefault();
    }

    @Provides @Singleton
    CircleCIService provideCircleCiService() {
        return new CircleCIService() {
            @Override
            public Observable<Me> getMe() {
                return null;
            }

            @Override
            public Observable<List<Project>> getRepositories() {
                return Observable.create(new Observable.OnSubscribe<List<Project>>() {
                    @Override
                    public void call(Subscriber<? super List<Project>> subscriber) {
                        List<Project> repos = new ArrayList<Project>();
                        Project r = new Project();
                        r.name = "Hello, world";
                        repos.add(r);
                        subscriber.onNext(repos);
                        subscriber.onCompleted();
                    }
                });
            }
        };
    }
}
