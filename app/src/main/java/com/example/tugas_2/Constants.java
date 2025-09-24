// File: com/example/tugas_2/Constants.java
package com.example.tugas_2;

public class Constants {
    public static final String KEY_MESSAGE = "ACTIVITY_MESSAGE"; // Text message key
    public static final String KEY_IMAGE_URI = "ACTIVITY_IMAGE_URI"; // Image URI key
    // KEY_REPLY is still used in MainActivity's onActivityResult,
    // ensure MainActivity2 sends back text using this key if MainActivity expects it
    // OR change MainActivity to expect KEY_MESSAGE for replies too.
    // For this example, I will assume MainActivity2 sends its text reply using KEY_MESSAGE
    // and its image URI using KEY_IMAGE_URI in the result Intent.
    public static final String KEY_REPLY = "REPLY_FROM_MAIN_ACTIVITY2"; // Keep if MainActivity specifically uses it for text reply
}