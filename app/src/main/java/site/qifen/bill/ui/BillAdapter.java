package site.qifen.bill.ui;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import site.qifen.bill.R;
import site.qifen.bill.dao.BillDao;
import site.qifen.bill.dao.UserDao;
import site.qifen.bill.db.BillDatabase;
import site.qifen.bill.model.Bill;
import site.qifen.bill.util.App;
import site.qifen.bill.util.BillUtil;
import site.qifen.bill.util.SpUtil;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.ViewHolder> {

    private List<Bill> billList;
    private TextView textView;
    private float inMoney = 0;
    private float outMoney = 0;

    private UserDao userDao = BillDatabase.getInstance(App.getContext()).userDao();
    private BillDao billDao = BillDatabase.getInstance(App.getContext()).billDao();


    public BillAdapter(List<Bill> billList, TextView textView) {
        this.billList = billList;
        this.textView = textView;
        for (Bill bill : billList) {
            if (bill.isType()) {
                inMoney += bill.getMoney();
            } else {
                outMoney += bill.getMoney();
            }
        }
        textView.setText("收入: " + inMoney + " 支出: " + outMoney);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameText;
        TextView moneyText;
        TextView fromText;
        TextView descText;
        TextView timeText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.bill_item_username);
            moneyText = itemView.findViewById(R.id.bill_item_money);
            fromText = itemView.findViewById(R.id.bill_item_type);
            descText = itemView.findViewById(R.id.bill_item_desc);
            timeText = itemView.findViewById(R.id.bill_item_time);
        }
    }

    @NonNull
    @Override
    public BillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillAdapter.ViewHolder holder, int position) {
        Bill bill = billList.get(position);
        String username = BillUtil.getThisUser().getUsername();
        holder.usernameText.setText("用户名: " + username + "  ID: " + bill.getUserId());
        if (bill.isType()) {
            holder.moneyText.setText("金额: " + bill.getMoney());
        } else {
            holder.moneyText.setText("金额: " + "-" + bill.getMoney());
        }
        holder.fromText.setText("类型: " + bill.getFrom());
        holder.descText.setText("备注: " + bill.getDesc());
        holder.timeText.setText("时间: " + bill.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog1 = new AlertDialog.Builder(v.getContext())
                        .setTitle("账单详情")
                        .setMessage("用户名: " + username + "\n金额: "
                                + bill.getMoney() + "\n类型: " + bill.getFrom() + "\n备注: "
                                + bill.getDesc() + "\n时间: " + bill.getDate())
                        .setPositiveButton("确定", null)
                        .create();
                alertDialog1.show();
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog alertDialog1 = new AlertDialog.Builder(v.getContext())
                        .setTitle("你确定删除这个账单吗?")
                        .setMessage("用户名: " + userDao.getUserNameByUserId(
                                Integer.parseInt(bill.getUserId())) + "\n金额: "
                                + bill.getMoney() + "\n类型: " + bill.getFrom() + "\n备注: "
                                + bill.getDesc() + "\n时间: " + bill.getDate())
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                billDao.deleteBill(bill);
                                billList.remove(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
                alertDialog1.show();
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return billList.size();
    }


}
