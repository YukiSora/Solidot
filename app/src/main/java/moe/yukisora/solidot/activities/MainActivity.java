package moe.yukisora.solidot.activities;

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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import moe.yukisora.solidot.R;
import moe.yukisora.solidot.SolidotApplication;
import moe.yukisora.solidot.fragments.AboutFragment;
import moe.yukisora.solidot.fragments.ArticleFragment;
import moe.yukisora.solidot.fragments.SettingFragment;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // config http
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cache(new Cache(getCacheDir(), 5 * 1024 * 1024))
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();

                        Response response = chain.proceed(request);

                        CacheControl cacheControl = new CacheControl.Builder()
                                .maxAge(10, TimeUnit.MINUTES)
                                .build();

                        return response.newBuilder()
                                .header("Cache-Control", cacheControl.toString())
                                .header("User-Agent", " Mozilla/5.0 (X11; Linux x86_64; rv:55.0) Gecko/20100101 Firefox/55.0")
                                .build();
                    }
                })
                .build();
        SolidotApplication.setOkHttpClient(okHttpClient);

        // fragment manager
        final FragmentManager fragmentManager = getFragmentManager();

        // toolbar
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // floating action button
        final FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton);

        // drawer layout
        drawer = findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        // navigation view
        ((NavigationView)findViewById(R.id.navigationView)).setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.navigationIndex) {
                    fragmentManager.beginTransaction().replace(R.id.fragment, ArticleFragment.newInstance()).commit();
                    toolbar.setTitle(getString(R.string.app_name));
                    floatingActionButton.show();
                } else if (id == R.id.navigationSetting) {
                    fragmentManager.beginTransaction().replace(R.id.fragment, SettingFragment.newInstance()).commit();
                    toolbar.setTitle(getString(R.string.setting));
                    floatingActionButton.hide();
                } else if (id == R.id.navigationAbout) {
                    fragmentManager.beginTransaction().replace(R.id.fragment, AboutFragment.newInstance()).commit();
                    toolbar.setTitle(getString(R.string.about));
                    floatingActionButton.hide();
                }

                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        // default fragment
        fragmentManager.beginTransaction().replace(R.id.fragment, ArticleFragment.newInstance()).commit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        toggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
