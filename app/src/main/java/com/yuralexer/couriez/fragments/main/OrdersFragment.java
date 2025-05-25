package com.yuralexer.couriez.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yuralexer.couriez.R;
import com.yuralexer.couriez.activity.OrderDetailsActivity;
import com.yuralexer.couriez.adapter.OrdersAdapter;
import com.yuralexer.couriez.db.AppDatabase;
import com.yuralexer.couriez.db.vm.OrderViewModel;
import com.yuralexer.couriez.util.SharedPreferencesHelper;
import java.util.ArrayList;

public class OrdersFragment extends Fragment {

    private RecyclerView recyclerViewOrders;
    private OrdersAdapter ordersAdapter;
    private TextView tvNoOrders;
    private SharedPreferencesHelper prefsHelper;
    private  OrderViewModel orderViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        recyclerViewOrders = view.findViewById(R.id.recyclerViewOrders);
        tvNoOrders = view.findViewById(R.id.tvNoOrders);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        prefsHelper = new SharedPreferencesHelper(getContext());

        ordersAdapter = new OrdersAdapter(new ArrayList<>(), order -> {
            Intent intent = new Intent(getActivity(), OrderDetailsActivity.class);
            intent.putExtra(OrderDetailsActivity.EXTRA_ORDER_ID, order.id);
            startActivity(intent);
        });
        recyclerViewOrders.setAdapter(ordersAdapter);

        loadOrders();

        return view;
    }

    private void loadOrders() {
        long userId = prefsHelper.getLoggedInUserId();
        if (userId == -1) {
            tvNoOrders.setText("Войдите в аккаунт, чтобы видеть заказы");
            tvNoOrders.setVisibility(View.VISIBLE);
            recyclerViewOrders.setVisibility(View.GONE);
            return;
        }

        orderViewModel.getOrdersByUserId(userId).observe(getViewLifecycleOwner(), orders -> {
            if (orders == null || orders.isEmpty()) {
                tvNoOrders.setText("У вас пока нет заказов");
                tvNoOrders.setVisibility(View.VISIBLE);
                recyclerViewOrders.setVisibility(View.GONE);
            } else {
                tvNoOrders.setVisibility(View.GONE);
                recyclerViewOrders.setVisibility(View.VISIBLE);
                ordersAdapter.setOrders(orders);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}