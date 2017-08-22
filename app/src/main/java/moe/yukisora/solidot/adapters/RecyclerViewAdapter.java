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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArticleFragment fragment;

    public RecyclerViewAdapter(Fragment fragment) {
        this.fragment = (ArticleFragment)fragment;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ArticleViewHolder(inflater.inflate(R.layout.article_item_view, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ArticleViewHolder) {
            ArticleViewHolder articleHolder = (ArticleViewHolder)holder;
            ArticleData article = fragment.getArticles().get(position);

            articleHolder.bindData(article);
        }
    }

    @Override
    public int getItemCount() {
        return fragment.getArticles().size();
    }

    private class ArticleViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout relativeLayout;
        public TextView title;
        public TextView date;
        public TextView reference;

        public ArticleViewHolder(View view) {
            super(view);

            relativeLayout = view.findViewById(R.id.articleItemRelativeLayout);
            title = view.findViewById(R.id.articleItemTitle);
            date = view.findViewById(R.id.articleItemDate);
            reference = view.findViewById(R.id.articleItemReference);
        }

        public void bindData(final ArticleData article) {
            title.setText(article.title);
            date.setText(article.datetime);
            if (!article.reference.isEmpty()) {
                reference.setText("来自" + article.reference);
            } else {
                reference.setText("");
            }
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent("moe.yukisora.solidot.activities.ArticleActivity");
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("articleData", article);
                    intent.putExtras(bundle);
                    fragment.startActivity(intent);
                }
            });
        }
    }
}
