package com.azuresamples.msalandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import eventbox.R;

public class TestActivity extends AppCompatActivity {
    TextView emailLogin;
    Button btnSignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        emailLogin = findViewById(R.id.emailLogin);
        btnSignOut = findViewById(R.id.ButtonSignOut);

        Intent intent = getIntent();
        String test = intent.getStringExtra("mail");
        emailLogin.setText("Welcome:" + test);

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(TestActivity.this, SigninActivity.class);
                intent1.putExtra("out", "out");
                startActivity(intent1);
            }
        });


    }
}
