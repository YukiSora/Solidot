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

public class ArticleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ArticleData article = (ArticleData)getIntent().getSerializableExtra("articleData");

        Toolbar toolbar = findViewById(R.id.articleActivityToolbar);
        TextView title = findViewById(R.id.articleActivityTitle);
        TextView date = findViewById(R.id.articleActivityDate);
        TextView reference = findViewById(R.id.articleActivityReference);
        TextView content = findViewById(R.id.articleActivityContent);

        // toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // content
        title.setText(article.title);
        date.setText(article.datetime);
        if (!article.reference.isEmpty()) {
            reference.setText("来自" + article.reference);
        } else {
            reference.setText("");
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            content.setText(Html.fromHtml(article.content, Html.FROM_HTML_MODE_LEGACY));
        } else {
            content.setText(Html.fromHtml(article.content));
        }
        content.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
