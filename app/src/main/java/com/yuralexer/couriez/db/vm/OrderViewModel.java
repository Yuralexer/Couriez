package com.yuralexer.couriez.db.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yuralexer.couriez.db.AppDatabase;
import com.yuralexer.couriez.db.dao.OrderDao;
import com.yuralexer.couriez.db.entity.Order;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OrderViewModel extends AndroidViewModel {
    private final AppDatabase db;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    public OrderViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getDatabase(application);
    }
    public LiveData<List<Order>> getOrdersForUser(long userId) {
        return db.orderDao().getOrdersByUserId(userId);
    }
    public LiveData<Order> getOrderById(long orderId) {
        return db.orderDao().getOrderById(orderId);
    }
    public LiveData<List<Order>> getOrdersByUserId(long userId) {
        return db.orderDao().getOrdersByUserId(userId);
    }
    public void insertOrder(Order order, Runnable onSuccess) {
        executor.execute(() -> {
            db.orderDao().insert(order);
            if (onSuccess != null) {
                onSuccess.run();
            }
        });
    }
}
