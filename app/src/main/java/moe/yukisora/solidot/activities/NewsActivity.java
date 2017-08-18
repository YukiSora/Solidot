package moe.yukisora.solidot.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.MenuItem;
import android.widget.TextView;

import moe.yukisora.solidot.R;
import moe.yukisora.solidot.modles.ArticleData;

public class NewsActivity extends AppCompatActivity {
    private ArticleData articleData;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        articleData = (ArticleData)getIntent().getSerializableExtra("articleData");

        Toolbar toolbar = findViewById(R.id.newsActivityToolbar);
        TextView title = findViewById(R.id.newsActivityTitle);
        TextView date = findViewById(R.id.newsActivityDate);
        TextView reference = findViewById(R.id.newsActivityReference);
        TextView article = findViewById(R.id.newsActivityArticle);

        // toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // content
        title.setText(articleData.title);
        date.setText(articleData.datetime);
        reference.setText(articleData.reference);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            article.setText(Html.fromHtml(articleData.article, Html.FROM_HTML_MODE_LEGACY));
        } else {
            article.setText(Html.fromHtml(articleData.article));
        }
        article.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
