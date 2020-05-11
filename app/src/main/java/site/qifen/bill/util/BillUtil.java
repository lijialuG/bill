package site.qifen.bill.util;

import android.widget.Toast;

import com.google.gson.Gson;

import site.qifen.bill.model.User;

public class BillUtil {
    public static void toast(String text) {
        Toast.makeText(App.getContext(), text, Toast.LENGTH_LONG).show();
    }


    public static User getThisUser() { //获取当前登陆用户
        return new Gson().fromJson(SpUtil.read("user"), User.class);
    }


}
