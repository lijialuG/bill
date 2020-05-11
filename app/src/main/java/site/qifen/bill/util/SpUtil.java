package site.qifen.bill.util;

import android.content.Context;
import android.content.SharedPreferences;


public class SpUtil {

    public static void write(String key, String value) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences("bill", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String read(String key) {
        SharedPreferences sharedPreferences = App.getContext().getSharedPreferences("bill", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
}
