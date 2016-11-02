package moe.yukisora.solidot;

import android.os.AsyncTask;
import android.util.Log;

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

    private NewsManager() {
    }

    public static NewsManager getInstance() {
        if (newsManager == null)
            newsManager = new NewsManager();

        return newsManager;
    }

    public void getNews(final String date) {
        new DownloadNewsLinksTask().execute(date);
    }

    private class DownloadNewsLinksTask extends AsyncTask<String, Void, ArrayList<String>> {
        protected ArrayList<String> doInBackground(String... date) {
            ArrayList<String> links = new ArrayList<>();
            Pattern p = Pattern.compile("\\d+");
            try {
                //connecting
                Connection connection = Jsoup.connect("http://www.solidot.org/?issue=" + date[0]);
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

        protected void onPostExecute(ArrayList<String> links) {
            for (String link : links) {
                Log.i("poi", link);
            }
        }
    }
}
