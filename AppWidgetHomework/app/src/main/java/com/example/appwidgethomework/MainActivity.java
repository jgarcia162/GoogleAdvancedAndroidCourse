package com.example.appwidgethomework;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences prefs;

    public static final String PREFS_FILE = "com.example.appwidgethomework";
    public static final String KEY_INPUT_STRING = "input_string";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText editText = findViewById(R.id.edittext);
        Button button = findViewById(R.id.submit_button);

        prefs = getSharedPreferences(PREFS_FILE, 0);
        String input = prefs.getString(KEY_INPUT_STRING, "Empty AF");
        editText.setText(input);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs.edit().putString(KEY_INPUT_STRING, String.valueOf(editText.getText())).apply();
            }
        });
    }
}
