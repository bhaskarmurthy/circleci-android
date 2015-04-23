package com.warpten.circleci.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import icepick.Icepick;

/**
 * Created by bhaskar on 15-04-23.
 */
public class BaseFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }
}
