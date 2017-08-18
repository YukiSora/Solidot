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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import moe.yukisora.solidot.R;
import moe.yukisora.solidot.adapters.RecyclerViewAdapter;
import moe.yukisora.solidot.core.NewsManager;
import moe.yukisora.solidot.interfaces.GetNewsCallback;
import moe.yukisora.solidot.interfaces.RecyclerViewOnScrollListener;

public class ArticleFragment extends Fragment {
    private ArrayList<Integer> newsDatas;
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
                Log.i("poi", "top");
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
            NewsManager.getInstance().getNews(nextDate(), new GetNewsCallback() {
                @Override
                public void onResponse(final ArrayList<Integer> newsDatas) {
                    final int startPosition = ArticleFragment.this.newsDatas.size();
                    final int itemCount = newsDatas.size();
                    ArticleFragment.this.newsDatas.addAll(newsDatas);
                    //render
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            getAdapter().notifyItemRangeInserted(startPosition, startPosition + itemCount);
                        }
                    });
                    isDownloading = false;
                }

                @Override
                public void onFailure() {

                }
            });
        }
    }

    public ArrayList<Integer> getNewsDatas() {
        return newsDatas;
    }

    public RecyclerViewAdapter getAdapter() {
        return adapter;
    }
}
