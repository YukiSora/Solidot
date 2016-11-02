package moe.yukisora.solidot;

import android.util.SparseArray;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NewsCache {
    private static NewsCache newsCache;
    private SparseArray<NewsData> newsDatas;

    private NewsCache() {
        newsDatas = new SparseArray<>();
    }

    public static NewsCache getInstance() {
        if (newsCache == null)
            newsCache = new NewsCache();

        return newsCache;
    }

    public NewsData getNews(int sid) {
        if (newsDatas.get(sid) == null)
            downloadNews(sid);

        return newsDatas.get(sid);
    }

    private void downloadNews(int sid) {
        try {
            NewsData newsData = new NewsData();
            newsDatas.put(sid, newsData);

            //connecting
            Connection connection = Jsoup.connect("http://www.solidot.org/story?sid=" + sid);
            connection.header("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:49.0) Gecko/20100101 Firefox/49.0");
            connection.timeout(30 * 1000);

            //parse
            Document document = connection.get();

            //---Main Block---
            Element main = document.select("div.block_m").first();

            //Title Block
            //by the way, tittle is typo by website
            Element title = main.select("div.ct_tittle").first();
            //title
            newsData.title = title.select("h2").text();

            //Attribute Block
            Element attribute = main.select("div.talk_time").first();
            //date
            newsData.date = attribute.ownText();
            //reference
            newsData.reference = attribute.select("b").first().text();

            //Content Block
            Element content = main.select("div.p_mainnew").first();
            //article
            newsData.article = content.html();

            //---Right Block---
            Element right = document.select("div.block_r").first();
            //Number of View Block
            Element view = right.select("div.content").first();
            //number of view
            int numberOfView = 0;
            String numberOfViewText = view.select("b").text();
            Matcher m = Pattern.compile("\\d+").matcher(numberOfViewText);
            if (m.find())
                numberOfView = Integer.parseInt(m.group());
            newsData.numberOfView = numberOfView;

        } catch (IOException ignore) {
        }
    }
}
