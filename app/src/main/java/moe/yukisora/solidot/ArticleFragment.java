package moe.yukisora.solidot;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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
    private ArrayList<Integer> newsDatas;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);

        initFragment();
        initRecyclerView(view);
        NewsManager.getInstance().getNewsByDate(this, "20161031");

        return view;
    }

    private void initFragment() {
        newsDatas = new ArrayList<>();
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
    }

    public ArrayList<Integer> getNewsDatas() {
        return newsDatas;
    }

    public RecyclerViewAdapter getAdapter() {
        return adapter;
    }
}
