package com.yuralexer.couriez.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.yuralexer.couriez.R;
import com.yuralexer.couriez.activity.LoginActivity;
import com.yuralexer.couriez.activity.RegistrationActivity;
import com.yuralexer.couriez.db.vm.UserViewModel;
import com.yuralexer.couriez.util.SharedPreferencesHelper;

public class AccountFragment extends Fragment {

    private TextView tvUserNameOrCompany, tvUserIdentifier;
    private Button btnLogin, btnRegister, btnChangeRegion, btnLogout;
    private LinearLayout authButtonsLayout, userInfoLayout;
    private Spinner regionSpinner;

    private SharedPreferencesHelper prefsHelper;
    private UserViewModel userViewModel;
    private long currentUserId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        prefsHelper = new SharedPreferencesHelper(getContext());
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);;

        tvUserNameOrCompany = view.findViewById(R.id.tvUserNameOrCompany);
        tvUserIdentifier = view.findViewById(R.id.tvUserIdentifier);
        btnLogin = view.findViewById(R.id.btnLogin);
        btnRegister = view.findViewById(R.id.btnRegister);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnChangeRegion = view.findViewById(R.id.btnChangeRegion);
        authButtonsLayout = view.findViewById(R.id.authButtonsLayout);
        userInfoLayout = view.findViewById(R.id.userInfoLayout);

        updateUI();

        btnLogin.setOnClickListener(v -> startActivity(new Intent(getActivity(), LoginActivity.class)));
        btnRegister.setOnClickListener(v -> startActivity(new Intent(getActivity(), RegistrationActivity.class)));
        btnLogout.setOnClickListener(v -> {
            prefsHelper.clearLoginData();
            updateUI();
        });
        btnChangeRegion.setOnClickListener(v -> showRegionSelectionDialog());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        currentUserId = prefsHelper.getLoggedInUserId();
        if (currentUserId != -1) {
            authButtonsLayout.setVisibility(View.GONE);
            userInfoLayout.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.VISIBLE);
            userViewModel.getUserById(currentUserId).observe(getViewLifecycleOwner(), user -> {
                if (user != null) {
                    tvUserNameOrCompany.setText(user.nameOrCompany);
                    tvUserIdentifier.setText(user.identifier);
                } else {
                    tvUserNameOrCompany.setText("Пользователь не найден");
                    tvUserIdentifier.setText("");
                }
            });
        } else {
            authButtonsLayout.setVisibility(View.VISIBLE);
            userInfoLayout.setVisibility(View.GONE);
            btnLogout.setVisibility(View.GONE);
            tvUserNameOrCompany.setText("Гость");
        }
        String currentCity = prefsHelper.getSelectedCity();
        btnChangeRegion.setText("Регион: " + currentCity);
    }


    private void showRegionSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Выберите регион");

        View spinnerView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_region_spinner, null);
        regionSpinner = spinnerView.findViewById(R.id.spinnerDialogRegion);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.cities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(adapter);

        String currentCity = prefsHelper.getSelectedCity();
        if (currentCity != null) {
            int spinnerPosition = adapter.getPosition(currentCity);
            if (spinnerPosition >= 0) {
                regionSpinner.setSelection(spinnerPosition);
            }
        }

        builder.setView(spinnerView);
        builder.setPositiveButton("Выбрать", (dialog, which) -> {
            String selectedRegion = regionSpinner.getSelectedItem().toString();
            prefsHelper.setSelectedCity(selectedRegion);
            updateUI();
            Toast.makeText(getContext(), "Регион изменен: " + selectedRegion, Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
}