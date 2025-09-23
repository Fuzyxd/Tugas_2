package com.example.tugas_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    public static final String EXTRA_REPLY = "extra_reply";

    TextView textReceived;
    EditText editReply;

    Button btnReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        textReceived = findViewById(R.id.textReceived);
        editReply = findViewById(R.id.editReply);
        btnReply = findViewById(R.id.btnReply);

        // Ambil pesan dari Activity 1
        String message = getIntent().getStringExtra(MainActivity.EXTRA_MESSAGE);
        textReceived.setText("Pesan dari Activity 1: " + message);

        // Balas pesan
        btnReply.setOnClickListener(v -> {
            String reply = editReply.getText().toString();
            Intent replyIntent = new Intent();
            replyIntent.putExtra(EXTRA_REPLY, reply);
            setResult(RESULT_OK, replyIntent);
            finish(); // kembali ke Activity 1
        });
    }
}
