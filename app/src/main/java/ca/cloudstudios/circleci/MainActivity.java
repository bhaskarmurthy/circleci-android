package ca.cloudstudios.circleci;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.util.KeyboardUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity implements Drawer.OnDrawerItemClickListener, Drawer.OnDrawerListener {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    private Drawer.Result mDrawer;

    private AccountHeader.Result mDrawerHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        setSupportActionBar(mToolbar);

        mDrawerHeader = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(getResources().getDrawable(R.drawable.cat_background, null))
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("circleci-android")
                                .withEmail("bhaskarmurthy")
                                .withIcon(getResources().getDrawable(R.drawable.cat, null)),
                        new ProfileDrawerItem()
                                .withName("bhaskarmurthy.github.io")
                                .withEmail("bhaskarmurthy")
                                .withIcon(getResources().getDrawable(R.drawable.cat, null))
                )
                .withProfileImagesClickable(false)
                .withSavedInstance(savedInstanceState)
                .build();

        mDrawer = new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(mDrawerHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem()
                            .withName("My branches"),
                        new PrimaryDrawerItem()
                            .withName("All branches"),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem()
                                .withName("develop")
                                .withDescription("Build #1157: Success")
                                .withBadge("1"),
                        new PrimaryDrawerItem()
                                .withName("master")
                                .withDescription("Build #1162: Timed out"),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem()
                                .withName("Settings"),
                        new SecondaryDrawerItem()
                                .withName("Help & feedback")
        )
        .withFireOnInitialOnClick(true)
                .withSavedInstance(savedInstanceState)
                .withOnDrawerListener(this)
                .withOnDrawerItemClickListener(this)
                .build();

        mDrawer.keyboardSupportEnabled(this, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = mDrawerHeader.saveInstanceState(outState);
        outState = mDrawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer != null && mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {

    }

    @Override
    public void onDrawerOpened(View view) {
        KeyboardUtil.hideKeyboard(this);
    }

    @Override
    public void onDrawerClosed(View view) {

    }
}
