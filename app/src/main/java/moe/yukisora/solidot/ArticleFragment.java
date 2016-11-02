package moe.yukisora.solidot;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ArticleFragment extends Fragment {
    public static final int NEW = 0;
    public static final int POPULAR = 1;
    private int mode;
    private ArrayList<NewsData> newsDatas;
    private RecyclerViewAdapter adapter;

    public static ArticleFragment newInstance(int mode) {
        Bundle args = new Bundle();
        ArticleFragment fragment = new ArticleFragment();
        args.putInt("mode", mode);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = getArguments().getInt("mode");

        newsDatas = new ArrayList<>();
        NewsManager.getInstance().getNews(this, "20161031");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);

        adapter = new RecyclerViewAdapter(this);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        return view;
    }

    public ArrayList<NewsData> getNewsDatas() {
        return newsDatas;
    }

    public RecyclerViewAdapter getAdapter() {
        return adapter;
    }
}
