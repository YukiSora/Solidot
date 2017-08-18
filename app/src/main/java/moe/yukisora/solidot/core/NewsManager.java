package moe.yukisora.solidot.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import moe.yukisora.solidot.SolidotApplication;
import moe.yukisora.solidot.interfaces.GetNewsCallback;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class NewsManager {
    private static NewsManager newsManager;

    private NewsManager() {
    }

    public static NewsManager getInstance() {
        if (newsManager == null)
            newsManager = new NewsManager();

        return newsManager;
    }

    public void getNews(String date, final GetNewsCallback callback) {
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
                    ArrayList<Integer> newsDatas = new ArrayList<>();
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
                        newsDatas.add(sid);
                    }
                    callback.onResponse(newsDatas);
                }
            }
        });
    }
}
