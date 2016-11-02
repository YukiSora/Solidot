package moe.yukisora.solidot;

import android.app.Fragment;
import android.os.Handler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
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

    public void getNews(Fragment fragment, String date) {
        new DownloadNewsLinksTask(fragment, date).start();
    }

    private class DownloadNewsLinksTask extends Thread {
        private ArticleFragment fragment;
        private String date;

        DownloadNewsLinksTask(Fragment fragment, String date) {
            this.fragment = (ArticleFragment)fragment;
            this.date = date;
        }

        private ArrayList<String> getNewsLinks() {
            ArrayList<String> links = new ArrayList<>();
            Pattern p = Pattern.compile("\\d+");
            try {
                //connecting
                Connection connection = Jsoup.connect("http://www.solidot.org/?issue=" + date);
                connection.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0");
                connection.timeout(30 * 1000);

                //parse
                Document document = connection.get();
                for (Element block : document.select("div.block_m")) {
                    String href = block.select("div.bg_htit h2").first().getElementsByTag("a").last().attr("href");
                    Matcher m = p.matcher(href);
                    if (m.find())
                        links.add(m.group());
                }
            } catch (IOException ignore) {
            }

            return links;
        }

        @Override
        public void run() {
            for (String link : getNewsLinks()) {
                NewsData data = new NewsData();
                data.sid = Integer.parseInt(link);
                fragment.getNewsDatas().add(data);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        fragment.getAdapter().notifyItemInserted(fragment.getNewsDatas().size() - 1);
                    }
                });
            }
        }
    }
}
