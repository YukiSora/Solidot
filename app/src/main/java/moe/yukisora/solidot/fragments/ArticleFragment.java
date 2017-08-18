package moe.yukisora.solidot.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import moe.yukisora.solidot.R;
import moe.yukisora.solidot.SolidotApplication;
import moe.yukisora.solidot.adapters.RecyclerViewAdapter;
import moe.yukisora.solidot.core.GetNews;
import moe.yukisora.solidot.interfaces.RecyclerViewOnScrollListener;
import moe.yukisora.solidot.modles.NewsData;

public class ArticleFragment extends Fragment {
    private ArrayList<NewsData> newsDatas;
    private Calendar calendar;
    private Handler handler;
    private RecyclerViewAdapter adapter;
    private boolean isDownloading;

    public static ArticleFragment newInstance() {
        Bundle args = new Bundle();
        ArticleFragment fragment = new ArticleFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);

        handler = new Handler();
        initFragment();
        initRecyclerView(view);
        getNews();

        return view;
    }

    private void initFragment() {
        newsDatas = new ArrayList<>();
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        isDownloading = false;
    }

    private void initRecyclerView(View view) {
        //RecyclerView
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        //Layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //Adapter
        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        //Divider
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.recycler_view_divider));
        recyclerView.addItemDecoration(divider);

        //Animator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //OnScrollListener
        recyclerView.addOnScrollListener(new RecyclerViewOnScrollListener() {
            @Override
            public void onBottom() {
                getNews();
            }

            @Override
            public void onTop() {
            }
        });

        //Floating Action Button
        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    public String nextDate() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        String date = String.format("%d%02d%02d", year, month, day);
        calendar.add(Calendar.DATE, -1);

        return date;
    }

    public void getNews() {
        if (!isDownloading) {
            isDownloading = true;

            GetNews.getNews("http://www.solidot.org/?issue=" + nextDate(), new Observer<ArrayList<NewsData>>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                }

                @Override
                public void onNext(@NonNull ArrayList<NewsData> newsDatas) {
                    final int startPosition = ArticleFragment.this.newsDatas.size();
                    ArticleFragment.this.newsDatas.addAll(newsDatas);
                    final int EndPosition = ArticleFragment.this.newsDatas.size();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyItemRangeInserted(startPosition, EndPosition);
                        }
                    });
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Fetch news failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e(SolidotApplication.TAG, e.toString());
                }

                @Override
                public void onComplete() {
                    isDownloading = false;
                }
            });
        }
    }

    public ArrayList<NewsData> getNewsDatas() {
        return newsDatas;
    }
}
