package moe.yukisora.solidot;

import android.app.Fragment;
import android.os.Handler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsManager {
    private static NewsManager newsManager;
    private Handler handler;

    private NewsManager() {
        handler = new Handler();
    }

    public static NewsManager getInstance() {
        if (newsManager == null)
            newsManager = new NewsManager();

        return newsManager;
    }

    public void getNewsByDate(Fragment fragment, String date) {
        new DownloadNewsByDateTask(fragment, date).start();
    }

    private class DownloadNewsByDateTask extends Thread {
        private ArticleFragment fragment;
        private String date;

        DownloadNewsByDateTask(Fragment fragment, String date) {
            this.fragment = (ArticleFragment)fragment;
            this.date = date;
        }

        private void getNews() {
            try {
                //connecting
                Connection connection = Jsoup.connect("http://www.solidot.org/?issue=" + date);
                connection.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0");
                connection.timeout(30 * 1000);

                //parse
                Document document = connection.get();
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
                }
            } catch (IOException ignore) {
            }
        }

        @Override
        public void run() {
            getNews();
        }
    }
}
