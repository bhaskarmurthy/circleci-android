package com.warpten.circleci.mvp.view;

/**
 * Created by bhaskar on 15-04-20.
 */
public interface LifecycleCallbacks {
    void onStarted();
    void onStopped();
    void onResumed();
    void onPaused();
}
