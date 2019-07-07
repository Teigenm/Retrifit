package com.swpu.retrofit.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.swpu.retrofit.R;
import com.swpu.retrofit.model.UserEntity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mUserNickname;

    public static UserEntity sUserEntity;

    public static HomeActivity sHomeActivity;
    public static HomeActivity getInstance() {
        return sHomeActivity;
    }

    public static void go(Context context, UserEntity userEntity) {
        Intent intent = new Intent();
        intent.setClass(context,HomeActivity.class);
        context.startActivity(intent);
        sUserEntity = userEntity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sHomeActivity = this;

        View avatarLayout = findViewById(R.id.user_avatar_layout);
        avatarLayout.setOnClickListener(this);
        View nicknameLayout = findViewById(R.id.user_nickname_layout);
        nicknameLayout.setOnClickListener(this);
        View dimensionalBarcodeLayout = findViewById(R.id.user_account_layout);
        dimensionalBarcodeLayout.setOnClickListener(this);
        View moreLayout = findViewById(R.id.user_sex_layout);
        moreLayout.setOnClickListener(this);

        mUserNickname = findViewById(R.id.user_nickname);
        initDate();
    }

    public void initDate() {
        mUserNickname.setText(sUserEntity.getNickname());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_nickname_layout:
                UpdateNicknameActivity.go(HomeActivity.this,sUserEntity.getUserId());
                break;
            case R.id.user_avatar:

            default:
                break;
        }
    }
}
