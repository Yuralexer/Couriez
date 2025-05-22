package com.yuralexer.couriez.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yuralexer.couriez.R;
import com.google.android.material.textfield.TextInputEditText;
import com.yuralexer.couriez.util.SharedPreferencesHelper;
import com.yuralexer.couriez.db.entity.User;
import com.yuralexer.couriez.db.AppDatabase;


public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etLoginContact;
    private TextInputEditText etLoginPassword;
    private Button btnLogin;
    private Button btnBack;

    private SharedPreferencesHelper prefshelper;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginContact = findViewById(R.id.etLoginContact);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnBack = findViewById(R.id.btnBack);
        prefshelper = new SharedPreferencesHelper(this);
        db = AppDatabase.getDatabase(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Вход в аккаунт");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void performLogin() {
        String contact = etLoginContact.getText().toString().trim();
        String password = etLoginPassword.getText().toString();

        if (contact.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = db.userDao().login(contact, password);

        if (user != null) {
            prefshelper.setLoggedInUserId(user.id);
            Toast.makeText(this, "Вход выполнен успешно!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Неверные данные для входа. Проверьте логин и пароль.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
