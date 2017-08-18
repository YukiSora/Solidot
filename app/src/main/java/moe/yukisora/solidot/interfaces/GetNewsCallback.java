package moe.yukisora.solidot.interfaces;

import java.util.ArrayList;

public interface GetNewsCallback {
    void onResponse(ArrayList<Integer> newsDatas);

    void onFailure();
}
