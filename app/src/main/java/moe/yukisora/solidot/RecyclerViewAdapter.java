package moe.yukisora.solidot;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("moe.yukisora.solidot.NewsActivity");
                Bundle bundle = new Bundle();
                bundle.putSerializable("newsData", newsData);
                intent.putExtras(bundle);
                fragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fragment.getNewsDatas().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout relativeLayout;
        public TextView title;
        public TextView reference;

        public ViewHolder(View view) {
            super(view);

            relativeLayout = (RelativeLayout)view.findViewById(R.id.relativeLayoutItemView);
            title = (TextView)view.findViewById(R.id.titleItemView);
            reference = (TextView)view.findViewById(R.id.referenceItemView);
        }
    }
}
