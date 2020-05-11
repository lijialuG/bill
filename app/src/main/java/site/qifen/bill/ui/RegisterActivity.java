package site.qifen.bill.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.qifen.bill.MainActivity;
import site.qifen.bill.R;
import site.qifen.bill.dao.UserDao;
import site.qifen.bill.db.BillDatabase;
import site.qifen.bill.model.User;
import site.qifen.bill.util.BillUtil;
import site.qifen.bill.util.SpUtil;

public class RegisterActivity extends AppCompatActivity {


    @BindView(R.id.register_text)
    TextView registerText;
    @BindView(R.id.username_text)
    TextView usernameText;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password_text)
    TextView passwordText;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.conform_password_text)
    TextView conformPasswordText;
    @BindView(R.id.conform_password)
    EditText conformPassword;
    @BindView(R.id.back_btn)
    Button backBtn;
    @BindView(R.id.register_btn)
    Button registerBtn;
    private boolean isOne;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        isOne = getIntent().getBooleanExtra("isOne", false);
        if (isOne) {
            registerText.setText("设置管理员");
            backBtn.setVisibility(View.GONE);
        }
    }


    @OnClick({R.id.back_btn, R.id.register_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.register_btn:
                String usernameValue = username.getText().toString();
                String passwordValue = password.getText().toString();
                String conformPasswordValue = conformPassword.getText().toString();
                if (!usernameValue.equals("") && !passwordValue.equals("") && !conformPassword.toString().equals("")) {
                    if (passwordValue.equals(conformPasswordValue)) {
                        UserDao userDao = BillDatabase.getInstance(this).userDao();
                        if (userDao.existUsername(usernameValue)) {
                            BillUtil.toast("用户名已经存在");
                        } else {
                            //插入用户数据到数据库
                            User user = new User(usernameValue, passwordValue);
                            userDao.insertUser(user);
                            SpUtil.write("user", new Gson().toJson(userDao.getUserByUserName(usernameValue)));
                            BillUtil.toast("注册成功");
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                            if (isOne) {
                                SpUtil.write("admin", usernameValue);
                            }
                        }
                    } else {
                        BillUtil.toast("密码/确认密码不相同");
                    }
                } else {
                    BillUtil.toast("账号/密码/确认密码不能为空");
                }
                break;
        }
    }
}
