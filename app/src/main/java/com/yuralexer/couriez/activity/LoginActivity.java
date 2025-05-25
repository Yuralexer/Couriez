package com.yuralexer.couriez.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yuralexer.couriez.R;
import com.google.android.material.textfield.TextInputEditText;
import com.yuralexer.couriez.db.vm.UserViewModel;
import com.yuralexer.couriez.util.SharedPreferencesHelper;
import com.yuralexer.couriez.db.entity.User;


public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etLoginContact;
    private TextInputEditText etLoginPassword;
    private Button btnLogin;
    private Button btnBack;

    private SharedPreferencesHelper prefshelper;
    private UserViewModel userViewModel;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        etLoginContact = findViewById(R.id.etLoginContact);
        etLoginPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnBack = findViewById(R.id.btnBack);
        prefshelper = new SharedPreferencesHelper(this);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

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

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            finish();
        }
    }

    private void performLogin() {
        String contact = etLoginContact.getText().toString().trim();
        String password = etLoginPassword.getText().toString();

        if (contact.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        userViewModel.login(contact, password, user -> {
            if (user != null) {
                prefshelper.setLoggedInUserId(user.getId());
                Toast.makeText(this, "Добро пожаловать, " + user.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show();
            }
        });
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
