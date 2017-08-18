package moe.yukisora.solidot;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

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

    public static boolean isOnline(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }
}
