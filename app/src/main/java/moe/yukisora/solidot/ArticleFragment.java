package moe.yukisora.solidot;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ArticleFragment extends Fragment {
    public static final int NEW = 0;
    public static final int POPULAR = 1;
    private int mode;

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
        if (mode == 0)
            ((TextView)view.findViewById(R.id.fragmentTextView)).setText("News");
        else if (mode == 1)
            ((TextView)view.findViewById(R.id.fragmentTextView)).setText("Popular");

        return view;
    }
}
