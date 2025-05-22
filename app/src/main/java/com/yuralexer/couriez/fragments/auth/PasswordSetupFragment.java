package com.yuralexer.couriez.fragments.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yuralexer.couriez.R;

public class PasswordSetupFragment extends Fragment {

    private TextInputLayout tilPassword;
    private TextInputEditText etPassword;
    private TextInputLayout tilRepeatPassword;
    private TextInputEditText etRepeatPassword;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_setup, container, false);

        tilPassword = view.findViewById(R.id.tilPassword);
        etPassword = view.findViewById(R.id.etPassword);
        tilRepeatPassword = view.findViewById(R.id.tilRepeatPassword);
        etRepeatPassword = view.findViewById(R.id.etRepeatPassword);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tilPassword = null;
        etPassword = null;
        tilRepeatPassword = null;
        etRepeatPassword = null;
    }

    public boolean validateInput() {
        if (etPassword == null || etRepeatPassword == null || tilPassword == null || tilRepeatPassword == null) return false;

        String password = etPassword.getText() != null ? etPassword.getText().toString() : "";
        String repeatPassword = etRepeatPassword.getText() != null ? etRepeatPassword.getText().toString() : "";

        if (password.isEmpty()) {
            tilPassword.setError("Пароль не может быть пустым");
            return false;
        } else {
            tilPassword.setError(null);
        }

        if (password.length() < 6) {
            tilPassword.setError("Пароль должен быть не менее 6 символов");
            return false;
        } else {
            tilPassword.setError(null);
        }

        if (repeatPassword.isEmpty()) {
            tilRepeatPassword.setError("Повторите пароль");
            return false;
        } else {
            tilRepeatPassword.setError(null);
        }

        if (!password.equals(repeatPassword)) {
            tilRepeatPassword.setError("Пароли не совпадают");
            return false;
        } else {
            tilRepeatPassword.setError(null);
        }

        return true;
    }

    public String getPassword() {
        return etPassword.getText() != null ? etPassword.getText().toString() : "";
    }
}
