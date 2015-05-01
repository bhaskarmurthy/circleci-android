package com.warpten.circleci.model;

import com.warpten.circleci.service.CircleCIService;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;

/**
 * Created by bhaskar on 15-04-30.
 */
public class Projects {
    private CircleCIService service;

    private BehaviorSubject<List<Project>> subject = BehaviorSubject.create((List<Project>) new ArrayList<Project>());

    public Projects(CircleCIService service) {
        this.service = service;
    }

    public Observable<List<Project>> getObservable() {
        return subject.asObservable();
    }

    public void refresh() {
        service.getRepositories()
                .subscribe(new Action1<List<Project>>() {
                    @Override
                    public void call(List<Project> projects) {
                        subject.onNext(projects);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        subject.onError(throwable);
                    }
                });
    }
}
