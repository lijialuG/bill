package site.qifen.bill.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

/**
 * 数据库映射User对象
 */

@Entity()
public class Bill {
    @PrimaryKey(autoGenerate = true)
    private int id; //唯一标识
    @ColumnInfo()
    private float money; //钱
    @ColumnInfo()
    private String userId; //用户
    @ColumnInfo()
    private boolean type; //收入in(true)还是支出out(false)
    @ColumnInfo()
    private String date; //时间
    @ColumnInfo()
    private String desc; //描述
    @ColumnInfo()
    private String from; //订单类型

    public Bill(float money, String userId, boolean type, String date, String desc, String from) {
        this.money = money;
        this.userId = userId;
        this.type = type;
        this.date = date;
        this.desc = desc;
        this.from = from;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
