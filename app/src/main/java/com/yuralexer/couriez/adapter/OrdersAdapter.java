package com.yuralexer.couriez.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yuralexer.couriez.R;
import com.yuralexer.couriez.db.entity.Order;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private List<Order> orders;
    private OnOrderClickListener listener;

    public interface OnOrderClickListener {
        void onOrderClick(Order order);
    }

    public OrdersAdapter(List<Order> orders, OnOrderClickListener listener) {
        this.orders = orders;
        this.listener = listener;
    }

    public void setOrders(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_card, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.bind(order, listener);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderTitle;
        TextView tvOrderParams;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderTitle = itemView.findViewById(R.id.tvOrderTitle);
            tvOrderParams = itemView.findViewById(R.id.tvOrderParams);
        }

        public void bind(final Order order, final OnOrderClickListener listener) {
            tvOrderTitle.setText(String.format(Locale.getDefault(),"Заказ №%d", order.id));
            String params = String.format(Locale.getDefault(),"Вес: %.1f кг, Размеры: %s", order.weightKg, order.dimensions != null ? order.dimensions : "не указаны");
            tvOrderParams.setText(params);
            itemView.setOnClickListener(v -> listener.onOrderClick(order));
        }
    }
}