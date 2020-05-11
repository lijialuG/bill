package site.qifen.bill.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import site.qifen.bill.model.User;

@Dao
public interface UserDao {


    @Query("select username from user where id = :id ")
    String getUserNameByUserId(int id);

    @Query("select * from user where username = :username ")
    User getUserByUserName(String username);


    @Query("select count(*) from user")
    int getUserLineCount(); //获取一共有多少用户

    @Query("select * from user where username = :username and  password = :password")
    boolean existUser(String username, String password); //账号密码是否正确

    @Query("select * from user where username = :username")
    boolean existUsername(String username); //是否用户名相同

    @Query("select * from user")
    List<User> getAllUser();

    @Insert()
    void insertUser(User user);

    @Delete()
    void deleteUser(User user);
}
