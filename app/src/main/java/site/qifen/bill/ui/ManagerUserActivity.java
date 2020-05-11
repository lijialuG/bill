package site.qifen.bill.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import site.qifen.bill.R;
import site.qifen.bill.dao.BillDao;
import site.qifen.bill.dao.UserDao;
import site.qifen.bill.db.BillDatabase;
import site.qifen.bill.model.Bill;
import site.qifen.bill.model.User;
import site.qifen.bill.util.App;
import site.qifen.bill.util.BillUtil;
import site.qifen.bill.util.SpUtil;

public class ManagerUserActivity extends AppCompatActivity {

    @BindView(R.id.listView)
    ListView listView;
    private UserDao userDao = BillDatabase.getInstance(this).userDao();
    private BillDao billDao = BillDatabase.getInstance(this).billDao();
    private List<String> userList = new ArrayList<>();
    private List<User> tempUserList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_user);
        ButterKnife.bind(this);
        tempUserList = userDao.getAllUser();
        userList = getUserString(tempUserList);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, userList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = tempUserList.get(position);
                AlertDialog alertDialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("删除账号")
                        .setMessage("你确定删除账号 " + user.getUsername() + " 吗?\n删除后ta的账单也会被删除")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!user.getUsername().equals(SpUtil.read("admin"))) {
                                    tempUserList.remove(position);
                                    userList.remove(position);
                                    arrayAdapter.notifyDataSetChanged();
                                    billDao.deleteUserAllBill(user.getId());
                                    userDao.deleteUser(user);
                                    BillUtil.toast("删除用户成功");
                                } else {
                                    BillUtil.toast("你不能删除自己");
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                alertDialog.show();
            }
        });
    }

    private List<String> getUserString(List<User> userList) {
        List<String> tempList = new ArrayList<>();
        for (User user : userList) {
            tempList.add(user.getId() + " | " + user.getUsername() + " | " + user.getPassword());
        }
        return tempList;
    }


}
