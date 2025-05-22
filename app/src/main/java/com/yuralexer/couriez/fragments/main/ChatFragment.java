package com.yuralexer.couriez.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.yuralexer.couriez.R;
import com.yuralexer.couriez.adapter.ChatMessagesAdapter;
import com.yuralexer.couriez.model.ChatMessage;
import java.util.ArrayList;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerViewChat;
    private ChatMessagesAdapter messagesAdapter;
    private EditText etChatMessage;
    private ImageButton btnSendMessage;
    private ArrayList<ChatMessage> chatMessagesList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerViewChat = view.findViewById(R.id.recyclerViewChat);
        etChatMessage = view.findViewById(R.id.etChatMessage);
        btnSendMessage = view.findViewById(R.id.btnSendMessage);

        chatMessagesList = new ArrayList<>();
        // Демо-сообщения
        chatMessagesList.add(new ChatMessage("Привет! Чем могу помочь?", false));

        messagesAdapter = new ChatMessagesAdapter(chatMessagesList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setStackFromEnd(true);
        recyclerViewChat.setLayoutManager(layoutManager);
        recyclerViewChat.setAdapter(messagesAdapter);

        btnSendMessage.setOnClickListener(v -> sendMessage());

        return view;
    }

    private void sendMessage() {
        String messageText = etChatMessage.getText().toString().trim();
        if (!messageText.isEmpty()) {
            messagesAdapter.addMessage(new ChatMessage(messageText, true));
            etChatMessage.setText("");
            recyclerViewChat.scrollToPosition(messagesAdapter.getItemCount() - 1);

            new android.os.Handler().postDelayed(() -> {
                messagesAdapter.addMessage(new ChatMessage("Я просто бот, который повторяет за вами: " + messageText, false));
                recyclerViewChat.scrollToPosition(messagesAdapter.getItemCount() - 1);
            }, 1000);
        }
    }
}