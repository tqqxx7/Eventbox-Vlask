package eventbox;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//import android.support.v7.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {


    private TextView signup;
    public TextView btnLoginVlu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();






        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, LoginActivity.class);
                startActivity(it);
            }
        });

        btnLoginVlu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentLogin = new Intent(LoginActivity.this, ListEventActivity.class);
                startActivity(intentLogin);
            }
        });



    }

    private void initView() {
        signup = findViewById(R.id.signup);
        btnLoginVlu = findViewById(R.id.ButtonLoginVlu);

    }




}
