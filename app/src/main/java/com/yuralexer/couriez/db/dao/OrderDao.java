package com.yuralexer.couriez.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.yuralexer.couriez.db.entity.Order;
import java.util.List;

@Dao
public interface OrderDao {
    @Insert
    void insert(Order order);

    @Query("SELECT * FROM orders WHERE user_id = :userId ORDER BY id DESC")
    LiveData<List<Order>> getOrdersByUserId(long userId);

    @Query("SELECT * FROM orders WHERE id = :orderId")
    LiveData<Order> getOrderById(long orderId);
}