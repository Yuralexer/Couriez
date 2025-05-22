package com.yuralexer.couriez.activity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.yuralexer.couriez.R;
import com.yuralexer.couriez.db.AppDatabase;
import com.yuralexer.couriez.db.entity.Order;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

import java.util.Locale;

public class OrderDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ORDER_ID = "extra_order_id";
    private long orderId;

    private TextView tvOrderTime, tvDeliveryMethod, tvWeight,
            tvAddressFrom, tvAddressTo, tvItemDesc, tvItemValue,
            tvContactPhone, tvTotalCost, tvDimensions;

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        orderId = getIntent().getLongExtra(EXTRA_ORDER_ID, -1);
        if (orderId == -1) {
            Toast.makeText(this, "Ошибка: ID заказа не найден", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Toolbar toolbar = findViewById(R.id.toolbar_order_details);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Заказ №" + orderId);
        }

        db = AppDatabase.getDatabase(this);
        initViews();
        loadOrderDetails();
    }

    private void initViews() {
        tvOrderTime = findViewById(R.id.tvDetailOrderTime);
        tvDeliveryMethod = findViewById(R.id.tvDetailDeliveryMethod);
        tvWeight = findViewById(R.id.tvDetailWeight);
        tvAddressFrom = findViewById(R.id.tvDetailAddressFrom);
        tvAddressTo = findViewById(R.id.tvDetailAddressTo);
        tvItemDesc = findViewById(R.id.tvDetailItemDesc);
        tvItemValue = findViewById(R.id.tvDetailItemValue);
        tvContactPhone = findViewById(R.id.tvDetailContactPhone);
        tvTotalCost = findViewById(R.id.tvDetailTotalCost);
        tvDimensions = findViewById(R.id.tvDetailDimensions);
    }

    private void loadOrderDetails() {
        db.orderDao().getOrderById(orderId).observe(this, order -> {
            if (order != null) {
                populateViews(order);
            } else {
                Toast.makeText(this, "Заказ не найден", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void populateViews(Order order) {
        String timeText;
        if ("asap".equals(order.orderTimeType)) {
            timeText = "Как можно скорее";
        } else {
            timeText = "Запланировано на: " + (order.scheduledDateTime != null ? order.scheduledDateTime : "не указано");
        }
        tvOrderTime.setText(timeText);

        String deliveryMethodText = "Не указан";
        if ("foot".equals(order.deliveryMethod)) deliveryMethodText = "Пешком";
        else if ("car".equals(order.deliveryMethod)) deliveryMethodText = "Легковой автомобиль";
        else if ("truck".equals(order.deliveryMethod)) deliveryMethodText = "Грузовой автомобиль";
        tvDeliveryMethod.setText(deliveryMethodText);

        tvWeight.setText(String.format(Locale.getDefault(), "%.1f кг", order.weightKg));
        tvAddressFrom.setText(order.addressFrom);
        tvAddressTo.setText(order.addressTo);
        tvItemDesc.setText(order.itemDescription);
        tvItemValue.setText(String.format(Locale.getDefault(), "%.2f руб.", order.itemValue));
        tvContactPhone.setText(order.contactPhone);
        tvTotalCost.setText(String.format(Locale.getDefault(), "Итого: %.2f руб.", order.totalCost));
        tvDimensions.setText(order.dimensions != null ? order.dimensions : "Не указаны");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}