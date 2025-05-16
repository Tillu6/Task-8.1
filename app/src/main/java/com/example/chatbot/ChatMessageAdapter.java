
// ChatMessageAdapter.java
package com.example.chatbot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatMessageAdapter extends RecyclerView.Adapter<ChatMessageAdapter.ViewHolder> {

    private List<ChatMessage> chatMessages;
    private Context context;

    public ChatMessageAdapter(Context context, List<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == ChatMessage.AUTHOR_TYPE.AUTHOR_TYPE_USER.ordinal()) {
            view = LayoutInflater.from(context).inflate(R.layout.item_user_message, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_ai_message, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ChatMessage message = chatMessages.get(position);
        holder.messageTextView.setText(message.getMessage());
        holder.messageTextView.setContentDescription(getMessageDescription(message));
    }

    private String getMessageDescription(ChatMessage message) {
        String author = (message.getAuthor() == ChatMessage.AUTHOR_TYPE.AUTHOR_TYPE_USER) ? "You said: " : "AI said: ";
        return author + message.getMessage();
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return chatMessages.get(position).getAuthor().ordinal();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.tvMessageText);
        }
    }
}

