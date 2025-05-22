package com.yuralexer.couriez.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.yuralexer.couriez.db.entity.User;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    @Query("SELECT * FROM users WHERE identifier = :identifier AND password = :password")
    User login(String identifier, String password);

    @Query("SELECT * FROM users WHERE id = :userId")
    LiveData<User> getUserById(long userId);

    @Query("SELECT * FROM users WHERE identifier = :identifier LIMIT 1")
    User findByIdentifier(String identifier);
}