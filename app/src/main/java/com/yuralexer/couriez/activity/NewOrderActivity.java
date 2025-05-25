package com.yuralexer.couriez.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.yuralexer.couriez.R;
import com.yuralexer.couriez.db.AppDatabase;
import com.yuralexer.couriez.db.entity.Order;
import com.yuralexer.couriez.db.vm.OrderViewModel;
import com.yuralexer.couriez.util.SharedPreferencesHelper;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Calendar;
import java.util.Locale;
import java.text.SimpleDateFormat;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.view.MenuItem;

public class NewOrderActivity extends AppCompatActivity {

    private RadioGroup rgOrderTime, rgDeliveryMethod;
    private Button btnScheduleDate;
    private EditText etWeight, etAddressFrom, etAddressTo, etItemDescription, etItemValue, etContactPhone, etDimensions;
    private TextView tvScheduledDate, tvTotalCost;
    private Button btnSubmitOrder;
    private TextInputLayout tilWeight;

    private Calendar scheduledCalendar;
    private SharedPreferencesHelper prefsHelper;
    private String currentDeliveryMethod = "foot";
    private OrderViewModel  orderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Новый заказ");
        }

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        prefsHelper = new SharedPreferencesHelper(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Новый заказ");
        }

        initViews();
        setupListeners();
        updateTotalCost(500);
    }

    private void initViews() {
        rgOrderTime = findViewById(R.id.rgOrderTime);
        btnScheduleDate = findViewById(R.id.btnScheduleDate);
        tvScheduledDate = findViewById(R.id.tvScheduledDate);
        rgDeliveryMethod = findViewById(R.id.rgDeliveryMethod);
        etWeight = findViewById(R.id.etWeight);
        tilWeight= findViewById(R.id.tilWeight);
        etAddressFrom = findViewById(R.id.etAddressFrom);
        etAddressTo = findViewById(R.id.etAddressTo);
        etItemDescription = findViewById(R.id.etItemDescription);
        etItemValue = findViewById(R.id.etItemValue);
        etContactPhone = findViewById(R.id.etContactPhone);
        etDimensions = findViewById(R.id.etDimensions);
        tvTotalCost = findViewById(R.id.tvTotalCost);
        btnSubmitOrder = findViewById(R.id.btnSubmitOrder);

        scheduledCalendar = Calendar.getInstance();
        tvScheduledDate.setVisibility(View.GONE);
        btnScheduleDate.setVisibility(View.GONE);
    }

    private void setupListeners() {
        rgOrderTime.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbSchedule) {
                btnScheduleDate.setVisibility(View.VISIBLE);
            } else {
                btnScheduleDate.setVisibility(View.GONE);
                tvScheduledDate.setVisibility(View.GONE);
                scheduledCalendar = Calendar.getInstance();
            }
        });

        btnScheduleDate.setOnClickListener(v -> showDatePicker());

        rgDeliveryMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbFoot) {
                currentDeliveryMethod = "foot";
                updateTotalCost(500.00);
            }
            else if (checkedId == R.id.rbCar) {
                currentDeliveryMethod = "car";
                updateTotalCost(1000.00);
            }
            else if (checkedId == R.id.rbTruck) {
                currentDeliveryMethod = "car";
                updateTotalCost(10000.00);
            };
            validateWeight();
        });

        etWeight.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                validateWeight();
            }
        });

        btnSubmitOrder.setOnClickListener(v -> submitOrder());
    }

    private void validateWeight() {
        String weightStr = etWeight.getText().toString();
        if (weightStr.isEmpty()) {
            tilWeight.setError(null);
            return;
        }
        try {
            double weight = Double.parseDouble(weightStr);
            if ("foot".equals(currentDeliveryMethod) && weight > 15) {
                tilWeight.setError("До 15 кг для пешего курьера");
            } else if ("car".equals(currentDeliveryMethod) && weight > 200) {
                tilWeight.setError("До 200 кг для легкового авто");
            } else if ("truck".equals(currentDeliveryMethod) && weight <=200) {
                tilWeight.setError("Более 200 кг для грузового авто");
            }
            else {
                tilWeight.setError(null);
            }
        } catch (NumberFormatException e) {
            tilWeight.setError("Неверный формат веса");
        }
    }


    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    scheduledCalendar.set(Calendar.YEAR, year1);
                    scheduledCalendar.set(Calendar.MONTH, monthOfYear);
                    scheduledCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                    tvScheduledDate.setText("Дата: " + sdf.format(scheduledCalendar.getTime()));
                    tvScheduledDate.setVisibility(View.VISIBLE);
                }, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

    private void updateTotalCost(double mockCost) {
        tvTotalCost.setText(String.format(Locale.getDefault(), "Итого: %.2f руб.", mockCost));
    }

    private boolean validateInputs() {
        if (rgOrderTime.getCheckedRadioButtonId() == R.id.rbSchedule && tvScheduledDate.getVisibility() == View.GONE) {
            Toast.makeText(this, "Выберите дату для запланированного заказа", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (etWeight.getText().toString().isEmpty()) {
            tilWeight.setError("Укажите вес");
            etWeight.requestFocus();
            return false;
        }
        if (tilWeight.getError() != null) {
            etWeight.requestFocus();
            return false;
        }
        if (etAddressFrom.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Укажите адрес отправления", Toast.LENGTH_SHORT).show();
            etAddressFrom.requestFocus();
            return false;
        }

        if (etContactPhone.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Укажите контактный телефон", Toast.LENGTH_SHORT).show();
            etContactPhone.requestFocus();
            return false;
        }
        long userId = prefsHelper.getLoggedInUserId();
        if (userId == -1) {
            Toast.makeText(this, "Необходимо войти в аккаунт для создания заказа", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }


    private void submitOrder() {
        if (!validateInputs()) return;

        long userId = prefsHelper.getLoggedInUserId();

        String orderTimeType = (rgOrderTime.getCheckedRadioButtonId() == R.id.rbAsap) ? "asap" : "scheduled";
        String scheduledDateTimeStr = null;
        if ("scheduled".equals(orderTimeType)) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
            scheduledDateTimeStr = sdf.format(scheduledCalendar.getTime());
        }

        int deliveryCheckedId = rgDeliveryMethod.getCheckedRadioButtonId();
        String deliveryMethod = (deliveryCheckedId == R.id.rbCar) ? "car"
                : (deliveryCheckedId == R.id.rbTruck) ? "truck"
                : "foot";

        double weight = Double.parseDouble(etWeight.getText().toString());
        String addressFrom = etAddressFrom.getText().toString().trim();
        String addressTo = etAddressTo.getText().toString().trim();
        String itemDesc = etItemDescription.getText().toString().trim();
        double itemValue = etItemValue.getText().toString().isEmpty() ? 0
                : Double.parseDouble(etItemValue.getText().toString());
        String contactPhone = etContactPhone.getText().toString().trim();
        String dimensions = etDimensions.getText().toString().trim();
        double totalCost = (deliveryCheckedId == R.id.rbCar) ? 1000.00
                : (deliveryCheckedId == R.id.rbTruck) ? 10000.00
                : 500.00;

        Order newOrder = new Order(userId, orderTimeType, scheduledDateTimeStr, deliveryMethod,
                weight, addressFrom, addressTo, itemDesc, itemValue,
                contactPhone, totalCost, dimensions);

        orderViewModel.insertOrder(newOrder, () -> runOnUiThread(() -> {
            Toast.makeText(NewOrderActivity.this, "Заказ создан!", Toast.LENGTH_SHORT).show();
            finish();
        }));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}