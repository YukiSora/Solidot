package moe.yukisora.solidot;

import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArticleFragment fragment;

    public RecyclerViewAdapter(Fragment fragment) {
        this.fragment = (ArticleFragment)fragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NewsData newsData = NewsCache.getInstance().getNews(fragment.getNewsDatas().get(position));

        holder.title.setText(newsData.title);
        holder.reference.setText(newsData.reference);
    }

    @Override
    public int getItemCount() {
        return fragment.getNewsDatas().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView reference;

        public ViewHolder(View view) {
            super(view);

            title = (TextView)view.findViewById(R.id.title);
            reference = (TextView)view.findViewById(R.id.reference);
        }
    }
}
