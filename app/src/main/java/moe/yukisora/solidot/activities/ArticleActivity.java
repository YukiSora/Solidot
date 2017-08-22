package moe.yukisora.solidot.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TextView;

import moe.yukisora.solidot.R;
import moe.yukisora.solidot.modles.ArticleData;

public class ArticleActivity extends AppCompatActivity {
    private GestureDetectorCompat detector;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ArticleData article = getIntent().getParcelableExtra("articleData");

        detector = new GestureDetectorCompat(this, new GestureListener());

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

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        super.dispatchTouchEvent(e);

        return detector.onTouchEvent(e);
    }

    private void onSwipeLeft() {
    }

    private void onSwipeRight() {
        finish();
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();
            if (Math.abs(diffX) > Math.abs(diffY) && Math.abs(diffX) > 100 && Math.abs(velocityX) > 100) {
                if (diffX > 0) {
                    onSwipeRight();
                } else {
                    onSwipeLeft();
                }
                return true;
            }

            return false;
        }
    }

}
