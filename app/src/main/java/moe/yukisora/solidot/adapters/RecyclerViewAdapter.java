package moe.yukisora.solidot.adapters;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import moe.yukisora.solidot.R;
import moe.yukisora.solidot.fragments.ArticleFragment;
import moe.yukisora.solidot.modles.ArticleData;

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
        final ArticleData articleData = fragment.getArticleDatas().get(position);

        holder.title.setText(articleData.title);
        holder.date.setText(articleData.datetime);
        holder.reference.setText(articleData.reference);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("moe.yukisora.solidot.NewsActivity");
                Bundle bundle = new Bundle();
                bundle.putSerializable("articleData", articleData);
                intent.putExtras(bundle);
                fragment.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fragment.getArticleDatas().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout relativeLayout;
        public TextView title;
        public TextView date;
        public TextView reference;

        public ViewHolder(View view) {
            super(view);

            relativeLayout = view.findViewById(R.id.newsItemRelativeLayout);
            title = view.findViewById(R.id.newsItemTitle);
            date = view.findViewById(R.id.newsItemDate);
            reference = view.findViewById(R.id.newsItemReference);
        }
    }
}
