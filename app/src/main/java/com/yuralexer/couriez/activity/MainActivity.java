package com.yuralexer.couriez.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.yuralexer.couriez.R;
import com.yuralexer.couriez.fragments.main.AccountFragment;
import com.yuralexer.couriez.fragments.main.ChatFragment;
import com.yuralexer.couriez.fragments.main.OrdersFragment;
import com.yuralexer.couriez.util.SharedPreferencesHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private SharedPreferencesHelper prefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefsHelper = new SharedPreferencesHelper(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_orders) {
                selectedFragment = new OrdersFragment();
            } else if (itemId == R.id.nav_new_order) {
                startActivity(new Intent(MainActivity.this, NewOrderActivity.class));
                return false; // Не меняем фрагмент, т.к. открываем Activity
            } else if (itemId == R.id.nav_chat) {
                selectedFragment = new ChatFragment();
            } else if (itemId == R.id.nav_account) {
                selectedFragment = new AccountFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment);
            }
            return true;
        });

        if (savedInstanceState == null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_orders);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}