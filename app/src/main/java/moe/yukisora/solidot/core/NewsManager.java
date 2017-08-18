package moe.yukisora.solidot.core;

import android.app.Fragment;
import android.os.Handler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import moe.yukisora.solidot.SolidotApplication;
import moe.yukisora.solidot.fragments.ArticleFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class NewsManager {
    private static NewsManager newsManager;
    private Handler handler;
    private boolean isDownloading;

    private NewsManager() {
        handler = new Handler();
    }

    public static NewsManager getInstance() {
        if (newsManager == null)
            newsManager = new NewsManager();

        return newsManager;
    }

    public void getNewsByDate(Fragment fragment, String date) {
        if (!isDownloading) {
            getNews((ArticleFragment)fragment, date);
        }
    }

    private void getNews(final ArticleFragment fragment, String date) {
        isDownloading = true;
        Request request = new Request.Builder()
                .url("http://www.solidot.org/?issue=" + date)
                .build();

        SolidotApplication.getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    //parse
                    Document document = Jsoup.parse(response.body().string());
                    for (Element block : document.select("div.block_m")) {
                        //sid
                        int sid = 0;
                        String href = block.select("div.bg_htit h2").first().getElementsByTag("a").last().attr("href");
                        Matcher m = Pattern.compile("\\d+").matcher(href);
                        if (m.find())
                            sid = Integer.parseInt(m.group());

                        //insert
                        NewsCache.getInstance().getNews(sid);
                        fragment.getNewsDatas().add(sid);

                        //render
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                fragment.getAdapter().notifyItemInserted(fragment.getNewsDatas().size() - 1);
                            }
                        });
                        isDownloading = false;
                    }
                }
            }
        });
    }
}
