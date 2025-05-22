package com.yuralexer.couriez.adapter;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yuralexer.couriez.R;
import com.yuralexer.couriez.model.ChatMessage;
import java.util.List;

public class ChatMessagesAdapter extends RecyclerView.Adapter<ChatMessagesAdapter.MessageViewHolder> {

    private List<ChatMessage> messages;

    public ChatMessagesAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
        notifyItemInserted(messages.size() - 1);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_sent, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessageText;
        LinearLayout messageContainer;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageText = itemView.findViewById(R.id.tvMessageText);
            messageContainer = itemView.findViewById(R.id.messageContainer);
        }

        public void bind(ChatMessage message) {
            tvMessageText.setText(message.getText());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) messageContainer. getLayoutParams();
            if (message.isSentByUser()) {
                params.gravity = Gravity.END;
            } else {
                params.gravity = Gravity.START;
            }
            messageContainer.setLayoutParams(params);
        }
    }
}