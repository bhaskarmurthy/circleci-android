package com.warpten.circleci.mvp.presenter;

import com.warpten.circleci.mvp.view.View;

/**
 * Created by bhaskar on 15-04-20.
 */
public interface Presenter<T extends View> {
    void bindView(T view);
    void unbindView();
}
