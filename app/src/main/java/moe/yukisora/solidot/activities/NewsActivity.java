package moe.yukisora.solidot.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import moe.yukisora.solidot.modles.NewsData;
import moe.yukisora.solidot.R;

public class NewsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        NewsData newsData = (NewsData)getIntent().getSerializableExtra("newsData");

        //Tool Bar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbarNewsActivity);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //content
        ((TextView)findViewById(R.id.titleNewsActivity)).setText(newsData.title);
        ((TextView)findViewById(R.id.dateNewsActivity)).setText(newsData.date);
        ((TextView)findViewById(R.id.referenceNewsActivity)).setText(newsData.reference);
        if (Build.VERSION.SDK_INT >= 24)
            ((TextView)findViewById(R.id.articleNewsActivity)).setText(Html.fromHtml(newsData.article, Html.FROM_HTML_MODE_COMPACT));
        else
            ((TextView)findViewById(R.id.articleNewsActivity)).setText(Html.fromHtml(newsData.article));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
