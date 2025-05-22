package com.yuralexer.couriez.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.yuralexer.couriez.db.dao.OrderDao;
import com.yuralexer.couriez.db.dao.UserDao;
import com.yuralexer.couriez.db.entity.Order;
import com.yuralexer.couriez.db.entity.User;

@Database(entities = {User.class, Order.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract OrderDao orderDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "kuryez_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}