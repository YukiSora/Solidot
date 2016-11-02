package moe.yukisora.solidot;

import android.app.Fragment;
import android.os.Bundle;
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

public class ArticleFragment extends Fragment {
    private ArrayList<Integer> newsDatas;
    private Calendar calendar;
    private RecyclerViewAdapter adapter;

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

        initFragment();
        initRecyclerView(view);
        NewsManager.getInstance().getNewsByDate(this, getDate());

        return view;
    }

    private void initFragment() {
        newsDatas = new ArrayList<>();
        calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
    }

    private void initRecyclerView(View view) {
        //RecyclerView
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

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
                NewsManager.getInstance().getNewsByDate(ArticleFragment.this, getDate());
            }

            @Override
            public void onTop() {
                Log.i("poi", "top");
            }
        });
    }

    public String getDate() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DATE);
        String date = String.format("%d%02d%02d", year, month, day);
        calendar.add(Calendar.DATE, -1);

        return date;
    }

    public ArrayList<Integer> getNewsDatas() {
        return newsDatas;
    }

    public RecyclerViewAdapter getAdapter() {
        return adapter;
    }
}
