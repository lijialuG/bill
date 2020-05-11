package site.qifen.bill.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import site.qifen.bill.model.Bill;

@Dao
public interface BillDao {


    //    @Query("select * from bill where `from` like '%' || :from || '%' and `date` like '%' ||  :date || '%' and `money` like '%' ||  :money || '%' and `desc` like '%' ||  :desc || '%'")
    @Query("select * from bill where  `date` like '%' ||  :date || '%' and `userId` = :userId")
    List<Bill> getLikeDateBill(String date,int userId);

    @Query("select * from bill where  `from` like '%' ||  :from || '%'  and `userId` = :userId")
    List<Bill> getLikeFromBill(String from,int userId);

    @Query("select * from bill where  `desc` like '%' ||  :desc || '%'  and `userId` = :userId")
    List<Bill> getLikeDescBill(String desc,int userId);

    @Query("select * from bill where  `money` = :money  and `userId` = :userId")
    List<Bill> getLikeMoneyBill(float money,int userId);


    @Query("select sum(money) from bill")
    float getInAndOutSumBill(); //获取所有账单的消费和支出


    @Query("select sum(money) from bill where type = 1 and userId = :userId group by userId")
    float getThisUserInSumBill(String userId); //获取总收入

    @Query("select sum(money) from bill where type = 0 and userId = :userId group by userId")
    float getThisUserOutSumBill(String userId); //获取总支出

    @Query("select * from bill where userId = :userId")
    List<Bill> getThisUserAllBill(int userId); //获取当前登陆用户的所有支出和收入账单

    @Query("select * from bill where userId = :userId and type = 1")
    List<Bill> getThisUserInBill(int userId); //获取当前登陆用户的收入账单

    @Query("select * from bill where userId = :userId and type = 0")
    List<Bill> getThisUserOutBill(int userId); //获取当前登陆用户的支出账单

    @Query("delete from bill where userId = :userId and type = 1")
    void deleteUserInBill(int userId); //删除当前登陆用户的收入账单

    @Query("delete from bill where userId = :userId and type = 0")
    void deleteUserOutBill(int userId); //删除当前登陆用户的支出账单

    @Query("delete from bill where userId = :userId")
    void deleteUserAllBill(int userId); //删除用户的所有账单


    @Insert()
    void insertBill(Bill bill);

    @Delete()
    void deleteBill(Bill bill);

    @Query("select * from bill")
    List<Bill> getAllBill();

}
