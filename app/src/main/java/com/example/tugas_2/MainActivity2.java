// File: com/example/tugas_2/MainActivity2.java
package com.example.tugas_2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan; // Ensure this is android.text.style.StyleSpan
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

// Ensure BubbleSpan is in its own file and can be imported
// import com.example.tugas_2.BubbleSpan;
// Ensure Constants is imported
// import com.example.tugas_2.Constants;

public class MainActivity2 extends AppCompatActivity {

    private EditText editReply; // Renamed from editMessage for clarity in this context
    private Button btnReply, btnSelectImageActivity2; // Renamed from btnSend
    private TextView textConversationLogActivity2;
    private ImageView imagePreviewActivity2;
    private Uri selectedImageUriActivity2 = null;

    // Launcher for getting image from gallery in Activity2
    private final ActivityResultLauncher<String> pickImageLauncherActivity2 =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUriActivity2 = uri;
                    imagePreviewActivity2.setImageURI(selectedImageUriActivity2);
                    imagePreviewActivity2.setVisibility(View.VISIBLE);
                    // Don't log here, log when actually sending
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        editReply = findViewById(R.id.editReply);
        textConversationLogActivity2 = findViewById(R.id.textConversationLogActivity2);
        btnReply = findViewById(R.id.btnReply);
        btnSelectImageActivity2 = findViewById(R.id.btnSelectImageActivity2);
        imagePreviewActivity2 = findViewById(R.id.imagePreviewActivity2);

        textConversationLogActivity2.setText("");
        textConversationLogActivity2.setMovementMethod(new ScrollingMovementMethod());

        // Display received message and/or image from MainActivity
        Intent intent = getIntent();
        if (intent != null) {
            String receivedMessage = intent.getStringExtra(Constants.KEY_MESSAGE);
            if (receivedMessage != null && !receivedMessage.isEmpty()) {
                appendToLog("Activity 1: " + receivedMessage, false);
            }

            String imageUriString = intent.getStringExtra(Constants.KEY_IMAGE_URI);
            if (imageUriString != null) {
                // We're just logging the fact an image was received, not displaying it from URI here
                appendToLog("Activity 1: [Gambar diterima]", false);
            }
        }

        btnSelectImageActivity2.setOnClickListener(v -> {
            pickImageLauncherActivity2.launch("image/*");
        });

        btnReply.setOnClickListener(view -> {
            String replyMessageText = editReply.getText().toString().trim();

            if (replyMessageText.isEmpty() && selectedImageUriActivity2 == null) {
                Toast.makeText(this, "Pesan atau gambar balasan tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent resultIntent = new Intent();
            String logEntry = ""; // For constructing the log message for "Saya:"

            if (!replyMessageText.isEmpty()) {
                logEntry += replyMessageText;
                resultIntent.putExtra(Constants.KEY_REPLY, replyMessageText); // Use KEY_REPLY as MainActivity expects it
            }

            if (selectedImageUriActivity2 != null) {
                if (!logEntry.isEmpty()) logEntry += "\n";
                logEntry += "[Gambar Terkirim]";
                resultIntent.putExtra(Constants.KEY_IMAGE_URI, selectedImageUriActivity2.toString()); // Send image URI
                resultIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            // Log what is being sent from this activity
            appendToLog("Saya: " + logEntry, true);

            setResult(Activity.RESULT_OK, resultIntent);

            // Clear inputs
            editReply.setText("");
            if (imagePreviewActivity2 != null) {
                imagePreviewActivity2.setImageURI(null);
                imagePreviewActivity2.setVisibility(View.GONE);
            }
            selectedImageUriActivity2 = null;

            finish(); // Close MainActivity2 and return to MainActivity
        });
    }

    private void appendToLog(String text, boolean isSentMessage) {
        if (textConversationLogActivity2 == null || text.isEmpty()) return;

        if (textConversationLogActivity2.length() > 0) {
            textConversationLogActivity2.append("\n\n");
        }

        SpannableString spannableText = new SpannableString(text);

        Layout.Alignment alignment = isSentMessage ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL;
        spannableText.setSpan(new AlignmentSpan.Standard(alignment), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Use your BubbleSpan here
        int bubbleColor = isSentMessage ? ContextCompat.getColor(this, R.color.teal_200) : ContextCompat.getColor(this, android.R.color.white);
        int textColor = isSentMessage ? ContextCompat.getColor(this, android.R.color.white) : ContextCompat.getColor(this, android.R.color.black);
        spannableText.setSpan(new BubbleSpan(this, bubbleColor, textColor, isSentMessage), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (text.startsWith("Saya:")) {
            spannableText.setSpan(new StyleSpan(Typeface.BOLD), 0, "Saya:".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (text.startsWith("Activity 1:")) { // Messages received from MainActivity
            spannableText.setSpan(new StyleSpan(Typeface.ITALIC), 0, "Activity 1:".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textConversationLogActivity2.append(spannableText);

        final Layout layout = textConversationLogActivity2.getLayout();
        if (layout != null) {
            int scrollDelta = layout.getLineBottom(textConversationLogActivity2.getLineCount() - 1)
                    - textConversationLogActivity2.getScrollY() - textConversationLogActivity2.getHeight();
            if (scrollDelta > 0)
                textConversationLogActivity2.scrollBy(0, scrollDelta);
        }
    }
}
