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

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import moe.yukisora.solidot.R;
import moe.yukisora.solidot.SolidotApplication;
import moe.yukisora.solidot.adapters.RecyclerViewAdapter;
import moe.yukisora.solidot.core.GetArticles;
import moe.yukisora.solidot.modles.ArticleData;

public class ArticleFragment extends Fragment {
    private static ArticleFragment fragment;
    private ArrayList<ArticleData> articles;
    private Calendar calendar;
    private Handler handler;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private TwinklingRefreshLayout refreshLayout;
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

        fragment = this;
        articles = new ArrayList<>();
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);

        initView(view);
        refreshLayout.startRefresh();

        return view;
    }

    private void initFragment() {
        articles = new ArrayList<>();
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        isDownloading = false;
    }

    private void initView(View view) {
        // recycler view
        recyclerView = view.findViewById(R.id.recyclerView);

        // layout
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // adapter
        adapter = new RecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);

        // divider
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.recycler_view_divider));
        recyclerView.addItemDecoration(divider);

        // animator
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setAutoLoadMore(true);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter(){
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initFragment();
                        adapter.notifyDataSetChanged();
                        downloadArticles();
                    }
                },2000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        downloadArticles();
                    }
                },2000);
            }
        });

        // floating action button
        FloatingActionButton floatingActionButton = getActivity().findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private String getDate() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        String date = String.format("%d%02d%02d", year, month, day);
        calendar.add(Calendar.DATE, -1);

        return date;
    }

    private void rollBackDate() {
        calendar.add(Calendar.DATE, +1);
    }

    private void downloadArticles() {
        if (!isDownloading) {
            isDownloading = true;

            GetArticles.getArticles("http://www.solidot.org/?issue=" + getDate(), new Observer<ArrayList<ArticleData>>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                }

                @Override
                public void onNext(@NonNull final ArrayList<ArticleData> articles) {
                    isDownloading = false;
                    final int itemCount = articles.size();
                    if (itemCount > 0) {
                        final int startPosition = ArticleFragment.this.articles.size();
                        final int endPosition = startPosition + itemCount;

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                ArticleFragment.this.articles.addAll(articles);
                                adapter.notifyItemRangeInserted(startPosition, endPosition);
                                if (startPosition == 0) {
                                    refreshLayout.finishRefreshing();
                                } else {
                                    refreshLayout.finishLoadmore();
                                }
                            }
                        });

                        if (itemCount > 5 && endPosition > 10) {
                            return;
                        }
                    }

                    downloadArticles();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            isDownloading = false;
                            rollBackDate();
                            refreshLayout.finishLoadmore();
                            refreshLayout.finishRefreshing();
                            Toast.makeText(getActivity(), "Fetch news failed.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Log.e(SolidotApplication.TAG, e.toString());
                }

                @Override
                public void onComplete() {
                }
            });
        }
    }

    public ArrayList<ArticleData> getArticles() {
        return articles;
    }

    public static ArticleData getNextArticle(int position) {
        if (position <= fragment.getArticles().size() - 1) {
            fragment.recyclerView.smoothScrollToPosition(position);
            if (position == fragment.getArticles().size() - 1) {

                fragment.downloadArticles();
            }

            return fragment.getArticles().get(position);
        } else {
            return null;
        }
    }
}
