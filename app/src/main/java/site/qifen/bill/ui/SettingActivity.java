package site.qifen.bill.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.qifen.bill.R;
import site.qifen.bill.dao.BillDao;
import site.qifen.bill.dao.UserDao;
import site.qifen.bill.db.BillDatabase;
import site.qifen.bill.model.Bill;
import site.qifen.bill.model.User;
import site.qifen.bill.util.App;
import site.qifen.bill.util.BillUtil;
import site.qifen.bill.util.SpUtil;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.managerInBillType)
    Button managerInBillType;
    @BindView(R.id.managerOutBillType)
    Button managerOutBillType;
    @BindView(R.id.clearInBillBtn)
    Button clearInBillBtn;
    @BindView(R.id.managerOutBillBtn)
    Button managerOutBillBtn;
    @BindView(R.id.managerUserBtn)
    Button managerUserBtn;
    @BindView(R.id.managerBackBtn)
    Button managerBackBtn;
    private User thisUser;

    private BillDao billDao = BillDatabase.getInstance(this).billDao();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        thisUser = BillUtil.getThisUser();

    }

    @OnClick({R.id.managerInBillType, R.id.managerOutBillType, R.id.clearInBillBtn, R.id.managerOutBillBtn, R.id.managerUserBtn, R.id.managerBackBtn})
    public void onClick(View view) {
        Intent intent = new Intent(this, EditTypeActivity.class);
        switch (view.getId()) {
            case R.id.managerInBillType:
                intent.putExtra("edit", 1); // in
                break;
            case R.id.managerOutBillType:
                intent.putExtra("edit", 0); //out
                break;
            case R.id.clearInBillBtn:
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("你确定删除所有收入账单吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                billDao.deleteUserInBill(thisUser.getId());
                                BillUtil.toast("删除收入账单成功!");
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
            case R.id.managerOutBillBtn:
                AlertDialog alertDialog1 = new AlertDialog.Builder(this)
                        .setTitle("你确定删除所有支出账单吗?")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                billDao.deleteUserOutBill(thisUser.getId());
                                BillUtil.toast("删除支出账单成功!");
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                alertDialog1.show();
                break;
            case R.id.managerUserBtn:
                if (thisUser.getUsername().equals(SpUtil.read("admin"))) {
                    startActivity(new Intent(this, ManagerUserActivity.class));
                } else {
                    BillUtil.toast("你没有权限操作");
                }
                break;
            case R.id.managerBackBtn:
                finish();
                break;
        }
        startActivity(intent);
    }

}
