package site.qifen.bill.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import site.qifen.bill.dao.BillDao;
import site.qifen.bill.dao.UserDao;
import site.qifen.bill.model.Bill;
import site.qifen.bill.model.User;

@Database(entities = {User.class, Bill.class}, version = 1)
public abstract class BillDatabase extends RoomDatabase {
    private static BillDatabase billDatabase;

    public abstract UserDao userDao();

    public abstract BillDao billDao();

    public synchronized static BillDatabase getInstance(Context context) {
        if (billDatabase == null) {
            billDatabase = Room.databaseBuilder(context,
                    BillDatabase.class, "bill.db")
                    .allowMainThreadQueries()
                    .build();
        }
        return billDatabase;
    }
}
