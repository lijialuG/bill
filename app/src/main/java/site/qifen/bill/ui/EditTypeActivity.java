package site.qifen.bill.ui;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import site.qifen.bill.R;
import site.qifen.bill.util.BillUtil;
import site.qifen.bill.util.SpUtil;

public class EditTypeActivity extends AppCompatActivity {

    @BindView(R.id.editInfoText)
    TextView editInfoText;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.info)
    TextView info;
    @BindView(R.id.saveTypeBtn)
    Button saveTypeBtn;
    @BindView(R.id.backBtn)
    Button backBtn;
    private int isIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_type);
        ButterKnife.bind(this);
        isIn = getIntent().getIntExtra("edit", 2);
        switch (isIn) {
            case 0: //编辑支出
                editInfoText.setText("编辑支出类型");
                editText.setText(SpUtil.read("outItem"));
                break;
            case 1: //编辑收入
                editText.setText(SpUtil.read("inItem"));
                break;
            case 2: //返回
                finish();
                break;
        }
    }

    @OnClick({R.id.saveTypeBtn, R.id.backBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.saveTypeBtn:
                String text = editText.getText().toString();
                if (!text.equals("")) {
                    switch (isIn) {
                        case 0: //编辑支出
                            SpUtil.write("outItem", text);
                            finish();
                            break;
                        case 1: //编辑收入
                            SpUtil.write("inItem", text);
                            finish();
                            break;
                    }
                } else {
                    BillUtil.toast("账单类型不能为空");
                }
                break;
            case R.id.backBtn:
                finish();
                break;
        }
    }
}
