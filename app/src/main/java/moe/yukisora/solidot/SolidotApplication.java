package moe.yukisora.solidot;

import android.app.Application;

import okhttp3.OkHttpClient;

public class SolidotApplication extends Application {
    public static String TAG = "solidot";
    private static OkHttpClient okHttpClient;

    public static OkHttpClient getOkHttpClient() {
        return SolidotApplication.okHttpClient;
    }

    public static void setOkHttpClient(OkHttpClient okHttpClient) {
        SolidotApplication.okHttpClient = okHttpClient;
    }
}
