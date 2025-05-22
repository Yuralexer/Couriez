package com.yuralexer.couriez.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.yuralexer.couriez.R;
import com.yuralexer.couriez.fragments.onboarding.OnboardingPage1Fragment;
import com.yuralexer.couriez.fragments.onboarding.OnboardingPage2Fragment;
import com.yuralexer.couriez.fragments.onboarding.OnboardingPage3Fragment;
import com.yuralexer.couriez.util.SharedPreferencesHelper;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class OnboardingActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private Button btnNext, btnBack, btnFinishCitySelection;
    private LinearLayout citySelectionLayout;
    private Spinner citySpinner;
    private TabLayout tabLayout;

    private SharedPreferencesHelper prefsHelper;
    private static final int NUM_PAGES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        prefsHelper = new SharedPreferencesHelper(this);

        if (!prefsHelper.isFirstLaunch()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        viewPager = findViewById(R.id.viewPagerOnboarding);
        btnNext = findViewById(R.id.btnNextOnboarding);
        btnBack = findViewById(R.id.btnBackOnboarding);
        citySelectionLayout = findViewById(R.id.citySelectionLayout);
        citySpinner = findViewById(R.id.spinnerCity);
        btnFinishCitySelection = findViewById(R.id.btnFinishCitySelection);
        tabLayout = findViewById(R.id.tabLayoutOnboarding);

        FragmentStateAdapter pagerAdapter = new OnboardingPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {}).attach();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateButtonVisibility(position);
            }
        });

        btnNext.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < NUM_PAGES - 1) {
                viewPager.setCurrentItem(currentItem + 1);
            } else {
                viewPager.setVisibility(View.GONE);
                tabLayout.setVisibility(View.GONE);
                btnNext.setVisibility(View.GONE);
                btnBack.setVisibility(View.GONE);
                citySelectionLayout.setVisibility(View.VISIBLE);
            }
        });

        btnBack.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem > 0) {
                viewPager.setCurrentItem(currentItem - 1);
            }
        });

        btnFinishCitySelection.setOnClickListener(v -> {
            String selectedCity = citySpinner.getSelectedItem().toString();
            prefsHelper.setSelectedCity(selectedCity);
            prefsHelper.setFirstLaunch(false);
            startActivity(new Intent(OnboardingActivity.this, MainActivity.class));
            finish();
        });

        updateButtonVisibility(0);
    }

    private void updateButtonVisibility(int position) {
        if (position == 0) {
            btnBack.setVisibility(View.INVISIBLE);
        } else {
            btnBack.setVisibility(View.VISIBLE);
        }

        if (position == NUM_PAGES - 1) {
            btnNext.setText("Далее");
        } else {
            btnNext.setText("Далее");
        }
        if (citySelectionLayout.getVisibility() == View.VISIBLE) {
            btnBack.setVisibility(View.GONE);
            btnNext.setVisibility(View.GONE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
        }
    }

    private static class OnboardingPagerAdapter extends FragmentStateAdapter {
        public OnboardingPagerAdapter(AppCompatActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return OnboardingPage1Fragment.newInstance("Заголовок 1", "Текст описания для первого экрана онбординга.", R.drawable.ic_launcher_background);
                case 1:
                    return OnboardingPage2Fragment.newInstance("Заголовок 2", "Текст описания для второго экрана.", R.drawable.ic_launcher_background);
                case 2:
                    return OnboardingPage3Fragment.newInstance("Заголовок 3", "Текст для третьего экрана.", R.drawable.ic_launcher_background);
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }
}