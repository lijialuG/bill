package site.qifen.bill;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.qifen.bill.dao.BillDao;
import site.qifen.bill.dao.UserDao;
import site.qifen.bill.db.BillDatabase;
import site.qifen.bill.model.Bill;
import site.qifen.bill.model.User;
import site.qifen.bill.ui.AddBillActivity;
import site.qifen.bill.ui.LoginActivity;
import site.qifen.bill.ui.SettingActivity;
import site.qifen.bill.ui.ViewBillActivity;
import site.qifen.bill.util.App;
import site.qifen.bill.util.BillUtil;
import site.qifen.bill.util.SpUtil;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.billNum)
    TextView billNum;
    @BindView(R.id.addBillBtn)
    CardView addBillBtn;
    @BindView(R.id.queryBillBtn)
    CardView queryBillBtn;

    private UserDao userDao = BillDatabase.getInstance(App.getContext()).userDao();
    private BillDao billDao = BillDatabase.getInstance(App.getContext()).billDao();
    private User thisUser = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        thisUser = BillUtil.getThisUser();

        if (SpUtil.read("inItem").equals(""))
            SpUtil.write("inItem", "工资,兼职,奖金,分红,股票,基金,报销款,应收款,其他");
        if (SpUtil.read("outItem").equals(""))
            SpUtil.write("outItem", "早餐,学习/工作用品,电子产品,衣服,夜宵,打的,食品,午餐,其他");
        initUserInfo();


    }

    @Override
    protected void onStart() {
        super.onStart();
        initUserInfo();
    }

    private void initUserInfo() {
        if (thisUser != null) {
            String textInfo = "用户名: ";
            if (SpUtil.read("admin").equals(thisUser.getUsername())) {
                textInfo = "管理员: ";
            }
            username.setText(textInfo + thisUser.getUsername()+"  ID: "+thisUser.getId());
            String thisUserId = String.valueOf(thisUser.getId());
            float inSumBill = billDao.getThisUserInSumBill(thisUserId);
            float outSumBill = billDao.getThisUserOutSumBill(thisUserId);
            billNum.setText("收入: " + inSumBill + "  支出: " + outSumBill);
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }


    @OnClick({R.id.addBillBtn, R.id.queryBillBtn, R.id.setBillBtn, R.id.exitUserBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addBillBtn:
                startActivity(new Intent(this, AddBillActivity.class));
                break;
            case R.id.queryBillBtn:
                startActivity(new Intent(this, ViewBillActivity.class));
                break;
            case R.id.setBillBtn:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.exitUserBtn:
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("你确定退出登录吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SpUtil.write("user", "");
                                startActivity(new Intent(App.getContext(), LoginActivity.class));
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                alertDialog.show();
                break;
        }
    }
}
