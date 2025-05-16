package com.example.chatbot;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbot.API.RetrofitClient;
import com.example.chatbot.API.models.ChatRequest;
import com.example.chatbot.API.models.ChatResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_USERS_NAME = "extra_users_name";

    private ImageButton btnSend;
    private EditText etMessage;
    private RecyclerView recyclerView;
    private ChatMessageAdapter adapter;
    private ArrayList<ChatMessage> chatMessages;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (v, insets) -> {
                    Insets sb = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(sb.left, sb.top, sb.right, sb.bottom);
                    return insets;
                }
        );

        // get username from LoginActivity
        Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_USERS_NAME)) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        username = intent.getStringExtra(EXTRA_USERS_NAME);

        btnSend = findViewById(R.id.btnSend);
        etMessage = findViewById(R.id.etMessage);
        recyclerView = findViewById(R.id.recyclerView);

        chatMessages = new ArrayList<>();
        adapter = new ChatMessageAdapter(this, chatMessages);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) // Changed to false
        );
        recyclerView.setAdapter(adapter);

        // Send on keyboard “Done”
        etMessage.setOnEditorActionListener((TextView v, int actionId, KeyEvent ev) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    actionId == EditorInfo.IME_NULL) {
                btnSend.performClick();
                return true;
            }
            return false;
        });

        btnSend.setOnClickListener(view -> {
            // prevent double-send
            if (!chatMessages.isEmpty()
                    && chatMessages.get(chatMessages.size() - 1).getAuthor()
                    == ChatMessage.AUTHOR_TYPE.AUTHOR_TYPE_USER) {
                return;
            }

            String message = etMessage.getText().toString().trim();
            if (message.isEmpty()) return;

            // hide keyboard
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);

            // 1) add user bubble
            chatMessages.add(
                    new ChatMessage(message, ChatMessage.AUTHOR_TYPE.AUTHOR_TYPE_USER)
            );
            adapter.notifyItemInserted(chatMessages.size() - 1);
            //  recyclerView.scrollToPosition(chatMessages.size() - 1); // Removed this line

            // 2) placeholder for AI
            chatMessages.add(
                    new ChatMessage("", ChatMessage.AUTHOR_TYPE.AUTHOR_TYPE_AI)
            );
            adapter.notifyItemInserted(chatMessages.size() - 1);
            // recyclerView.scrollToPosition(chatMessages.size() - 1);  // Removed this line

            etMessage.setText("");
            etMessage.setEnabled(false);

            // 3) call backend
            Call<ChatResponse> call = RetrofitClient
                    .getInstance()
                    .getAPI()
                    .getChatResponse(new ChatRequest(username, message));

            call.enqueue(new Callback<ChatResponse>() {
                @Override
                public void onResponse(Call<ChatResponse> call,
                                       Response<ChatResponse> resp) {
                    etMessage.setEnabled(true);
                    if (!resp.isSuccessful() || resp.body() == null) {
                        Toast.makeText(MainActivity.this,
                                "Server error: " + resp.code(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // fill in the AI bubble
                    String aiText = resp.body().getMessage();
                    ChatMessage aiBubble = chatMessages.get(chatMessages.size() - 1);
                    aiBubble.setMessage(aiText);
                    adapter.notifyItemChanged(chatMessages.size() - 1);
                    // recyclerView.scrollToPosition(chatMessages.size() - 1); // Removed this line
                }

                @Override
                public void onFailure(Call<ChatResponse> call, Throwable t) {
                    etMessage.setEnabled(true);
                    Toast.makeText(MainActivity.this,
                            "Network error: " + t.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

