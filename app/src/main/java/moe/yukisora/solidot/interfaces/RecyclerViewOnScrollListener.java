package moe.yukisora.solidot.interfaces;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import moe.yukisora.solidot.interfaces.OnPositionListener;

public abstract class RecyclerViewOnScrollListener extends RecyclerView.OnScrollListener implements OnPositionListener {
    private int firstPosition;
    private int lastPosition;

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        int totalItemCount = layoutManager.getItemCount();
        if (totalItemCount == 0 || newState == RecyclerView.SCROLL_STATE_IDLE &&
                lastPosition >= totalItemCount - 1) {
            onBottom();
        }
        if (newState == RecyclerView.SCROLL_STATE_IDLE &&
                firstPosition == 0) {
            onTop();
        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        LinearLayoutManager layoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
        firstPosition = layoutManager.findFirstVisibleItemPosition();
        lastPosition = layoutManager.findLastVisibleItemPosition();
    }
}
