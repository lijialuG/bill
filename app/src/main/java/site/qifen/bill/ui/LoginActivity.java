package site.qifen.bill.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.qifen.bill.MainActivity;
import site.qifen.bill.R;
import site.qifen.bill.dao.UserDao;
import site.qifen.bill.db.BillDatabase;
import site.qifen.bill.model.Bill;
import site.qifen.bill.model.User;
import site.qifen.bill.util.App;
import site.qifen.bill.util.BillUtil;
import site.qifen.bill.util.SpUtil;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.login_text)
    TextView loginText;
    @BindView(R.id.username_text)
    TextView usernameText;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password_text)
    TextView passwordText;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.reg_btn)
    Button regBtn;
    @BindView(R.id.login_btn)
    Button loginBtn;

    UserDao userDao = BillDatabase.getInstance(App.getContext()).userDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        User thisUser = BillUtil.getThisUser();
        if (SpUtil.read("admin").equals("")) {
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra("isOne", true);
            startActivity(intent);
        } else {
            if (thisUser != null) {
                if (userDao.existUser(thisUser.getUsername(), thisUser.getPassword())) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            }
        }
    }

    @OnClick({R.id.reg_btn, R.id.login_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.reg_btn:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.login_btn:
                String usernameValue = username.getText().toString();
                String passwordValue = password.getText().toString();
                if (!usernameValue.equals("") && !passwordValue.equals("")) {
                    if (!userDao.existUser(usernameValue, passwordValue)) {
                        BillUtil.toast("账号/密码错误");
                    } else {
                        BillUtil.toast("登陆成功");
                        SpUtil.write("user", new Gson().toJson(userDao.getUserByUserName(usernameValue)));
                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    }
                } else {
                    BillUtil.toast("账号/密码不能为空");
                }
                break;
        }
    }
}
