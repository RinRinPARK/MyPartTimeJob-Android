package com.ssuandroid.my_parttime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class LoginFragment extends AppCompatActivity {
    TextView sign;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fragment);

        loginButton = findViewById(R.id.loginbutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그인이 성공하면 MainActivity로 이동
                Intent intent = new Intent(LoginFragment.this, MainActivity.class);
                startActivity(intent);
                finish(); // LoginActivity는 종료
            }
        });

        // 회원가입 버튼
        sign = findViewById(R.id.signin);

        // 회원가입 버튼 클릭시, 회원가입 페이지로 이동
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginFragment.this, Signup.class);
                startActivity(intent);
            }
        });
    }
}