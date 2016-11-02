package moe.yukisora.solidot;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;
    private FloatingActionButton floatingActionButton;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Fragment Manager
        fragmentManager = getFragmentManager();

        //Tool Bar
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Floating Action Button
        floatingActionButton = (FloatingActionButton)findViewById(R.id.floatingActionButton);

        //Drawer Layout
        drawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        //Navigation View
        ((NavigationView)findViewById(R.id.navigationView)).setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navigationIndex) {
                    fragmentManager.beginTransaction().replace(R.id.fragment, ArticleFragment.newInstance()).commit();
                    toolbar.setTitle(getString(R.string.app_name));
                    floatingActionButton.show();
                }
                else if (id == R.id.navigationPopular) {
                    fragmentManager.beginTransaction().replace(R.id.fragment, ArticleFragment.newInstance()).commit();
                    toolbar.setTitle(getString(R.string.popular));
                    floatingActionButton.show();
                }
                else if (id == R.id.navigationSetting) {
                    fragmentManager.beginTransaction().replace(R.id.fragment, SettingFragment.newInstance()).commit();
                    toolbar.setTitle(getString(R.string.setting));
                    floatingActionButton.hide();
                }
                else if (id == R.id.navigationAbout) {
                    fragmentManager.beginTransaction().replace(R.id.fragment, AboutFragment.newInstance()).commit();
                    toolbar.setTitle(getString(R.string.about));
                    floatingActionButton.hide();
                }

                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        //Default Fragment
        fragmentManager.beginTransaction().replace(R.id.fragment, ArticleFragment.newInstance()).commit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }
}
