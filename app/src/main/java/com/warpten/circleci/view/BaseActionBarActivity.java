package com.warpten.circleci.view;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ca.cloudstudios.circleci.R;

/**
 * Created by bhaskar on 15-04-19.
 */
public class BaseActionBarActivity extends ActionBarActivity {

    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        ButterKnife.inject(this);
        setSupportActionBar(toolbar);
    }
}
