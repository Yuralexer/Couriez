package com.yuralexer.couriez.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yuralexer.couriez.R;
import com.yuralexer.couriez.fragments.auth.AccountTypeSelectionFragment;
import com.yuralexer.couriez.fragments.auth.ContactInfoFragment;
import com.yuralexer.couriez.fragments.auth.NameInputFragment;
import com.yuralexer.couriez.fragments.auth.PasswordSetupFragment;
import com.yuralexer.couriez.db.entity.User;
import com.yuralexer.couriez.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private Button btnBackRegistration;
    private Button btnNextRegistration;
    private AppDatabase db;

    private String accountType;
    private String userNameOrOrgName;
    private String contactType;
    private String contactValue;
    private String passwordValue;

    private RegistrationPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        viewPager = findViewById(R.id.viewPagerRegistration);
        tabLayout = findViewById(R.id.tabLayoutRegistration);
        btnBackRegistration = findViewById(R.id.btnBackRegistration);
        btnNextRegistration = findViewById(R.id.btnNextRegistration);
        db = AppDatabase.getDatabase(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Регистрация");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        pagerAdapter = new RegistrationPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setUserInputEnabled(false);

        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                    }
                }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateNavigationButtons(position);
            }
        });

        btnNextRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = viewPager.getCurrentItem();
                Fragment currentFragment = pagerAdapter.getFragmentAtPosition(currentPosition);

                switch (currentPosition) {
                    case 0:
                        if (currentFragment instanceof AccountTypeSelectionFragment) {
                            AccountTypeSelectionFragment accTypeFragment = (AccountTypeSelectionFragment) currentFragment;
                            if (accTypeFragment.validateInput()) {
                                accountType = accTypeFragment.getAccountType();
                                viewPager.setCurrentItem(currentPosition + 1);
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Пожалуйста, выберите тип аккаунта", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case 1:
                        if (currentFragment instanceof NameInputFragment) {
                            NameInputFragment nameInputFragment = (NameInputFragment) currentFragment;
                            if (nameInputFragment.validateInput()) {
                                userNameOrOrgName = nameInputFragment.getName();
                                viewPager.setCurrentItem(currentPosition + 1);
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Пожалуйста, введите имя/название", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case 2:
                        if (currentFragment instanceof ContactInfoFragment) {
                            ContactInfoFragment contactFragment = (ContactInfoFragment) currentFragment;
                            if (contactFragment.validateInput()) {
                                Pair<String, String> contactInfo = contactFragment.getContactInfo();
                                contactType = contactInfo.first;
                                contactValue = contactInfo.second;
                                viewPager.setCurrentItem(currentPosition + 1);
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Пожалуйста, введите корректные контактные данные", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                    case 3:
                        if (currentFragment instanceof PasswordSetupFragment) {
                            PasswordSetupFragment passwordFragment = (PasswordSetupFragment) currentFragment;
                            if (passwordFragment.validateInput()) {
                                passwordValue = passwordFragment.getPassword();
                                performRegistration();
                            } else {
                                Toast.makeText(RegistrationActivity.this, "Пожалуйста, введите и подтвердите пароль", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }
            }
        });

        btnBackRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = viewPager.getCurrentItem();
                if (currentPosition > 0) {
                    viewPager.setCurrentItem(currentPosition - 1);
                } else {
                    finish();
                }
            }
        });

        updateNavigationButtons(0);
    }

    public String getAccountTypeForFragments() {
        return accountType;
    }

    private void updateNavigationButtons(int position) {
        int totalPages = pagerAdapter.getItemCount();

        if (position == totalPages - 1) {
            btnNextRegistration.setText("Зарегистрироваться");
        } else {
            btnNextRegistration.setText("Далее");
        }
    }

    private void performRegistration() {

        if (db.userDao().findByIdentifier(contactValue) != null) {
            Toast.makeText(this, "Ошибка! такой пользователь уже существует", Toast.LENGTH_LONG).show();
            return;
        }
        User user = new User(accountType, contactValue, passwordValue, userNameOrOrgName);
        db.userDao().insert(user);

        Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            int currentPosition = viewPager.getCurrentItem();
            if (currentPosition > 0) {
                viewPager.setCurrentItem(currentPosition - 1);
            } else {
                getOnBackPressedDispatcher().onBackPressed();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class RegistrationPagerAdapter extends FragmentStateAdapter {

        private final List<Fragment> fragments = new ArrayList<>();

        public RegistrationPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
            fragments.add(new AccountTypeSelectionFragment());
            fragments.add(new NameInputFragment());
            fragments.add(new ContactInfoFragment());
            fragments.add(new PasswordSetupFragment());
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }

        public Fragment getFragmentAtPosition(int position) {
            return fragments.get(position);
        }
    }
}