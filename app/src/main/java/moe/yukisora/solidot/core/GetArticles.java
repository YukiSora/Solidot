package moe.yukisora.solidot.core;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import moe.yukisora.solidot.SolidotApplication;
import moe.yukisora.solidot.modles.ArticleData;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class GetArticles {
    public static void getArticles(String url, Observer<ArrayList<ArticleData>> observer) {
        downloadArticles(url)
                .map(new Function<String, ArrayList<ArticleData>>() {
                    @Override
                    public ArrayList<ArticleData> apply(@NonNull String html) throws Exception {
                        return parseArticles(html);
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private static Observable<String> downloadArticles(final String url) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull final ObservableEmitter<String> emitter) throws Exception {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                SolidotApplication.getOkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        emitter.onError(e);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.isSuccessful() && response.body() != null) {
                            emitter.onNext(response.body().string());
                        } else {
                            emitter.onError(new IOException("Response failed."));
                        }
                    }
                });
            }
        });
    }

    private static ArrayList<ArticleData> parseArticles(String html) {
        ArrayList<ArticleData> articles = new ArrayList<>();

        Document document = Jsoup.parse(html);
        for (Element block : document.select("div.block_m")) {
            ArticleData articleData = new ArticleData();

            // title block
            Element titleBlock = block.select("div.bg_htit").first();
            Element titleBlockA = titleBlock.getElementsByTag("a").last();
            // sid
            Matcher m = Pattern.compile("\\d+").matcher(titleBlockA.attr("href"));
            if (m.find()) {
                articleData.sid = Integer.parseInt(m.group());
            }
            // title
            articleData.title = titleBlockA.text();

            // title block
            Element timeBlock = block.select("div.talk_time").first();
            // datetime
            String datetime = timeBlock.ownText();
            articleData.datetime = datetime.substring(3, datetime.length() - 4);
            // reference
            Element timeBlockB = timeBlock.select("b").first();
            articleData.reference = timeBlockB.text().replaceAll("\\s+","").substring(2);

            // content block
            Element contentBlock = block.select("div.p_content").first();
            // content
            articleData.content = contentBlock.select("div.p_mainnew").html();

            articles.add(articleData);
        }

        return articles;
    }
}
