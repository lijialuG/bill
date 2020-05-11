package site.qifen.bill.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;


import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnChangeLisener;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.qifen.bill.MainActivity;
import site.qifen.bill.R;
import site.qifen.bill.dao.BillDao;
import site.qifen.bill.db.BillDatabase;
import site.qifen.bill.model.Bill;
import site.qifen.bill.model.User;
import site.qifen.bill.util.App;
import site.qifen.bill.util.BillUtil;
import site.qifen.bill.util.SpUtil;

public class AddBillActivity extends AppCompatActivity {

    @BindView(R.id.bill_text)
    TextView billText;
    @BindView(R.id.bill_money_text)
    TextView billMoneyText;
    @BindView(R.id.bill_money_edit)
    EditText billMoneyEdit;
    @BindView(R.id.money_relative)
    RelativeLayout moneyRelative;
    @BindView(R.id.bill_type_text)
    TextView billTypeText;
    @BindView(R.id.bill_type_in_radio_btn)
    RadioButton billTypeInRadioBtn;
    @BindView(R.id.bill_type_out_radio_btn)
    RadioButton billTypeOutRadioBtn;
    @BindView(R.id.type_relative)
    RelativeLayout typeRelative;
    @BindView(R.id.bill_from_text)
    TextView billFromText;
    @BindView(R.id.bill_from_edit)
    EditText billFromEdit;
    @BindView(R.id.from_relative)
    RelativeLayout fromRelative;
    @BindView(R.id.bill_time_text)
    TextView billTimeText;
    @BindView(R.id.bill_time_edit)
    TextView billTimeEdit;
    @BindView(R.id.time_relative)
    RelativeLayout timeRelative;
    @BindView(R.id.bill_desc_text)
    TextView billDescText;
    @BindView(R.id.bill_desc_edit)
    EditText billDescEdit;
    @BindView(R.id.desc_relative)
    RelativeLayout descRelative;
    @BindView(R.id.back_bill_btn)
    Button backBillBtn;
    @BindView(R.id.add_bill_btn)
    Button addBillBtn;

    private String formatTimeString = "yyyy-MM-dd HH:mm";
    private String[] list = {};//要填充的数据
    private ListPopupWindow listPopupWindow = null;
    private BillDao billDao = BillDatabase.getInstance(App.getContext()).billDao();
    private User thisUser = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bill);
        ButterKnife.bind(this);


        billTimeEdit.setText(new SimpleDateFormat(formatTimeString).format(new Date()));

        thisUser = BillUtil.getThisUser();

        //popwindow
        if (billTypeInRadioBtn.isChecked()) list = SpUtil.read("inItem").split(",");
        if (billTypeOutRadioBtn.isChecked()) list = SpUtil.read("outItem").split(",");
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(billFromEdit);//以哪个控件为基准，在该处以mEditText为基准


        billFromEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (!listPopupWindow.isShowing()) showListPopWindow();
                }
            }
        });


        billTypeInRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                billFromEdit.setText("");
                if (billTypeInRadioBtn.isChecked()) list = SpUtil.read("inItem").split(",");
                if (billTypeOutRadioBtn.isChecked()) list = SpUtil.read("outItem").split(",");
                listPopupWindow.setAdapter(new ArrayAdapter<String>(buttonView.getContext(), android.R.layout.simple_list_item_1, list));//用android内置布局，或设计自己的样式
                if (!listPopupWindow.isShowing()) showListPopWindow();
            }
        });
        billTypeOutRadioBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                billFromEdit.setText("");
                if (billTypeInRadioBtn.isChecked()) list = SpUtil.read("inItem").split(",");
                if (billTypeOutRadioBtn.isChecked()) list = SpUtil.read("outItem").split(",");
                listPopupWindow.setAdapter(new ArrayAdapter<String>(buttonView.getContext(), android.R.layout.simple_list_item_1, list));//用android内置布局，或设计自己的样式
                if (!listPopupWindow.isShowing()) showListPopWindow();
            }
        });


    }

    private void showListPopWindow() {
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                billFromEdit.setText(list[i]);//把选择的选项内容展示在EditText上
                listPopupWindow.dismiss();//如果已经选择了，隐藏起来
            }
        });
        listPopupWindow.show();//把ListPopWindow展示出来
    }

    @OnClick({R.id.back_bill_btn, R.id.add_bill_btn, R.id.bill_time_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_bill_btn:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.add_bill_btn:
                String from = billFromEdit.getText().toString();
                String time = billTimeEdit.getText().toString();
                String desc = billDescEdit.getText().toString();
                boolean type = billTypeInRadioBtn.isChecked();
                String parseMoneyFloat = billMoneyEdit.getText().toString();
                if (!from.equals("") && !time.equals("") && !parseMoneyFloat.equals("")) {
                    float money = Float.parseFloat(parseMoneyFloat);
                    if (thisUser != null) {
                        billDao.insertBill(new Bill(money, String.valueOf(thisUser.getId()), type, time, desc, from));
                        BillUtil.toast("添加成功");
                        finish();
                    }
                } else {
                    BillUtil.toast("类型/金额/时间不能为空");
                }


                break;
            case R.id.bill_time_edit:
//                DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_24);//24小时值
//                picker.setDateRangeStart(2000, 1, 1);//日期起点
//                picker.setDateRangeEnd(2030, 1, 1);//日期终点
//                picker.setTimeRangeStart(0, 0);//时间范围起点
//                picker.setTimeRangeEnd(23, 59);//时间范围终点
//                picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
//                    @Override
//                    public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
////                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                        String stringDate = year + "-" + month + "-" + day + " " + hour + ":" + minute;
////                        try {
////                            Date date = sdf.parse(stringDate);
////                        } catch (ParseException e) {
////                            e.printStackTrace();
////                        }
//                        billTimeEdit.setText(stringDate);
//                    }
//                });
//                picker.show();


                DatePickDialog dialog = new DatePickDialog(this);
                //设置上下年分限制
                dialog.setYearLimt(100);
                //设置标题
                dialog.setTitle("选择时间");
                //设置类型
                dialog.setType(DateType.TYPE_ALL);
                //设置消息体的显示格式，日期格式
                dialog.setMessageFormat("yyyy-MM-dd HH:mm");
                //设置选择回调
                dialog.setOnChangeLisener(null);
                //设置点击确定按钮回调
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {
                        SimpleDateFormat sdf = new SimpleDateFormat(formatTimeString);
                        String time = sdf.format(date);
                        billTimeEdit.setText(time);
                    }
                });
                dialog.show();


                break;
        }
    }
}
