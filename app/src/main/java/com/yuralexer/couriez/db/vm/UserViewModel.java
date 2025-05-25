package com.yuralexer.couriez.db.vm;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.util.Consumer;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yuralexer.couriez.db.AppDatabase;
import com.yuralexer.couriez.db.entity.User;

import java.util.concurrent.Executors;

public class UserViewModel extends AndroidViewModel {
    private final AppDatabase db;

    public UserViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getDatabase(application);
    }
    public LiveData<User> getUserById(long userId) {
        return db.userDao().getUserById(userId);
    }
    public void insert(User user, Consumer<Long> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            long id = db.userDao().insert(user);
            if (callback != null) {
                new Handler(Looper.getMainLooper()).post(() -> callback.accept(id));
            }
        });
    }
    public void login(String identifier, String password, Consumer<User> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            User user = db.userDao().login(identifier, password);
            new Handler(Looper.getMainLooper()).post(() -> callback.accept(user));
        });
    }
    public void register(String accountType, String contactValue, String passwordValue, String userNameOrOrgName, Consumer<Boolean> callback) {
        Executors.newSingleThreadExecutor().execute(() -> {
            User existingUser = db.userDao().findByIdentifier(contactValue);
            if (existingUser != null) {
                new Handler(Looper.getMainLooper()).post(() -> callback.accept(false));
            } else {
                User user = new User(accountType, contactValue, passwordValue, userNameOrOrgName);
                db.userDao().insert(user);
                new Handler(Looper.getMainLooper()).post(() -> callback.accept(true));
            }
        });
    }
}

