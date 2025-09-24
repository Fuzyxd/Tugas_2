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
import android.text.style.StyleSpan;
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

// Import your custom BubbleSpan if it's in the same package (no import needed)
// or explicit import: import com.example.tugas_2.BubbleSpan;
// Import Constants: import com.example.tugas_2.Constants;

public class MainActivity extends AppCompatActivity {

    private EditText editMessage;
    private Button btnSend, btnSelectImage;
    private TextView textConversationLog;
    private ImageView imagePreview;
    private Uri selectedImageUri = null;

    private final ActivityResultLauncher<String> pickImageLauncher =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    selectedImageUri = uri;
                    imagePreview.setImageURI(selectedImageUri);
                    imagePreview.setVisibility(View.VISIBLE);
                    // Don't log here, log when actually sending
                }
            });

    private final ActivityResultLauncher<Intent> startChatForResult =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                String senderName = "Activity 2"; // From whom the reply is
                                boolean hasReplied = false;

                                // *** FIXED HERE ***
                                // Changed Constants.KEY_REPLY_FROM_ACTIVITY2 to Constants.KEY_REPLY
                                String replyText = data.getStringExtra(Constants.KEY_REPLY);
                                if (replyText != null && !replyText.isEmpty()) {
                                    appendToLog(senderName + ": " + replyText, false);
                                    hasReplied = true;
                                }

                                String imageUriStringReply = data.getStringExtra(Constants.KEY_IMAGE_URI);
                                if (imageUriStringReply != null) {
                                    appendToLog(senderName + ": [Gambar diterima]", false);
                                    // You could optionally parse and display this imageUriStringReply if needed
                                    hasReplied = true;
                                }
                                if(hasReplied){
                                    Toast.makeText(MainActivity.this, "Balasan diterima", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editMessage = findViewById(R.id.editMessage);
        textConversationLog = findViewById(R.id.textConversationLog);
        btnSend = findViewById(R.id.btnSend);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        imagePreview = findViewById(R.id.imagePreview);

        textConversationLog.setText("");
        textConversationLog.setMovementMethod(new ScrollingMovementMethod());

        btnSelectImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        btnSend.setOnClickListener(view -> {
            String messageText = editMessage.getText().toString().trim();
            if (messageText.isEmpty() && selectedImageUri == null) {
                Toast.makeText(this, "Pesan atau gambar tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intentToActivity2 = new Intent(this, MainActivity2.class);
            String logEntryContent = ""; // Content part of the log

            if (!messageText.isEmpty()) {
                logEntryContent += messageText;
                intentToActivity2.putExtra(Constants.KEY_MESSAGE, messageText);
            }

            if (selectedImageUri != null) {
                if (!logEntryContent.isEmpty()) logEntryContent += "\n";
                logEntryContent += "[Gambar Terkirim]";
                intentToActivity2.putExtra(Constants.KEY_IMAGE_URI, selectedImageUri.toString());
                intentToActivity2.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            appendToLog("Saya: " + logEntryContent, true); // Log self message

            startChatForResult.launch(intentToActivity2);

            // Clear inputs
            editMessage.setText("");
            imagePreview.setImageURI(null);
            imagePreview.setVisibility(View.GONE);
            selectedImageUri = null;
        });
    }

    private void appendToLog(String text, boolean isSentMessage) {
        if (textConversationLog == null || text.isEmpty()) return;
        if (textConversationLog.length() > 0) {
            textConversationLog.append("\n\n");
        }

        SpannableString spannableText = new SpannableString(text);
        Layout.Alignment alignment = isSentMessage ? Layout.Alignment.ALIGN_OPPOSITE : Layout.Alignment.ALIGN_NORMAL;
        spannableText.setSpan(new AlignmentSpan.Standard(alignment), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        int bubbleColor = isSentMessage ? ContextCompat.getColor(this, R.color.teal_700) // Example sent color
                : ContextCompat.getColor(this, R.color.grey_200); // Example received color
        int textColor = isSentMessage ? ContextCompat.getColor(this, android.R.color.white)
                : ContextCompat.getColor(this, android.R.color.black);

        spannableText.setSpan(new BubbleSpan(this, bubbleColor, textColor, isSentMessage), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (text.startsWith("Saya:")) {
            spannableText.setSpan(new StyleSpan(Typeface.BOLD), 0, "Saya:".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (text.startsWith("Activity 2:")) {
            spannableText.setSpan(new StyleSpan(Typeface.ITALIC), 0, "Activity 2:".length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        textConversationLog.append(spannableText);
        // Auto-scroll logic
        final Layout layout = textConversationLog.getLayout();
        if (layout != null) {
            int scrollDelta = layout.getLineBottom(textConversationLog.getLineCount() - 1)
                    - textConversationLog.getScrollY() - textConversationLog.getHeight();
            if (scrollDelta > 0) textConversationLog.scrollBy(0, scrollDelta);
        }
    }
}
