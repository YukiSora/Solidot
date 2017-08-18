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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class GetNews {
    public static void getNews(String url, Observer<ArrayList<Integer>> observer) {
        downloadNews(url)
                .map(new Function<String, ArrayList<Integer>>() {
                    @Override
                    public ArrayList<Integer> apply(@NonNull String html) throws Exception {
                        ArrayList<Integer> newsDatas = new ArrayList<>();

                        Document document = Jsoup.parse(html);
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

                        return newsDatas;
                    }
                })
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    private static Observable<String> downloadNews(final String url) {
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
}
