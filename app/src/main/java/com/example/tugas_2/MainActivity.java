package com.example.tugas_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public static final int REQ_CODE = 1; // untuk startActivityForResult
    public static final String EXTRA_MESSAGE = "extra_message";

    EditText editMessage;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editMessage = findViewById(R.id.editMessage);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(v -> {
            String message = editMessage.getText().toString();
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            intent.putExtra(EXTRA_MESSAGE, message);
            startActivityForResult(intent, REQ_CODE); // supaya bisa terima balasan
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE && resultCode == RESULT_OK) {
            String reply = data.getStringExtra(MainActivity2.EXTRA_REPLY);
            Toast.makeText(this, "Balasan dari Activity 2: " + reply, Toast.LENGTH_LONG).show();
        }
    }
}
