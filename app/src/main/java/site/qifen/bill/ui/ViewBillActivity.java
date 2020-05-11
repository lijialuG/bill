package site.qifen.bill.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.qifen.bill.R;
import site.qifen.bill.dao.BillDao;
import site.qifen.bill.db.BillDatabase;
import site.qifen.bill.model.Bill;
import site.qifen.bill.model.User;
import site.qifen.bill.util.App;
import site.qifen.bill.util.BillUtil;

public class ViewBillActivity extends AppCompatActivity {

    @BindView(R.id.view_bill)
    EditText viewBill;
    @BindView(R.id.start_view_btn)
    Button startViewBtn;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.in_and_out_bill_money)
    TextView inAndOutBillMoney;

    private List<Bill> billList = new ArrayList<>();
    private BillDao billDao = BillDatabase.getInstance(App.getContext()).billDao();
    private User thisUser;
    private String[] spinnerArr = {"类型", "描述", "时间", "金额"};
    private String spinnerString = "类型";
    private BillAdapter billAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        thisUser = BillUtil.getThisUser();
        billList = billDao.getThisUserAllBill(thisUser.getId());


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        billAdapter = new BillAdapter(billList,inAndOutBillMoney );
        recyclerView.setAdapter(billAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerString = spinnerArr[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.show_all_menu:
                billList = billDao.getThisUserAllBill(thisUser.getId());
                recyclerView.setAdapter(new BillAdapter(billList,inAndOutBillMoney));
                billAdapter.notifyDataSetChanged();
                break;
            case R.id.show_in_menu:
                billList = billDao.getThisUserInBill(thisUser.getId());
                recyclerView.setAdapter(new BillAdapter(billList,inAndOutBillMoney));
                billAdapter.notifyDataSetChanged();
                break;
            case R.id.show_out_menu:
                billList = billDao.getThisUserOutBill(thisUser.getId());
                recyclerView.setAdapter(new BillAdapter(billList,inAndOutBillMoney));
                billAdapter.notifyDataSetChanged();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bill_menu, menu);
        return true;
    }

    @OnClick(R.id.start_view_btn)
    public void onClick() {
        String text = viewBill.getText().toString();
        if (!text.equals("")) {
            List<Bill> tempList = new ArrayList<>();
            switch (spinnerString) {
                case "描述":
                    tempList = billDao.getLikeDescBill(text,thisUser.getId());
                    break;
                case "时间":
                    tempList = billDao.getLikeDateBill(text,thisUser.getId());
                    break;
                case "金额":
                    tempList = billDao.getLikeMoneyBill(Float.parseFloat(text),thisUser.getId());
                    break;
                case "类型":
                    tempList = billDao.getLikeFromBill(text,thisUser.getId());
                    break;
            }
            recyclerView.setAdapter(new BillAdapter(tempList,inAndOutBillMoney));
            billAdapter.notifyDataSetChanged();
        } else {
            BillUtil.toast("搜索条件不能为空");
        }
    }
}
