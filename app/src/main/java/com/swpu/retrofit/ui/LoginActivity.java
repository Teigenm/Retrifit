package com.swpu.retrofit.ui;


import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.swpu.retrofit.R;
import com.swpu.retrofit.model.UserEntity;
import com.swpu.retrofit.net.ApiRetrofit;
import com.swpu.retrofit.net.UserApiService;
import com.swpu.retrofit.util.Validator;

import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mUsernameText;
    private EditText mPasswordText;

    private UserApiService mUserApiService ;
    private UserEntity mUserEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUserApiService = ApiRetrofit.getInstance().getService(UserApiService.class);
        View view = findViewById(R.id.login_register);
        view.setOnClickListener(this);

        View login = findViewById(R.id.login_confirm);
        login.setOnClickListener(this);

        mUsernameText = findViewById(R.id.login_username);
        mPasswordText = findViewById(R.id.login_password);


    }

    private void attemptLogin() {
        String username = mUsernameText.getText().toString();
        if (Validator.isEmpty(username)) {
            mUsernameText.setError(getString(R.string.error_username_empty));
            mUsernameText.requestFocus();
            return;
        }

        String password = mPasswordText.getText().toString();
        if (Validator.isEmpty(password)) {
            mPasswordText.setError(getString(R.string.error_password_empty));
            mPasswordText.requestFocus();
            return;
        }

        if (password.length() < 6) {
            mPasswordText.setError(getString(R.string.error_password_too_short));
            mPasswordText.requestFocus();
            return;
        }

        login(username, password);
    }
    public void login(String username, String password) {

        Flowable<UserEntity> login = mUserApiService.login(username, password);
        //订阅在主线程
        login.observeOn(AndroidSchedulers.mainThread())
                //订阅
                .subscribe(new FlowableSubscriber<UserEntity>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(UserEntity userInfo) {
                        Toast.makeText(getApplicationContext(), userInfo.toString(), Toast.LENGTH_SHORT).show();
                        mUserEntity = userInfo;
                    }

                    @Override
                    public void onError(Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(getApplicationContext(), "账号或密码错误", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                        HomeActivity.go(LoginActivity.this,mUserEntity);
                    }
                });
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.login_confirm:
                attemptLogin();
                break;
            case R.id.login_register:
                RegisterActivity.go(LoginActivity.this);
                break;
        }
    }

}